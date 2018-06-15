package com.hcl.pmsiot.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingKafkaPublisher {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	 
     public void publishMesssage(String topic , String message) throws Exception{ 
    	 kafkaTemplate.send(topic, message);
     }
     
}
