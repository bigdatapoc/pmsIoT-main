package com.hcl.pmsiot.dashboard.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.hcl.pmsiot.dashboard.data.NotificationData;
import com.hcl.pmsiot.dashboard.data.UserDetailData;

public interface UserService {

	List<UserDetailData> getAllUser();

	UserDetailData getUserById(String userId);

	boolean sendUserNotification(NotificationData notificationData);

	boolean sendLocationUserNotification(String locationId, NotificationData notificationData);

}
