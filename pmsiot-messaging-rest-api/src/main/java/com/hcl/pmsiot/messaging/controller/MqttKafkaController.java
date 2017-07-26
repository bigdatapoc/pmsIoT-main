package com.hcl.pmsiot.messaging.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.pmsiot.messaging.dto.MessageDTO;
import com.hcl.pmsiot.messaging.utility.KafkaConsumer;
import com.hcl.pmsiot.messaging.utility.KafkaProducer;


@RestController
public class MqttKafkaController {

	
	@RequestMapping(value = "/pubSubMqttToKafka", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addMqttMsg(@RequestBody MessageDTO messageDTO) throws Exception {
		ResponseEntity<?> responseEntity = null;
		KafkaProducer.publishMesssage(messageDTO);
		KafkaConsumer.consumeMessage(messageDTO.getTopic());
		responseEntity = new ResponseEntity<>("successfully published and consumed msg", HttpStatus.OK);
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/getMqttMsg", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMqttMsg() {
		ResponseEntity<?> responseEntity = null;
		
		responseEntity = new ResponseEntity<>("success msg", HttpStatus.OK);
		return responseEntity;
	}
}
