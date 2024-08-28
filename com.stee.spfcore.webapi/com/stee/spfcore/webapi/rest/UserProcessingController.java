package com.stee.spfcore.webapi.rest;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.OperationCode;
import com.stee.spfcore.webapi.model.ProcessFlag;
import com.stee.spfcore.webapi.model.UserProcessingDetails;
import com.stee.spfcore.webapi.service.UserProcessingService;

@RestController
@RequestMapping("/userProcessing")
public class UserProcessingController {

	private UserProcessingService service;
	private static final Logger logger = LoggerFactory.getLogger(UserProcessingController.class);
	
	@Autowired
	public UserProcessingController(UserProcessingService service) {
		this.service = service;
	}
	
	@GetMapping("/getUserProcessingDetails")
	public List<UserProcessingDetails> getUserProcessingDetails(@RequestParam String processFlag, String operationCode) {
		logger.info("getUserProcessingDetails >> ProcessFlag:%s, OperationCode:%s", processFlag, operationCode);
		
		List<UserProcessingDetails> users = service.getUserProcessingDetails(ProcessFlag.fromValue(processFlag), OperationCode.fromValue(operationCode)); 
		if (null != users) {
			logger.info("getUserProcessingDetails size >> " + users.size());
			return users;
		} else {
			logger.warn("getUserProcessingDetails: Failed, object returned is null.");
			throw new IllegalStateException("Object returned is empty");
		}
	}
	
	@PostMapping("/updateUserProcessingDetailsList")
	public void updateUserProcessingDetailsList (@RequestBody List<UserProcessingDetails> userProcessingDetailsList) {
		logger.info("updateUserProcessingDetailsList: Starting service.");
		if (userProcessingDetailsList != null) {
			service.updateUserProcessingDetailsList(userProcessingDetailsList);
			
			logger.info("updateUserProcessingDetailsList complete ");
		}
	}
	
}
