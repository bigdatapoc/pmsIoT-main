package com.hcl.pmsiot.dashboard.service;

import java.util.List;

import com.hcl.pmsiot.dashboard.data.UserDetailData;

public interface UserService {

	List<UserDetailData> getAllUser();

	UserDetailData getUserById(String userId);

}
