package com.hcl.pmsiot.dashboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.hcl.pmsiot.dashboard.data.DashboardResponse;
import com.hcl.pmsiot.dashboard.data.LocationDetailData;
import com.hcl.pmsiot.dashboard.data.NotificationData;
import com.hcl.pmsiot.dashboard.service.LocationService;
import com.hcl.pmsiot.dashboard.service.UserService;
import com.hcl.pmsiot.dashboard.util.DashboardConstant;

@RestController
@CrossOrigin( origins = {"http://localhost:4200", "http://localhost:8074", "http://192.168.1.59:4200"})
@RequestMapping(value = "/location")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardResponse> getAllLocation() {

		DashboardResponse dashboardResponse = new DashboardResponse();
		List<LocationDetailData> locationList = locationService.getAllLocation();
		dashboardResponse.setData(locationList);
		dashboardResponse.setStatus(DashboardConstant.statusSucess);
		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}/count")
	public ResponseEntity<DashboardResponse> getLocationCount(@PathVariable("id") String id) {

		DashboardResponse dashboardResponse = new DashboardResponse();
		
		int count = locationService.noOfUserInLocation(id);
		Map<String, String> data = new HashMap<>();
		data.put("count", ""+count);
		dashboardResponse.setData(data);
		dashboardResponse.setStatus(DashboardConstant.statusSucess);
		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/{locationId}/notify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardResponse> sendNotification(@PathVariable("locationId") String locationId, @RequestBody(required= true) NotificationData notificationData ) {

		DashboardResponse dashboardResponse = new DashboardResponse();
		boolean response = userService.sendLocationUserNotification(locationId, notificationData);
		if(response)
			dashboardResponse.setStatus(DashboardConstant.statusSucess);
		else
			dashboardResponse.setStatus(DashboardConstant.statusFailure);
		
		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);

	}
}
