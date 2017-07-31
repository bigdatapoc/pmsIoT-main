package com.hcl.pmsiot.mqtt.connector;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Subscriber {

 /* public static void main(String[] args) throws MqttException {
	String mqttClientUrl = "tcp://192.168.1.13:1883";
	String topic = "iot_data";
    System.out.println("== START SUBSCRIBER ==");

    MqttClient client=new MqttClient(mqttClientUrl, MqttClient.generateClientId());
    client.setCallback( new SimpleMqttCallBack() );
    client.connect();

    client.subscribe(topic);

  }*/
  
  public static void main() throws MqttException {
		String mqttClientUrl = "tcp://192.168.1.13:1883";
		String topic = "iot_data";
	    System.out.println("== START SUBSCRIBER ==");

	    MqttClient client=new MqttClient(mqttClientUrl, MqttClient.generateClientId());
	    client.setCallback( new SimpleMqttCallBack() );
	    client.connect();

	    client.subscribe(topic);

	  }

}
