package com.hcl.pmsiot.dashboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.dashboard.Dao.UserLocationDao;
import com.hcl.pmsiot.dashboard.Model.UserLocation;


@Service
public class UserService {
	
	    @Autowired
		private UserLocationDao userLocationDao;
		
		
		//service methode to provide list of employee using Mongotemplete
		public List<UserLocation> getAllEmployeeService() {
			
			return userLocationDao.getAllEmployeeDao();
			
		}
		
		//service methode to provide employee object using Mongotemplete
		public UserLocation getEmployeeByIdService(String userId) {
			
			return userLocationDao.getEmployeeByIdDao(userId);
			
		}
		

}
