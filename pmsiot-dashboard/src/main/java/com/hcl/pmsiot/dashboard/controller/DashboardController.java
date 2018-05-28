package com.hcl.pmsiot.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.pmsiot.dashboard.Model.BuildingBoundary;
import com.hcl.pmsiot.dashboard.Model.UserLocation;
import com.hcl.pmsiot.dashboard.service.UserService;


@RestController
public class DashboardController {

	@Autowired
	private UserService userLocationService;
	
	
	//This End point returns list of Employees
	@RequestMapping(value = "/allemployee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserLocation>>getAllEmployee(){
		
		
		List<UserLocation> employeeList = userLocationService.getAllEmployeeService();
		
		if (!employeeList.isEmpty()) {
				return new ResponseEntity<>(employeeList, HttpStatus.OK);
		    }else{
		    	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
	
	}
	
	//This End point returns object of Employee according to employee id
	@RequestMapping(value = "/byid/{userid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserLocation> getEmployeeById(@PathVariable("userid") String userId) {
		
		UserLocation userlocation = userLocationService.getEmployeeByIdService(userId);
		return  new ResponseEntity<>(userlocation, HttpStatus.OK);
	
	}
	
	@RequestMapping(value = "/allbuildings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BuildingBoundary>> getAlluilding() {
		
		List<BuildingBoundary> buildingsList = userLocationService.getBuildingsService();
		return new ResponseEntity<>(buildingsList, HttpStatus.OK);
	
	}
}
