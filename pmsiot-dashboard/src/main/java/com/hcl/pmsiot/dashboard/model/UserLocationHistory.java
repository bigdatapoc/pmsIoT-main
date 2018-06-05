package com.hcl.pmsiot.dashboard.model;

import java.util.Date;

public class UserLocationHistory {

	
	public UserLocationHistory(Boundary coordinate, Date timestamp) {
		super();
		this.coordinate = coordinate;
		this.timestamp = timestamp;
	}

	private Boundary coordinate;
	
	private Date timestamp;

	public Boundary getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Boundary coordinate) {
		this.coordinate = coordinate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
