package com.hcl.pmsiot.dashboard.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hcl.pmsiot.dashboard.dao.LocationDetailDao;
import com.hcl.pmsiot.dashboard.model.LocationDetail;

@Repository
public class LocationDetailDaoImpl implements LocationDetailDao {

	@Autowired
	private MongoTemplate mongotemplate;

	@Override
	public List<LocationDetail> getAllLocation() {
		
		return mongotemplate.findAll(LocationDetail.class);
	}
	
	@Override
	public LocationDetail getLocationByName(String name) {
		
		return mongotemplate.findOne(new Query(Criteria.where("name").is(name)), LocationDetail.class);
	}
}
