package com.hcl.pmsiot.dashboard.dao;

import java.util.List;

import com.hcl.pmsiot.dashboard.model.UserDetail;

public interface UserDetailDao {

	List<UserDetail> getAllUsers();

	UserDetail getUserById(String userId);
}
