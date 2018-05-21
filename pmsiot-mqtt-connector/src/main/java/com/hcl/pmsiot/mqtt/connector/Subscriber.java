package com.hcl.pmsiot.mqtt.connector;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.hcl.pmsiot.mqtt.constants.Constants;

public class Subscriber {

	public static void main(String[] args) throws MqttException {

		System.out.println("== SUBSCRIBER STARTED ==");
		int qos = 2;
		//MqttConnectOptions conOpt = new MqttConnectOptions();
		//conOpt.setCleanSession(true);
		MqttClient client = new MqttClient(Constants.MqttClientUrl, MqttClient.generateClientId());
		client.connect();
		client.setCallback(new SimpleMqttCallBack());
		client.subscribe(Constants.MqttTopic);
		//client.connect(conOpt);
		//client.unsubscribe(topic);
		
		//System.out.println("Reading ........ Please enter to exit!!!!");
		/*try {
			System.in.read();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		client.disconnect();*/

	}
}
