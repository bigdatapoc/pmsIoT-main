package com.hcl.pmsiot.dashboard.Model;

import org.springframework.data.mongodb.core.mapping.Document;

//Entity class to map object of Mongodb 
@Document(collection = "UserLocation")
public class UserLocation {
	
	private String userId;
	private String latitude;
	private String longitude;

	public UserLocation() {
		// TODO Auto-generated constructor stub
	}

	public UserLocation(String userId, String latitude, String longitude) {
		super();
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


}
