package com.hcl.pmsiot.dashboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.hcl.pmsiot.dashboard.data.DashboardResponse;
import com.hcl.pmsiot.dashboard.exception.DashboardException;
import com.hcl.pmsiot.dashboard.util.DashboardConstant;

@ControllerAdvice
public class DashboardExceptionController {

	@ExceptionHandler(value = { DashboardException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		
		DashboardResponse dashboardResponse = new DashboardResponse();
		dashboardResponse.setStatus(DashboardConstant.statusFailure);
		dashboardResponse.setMessage(ex.getMessage());
		
        return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
    }

}
