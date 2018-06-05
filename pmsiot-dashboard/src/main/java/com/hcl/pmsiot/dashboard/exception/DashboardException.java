package com.hcl.pmsiot.dashboard.exception;

public class DashboardException extends RuntimeException {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DashboardException(String message) {
        super(message);
    }
	
	public DashboardException(String message, Throwable cause) {
        super(message, cause);
    }
}
