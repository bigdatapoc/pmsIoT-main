package com.hcl.pmsiot.dashboard.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.dashboard.Model.BuildingBoundary;
import com.hcl.pmsiot.dashboard.Model.UserLocation;

@Service
public class UserLocationDao {

	
	    @Autowired
		private MongoTemplate mongotemplate;
		
	 
	    public List<UserLocation> getAllEmployeeDao() {
			
			return mongotemplate.findAll(UserLocation.class);
			
		}
	    
        public UserLocation getEmployeeByIdDao(String userId) {
			
			return mongotemplate.findOne(new Query(Criteria.where("userId").is(userId)), UserLocation.class,"UserLocation");
			
		}
        
        public List<BuildingBoundary> getBuildingsDao() {
			
			return mongotemplate.findAll(BuildingBoundary.class);
			
		}
}
