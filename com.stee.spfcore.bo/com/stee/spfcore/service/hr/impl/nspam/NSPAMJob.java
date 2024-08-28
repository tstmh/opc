package com.stee.spfcore.service.hr.impl.nspam;

import java.io.File;
import java.util.logging.Logger;

import com.stee.spfcore.model.code.ExternalSystemType;
import com.stee.spfcore.service.configuration.IHRInterfaceConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.hr.IHRService;
import com.stee.spfcore.service.hr.impl.IAckFileWriter;
import com.stee.spfcore.service.hr.impl.IBatchJob;
import com.stee.spfcore.service.hr.impl.JobType;
import com.stee.spfcore.service.hr.impl.input.IDataFileParser;
import com.stee.spfcore.service.hr.impl.log.LoggerBuilder;
import com.stee.spfcore.service.hr.impl.util.CodeMappingUtil;

public class NSPAMJob implements IBatchJob {

	private IHRInterfaceConfig config;
	private Logger logger;
	private CodeMappingUtil codeMappingUtil;
	
	public NSPAMJob() {
		config = ServiceConfig.getInstance().getHRInterfaceConfig(IHRService.NSPAM);
		logger = LoggerBuilder.getInstance().getLogger(JobType.NSPAM);
		codeMappingUtil = new CodeMappingUtil(ExternalSystemType.NSPAM, logger);
	}
	
	@Override
	public JobType getType() {
		return JobType.NSPAM;
	}

	@Override
	public File getInboundFile() {
		return new File (config.inboundFolder().trim(), config.inboundFile().trim());
	}
	
	@Override
	public File getOutboundFile() {
		return new File (config.outboundFolder().trim(), config.outboundFile().trim());
	}

	@Override
	public File getWorkingFolder() {
		return new File (config.workingFolder().trim());
	}

	@Override
	public File getInboundArchiveFolder() {
		return new File (config.inboundArchiveFolder().trim());
	}

	@Override
	public File getOutboundArchiveFolder() {
		return new File (config.outboundArchiveFolder().trim());
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}
	
	@Override
	public IDataFileParser getParser() {
		return new DataFileParser(logger, codeMappingUtil);
	}

	@Override
	public IAckFileWriter getAcknowledgementFileWriter() {
		return new AckFileWriter(new File (config.workingFolder(), config.outboundFile()), logger);
	}
}
