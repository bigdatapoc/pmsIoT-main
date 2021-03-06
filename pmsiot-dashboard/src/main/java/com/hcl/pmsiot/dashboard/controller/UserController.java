package com.hcl.pmsiot.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.pmsiot.dashboard.data.NotificationData;
import com.hcl.pmsiot.dashboard.data.DashboardResponse;
import com.hcl.pmsiot.dashboard.data.UserDetailData;
import com.hcl.pmsiot.dashboard.service.UserService;
import com.hcl.pmsiot.dashboard.util.DashboardConstant;

@RestController
@CrossOrigin( origins = {"http://192.168.1.59:4200", "http://localhost:8074", "http://localhost:4200"})
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardResponse> getAllUser() {

		List<UserDetailData> userList = userService.getAllUser();
		DashboardResponse dashboardResponse = new DashboardResponse();
		dashboardResponse.setData(userList);
		dashboardResponse.setStatus(DashboardConstant.statusSucess);

		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardResponse> getEmployeeById(@PathVariable("userid") String userId) {

		UserDetailData userDetail = userService.getUserById(userId);
		DashboardResponse dashboardResponse = new DashboardResponse();
		if (userDetail != null) {
			dashboardResponse.setData(userDetail);
			dashboardResponse.setStatus(DashboardConstant.statusSucess);
		} else {
			dashboardResponse.setStatus(DashboardConstant.statusFailure);
			dashboardResponse.setMessage("No empolyee found with Sap-Id " + userId);
		}
		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/{userid}/notify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardResponse> sendUserNotification(@PathVariable("userid") String userId, @RequestBody(required=true) NotificationData notificationData) {

		DashboardResponse dashboardResponse = new DashboardResponse();
		boolean response = userService.sendUserNotification(notificationData);
		if(response)
			dashboardResponse.setStatus(DashboardConstant.statusSucess);
		else
			dashboardResponse.setStatus(DashboardConstant.statusFailure);
		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
	}
}
