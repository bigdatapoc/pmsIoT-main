package com.hcl.pmsiot.mqtt.connector;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publisher {


  public static void main(String[] args) throws MqttException {
	  
    String mqttClientUrl = "tcp://192.168.1.13:1883";
    @SuppressWarnings("resource")
	Scanner console = new Scanner(System.in);
    
    System.out.println("Please eneter topic name :: ");
    String topicName = console.nextLine();
    System.out.println("Please eneter message :: ");
    String messag = console.nextLine();
   /* MessageDTO messageDTO = new MessageDTO();
    messageDTO.setMessage(messag);
    messageDTO.setTopic(topicName);*/
   // String messageString = Arrays.toString(args);


    MqttClient client = new MqttClient(mqttClientUrl, MqttClient.generateClientId());
    client.connect();
    MqttMessage message = new MqttMessage();
    message.setPayload(messag.getBytes());
    client.publish(topicName, message);

    System.out.println("Message :: "+ messag +" , topic ::"+topicName);

    //client.disconnect();

  }


}
