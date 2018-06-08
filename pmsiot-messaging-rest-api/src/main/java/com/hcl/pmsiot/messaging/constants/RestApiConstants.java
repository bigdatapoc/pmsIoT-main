package com.hcl.pmsiot.messaging.constants;

public class RestApiConstants {

	
	
	// MQTT Constants
	public static final String TopicMQTT = "processed_data";

	// Kafka Constants
	public static final String KafkaBrokerUri = "192.168.99.100:9092";
	public static final String SerializerClass = "kafka.serializer.StringEncoder";
	

	// Zookeeper Constants
	public static final String ZookeeperUri = "192.168.99.100:2181";
	public static final String ZookeeperSessionTimeout = "400";
	public static final String ZookeeperSyncTime = "300";
	public static final String ZookeeperAutoCommit = "1000";
	public static final String GroupId = "testgroup";

}
