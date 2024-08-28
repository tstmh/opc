package com.stee.spfcore.service.hrps.impl;

import static com.stee.spfcore.service.hrps.impl.FieldLengthConstants.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.model.hrps.HRPSConfig;
import com.stee.spfcore.model.hrps.HRPSDetails;

public class InboundBuilder {
	
	private static final String NEW_LINE = System.getProperty("line.separator");
	private HRPSConfig hrpsConfig;
	private Logger logger;
	private EmailUtil emailUtil;
	private double total;
	
	public InboundBuilder(HRPSConfig hrpsConfig, Logger logger, EmailUtil emailUtil) {
		this.hrpsConfig = hrpsConfig;
		this.logger = logger;
		this.emailUtil = emailUtil;
	}
	
	public void generateInbound(List<HRPSDetails> hrpsDetails) {
		StringBuilder fileContent = new StringBuilder();
		Date date = new Date();
		
		generateInboundHeader(fileContent);
		
		generateInboundDetails(fileContent, hrpsDetails);
		
		generateInboundTrailer(fileContent, hrpsDetails);

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("[HRPS] Inbound File Detail: %s", fileContent));
		}
		// Generate HRPS Inbound File
		FileWriter writer = null;
		File inboundFolder = new File(hrpsConfig.getInboundFolder().trim());
		
		if(!inboundFolder.exists()) {
			inboundFolder.mkdirs();
		}
		
		File inboundFile = new File(hrpsConfig.getInboundFolder().trim(), hrpsConfig.getInboundFile().trim() + StringUtil.getFileDateTimeString(date) + ".txt");
	
		try {
			writer = new FileWriter(inboundFile);
			writer.write(fileContent.toString());
		} catch (IOException e) {
			logger.info(String.format("[HRPS] Fail to generate HRPS Inbound file: %s %s", inboundFile.getAbsolutePath(), e));
			//Email to Technical Team on Internal Error
			emailUtil.sendInternalError(inboundFile.getName(), new Date(), "Fail to generate HRPS Inbound file at " + inboundFile.getAbsolutePath(), e, hrpsConfig);
		}
		finally {
			try {
				if (writer != null) 
					writer.close();
			} catch (IOException e) {
				logger.log(Level.INFO, "[HRPS] Fail to close the FileWriter", e);
			}
		}
		
		//Copy HRPS Inbound File to Archive Folder for backup
		File archiveInboundFolder = new File(hrpsConfig.getInboundArchiveFolder().trim(),inboundFile.getName());
		FileUtil.copyFile(inboundFile, archiveInboundFolder);
		
	}
	
	private void generateInboundHeader(StringBuilder fileContent) {
		logger.log(Level.INFO,"[HRPS] Generating Inbound Header !!!");
		//Inbound Header
		fileContent.append(StringUtil.rightPadSpaces(hrpsConfig.getInboundHeaderRecordType(), INBOUND_HEADER_RECORD_TYPE_LEN));
		fileContent.append(StringUtil.rightPadSpaces(hrpsConfig.getInboundHeaderSource(), INBOUND_HEADER_SOURCE_LEN));
		fileContent.append(StringUtil.rightPadSpaces(StringUtil.getHeaderDateTimeString(new Date()), INBOUND_HEADER_DATETIME_LEN));
		fileContent.append(NEW_LINE);
	}
	
	private void generateInboundDetails(StringBuilder fileContent, List<HRPSDetails> hrpsDetails) {
		logger.log(Level.INFO, "[HRPS] Generating Inbound Details !!!");
		if (hrpsDetails != null && !hrpsDetails.isEmpty()) {
			for (HRPSDetails detail : hrpsDetails) {
				fileContent.append(StringUtil.rightPadSpaces(hrpsConfig.getInboundDetailRecordType(), INBOUND_DETAIL_RECORD_TYPE_LEN));
				fileContent.append(StringUtil.rightPadSpaces(detail.getNric(), INBOUND_DETAIL_ID_LEN));
				fileContent.append(StringUtil.rightPadSpaces(StringUtil.getDetailDateString(detail.getStartDate()), INBOUND_DETAIL_STARTDATE_LEN));
				fileContent.append(StringUtil.rightPadSpaces(StringUtil.getDetailDateString(detail.getEndDate()), INBOUND_DETAIL_ENDDATE_LEN));
				fileContent.append(StringUtil.rightPadSpaces(hrpsConfig.getInboundDetailWageType(), INBOUND_DETAIL_WAGE_TYPE_LEN));
				this.total += detail.getAmount();
				fileContent.append(StringUtil.convertAmountToSignedString(detail.getAmount(), INBOUND_DETAIL_AMOUNT_LEN));
				fileContent.append(StringUtil.rightPadSpaces(detail.getReference(), INBOUND_DETAIL_REFERENCE_LEN));
				fileContent.append(NEW_LINE);
			}
		}

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Total Amount: %s", this.total));
		}
	}
	
	private void generateInboundTrailer(StringBuilder fileContent, List<HRPSDetails> hrpsDetails) {
		logger.log(Level.INFO, "[HRPS] Generating Inbound Trailer !!!");
		//Inbound Trailer
		fileContent.append(StringUtil.rightPadSpaces(hrpsConfig.getInboundTrailerRecordType(),INBOUND_TRAILER_RECORD_TYPE_LEN));
		fileContent.append(StringUtil.leftPadZeros(String.valueOf(hrpsDetails.size()), INBOUND_TRAILER_TOTAL_RECORD_LEN));
		fileContent.append(StringUtil.convertAmountToSignedString(this.total, INBOUND_TRAILER_TOTAL_AMOUNT_LEN));
	
	}
}
