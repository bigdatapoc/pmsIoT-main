package com.hcl.pmsiot.dashboard.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.dashboard.dao.UserDetailDao;
import com.hcl.pmsiot.dashboard.data.BoundaryData;
import com.hcl.pmsiot.dashboard.data.UserDetailData;
import com.hcl.pmsiot.dashboard.data.UserLocationHistoryData;
import com.hcl.pmsiot.dashboard.model.UserDetail;
import com.hcl.pmsiot.dashboard.model.UserLocationHistory;
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
						.setLatitude(ud.getLatitude()).setLongitude(ud.getLongitude()).setUserId(ud.getUserId()).setOnline(ud.isOnline())
						.getUserDetailData());
			});
		}

		return userDetailList;
	}

	@Override
	public UserDetailData getUserById(String userId) {

		UserDetailData userDetailData = null;
		UserDetail ud = userDetailDao.getUserById(userId);
		final List<UserLocationHistoryData> locationHistoryDataList = new ArrayList<>();
		if (ud != null) {
			if(ud.getLocationHistory() != null && ud.getLocationHistory().size() > 0) {
				List<UserLocationHistory> uldList = ud.getLocationHistory();
				uldList.forEach(uld->{
					locationHistoryDataList.add(new UserLocationHistoryData(new BoundaryData(uld.getCoordinate().getLatitude(), uld.getCoordinate().getLongitude()), uld.getTimestamp()));
				});
			}
			userDetailData = new UserDetailData.UserDetailBuilder().setLastKnown(ud.getLastKnown())
					.setLatitude(ud.getLatitude()).setLongitude(ud.getLongitude()).setUserId(ud.getUserId()).setOnline(ud.isOnline()).setLocationHistory(locationHistoryDataList)
					.getUserDetailData();
		}
		return userDetailData;

	}

}
