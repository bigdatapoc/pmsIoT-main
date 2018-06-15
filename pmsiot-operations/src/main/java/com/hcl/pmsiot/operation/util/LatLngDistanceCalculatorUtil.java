package com.hcl.pmsiot.operation.util;

/**
 * @author abhilash.j
 *
 */
public class LatLngDistanceCalculatorUtil {

	
	/**
	 * @param objLatitude
	 * @param objLongitude
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @return true/false if found within required radius
	 */
	public static boolean isWithinDistance(double objLatitude, double objLongitude, double latitude, double longitude,
			double radius) {
		double dist2Or = getDistanceFromLatLonInKm(latitude, longitude, objLatitude, objLongitude);
		return dist2Or <= (radius / 1000);
	}

	/**
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return distance between two points
	 */
	public static double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
		double R = 6371; // Radius of the earth in km
		double dLat = deg2rad(lat2 - lat1); // deg2rad below
		double dLon = deg2rad(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c; // Distance in km
		return d;
	}

	/**
	 * @param deg
	 * @return
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	
}
