package com.stee.spfcore.webapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.model.ReferenceGenerator;
import com.stee.spfcore.webapi.model.internal.ApplicationType;
import com.stee.spfcore.webapi.service.ReferenceGeneratorService;

//@Controller
@RestController
@RequestMapping("/referenceGenerator")
public class ReferenceGeneratorController {
	
	private ReferenceGeneratorService referenceGeneratorService;
	
	@Autowired
	public ReferenceGeneratorController(ReferenceGeneratorService referenceGeneratorService) {
		this.referenceGeneratorService = referenceGeneratorService;
	}
	
	@GetMapping("/getReferenceGenerator")
	public ReferenceGenerator getReferenceGenerator(@RequestParam String applicationType) {
		
		if (applicationType != null) {
			ApplicationType newApplicationType = ApplicationType.get(applicationType);
			return referenceGeneratorService.getReferenceGenerator(newApplicationType);
		}
		return null;
	}
	
	@PostMapping("/addReferenceGenerator")
	public void addReferenceGenerator(@RequestBody ReferenceGenerator referenceGenerator) {
		referenceGeneratorService.addReferenceGenerator(referenceGenerator);
	}
	
	@PostMapping("/updateReferenceGenerator")
	public void updateReferenceGenerator(@RequestBody ReferenceGenerator referenceGenerator) {
		referenceGeneratorService.updateReferenceGenerator(referenceGenerator);
	}
}
