package com.hcl.pmsiot.dashboard.service;

import java.util.List;

import com.hcl.pmsiot.dashboard.data.LocationDetailData;

public interface LocationService {

	List<LocationDetailData> getAllLocation();

	int noOfUserInLocation(String buildingName);
}
