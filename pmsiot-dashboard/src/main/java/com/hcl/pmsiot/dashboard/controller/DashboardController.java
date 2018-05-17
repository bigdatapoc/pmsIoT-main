package com.hcl.pmsiot.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.pmsiot.dashboard.Model.UserLocation;
import com.hcl.pmsiot.dashboard.service.UserService;


@RestController
public class DashboardController {

	@Autowired
	private UserService userLocationService;
	
	@RequestMapping(value = "/allemployee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserLocation> getAllEmployee() {
		return userLocationService.getAllEmployeeService();
	
	}
	
	
	@RequestMapping(value = "/byid/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserLocation getEmployeeById(@PathVariable("userid") String userId) {
		return userLocationService.getEmployeeByIdService(userId);
	
	}
}
