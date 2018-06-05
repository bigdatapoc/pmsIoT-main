package com.hcl.pmsiot.operation;

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

import com.hcl.pmsiot.operation.service.UserDetailService;


@Configuration
@SpringBootApplication
public class PmsiotOperationApplication implements CommandLineRunner{

	@Value("${spring.application.name}")
	private String appName;
	
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
}
