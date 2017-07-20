package com.hcl.pmsiot;

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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

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
	public static String topics = "test";
	public static String mongoHost = "192.168.2.68";
	public static int mongoPort = 27017;

	public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount").setMaster("local[2]")
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

		data.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			@Override
			public void call(JavaRDD<String> data) throws Exception {
				Mongo mongo = new Mongo(mongoHost, mongoPort);
				DB db = mongo.getDB("pmsdb");

				DBCollection collection = db.getCollection("TestCollection");
				if (data != null) {
					List<String> result = data.collect();

					for (String latLongs : result) {

						System.out.println(latLongs);

						String[] latLongArray = latLongs.split(",");

						DBObject dbObject = (DBObject) JSON.parse(
								"{'latitute':'" + latLongArray[0] + " ', 'longitude':'" + latLongArray[1] + "'}");

						collection.insert(dbObject);
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
