package com.hcl.pmsiot.messaging;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.hcl.pmsiot.messaging.service.AdminMessagingKafkaConsumer;
import com.hcl.pmsiot.messaging.service.UserMessagingKafkaConsumer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.apache.kafka.clients.producer.ProducerConfig;

@SpringBootApplication
@EnableSwagger2
public class MqttKafkaRestAppApplication implements CommandLineRunner{

	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${mqtt.broker.host.url}")
	private String mqttClientUrl;
	
	@Autowired
	private AdminMessagingKafkaConsumer adminMessagingService;
	
	@Autowired
	private UserMessagingKafkaConsumer userMessagingService;

	@Value("${kafka.broker.host.url}")
	private String bootstrapServers;
	
	public static void main(String[] args) {
		SpringApplication.run(MqttKafkaRestAppApplication.class, args);
		
	}
	
	@Bean
	public SparkConf sparkConf() {
		SparkConf sparkConf = new SparkConf()
				.setAppName(appName)
				.setMaster("local[10]")
				.set("spark.executor.memory", "1g")
				.set("spark.testing.memory", "471859200");
		return sparkConf;
	}
	
	@Bean
	public  JavaStreamingContext javaStreamingContext(){
	    return new JavaStreamingContext(sparkConf(), new Duration(2000));
	}

	@Bean
	public MqttMessage getMqttMessage() {
		return new MqttMessage();
	}
	
	@Bean
	public MqttClient getMqttClient() throws MqttException {
		return new MqttClient(mqttClientUrl, MqttClient.generateClientId());
	}
	
	@Override
	public void run(String... args) throws Exception {
		adminMessagingService.startStreaming();
		//userMessagingService.startStreaming();
	}
	
	@Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
	
}
