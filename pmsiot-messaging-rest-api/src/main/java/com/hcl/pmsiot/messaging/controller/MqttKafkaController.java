package com.hcl.pmsiot.messaging.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.pmsiot.messaging.data.NotificationData;
import com.hcl.pmsiot.messaging.data.UserDetailData;
import com.hcl.pmsiot.messaging.service.MessagingService;

@RestController
public class MqttKafkaController {

	@Autowired
	MessagingService messagingService;
	
	@RequestMapping(value = "/user/location", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> userLocation(@RequestBody UserDetailData userDetail) throws Exception {
		
		ResponseEntity<?> responseEntity = null;
		messagingService.publishUserLocation(userDetail);
		responseEntity = new ResponseEntity<>("successfully published and consumed msg", HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/getMqttMsg", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMqttMsg() {
		ResponseEntity<?> responseEntity = null;

		responseEntity = new ResponseEntity<>("success msg", HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "/user/notification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendNotification(@RequestBody NotificationData notificationData) {
		ResponseEntity<?> responseEntity = null;
		messagingService.sendUserNotification(notificationData);
		responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
		return responseEntity;
	}
}
