package com.hcl.pmsiot.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.pmsiot.dashboard.data.DashboardResponse;
import com.hcl.pmsiot.dashboard.data.LocationDetailData;
import com.hcl.pmsiot.dashboard.service.LocationService;
import com.hcl.pmsiot.dashboard.util.DashboardConstant;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardResponse> getAllLocation() {
		
		DashboardResponse dashboardResponse = new DashboardResponse();
		List<LocationDetailData> locationList = locationService.getAllLocation();
		dashboardResponse.setData(locationList);
		dashboardResponse.setStatus(DashboardConstant.statusSucess);
		return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
	
	}
}
