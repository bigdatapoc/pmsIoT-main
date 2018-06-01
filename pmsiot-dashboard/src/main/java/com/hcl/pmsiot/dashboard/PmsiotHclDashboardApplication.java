package com.hcl.pmsiot.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication
public class PmsiotHclDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmsiotHclDashboardApplication.class, args);
	}
	
	//creteded  mongo client to connect mongodb
	/*@Bean
    public MongoDbFactory mongoDbFactory() throws Exception {

        MongoClient mongoClient = new MongoClient("192.168.99.100",27017);
        //UserCredentials userCredentials = new UserCredentials("", "");
        return new SimpleMongoDbFactory(mongoClient, "campus");
    }

	//creteded Mongo Templete using Mongo client  to connect mongodb
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }*/
}
