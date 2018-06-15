package com.hcl.pmsiot.mqtt.connector.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttSubscriberService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MqttClient mqttClient;
	
	@Value("${mqtt.broker.topic.user.location}")
	private String mqttUserLocationTopic;
	
	@Autowired
	private UserLocationMqttCallBack userLocationMqttCallBack;
	
	public void subscriber() throws MqttException {

		LOGGER.debug("MQTT SUBSCRIBER STARTED ");
		mqttClient.connect();
		mqttClient.setCallback(userLocationMqttCallBack);
		mqttClient.subscribe(mqttUserLocationTopic);
		//mqttClient.disconnect();

	}
}
