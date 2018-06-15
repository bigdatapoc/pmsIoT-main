package com.hcl.pmsiot.messaging.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingMqttPublisher {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MqttMessage mqttMessage;

	@Autowired
	private MqttClient mqttClient;
	
	
	
	public void publishMessage(String topic, String message) {
		try {
			LOGGER.debug("Received :- " + message);
			mqttClient.connect();
			mqttMessage.setPayload(message.getBytes());
			mqttClient.publish(topic, mqttMessage);
			

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
