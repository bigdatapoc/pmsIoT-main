package com.hcl.pmsiot.mqtt.connector;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.hcl.pmsiot.mqtt.constants.Constants;

public class SimpleMqttCallBack implements MqttCallback {

	public void connectionLost(Throwable throwable) {
		System.out.println("Exception::: " + throwable.getMessage());
		System.out.println("Connection to MQTT broker lost!");
	}

	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
		System.out.println("Message received:::: \t" + new String(mqttMessage.getPayload()));

		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setMessage(new String(mqttMessage.getPayload()));
		messageDTO.setTopic(Constants.MqttTopic);
		

		// final String uri = "http://localhost:12345/mqttKafkaRestApp/addMqttMsg"; h1
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<MessageDTO> request = new HttpEntity<MessageDTO>(messageDTO, headers);
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		String response = null;
		try {
			response = restTemplate.postForObject(Constants.UrlRestApi, request, String.class);
			System.out.println(response);
		} catch (Exception e) {
			System.out.println("Exception in Rest call ::: " + e.getMessage());
		}

		System.out.println(response);
		
		if(mqttMessage!=null)
		{
			
		}
		
	
	}

	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		System.out.println("delievered : " + iMqttDeliveryToken.toString());
		System.out.println("delievered : " + iMqttDeliveryToken.getMessageId());
		System.out.println("delievered : " + iMqttDeliveryToken.getResponse());
			
	}

}
