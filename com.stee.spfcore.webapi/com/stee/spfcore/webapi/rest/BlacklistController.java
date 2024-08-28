package com.stee.spfcore.webapi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.service.BlacklistService;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

	private BlacklistService service;
	
	@Autowired
	public BlacklistController(BlacklistService service) {
		//comments
		this.service = service;
	}
	
	@GetMapping("/isBlacklisted")
	public boolean isBlacklisted ( @RequestParam String nric, String module) {
		Boolean output = service.isBlacklisted(nric, module);
		return output;	
	}
	
	
	
}
