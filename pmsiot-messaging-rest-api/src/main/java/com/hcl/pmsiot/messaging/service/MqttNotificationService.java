package com.hcl.pmsiot.messaging.service;

import java.text.MessageFormat;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hcl.pmsiot.messaging.data.NotificationData;

@Service
public class MqttNotificationService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MqttMessage mqttMessage;

	@Autowired
	private MqttClient mqttClient;
	
	@Value("${mqtt.broker.topic.user.notification}")
	private String mqttUserNotificationTopic;
	
	public void sendUserNotification(NotificationData userNotificationData) {
		try {
			Gson gson = new Gson();
			String message = gson.toJson(userNotificationData);
			LOGGER.debug("Received :- " + message);
			mqttClient.connect();
			mqttMessage.setPayload(message.getBytes());
			for(String userId : userNotificationData.getTo().split(",")) {
				mqttClient.publish(MessageFormat.format(mqttUserNotificationTopic, userId), mqttMessage);
			}

		} catch (MqttException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());

		} finally {
			try {
				mqttClient.disconnect();
			} catch (MqttException e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
