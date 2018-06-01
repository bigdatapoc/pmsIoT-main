package com.hcl.pmsiot.dashboard.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_detail")
public class UserDetail {
	
	private String _id;

	@Indexed(unique = true)
	private String userId;
	
	private double latitude;
	
	private double longitude;

	private Date lastKnown;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public Date getLastKnown() {
		return lastKnown;
	}

	public void setLastKnown(Date lastKnown) {
		this.lastKnown = lastKnown;
	}

	


}
