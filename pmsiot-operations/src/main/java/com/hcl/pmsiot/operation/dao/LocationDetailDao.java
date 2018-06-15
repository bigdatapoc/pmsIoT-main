package com.hcl.pmsiot.operation.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hcl.pmsiot.operation.model.LocationDetail;

@Repository
public class LocationDetailDao  {

	@Autowired
	private MongoTemplate mongotemplate;

	public List<LocationDetail> getAllLocation() {
		
		return mongotemplate.findAll(LocationDetail.class);
	}
	
	public LocationDetail getLocationByName(String name) {
		
		return mongotemplate.findOne(new Query(Criteria.where("name").is(name)), LocationDetail.class);
	}
	
	public LocationDetail getLocationMaster() {
		
		return mongotemplate.findOne(new Query(Criteria.where("master").is(true)), LocationDetail.class);
	}
}
