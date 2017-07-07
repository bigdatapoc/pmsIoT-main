package com.hcl.microservicepoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main class - Execution point
 * 
 * @author
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
public class FmsApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FmsApp.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FmsApp.class);
	}

}
