package com.hcl.pmsiot.messaging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.messaging.data.MessageDTO;

@Service
public class AdminMessagingKafkaProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	 
     public void publishMesssage(MessageDTO messageDTO) throws Exception{ 
    	 kafkaTemplate.send(messageDTO.getTopic(), messageDTO.getMessage());
     }
     
}
