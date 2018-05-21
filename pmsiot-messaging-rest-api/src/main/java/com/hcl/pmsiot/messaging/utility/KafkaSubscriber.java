package com.hcl.pmsiot.messaging.utility;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.hcl.pmsiot.messaging.constants.RestApiConstants;

import java.util.Collections;
import java.util.Properties;
/**
* Kafka Consumer with Example Java Application
*/
public class KafkaSubscriber extends ShutdownableThread {
    private final KafkaConsumer<String, String> consumer;
    private final String topic;

    public KafkaSubscriber(String topic) {
        super("KafkaConsumerExample", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, RestApiConstants.KafkaServerHost+ ":" + RestApiConstants.KafkaServerPort);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, RestApiConstants.GroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    @Override
    public void doWork() {
        consumer.subscribe(Collections.singletonList(this.topic));
        ConsumerRecords<String, String> records = consumer.poll(1000);
        String mqttClientUrl = RestApiConstants.MQTTClientURL;
		String topicName = RestApiConstants.TopicMQTT;

		MqttClient client = null;
		try {
			client = new MqttClient(mqttClientUrl, MqttClient.generateClientId());
			client.connect();
			
	        for (ConsumerRecord<String, String> record : records) {
	            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
	            MqttMessage message = new MqttMessage();
	    		message.setPayload(record.value().getBytes());
	    		String userId = record.key();
	    		topicName = topicName + "/"+ userId;
	    		client.publish(topicName, message);
	        }
        
		} catch (MqttException e) {
			e.printStackTrace();
		}
		finally {
			try {
				client.disconnect();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }
}
