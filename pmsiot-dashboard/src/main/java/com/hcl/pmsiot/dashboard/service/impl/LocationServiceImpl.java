package com.hcl.pmsiot.dashboard.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.pmsiot.dashboard.dao.LocationDetailDao;
import com.hcl.pmsiot.dashboard.data.BoundaryData;
import com.hcl.pmsiot.dashboard.data.LocationDetailData;
import com.hcl.pmsiot.dashboard.model.Boundary;
import com.hcl.pmsiot.dashboard.model.LocationDetail;
import com.hcl.pmsiot.dashboard.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	LocationDetailDao locationDetailDao;

	@Override
	public List<LocationDetailData> getAllLocation() {

		List<LocationDetail> ldList = locationDetailDao.getAllLocation();
		List<LocationDetailData> ldDataList = new ArrayList<>();
		if (ldList != null && ldList.size() > 0) {
			ldList.forEach(locationDetail -> {
				BoundaryData boundList[] = new BoundaryData[locationDetail.getBoudary().length];
				for (int i = 0; i < locationDetail.getBoudary().length; i++) {
					Boundary boundary = locationDetail.getBoudary()[i];
					boundList[i] = (new BoundaryData(boundary.getLatitude(), boundary.getLongitude()));
				}
				ldDataList.add(new LocationDetailData.LocationDetailBuilder().setCapacity(locationDetail.getCapacity())
						.setLatitude(locationDetail.getLatitude()).setLongitude(locationDetail.getLongitude())
						.setName(locationDetail.getName()).setBoudary(boundList).getLocationDetailData());
			});
		}
		return ldDataList;
	}

}
