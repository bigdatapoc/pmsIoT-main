package com.hcl.pmsiot.mqtt.connector;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.hcl.pmsiot.mqtt.constants.Constants;

public class AdminSubscriber {

	public static void main(String[] args) throws MqttException {

		System.out.println("== Local SUBSCRIBER STARTED ==");
		int qos = 2;
		//MqttConnectOptions conOpt = new MqttConnectOptions();
		//conOpt.setCleanSession(true);
		MqttClient client = new MqttClient(Constants.MqttClientUrl, MqttClient.generateClientId());
		client.connect();
		client.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println(topic);
				System.out.println(message);
				
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionLost(Throwable cause) {
				// TODO Auto-generated method stub
				
			}
		});
		client.subscribe(new String[] {"admin/notification", "user/#"});
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
