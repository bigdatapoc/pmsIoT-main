package com.hcl.pmsiot.operation.service;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hcl.pmsiot.operation.dao.LocationDetailDao;
import com.hcl.pmsiot.operation.dao.UserDetailDao;
import com.hcl.pmsiot.operation.model.LocationDetail;
import com.hcl.pmsiot.operation.model.UserDetail;
import com.hcl.pmsiot.operation.util.OperationUtil;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

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
	
	private static LocationDetail hclLocationDetail;
	
	@Autowired
	private JavaStreamingContext jssc;

	@Value("${kafka.broker.host.url}")
	private String kafkaBrokerUrl;

	@Value("${kafka.broker.topic}")
	private String kafkaTopic;

	@Autowired
	private UserDetailDao userDetailDao;

	@Autowired
	LocationDetailDao locationDetailDao;
	
	static Function<Tuple2<String, String>, String> mapFunc = new Function<Tuple2<String, String>, String>() {
		@Override
		public String call(Tuple2<String, String> tuple2) {
			return tuple2._2();
		}
	};
	
	public void startStreaming() throws UnknownHostException, InterruptedException {

		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(kafkaTopic.split(",")));
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
							
							if(hclLocationDetail == null)
								hclLocationDetail =  locationDetailDao.getLocationByName("Hcl");
							userDetail.setOnline(OperationUtil.containsLocation(Arrays.asList(hclLocationDetail.getBoudary()), userDetail.getLatitude(), userDetail.getLongitude(),true));
							if(userDetail.isOnline())
								userDetailDao.updateUserLocation(userDetail);
							else
								userDetailDao.updateUserAvailability(userDetail);
							
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

	

	
	
}
