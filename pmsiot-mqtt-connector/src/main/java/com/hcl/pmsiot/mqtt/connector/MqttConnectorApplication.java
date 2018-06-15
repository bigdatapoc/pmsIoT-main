package com.hcl.pmsiot.mqtt.connector;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hcl.pmsiot.mqtt.connector.service.MqttSubscriberService;

@Configuration
@SpringBootApplication
public class MqttConnectorApplication implements CommandLineRunner {

	@Value("${mqtt.broker.host.url}")
	private String mqttClientUrl;

	@Autowired
	private MqttSubscriberService mqttSubscriberService;
	
	public static void main(String[] args) {
		SpringApplication.run(MqttConnectorApplication.class, args);

	}
	
	@Override
	public void run(String... args) throws Exception {
		
		mqttSubscriberService.subscriber();
	}

	@Bean
	public MqttMessage getMqttMessage() {
		return new MqttMessage();
	}

	@Bean
	public MqttClient getMqttClient() throws MqttException {
		return new MqttClient(mqttClientUrl, MqttClient.generateClientId());
	}

	

}
