package com.hcl.pmsiot.mqtt.connector.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.hcl.pmsiot.mqtt.connector.data.UserDetailData;
import com.hcl.pmsiot.mqtt.constants.Constants;

@Service
public class UserLocationMqttCallBack implements MqttCallback {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Value("${pmsiot.messaging.host}")
	private String messaginghostUrl;
	
	public void connectionLost(Throwable throwable) {
		LOGGER.error("MQTT Connection Lost", throwable);
	}

	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

		String message = new String(mqttMessage.getPayload());
		LOGGER.debug("Message received:::: \t" + message);

		Gson gson = new Gson();
		UserDetailData userDetail = gson.fromJson(message, UserDetailData.class);

		// final String uri = "http://localhost:12345/mqttKafkaRestApp/addMqttMsg"; h1
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UserDetailData> request = new HttpEntity<UserDetailData>(userDetail, headers);
		//restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		String response = null;
		try {
			response = restTemplate.postForObject(messaginghostUrl+Constants.userLocationRestApi, request, String.class);
			LOGGER.debug(response);
		} catch (Exception e) {
			LOGGER.error("Exception in Rest call :::", e);
			LOGGER.debug("Exception in Rest call ::: " + e.getMessage());
		}

	}

	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		LOGGER.debug("delievered : " + iMqttDeliveryToken.toString());
		LOGGER.debug("delievered : " + iMqttDeliveryToken.getMessageId());
		LOGGER.debug("delievered : " + iMqttDeliveryToken.getResponse());

	}

}
