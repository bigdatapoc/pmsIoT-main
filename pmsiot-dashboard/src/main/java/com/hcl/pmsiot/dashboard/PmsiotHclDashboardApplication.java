package com.hcl.pmsiot.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
@SpringBootApplication
public class PmsiotHclDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmsiotHclDashboardApplication.class, args);
	}
	
	
	@Bean
    public MongoDbFactory mongoDbFactory() throws Exception {

        MongoClient mongoClient = new MongoClient("192.168.1.54", 27017);
        //UserCredentials userCredentials = new UserCredentials("", "");
        return new SimpleMongoDbFactory(mongoClient, "campus");
    }


    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
}
