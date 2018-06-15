package com.hcl.pmsiot.messaging.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.messaging.data.NotificationData;
import com.hcl.pmsiot.messaging.data.UserDetailData;

@Service
public class MessagingService {

	@Autowired
	MessagingKafkaPublisher messagingKafkaPublisher;
	
	@Autowired
	MessagingMqttPublisher  messagingMqttPublisher;
	
	@Value("${kafka.broker.topic.user.location}")
	private String userLocationTopic;
	
	@Value("${mqtt.broker.topic.user.notification}")
	private String mqttUserNotificationTopic;
	
	public void publishUserLocation(UserDetailData userDetail) throws Exception {
		messagingKafkaPublisher.publishMesssage(userLocationTopic, userDetail.toString());
	}
	
	public void sendUserNotification(NotificationData notificationData) {
		for(String userId : notificationData.getTo().split(",")) {
			messagingMqttPublisher.publishMessage(MessageFormat.format(mqttUserNotificationTopic, userId), notificationData.toString());
		}
	}
}
