package com.stee.spfcore.service.hr.impl.log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.service.configuration.IHRInterfaceConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.hr.IHRService;
import com.stee.spfcore.service.hr.impl.JobType;

public class LoggerBuilder {

	private static final Logger LOGGER = Logger.getLogger(LoggerBuilder.class.getName());
	private static final LoggerBuilder instance = new LoggerBuilder();

	public static LoggerBuilder getInstance() {
		return instance;
	}

	private Logger hrhubLogger = Logger.getLogger("HRHUB.Processing.Log");
	private Logger nspamLogger = Logger.getLogger("NSPAM.Processing.Log");
	private Logger htvmsLogger = Logger.getLogger("HTVMS.Processing.Log");

	private LoggerBuilder() {

		// Requirement state that separate application log to be maintained for
		// HR interface processing
		try {
			initHRHUBLogger();
			initNSPAMLogger();
			initHTVMSLogger();
		} catch (SecurityException | IOException e) {
			LOGGER.log(Level.SEVERE, "Fail to initialize Application log for HR Interface.", e);
		}
	}

	private void initHRHUBLogger() throws SecurityException, IOException {
		IHRInterfaceConfig config = ServiceConfig.getInstance().getHRInterfaceConfig(IHRService.HRHUB);
		hrhubLogger.setUseParentHandlers(false);
		String filePattern = config.logFolder().trim() + File.separator + "HRHUB-%g.log";

		FileHandler fileHandler = new FileHandler(filePattern, 50000, 5000, true);
		fileHandler.setFormatter(new LogFormatter());

		hrhubLogger.addHandler(fileHandler); // NOSONAR
	}

	private void initNSPAMLogger () throws SecurityException, IOException {
		IHRInterfaceConfig config = ServiceConfig.getInstance().getHRInterfaceConfig(IHRService.NSPAM);
		
		nspamLogger.setUseParentHandlers(false);
		String filePattern = config.logFolder().trim() + File.separator + "NSPAM-%g.log";
		
		FileHandler fileHandler = new FileHandler (filePattern, 50000, 5000, true);
		fileHandler.setFormatter(new LogFormatter());
		
		nspamLogger.addHandler(fileHandler); // NOSONAR
	}
	
	private void initHTVMSLogger () throws SecurityException, IOException {
		IHRInterfaceConfig config = ServiceConfig.getInstance().getHRInterfaceConfig(IHRService.HTVMS);
		
		htvmsLogger.setUseParentHandlers(false);
		String filePattern = config.logFolder().trim() + File.separator + "HTVMS-%g.log";
		
		FileHandler fileHandler = new FileHandler (filePattern, 50000, 5000, true);
		fileHandler.setFormatter(new LogFormatter ());
		
		htvmsLogger.addHandler(fileHandler); // NOSONAR
	}
	
	
	public Logger getLogger (JobType type) {
		if (JobType.HRHUB == type) {
			return hrhubLogger;
		}
		else if (JobType.HTVMS == type) {
			return htvmsLogger;
		}
		else if (JobType.NSPAM == type) {
			return nspamLogger;
		}
		return null;
	}
	
	
}
