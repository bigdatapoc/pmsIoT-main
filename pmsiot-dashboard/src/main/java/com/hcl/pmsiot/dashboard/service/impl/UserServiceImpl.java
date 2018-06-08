package com.hcl.pmsiot.dashboard.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcl.pmsiot.dashboard.dao.LocationDetailDao;
import com.hcl.pmsiot.dashboard.dao.UserDetailDao;
import com.hcl.pmsiot.dashboard.data.BoundaryData;
import com.hcl.pmsiot.dashboard.data.NotificationData;
import com.hcl.pmsiot.dashboard.data.UserDetailData;
import com.hcl.pmsiot.dashboard.data.UserLocationHistoryData;
import com.hcl.pmsiot.dashboard.exception.DashboardException;
import com.hcl.pmsiot.dashboard.model.LocationDetail;
import com.hcl.pmsiot.dashboard.model.UserDetail;
import com.hcl.pmsiot.dashboard.model.UserLocationHistory;
import com.hcl.pmsiot.dashboard.service.UserService;
import com.hcl.pmsiot.dashboard.util.DashboardUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDetailDao userDetailDao;

	@Autowired
	LocationDetailDao locationDetailDao;
	
	@Value("${pmsiot.messaging.host}")
	private String messagingHostUrl;
	
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

	
	@Override
	public boolean sendUserNotification(NotificationData notificationData) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<NotificationData> request = new HttpEntity<>(notificationData);
		
		return restTemplate.postForObject(messagingHostUrl+"/user/notification", request, boolean.class);
		
	}
	
	@Override
	public  boolean sendLocationUserNotification(String locationId, NotificationData notificationData) {
		
		List<UserDetail> userList = getUsersInLocation(locationId);
		StringBuilder stringBuilder = new StringBuilder();
		userList.forEach(ud-> stringBuilder.append(ud.getUserId()).append(","));
		notificationData.setTo(stringBuilder.toString());
		return sendUserNotification(notificationData);
		
	}
	
	private List<UserDetail> getUsersInLocation(String locationId) {
		LocationDetail locationDetail = locationDetailDao.getLocationById(locationId);
		if(locationDetail == null)
			throw new DashboardException("No location found with id "+ locationId);
		List<UserDetail> userDetailList = userDetailDao.getAllUsers();
		
		List<UserDetail> presentUsers = null;
		if(userDetailList != null)
			presentUsers = userDetailList.stream().filter(ud-> DashboardUtil.containsLocation(Arrays.asList(locationDetail.getBoudary()), ud.getLatitude(), ud.getLongitude(), true)).collect(Collectors.toList());
		return presentUsers;
	}
	
	
}
