package com.hcl.pmsiot.dashboard.data;

public class LocationDetailData {

	public LocationDetailData(String locationId, String name, String capacity, BoundaryData[] boundary, double latitude,
			double longitude) {
		super();
		this.locationId = locationId;
		this.name = name;
		this.capacity = capacity;
		this.boundary = boundary;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	private String locationId;
	
	private String name;

	private String capacity;

	private BoundaryData[] boundary;

	private double latitude;

	private double longitude;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public BoundaryData[] getBoundary() {
		return boundary;
	}

	public void setBoudary(BoundaryData[] boundary) {
		this.boundary = boundary;
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

	public static LocationDetailBuilder createBuilder() {
		return new LocationDetailBuilder();
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public void setBoundary(BoundaryData[] boundary) {
		this.boundary = boundary;
	}
	
	public static class LocationDetailBuilder {

		private String locationId;
		
		private String name;

		private String capacity;

		private BoundaryData[] boudary;

		private double latitude;

		private double longitude;


		public LocationDetailBuilder setLocationId(String locationId) {
			this.locationId = locationId;
			return this;
		}

		public LocationDetailBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public LocationDetailBuilder setCapacity(String capacity) {
			this.capacity = capacity;
			return this;
		}

		public LocationDetailBuilder setBoudary(BoundaryData[] boudary) {
			this.boudary = boudary;
			return this;
		}

		public LocationDetailBuilder setLatitude(double latitude) {
			this.latitude = latitude;
			return this;
		}

		public LocationDetailBuilder setLongitude(double longitude) {
			this.longitude = longitude;
			return this;
		}

		public LocationDetailData getLocationDetailData() {
			return new LocationDetailData(this.locationId, this.name, this.capacity, this.boudary, this.latitude, this.longitude);
		}
	}

}
