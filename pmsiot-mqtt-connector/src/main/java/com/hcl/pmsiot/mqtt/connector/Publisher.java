package com.hcl.pmsiot.mqtt.connector;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.hcl.pmsiot.mqtt.constants.Constants;

public class Publisher {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws MqttException {

		String mqttClientUrl = Constants.MQTT_Client_URL;
		String topicName = Constants.Topic_MQTT;
		MqttClient client = new MqttClient(mqttClientUrl, MqttClient.generateClientId());
		client.setCallback(new SimpleMqttCallBack()); // SETTING CALLBACK => Will Work After This Function Is Done.
		client.connect(); // CONNECTING TO TARGET
		// Input:: Message from Users

		while(true ) {
			Scanner console = new Scanner(System.in);
			System.out.println("Please enter message :: ");
	
			String message = console.nextLine();
	
			// Convert Message in Bytes
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setPayload(message.getBytes());
	
			// MQTT Client
			
			client.publish(topicName, mqttMessage); // PUBLISHING MESSAGE ON TARGET
			
			System.out.println("Message :: " + message + " , topic ::" + topicName);
		}

		// client.disconnect();

	}

}
