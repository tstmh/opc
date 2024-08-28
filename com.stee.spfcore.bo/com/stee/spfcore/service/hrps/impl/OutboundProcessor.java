package com.stee.spfcore.service.hrps.impl;

import com.ibm.websphere.security.auth.WSSubject;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.hrps.HRPSConfig;
import com.stee.spfcore.model.hrps.HRPSDetails;
import com.stee.spfcore.model.hrps.RecordStatus;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.benefits.BenefitsServiceException;
import com.stee.spfcore.service.benefits.BenefitsServiceFactory;
import com.stee.spfcore.service.benefits.IBenefitsService;
import com.stee.spfcore.service.hrps.HRPSServiceException;
import com.stee.spfcore.service.hrps.HRPSServiceFactory;
import com.stee.spfcore.service.hrps.IHRPSService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.stee.spfcore.service.hrps.impl.FieldLengthConstants.OUTBOUND_TRAILER_FILE_LEVEL_REJ_START;

public class OutboundProcessor {
    private HRPSConfig hrpsConfig;
	private Logger logger;
	private EmailUtil emailUtil;
	private IBenefitsService benefitsService;
	private IHRPSService hrpsService;
	
	public OutboundProcessor(HRPSConfig hrpsConfig, Logger logger, EmailUtil emailUtil) {
		this.hrpsConfig = hrpsConfig;
		this.logger = logger;
		this.emailUtil = emailUtil;
		this.benefitsService = BenefitsServiceFactory.getBenefitsService();
		this.hrpsService = HRPSServiceFactory.getHRPSService();
	}
	
	public void processOutboundFile() throws HRPSServiceException {
		logger.log(Level.INFO,"[HRPS] Processing Outbound File from HRPS");
		
		// Get Outbound File Folder
		File outboundFileFolder = new File(hrpsConfig.getOutboundFolder().trim());
		File outboundFile = null;
		
		for (File hrpsFile : outboundFileFolder.listFiles()) {
			if (hrpsFile.isFile()) {
				//Assume that only 1 file is in the folder
				//As after processing, it will be moved to the archive folder
				outboundFile = new File(hrpsConfig.getOutboundFolder().trim(),hrpsFile.getName());
				break;
			}
		}
		
		if (outboundFile == null) {
			logger.log(Level.INFO, "[HRPS] Outbound file not found at " + hrpsConfig.getInboundFolder().trim());
			return;
		}
		
		// Move outbound file to working folder
		File workingFile = FileUtil.moveFile(outboundFile, new File (hrpsConfig.getWorkingFolder().trim()));
		
		processFile(workingFile);
		
		// Archive outbound file 
		logger.info(String.format("[HRPS] Archive Outbound File at %s", hrpsConfig.getOutboundArchiveFolder().trim()));
		FileUtil.archiveFile(workingFile, new File (hrpsConfig.getOutboundArchiveFolder().trim()));
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("[HRPS] Completed processing of outbound file: %s", workingFile.getAbsolutePath()));
		}
	}
	
	public void processOutboundPostFile() throws HRPSServiceException, BenefitsServiceException, AccessDeniedException, ParseException {
		logger.log(Level.INFO,"[HRPS] Processing Outbound Post File from HRPS");
		
		// Get Outbound Post File Folder
		File outboundPostFileFolder = new File(hrpsConfig.getOutboundPostFolder().trim());
		File outboundPostFile = null;
		
		for (File hrpsFile : outboundPostFileFolder.listFiles()) {
			if (hrpsFile.isFile()) {
				//Assume that only 1 file is in the folder
				//As after processing, it will be moved to the archive folder
				outboundPostFile = new File(hrpsConfig.getOutboundPostFolder().trim(),hrpsFile.getName());
				break;
			}
		}
		
		if (outboundPostFile == null) {
			logger.log(Level.INFO, "[HRPS] Outbound Post File not found at " + hrpsConfig.getOutboundPostFolder().trim());
			return;
		}
		
		// Move outbound file to working folder
		File workingFile = FileUtil.moveFile(outboundPostFile, new File (hrpsConfig.getWorkingFolder().trim()));
		
		processPostFile(workingFile);
		
		// Archive outbound file 
		logger.info(String.format("[HRPS] Archive Outbound File at %s" ,hrpsConfig.getOutboundArchiveFolder().trim()));
		FileUtil.archiveFile(workingFile, new File (hrpsConfig.getOutboundPostArchiveFolder().trim()));
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("[HRPS] Completed processing of outbound post file: %s", workingFile.getAbsolutePath()));
		}
	}
	
	private void processFile(File workingFile) throws HRPSServiceException {
		logger.log(Level.INFO,"[HRPS] Start Processing HRPS Outbound File");
		
		Charset charset = StandardCharsets.UTF_8;
		List<String> result = new ArrayList<>();
		List<Record> rejRecord = new ArrayList<>();
		int rejectCount = 0;
		int successCount = 0;
		
		try {
			
			result = Files.readAllLines(Paths.get(workingFile.getAbsolutePath()),charset);
			
		} catch (IOException e) {
			logger.severe(String.format("[HRPS] Unable to process outbound file: %s %s" ,workingFile.getAbsolutePath(),e));
			//Email to Technical Team on Internal Error
			emailUtil.sendInternalError(workingFile.getName(), new Date(), "Unable to process outbound file at " + workingFile.getAbsolutePath(), e, hrpsConfig);
			return;
		}
		
		if (result != null && !result.isEmpty()) {
			//Get Trailer Record
			String trailer = result.get(result.size() - 1);
			if (!trailer.substring(OUTBOUND_TRAILER_FILE_LEVEL_REJ_START - 1).trim().isEmpty()) {
				String fileLevelRejStr = trailer.substring(OUTBOUND_TRAILER_FILE_LEVEL_REJ_START - 1);
				if ( logger.isLoggable( Level.INFO ) ) {
					logger.info(String.format("[HRPS] File Level Rejection Reason: %s", fileLevelRejStr));
				}
				return;
			} else {
				//Ignore 1st and last row as it is the header and trailer
				for (int i = 1; i < result.size()-1; i++) {
					Record recordDetail = new Record (result.get(i));
					if ( logger.isLoggable( Level.INFO ) ) {
						logger.info(String.format(recordDetail.toString()));
					}
					HRPSDetails detail = hrpsService.getHRPSDetails(recordDetail.getReference());
					if (detail != null) {
						if (RecordStatus.get(recordDetail.getStatus()).equals(RecordStatus.REJECTED)) {
							detail.setStatus(RecordStatus.get(recordDetail.getStatus()));
							detail.setReason(recordDetail.getReason());
							rejRecord.add(recordDetail);
							rejectCount++;
						} else {
							detail.setStatus(RecordStatus.get(recordDetail.getStatus()));
							detail.setPayrollMonth(recordDetail.getPayrollMonth());
							successCount++;
						}
						hrpsService.updateHRPSDetails(detail);
					}
				}
				if ( logger.isLoggable( Level.INFO ) ) {
					logger.info(String.format("[HRPS] Rejected Count: %s", rejectCount));
					logger.info(String.format("[HRPS] Success Count: %s", successCount));
				}
			}
		}
		
		//Email to Techincal Team on status
		emailUtil.sendProcessedStatus(workingFile.getName(),new Date(), successCount, rejectCount, rejRecord, hrpsConfig);
	}
	
	private void processPostFile(File workingFile) throws HRPSServiceException, BenefitsServiceException, AccessDeniedException, ParseException {
		logger.log(Level.INFO,"[HRPS] Start Processing HRPS Outbound Post File");
		
		Charset charset = StandardCharsets.UTF_8;
		List<String> result = new ArrayList<>();
		List<PostRecord> rejRecord = new ArrayList<>();
		int rejectCount = 0;
		int successCount = 0;
		
		try {
			result = Files.readAllLines(Paths.get(workingFile.getAbsolutePath()),charset);
			
		} catch (IOException e) {
			logger.severe(String.format("[HRPS] Unable to process outbound post file: %s %s", workingFile.getAbsolutePath(),e));
			//Email to Technical Team on Internal Error
			emailUtil.sendInternalError(workingFile.getName(), new Date(), "Unable to process outbound file at " + workingFile.getAbsolutePath(), e, hrpsConfig);
			return;
		}
		
		if (result != null && !result.isEmpty()) {
			//Ignore 1st and last row as it is the header and trailer
			for (int i = 1; i < result.size()-1; i++) {
				PostRecord postRecord = new PostRecord (result.get(i));
				logger.log(Level.INFO,postRecord.toString());
				HRPSDetails detail = hrpsService.getHRPSDetails(postRecord.getReference());
				if (detail != null) {
					String type = StringUtil.getBenefitsTypeFromId(detail.getId());
					switch(type) {
						case "BG":
							if(!RecordStatus.get(postRecord.getStatus()).equals(RecordStatus.REJECTED))
							{
							BereavementGrant bereavement = benefitsService.getBereavementGrant(detail.getId());
							bereavement.setPaymentDate(StringUtil.convertPayrollMonthToDate(postRecord.getPayrollMonth()));
							benefitsService.updateBereavementGrant(bereavement, WSSubject.getCallerPrincipal());
							}
							break;
						case "WG":
							if(!RecordStatus.get(postRecord.getStatus()).equals(RecordStatus.REJECTED))
							{
							WeddingGift wedding = benefitsService.getWeddingGift(detail.getId());
							wedding.setPaymentDate(StringUtil.convertPayrollMonthToDate(postRecord.getPayrollMonth()));
							benefitsService.updateWeddingGift(wedding, WSSubject.getCallerPrincipal());
							}
							break;
						case "NB":
							if(!RecordStatus.get(postRecord.getStatus()).equals(RecordStatus.REJECTED))
							{
							NewBornGift newborn = benefitsService.getNewBorn(detail.getId());
							newborn.setPaymentDate(StringUtil.convertPayrollMonthToDate(postRecord.getPayrollMonth()));
							benefitsService.updateNewBorn(newborn, WSSubject.getCallerPrincipal());
							}
							break;
						default:
							break;
					}
					if (RecordStatus.get(postRecord.getStatus()).equals(RecordStatus.REJECTED)) {
						detail.setPostStatus(RecordStatus.get(postRecord.getStatus()));
						detail.setPostReason(postRecord.getReason());
						rejRecord.add(postRecord);
						rejectCount++;
					} else {
						detail.setPostStatus(RecordStatus.get(postRecord.getStatus()));
						detail.setPayrollMonth(postRecord.getPayrollMonth());
						successCount++;
					}
					logger.log(Level.INFO,detail.toString());
					hrpsService.updateHRPSDetails(detail);
				}
			}
		}

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("[HRPS] Rejected Count: %s", rejectCount));
			logger.info(String.format("[HRPS] Success Count: %s", successCount));
		}
		//Email to Techincal Team on status
		emailUtil.sendPostProcessedStatus(workingFile.getName(), new Date(), successCount, rejectCount, rejRecord, hrpsConfig);

	}
	
}