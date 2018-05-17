package com.hcl.pmsiot;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.hcl.pmsiot.data.UserLocation;
import com.hcl.pmsiot.mongo.MongoDao;

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

public final class KafkaToMongoDb {

	public static String brokerHost = "localhost:9092";
	public static String topics = "iot_data";
	
	public static void main(String[] args) throws UnknownHostException {

		SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount").setMaster("local[10]")
				.set("spark.executor.memory", "1g");

		// Create context with a 2 seconds batch interval
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));

		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokerHost);

		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);

		// Get the lines, split them into words, count the words and print
		JavaDStream<String> data = messages.map(new Function<Tuple2<String, String>, String>() {
			public String call(Tuple2<String, String> message) {
				return message._2();
			}

		});
		MongoDao dao = new MongoDao();
		data.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			@Override
			public void call(JavaRDD<String> data) throws Exception {
				
				
				
				if (data != null) {
					List<String> result = data.collect();

					for (String userDetail : result) {

						System.out.println(userDetail);
						userDetail = userDetail.substring(1, userDetail.length() - 1);
						String[] str = userDetail.split(",");
						UserLocation userLocation = new UserLocation(str[0], str[1], str[2]);
						dao.saveorUpdateUserLocation(userLocation);
						System.out.println("Inserted Data Done");
					}
				}

			}
		});

		// Start the computation
		jssc.start();

		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
