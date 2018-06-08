package com.hcl.pmsiot.messaging.service;

import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hcl.pmsiot.messaging.data.NotificationData;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

@Service
public class AdminMessagingKafkaConsumer {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Value("${kafka.broker.host.url}")
	private String kafkaBrokerUrl;

	@Value("${kafka.broker.topic.admin.notification}")
	private String kafkaAdminNotificationTopic;

	@Value("${mqtt.broker.topic.admin.notification}")
	private String mqttAdminNotificationTopic;

	@Value("${kafka.broker.topic.user.location.tracking}")
	private String userLocationTrackingTopic;
	
	@Autowired
	private JavaStreamingContext jssc;

	@Autowired
	private MqttMessage mqttMessage;

	@Autowired
	private MqttClient mqttClient;

	static Function<Tuple2<String, String>, String> mapFunc = new Function<Tuple2<String, String>, String>() {
		@Override
		public String call(Tuple2<String, String> tuple2) {
			return tuple2._2();
		}
	};

	public void startStreaming() throws UnknownHostException, InterruptedException {

		Set<String> topicsSet = new HashSet<String>(Arrays.asList(new String[] {kafkaAdminNotificationTopic, userLocationTrackingTopic}));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", kafkaBrokerUrl);
		kafkaParams.put("auto.create.topics.enable", "true");
		Gson gson = new Gson();
		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);

		// Get the lines, split them into words, count the words and print
		JavaDStream<String> data = messages.map(mapFunc);
		data.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaRDD<String> data) throws Exception {

				if (data != null) {
					List<String> result = data.collect();

					for (String str : result) {
						try {
							LOGGER.debug("Received :- " + str);
							NotificationData notification = gson.fromJson(str, NotificationData.class);
							mqttClient.connect();
							mqttMessage.setPayload(str.getBytes());
							//String topic = MessageFormat.format(mqttUserLocationTrackingTopic, notification.getTo());
							mqttClient.publish(notification.getTo(), mqttMessage);

						} catch (MqttException e) {
							e.printStackTrace();
							LOGGER.error(e.getMessage());

						} finally {
							try {
								mqttClient.disconnect();
							} catch (MqttException e) {
								LOGGER.error(e.getMessage());
								e.printStackTrace();
							}
						}

					}
				}

			}
		});

		// Start the computation
		jssc.start();
		jssc.awaitTermination();
	}
}
