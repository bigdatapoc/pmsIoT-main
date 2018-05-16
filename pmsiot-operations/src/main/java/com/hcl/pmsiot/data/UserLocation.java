package com.hcl.pmsiot.data;

public class UserLocation {

	public UserLocation() {
		// TODO Auto-generated constructor stub
	}
	
	public UserLocation(String latitude, String longitude, String userId) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.userId = userId;
	}
	
	private String userId;
	
	private String latitude;
	
	private String longitude;

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
