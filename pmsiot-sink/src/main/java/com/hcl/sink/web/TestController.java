package com.hcl.microservicepoc.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.microservicepoc.model.AssetCatalog;

@RestController
public class TestController {

	@RequestMapping(value = "/catalog", method = RequestMethod.GET)
	public AssetCatalog firstPage() {

		AssetCatalog emp = new AssetCatalog();
		emp.setName("emp1");
		emp.setDesignation("manager");
		emp.setEmpId("1");
		emp.setSalary(3000);

		return emp;
	}

}