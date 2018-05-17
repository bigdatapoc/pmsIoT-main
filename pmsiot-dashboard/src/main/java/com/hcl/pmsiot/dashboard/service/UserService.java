package com.hcl.pmsiot.dashboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.dashboard.Model.UserLocation;


@Service
public class UserService {
	
	    @Autowired
		private MongoTemplate mongotemplate;
		
		//@Autowired
		//private MongoCrudRepository crudRepository;
		
		public List<UserLocation> getAllEmployeeService() {
			
			return mongotemplate.findAll(UserLocation.class);
			
		}
		
		public UserLocation getEmployeeByIdService(String userId) {
			
			return mongotemplate.findOne(new Query(Criteria.where("userId").is(userId)), UserLocation.class,"UserLocation");
			
		}
		

}
