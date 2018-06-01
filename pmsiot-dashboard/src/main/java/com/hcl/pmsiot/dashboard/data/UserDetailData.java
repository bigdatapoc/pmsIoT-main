package com.hcl.pmsiot.dashboard.data;

import java.util.Date;

public class UserDetailData {

	public UserDetailData(String userId, double latitude, double longitude, Date lastKnown) {
		super();
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.lastKnown = lastKnown;
	}

	private String userId;

	private double latitude;

	private double longitude;

	private Date lastKnown;

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

	public static UserDetailBuilder newBuilder() {
		return new UserDetailBuilder();
	}

	public static class UserDetailBuilder {

		private String userId;

		private double latitude;

		private double longitude;

		private Date lastKnown;

		public UserDetailData getUserDetailData() {
			return new UserDetailData(this.userId, this.latitude, this.longitude, this.lastKnown);
		}

		public UserDetailBuilder setUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public UserDetailBuilder setLatitude(double latitude) {
			this.latitude = latitude;
			return this;
		}

		public UserDetailBuilder setLongitude(double longitude) {
			this.longitude = longitude;
			return this;
		}

		public UserDetailBuilder setLastKnown(Date lastKnown) {
			this.lastKnown = lastKnown;
			return this;
		}

	}
}
