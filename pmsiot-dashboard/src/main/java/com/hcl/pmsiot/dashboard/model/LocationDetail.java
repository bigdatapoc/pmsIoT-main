package com.hcl.pmsiot.dashboard.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "location_detail")
public class LocationDetail {

	private String _id;

	private String locationId;
	
	@Indexed(unique = true)
    private String name;

    private String capacity;
    
    private Boundary[] boudary;
    
    private double latitude;
	
    private double longitude;
    
    private boolean master;
    
    public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

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

	public Boundary[] getBoudary() {
		return boudary;
	}

	public void setBoudary(Boundary[] boudary) {
		this.boudary = boudary;
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

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	
}
