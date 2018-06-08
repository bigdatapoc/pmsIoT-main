package com.hcl.pmsiot.dashboard.dao;

import java.util.List;

import com.hcl.pmsiot.dashboard.model.LocationDetail;

public interface LocationDetailDao {

	List<LocationDetail> getAllLocation();

	LocationDetail getLocationByName(String name);

	LocationDetail getLocationById(String id);
	
}
