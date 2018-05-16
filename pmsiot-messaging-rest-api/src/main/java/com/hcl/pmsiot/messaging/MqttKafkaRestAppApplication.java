package com.hcl.pmsiot.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hcl.pmsiot.messaging.utility.KafkaSubscriber;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MqttKafkaRestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttKafkaRestAppApplication.class, args);
		KafkaSubscriber consumerThread = new KafkaSubscriber("processed_data");
        consumerThread.start();
	}
	
	
}
