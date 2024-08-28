package com.stee.spfcore.webapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.UserProcessingDetailsDAO;
import com.stee.spfcore.webapi.model.OperationCode;
import com.stee.spfcore.webapi.model.ProcessFlag;
import com.stee.spfcore.webapi.model.UserProcessingDetails;

@Service
public class UserProcessingService {

	private UserProcessingDetailsDAO dao;
	private static final Logger logger = LoggerFactory.getLogger(UserProcessingService.class);
	
	@Autowired
	public UserProcessingService (UserProcessingDetailsDAO dao) {
		this.dao = dao;
	}
	
	@Transactional
	public List<UserProcessingDetails> getUserProcessingDetails(ProcessFlag processFlag, OperationCode operationCode) {
		return dao.getUserProcessingDetails(processFlag, operationCode);
	}
	
	@Transactional
	public void updateUserProcessingDetailsList (List<UserProcessingDetails> details) {
		dao.updateUserProcessingDetailsList(details);
	}
	
}
