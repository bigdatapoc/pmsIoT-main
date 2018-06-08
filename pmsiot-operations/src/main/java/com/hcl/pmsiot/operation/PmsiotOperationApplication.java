package com.hcl.pmsiot.operation;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.hcl.pmsiot.operation.service.UserDetailService;


@Configuration
@SpringBootApplication
public class PmsiotOperationApplication implements CommandLineRunner{

	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${kafka.broker.host.url}")
	private String bootstrapServers;
	
	@Autowired
	UserDetailService userDetailService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(PmsiotOperationApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
		
		userDetailService.startStreaming();
		
    }
	
	@Bean
	public SparkConf sparkConf() {
		SparkConf sparkConf = new SparkConf()
				.setAppName("operation")
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
