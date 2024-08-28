package com.stee.spfcore.webapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.globalParameters.GlobalParameters;
import com.stee.spfcore.webapi.service.GlobalParametersService;

@RestController
@RequestMapping("/globalParameters")
public class GlobalParametersController {

	private GlobalParametersService service;
	
	@Autowired
	public GlobalParametersController(GlobalParametersService service) {
		//comments
		this.service = service;
	}
	
	@GetMapping("/getGlobalParametersByUnit")
	public GlobalParameters getGlobalParametersByUnit(@RequestParam String unit, String subunit) {
		GlobalParameters output = service.getGlobalParametersByUnit(unit, subunit); 
		return output;	
	}
	
}
