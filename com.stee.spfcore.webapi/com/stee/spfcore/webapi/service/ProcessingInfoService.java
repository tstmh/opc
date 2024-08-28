package com.stee.spfcore.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.ProcessingInfoDAO;
import com.stee.spfcore.webapi.model.internal.ProcessingInfo;

@Service
public class ProcessingInfoService {

	private ProcessingInfoDAO processingInfoDAO;
	
	@Autowired
	public ProcessingInfoService ( ProcessingInfoDAO processingInfoDAO ) {
		this.processingInfoDAO = processingInfoDAO;
	}
	
	@Transactional
	public void saveProcessingInfo (ProcessingInfo info) {
		processingInfoDAO.saveProcessingInfo(info);
	}

}
