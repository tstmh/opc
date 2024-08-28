package com.stee.spfcore.service.hr.impl;

import java.io.File;
import java.util.logging.Logger;

import com.stee.spfcore.service.hr.impl.input.IDataFileParser;

public interface IBatchJob {

	public JobType getType ();
	
	public File getInboundFile ();
	
	public File getOutboundFile ();
	
	public File getWorkingFolder ();
	
	public File getInboundArchiveFolder ();
	
	public File getOutboundArchiveFolder ();
	
	public Logger getLogger ();
	
	public IDataFileParser getParser ();
	
	public IAckFileWriter getAcknowledgementFileWriter ();
	
}
