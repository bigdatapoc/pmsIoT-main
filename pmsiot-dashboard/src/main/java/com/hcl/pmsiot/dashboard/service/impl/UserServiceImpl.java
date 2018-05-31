package com.hcl.pmsiot.dashboard.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.dashboard.dao.UserDetailDao;
import com.hcl.pmsiot.dashboard.data.UserDetailData;
import com.hcl.pmsiot.dashboard.model.UserDetail;
import com.hcl.pmsiot.dashboard.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDetailDao userDetailDao;

	@Override
	public List<UserDetailData> getAllUser() {

		List<UserDetailData> userDetailList = new ArrayList<>();

		List<UserDetail> udList = userDetailDao.getAllUsers();
		if (udList != null && udList.size() > 0) {
			udList.forEach(ud -> {
				userDetailList.add(new UserDetailData.UserDetailBuilder().setLastKnown(ud.getLastKnown())
						.setLatitude(ud.getLatitude()).setLongitude(ud.getLongitude()).setUserId(ud.getUserId())
						.getUserDetailData());
			});
		}

		return userDetailList;
	}

	@Override
	public UserDetailData getUserById(String userId) {

		UserDetailData userDetailData = null;
		UserDetail ud = userDetailDao.getUserById(userId);
		if (ud != null) {
			userDetailData = new UserDetailData.UserDetailBuilder().setLastKnown(ud.getLastKnown())
					.setLatitude(ud.getLatitude()).setLongitude(ud.getLongitude()).setUserId(ud.getUserId())
					.getUserDetailData();
		}
		return userDetailData;

	}

}
