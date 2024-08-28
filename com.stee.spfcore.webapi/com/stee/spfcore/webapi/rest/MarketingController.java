package com.stee.spfcore.webapi.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.service.MarketingService;

@RestController
@RequestMapping("/marketing")
public class MarketingController {

	private MarketingService service;
	
	@Autowired
	public MarketingController(MarketingService service) {
		//comments
		this.service = service;
	}
	
	@GetMapping("/getMemberInGroup")
	public List <String> getMemberInGroup(@RequestParam String groupId) {
		List <String> output = service.getMemberInGroup(groupId); 
		return output;	
	}
	
}
