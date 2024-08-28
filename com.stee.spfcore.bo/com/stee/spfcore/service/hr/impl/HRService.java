package com.stee.spfcore.service.hr.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.service.hr.IHRService;
import com.stee.spfcore.service.hr.impl.hrhub.HRHUBJob;
import com.stee.spfcore.service.hr.impl.htvms.HTVMSJob;
import com.stee.spfcore.service.hr.impl.nspam.NSPAMJob;

public class HRService implements IHRService {

	private static final Logger LOGGER = Logger.getLogger(HRService.class.getName());
	
	public HRService() {
		// DO NOTHING
	}
	
	
	@Override
	public void processHRHUBFile() {
		
		LOGGER.log(Level.INFO, "Processing HRHUB file");
		
		IBatchJob job = new HRHUBJob();
		
		JobProcessor processor = new JobProcessor();
		processor.process (job);
		
		LOGGER.log(Level.INFO, "Processed HRHUB file");
	}
	
	@Override
	public void processNSPAMFile() {
		LOGGER.log(Level.INFO, "Processing NSPAM file");
		
		IBatchJob job = new NSPAMJob();
		
		JobProcessor processor = new JobProcessor();
		processor.process (job);
		
		LOGGER.log(Level.INFO, "Processed NSPAM file");
	}

	@Override
	public void processHTVMSFile() {
		LOGGER.log(Level.INFO, "Processing HTVMS file");
		
		IBatchJob job = new HTVMSJob();
		
		JobProcessor processor = new JobProcessor();
		processor.process (job);
		
		LOGGER.log(Level.INFO, "Processed HTVMS file");
		
	}
}
