package com.hcl.pmsiot.messaging.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaConsumer {

	private static ConsumerConnector consumerConnector = null;

    public static void initialize() {
          Properties props = new Properties();
          props.put("zookeeper.connect", "localhost:2181");
          props.put("group.id", "testgroup");
          props.put("zookeeper.session.timeout.ms", "400");
          props.put("zookeeper.sync.time.ms", "300");
          props.put("auto.commit.interval.ms", "1000");
          ConsumerConfig conConfig = new ConsumerConfig(props);
          consumerConnector = Consumer.createJavaConsumerConnector(conConfig);
    }

    public static void consumeMessage(String topic) {
    		initialize();
          Map<String, Integer> topicCount = new HashMap<String, Integer>();       
          topicCount.put(topic, new Integer(1));
         
          Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams =
                consumerConnector.createMessageStreams(topicCount);         
         
          List<KafkaStream<byte[], byte[]>> kStreamList =
                                               consumerStreams.get(topic);
          for (final KafkaStream<byte[], byte[]> kStreams : kStreamList) {
                 ConsumerIterator<byte[], byte[]> consumerIte = kStreams.iterator();
                
                 while (consumerIte.hasNext())
                        System.out.println("Message consumed from topic [" + topic + "] : "+ new String(consumerIte.next().message()));              
          }
          if (consumerConnector != null)   
        	  consumerConnector.shutdown();          
    }
    
}
