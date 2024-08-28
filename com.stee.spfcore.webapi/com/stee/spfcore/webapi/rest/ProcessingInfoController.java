package com.stee.spfcore.webapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.model.internal.ProcessingInfo;
import com.stee.spfcore.webapi.service.ProcessingInfoService;

@RestController
@RequestMapping("/processingInfo")
public class ProcessingInfoController {

	private ProcessingInfoService service;
	
	@Autowired
	public ProcessingInfoController(ProcessingInfoService service) {
		this.service = service;
	}
	
	@PostMapping("/saveProcessingInfo")
	public void saveProcessingInfo(@RequestBody ProcessingInfo info) {
		service.saveProcessingInfo(info); 
	}
	
}
