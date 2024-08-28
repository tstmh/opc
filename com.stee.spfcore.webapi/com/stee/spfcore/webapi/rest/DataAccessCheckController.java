package com.stee.spfcore.webapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.dao.dac.DepartmentAndSubUnit;
import com.stee.spfcore.webapi.service.DataAccessCheckService;

@RestController
@RequestMapping("/dac")
public class DataAccessCheckController {

	private DataAccessCheckService service;
	
	@Autowired
	public DataAccessCheckController(DataAccessCheckService service) {
		this.service = service;
	}
	
	@GetMapping("/getDepartmentAndSubUnit")
	public DepartmentAndSubUnit getDepartmentAndSubUnit(@RequestParam String nric) {
		DepartmentAndSubUnit departmentAndSubUnit = service.getDepartmentAndSubUnit(nric); 
		return departmentAndSubUnit;	
	}
	
}
