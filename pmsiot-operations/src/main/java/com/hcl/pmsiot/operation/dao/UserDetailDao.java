package com.hcl.pmsiot.operation.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.hcl.pmsiot.operation.model.Boundary;
import com.hcl.pmsiot.operation.model.UserDetail;
import com.hcl.pmsiot.operation.model.UserLocationHistory;


@Repository
public class UserDetailDao {

	@Autowired
	private MongoTemplate mongotemplate;
	
	public  void updateUserLocation(UserDetail user) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(user.getUserId()));
		Update update = new Update();
		update.set("latitude", user.getLatitude());
		update.set("longitude", user.getLongitude());
		update.set("lastKnown", new Date());
		update.set("online",user.isOnline());
		UserLocationHistory locationHistory = new UserLocationHistory(new Boundary(user.getLatitude(), user.getLongitude()), new Date());
		update.addToSet("locationHistory", locationHistory);
		mongotemplate.updateFirst(query, update, UserDetail.class);
		
	}	
	
public  void updateUserAvailability(UserDetail user) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(user.getUserId()));
		Update update = new Update();
		update.set("online",user.isOnline());
		mongotemplate.updateFirst(query, update, UserDetail.class);
		
	}
	
	public UserDetail getUserById(String userId) {

		return mongotemplate.findOne(new Query(Criteria.where("userId").is(userId)), UserDetail.class, "UserLocation");

	}
	
	
}