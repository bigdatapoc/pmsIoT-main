package com.hcl.pmsiot.dashboard.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hcl.pmsiot.dashboard.dao.UserDetailDao;
import com.hcl.pmsiot.dashboard.model.UserDetail;

@Repository
public class UserDetailDaoImpl implements UserDetailDao{

	@Autowired
	private MongoTemplate mongotemplate;

	public List<UserDetail> getAllUsers() {

		Query query = new Query();
		query.fields().exclude("locationHistory");
		return mongotemplate.findAll(UserDetail.class);

	}

	public UserDetail getUserById(String userId) {

		return mongotemplate.findOne(new Query(Criteria.where("userId").is(userId)), UserDetail.class);

	}

}
