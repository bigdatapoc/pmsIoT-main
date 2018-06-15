package com.hcl.pmsiot.operation.service;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hcl.pmsiot.operation.dao.LocationDetailDao;
import com.hcl.pmsiot.operation.dao.UserDetailDao;
import com.hcl.pmsiot.operation.data.FirebasePushNotificationData;
import com.hcl.pmsiot.operation.data.NotificationData;
import com.hcl.pmsiot.operation.model.LocationDetail;
import com.hcl.pmsiot.operation.model.UserDetail;
import com.hcl.pmsiot.operation.util.LatLngDistanceCalculatorUtil;
import com.hcl.pmsiot.operation.util.PolygonOperationUtil;

import kafka.serializer.StringDecoder;
import scala.Tuple2;
import java.lang.reflect.Type;

/**
 * Consumes messages from one or more topics in Kafka and does save data to
 * mongodb.
 * 
 * 
 * @author Manish gour
 * 
 */

@Service
public final class UserDetailService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private static LocationDetail masterLocationDetail;
	
	@Autowired
	private JavaStreamingContext jssc;

	@Value("${kafka.broker.host.url}")
	private String kafkaBrokerUrl;

	@Value("${kafka.broker.topic.user.location}")
	private String userLocationTopic;

	@Value("${firebase.admin.topic.user.offline}")
	private String adminFirebaseUserOfflineTopic;
	
	@Value("${kafka.broker.topic.admin.notification}")
	private String adminNotificationTopic;
	
	@Value("${kafka.broker.topic.user.location.tracking}")
	private String userLocationTrackingTopic;
	
	@Value("${kafka.broker.topic.user.location.nearby}")
	private String userLocationNearbyTopic;
	
	@Value("${mqtt.broker.topic.user.location.tracking}")
	private String mqttUserLocationTrackingTopic;
	
	@Value("${mqtt.broker.topic.user.location.nearby}")
	private String mqttUserLocationNearbyTopic;
	 
	
	@Value("${radius}")
	private int nearbyRadius;
	
	@Autowired
	private UserDetailDao userDetailDao;

	@Autowired
	LocationDetailDao locationDetailDao;
	
	@Autowired
	FirebasePushNotificationsService pushNotificationsService;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	static Function<Tuple2<String, String>, String> mapFunc = new Function<Tuple2<String, String>, String>() {
		@Override
		public String call(Tuple2<String, String> tuple2) {
			return tuple2._2();
		}
	};
	
	public void startStreaming() throws UnknownHostException, InterruptedException {

		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(new String[] {userLocationTopic}));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", kafkaBrokerUrl);
		kafkaParams.put("auto.create.topics.enable", "true");

		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);

		// Get the lines, split them into words, count the words and print
		JavaDStream<String> data = messages.map(mapFunc);
		Gson gson = new Gson();
		data.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaRDD<String> data) throws Exception {

				if (data != null) {
					List<String> result = data.collect();

					for (String str : result) {
						try {
							UserDetail userDetail = gson.fromJson(str, UserDetail.class);
							
							if(masterLocationDetail == null)
								masterLocationDetail =  locationDetailDao.getLocationMaster();
							userDetail.setOnline(PolygonOperationUtil.containsLocation(Arrays.asList(masterLocationDetail.getBoudary()), userDetail.getLatitude(), userDetail.getLongitude(),true));
							if(userDetail.isOnline()) {
								userDetailDao.updateUserLocation(userDetail);
								sendLiveLocationNotification(userDetail);
								sendNearbyLocationNotification(userDetail);
							} else {
								userDetailDao.updateUserAvailability(userDetail);
								sendUserOfflineNotification(userDetail.getUserId());
							}
							
						} catch (Exception ex) {
							ex.printStackTrace();
							LOGGER.error(ex.getMessage());
						}
					}
				}

			}

			
		});

		// Start the computation
		jssc.start();
		jssc.awaitTermination();
	}

	private void sendUserOfflineNotification(String userId) {
		
		//firebaseNotification(userId);
		NotificationData notificationData = new NotificationData();
		notificationData.setTitle("Offline Notification");
		notificationData.setBody(userId + " is offline");
		notificationData.setTo("admin/notification");
		Gson gson = new Gson();
		kafkaTemplate.send(adminNotificationTopic, gson.toJson(notificationData));
		
	}

	private void sendLiveLocationNotification(UserDetail userDetail) {
		
		//firebaseNotification(userId);
		NotificationData notificationData = new NotificationData();
		notificationData.setTitle("Live location");
		notificationData.setTo(MessageFormat.format(mqttUserLocationTrackingTopic, userDetail.getUserId()));
		notificationData.setFrom(userDetail.getUserId());
		Map<String, String> data = new HashMap<String, String>();
		data.put("latitude", ""+userDetail.getLatitude());
		data.put("longitude", ""+userDetail.getLongitude());
		notificationData.setData(data);
		Gson gson = new Gson();
		kafkaTemplate.send(userLocationTrackingTopic, gson.toJson(notificationData));
		
	}

	private void sendNearbyLocationNotification(UserDetail userDetail) {
		
		
		List<LocationDetail> locationDetails = locationDetailDao.getAllLocation();
		List<LocationDetail> nearByLocation = new ArrayList<>();
		if(locationDetails != null) {
			nearByLocation = locationDetails.stream().filter(location -> 
				LatLngDistanceCalculatorUtil.isWithinDistance(userDetail.getLatitude(), userDetail.getLongitude(), location.getLatitude(), location.getLongitude(), nearbyRadius)
			).collect(Collectors.toList());
		}
		if(nearByLocation.size() > 0) {
			NotificationData notificationData = new NotificationData();
			notificationData.setTitle("Nearby location");
			notificationData.setTo(MessageFormat.format(mqttUserLocationNearbyTopic, userDetail.getUserId()));
			notificationData.setFrom(userDetail.getUserId());
			Gson gson = new Gson();
			Map<String, String> dataMap = new HashMap<>();
			Type collectionType = new TypeToken<List<LocationDetail>>(){}.getType();
			dataMap.put("nearby", gson.toJson(nearByLocation, collectionType));
			notificationData.setData(dataMap);
			kafkaTemplate.send(userLocationNearbyTopic, gson.toJson(notificationData));
		}
		
		
	}
	
	private void firebaseNotification(String userId) {
		
		FirebasePushNotificationData notificationData = new FirebasePushNotificationData();
		notificationData.setNotification(notificationData.new Notification("Offline Notification", userId + " is offline"));
		notificationData.setTopic(adminFirebaseUserOfflineTopic);
		HttpEntity<FirebasePushNotificationData> request = new HttpEntity<>(notificationData);
		pushNotificationsService.send(request);
	}

	
	
}
