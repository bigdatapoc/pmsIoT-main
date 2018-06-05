package com.hcl.pmsiot.operation.model;

public class Boundary {
	
	public Boundary() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Boundary(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}


	private double latitude;
	
	private double longitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	

	

}
