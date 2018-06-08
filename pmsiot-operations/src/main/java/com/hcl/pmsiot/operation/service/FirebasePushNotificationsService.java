package com.hcl.pmsiot.operation.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcl.pmsiot.operation.data.FirebasePushNotificationData;
 
@Service
public class FirebasePushNotificationsService {
 
	@Value("${firebase.server.key}")
	private String serverKey;
	
	@Value("${firebase.host.url}")
	private String hostUrl;
	
	@Async
	public CompletableFuture<String> send(HttpEntity<FirebasePushNotificationData> entity) {
 
		RestTemplate restTemplate = new RestTemplate();
 
		/**
		https://fcm.googleapis.com/fcm/send
		Content-Type:application/json
		Authorization:key=FIREBASE_SERVER_KEY*/
 
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + serverKey));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);
 
		String firebaseResponse = restTemplate.postForObject(hostUrl, entity, String.class);
 
		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
