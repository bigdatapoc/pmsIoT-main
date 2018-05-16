package com.hcl.processing

object ScalaConstants {
  
	  val Kafka_Zookeeper_URL : String = "localhost:2181";
    val Kafka_App_Name : String = "spark-streaming-consumer-group";
    val Kafka_Topic : String = "iot_data";
    val Kafka_Broker_URI : String = "localhost:9092";
    val Key_Serializer : String = "org.apache.kafka.common.serialization.StringSerializer";
    val Value_Serializer : String = "org.apache.kafka.common.serialization.StringSerializer";
    val Kafka_Processed_Topic : String = "processed_data";
       
}
    