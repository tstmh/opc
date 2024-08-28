package com.stee.spfcore.service.hr.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.UserProcessingDetailsDAO;
import com.stee.spfcore.model.OperationCode;
import com.stee.spfcore.model.ProcessFlag;
import com.stee.spfcore.model.UserProcessingDetails;

import com.stee.spfcore.model.code.ExternalSystemType;
import com.stee.spfcore.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.model.personnel.HRChangeRecord;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.internal.HRProcessingInfo;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.DataException;
import com.stee.spfcore.service.hr.impl.input.IData;
import com.stee.spfcore.service.hr.impl.input.IDataFileParser;
import com.stee.spfcore.service.hr.impl.input.IRecord;
import com.stee.spfcore.service.hr.impl.util.EmailUtil;
import com.stee.spfcore.service.hr.impl.util.FileUtil;
import com.stee.spfcore.service.hr.impl.util.PersonalFieldUtil;
import com.stee.spfcore.service.hr.impl.validation.RecordValidator;
import com.stee.spfcore.service.personnel.IPersonnelService;
import com.stee.spfcore.service.personnel.PersonnelServiceException;
import com.stee.spfcore.service.personnel.PersonnelServiceFactory;
import com.stee.spfcore.service.process.IProcessService;
import com.stee.spfcore.service.process.ProcessServiceException;
import com.stee.spfcore.service.process.ProcessServiceFactory;
import com.stee.spfcore.utils.PersonnelUtils;

public class JobProcessor {

	private static final String INTERFACE_USER = "HR Interface";
	private UserProcessingDetailsDAO dao;
	
	public JobProcessor() {
		this.dao = new UserProcessingDetailsDAO();
	}
	
	public void process (IBatchJob job) {
		Logger logger = job.getLogger();
		
		logger.log(Level.INFO, "Processing inbound file from " + job.getType());
		
		File inputFile = job.getInboundFile ();
		File workingFolder = job.getWorkingFolder();
		
		// 1) Move inbound file to working folder
		File workingFile = FileUtil.moveFile (inputFile, workingFolder);
			
		// File doesn't exists.
		if (workingFile == null) {
			logger.log (Level.INFO, job.getType() + " inbound file not found: " + inputFile.getAbsolutePath());
			EmailUtil.sendFileMissing (job, new Date ());
			return;
		}
		
		// 2) Process file
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Processing file %s", workingFile.getAbsolutePath()));
		}
		processFile (job, workingFile);
		
		// 3) Archive file
		logger.log (Level.INFO, "Archive inbound file from " + job.getType());
		File archiveFolder = job.getInboundArchiveFolder();
		FileUtil.archiveFile (workingFile, archiveFolder);
		
		logger.log(Level.INFO, "Completed processing of inbound file from " + job.getType());
	}
	
	
	private void processFile (IBatchJob job, File workingFile) {
		
		Logger logger = Logger.getLogger(JobProcessor.class.getName());
		
		// Parse file
		IDataFileParser parser = job.getParser();
		IAckFileWriter ackFileWriter = job.getAcknowledgementFileWriter();
		File outputFile = job.getOutboundFile ();
		File archiveFolder = job.getOutboundArchiveFolder();
		IData data = null;
		
		// Remove the output file if already exists.
		if (outputFile.exists()) {
			logger.warning("Output file exists and will be deleted.");

			try {
				Files.delete(Paths.get(outputFile.toURI())); // Use Files.delete for potential exceptions
				logger.info("Output file deleted successfully.");
			} catch (IOException e) {
				logger.severe(String.format("Failed to delete output file: %s %s", outputFile.getAbsolutePath(), e));
				// Handle the error appropriately, e.g., throw a custom exception or log for analysis
			}
		}


		try {
			data = parser.parse(workingFile);
		}
		catch (DataException e) {
			// Unable to parse inbound file
			// Email CORE Technical Team 
			logger.log(Level.SEVERE, "Unable to process " + job.getType() + " inbound file.", e);
			EmailUtil.sendUnableToProcess (job, new Date(), e);
			return;
		}
		
		// Total record count doesn't tally
		if (data.getRecordCount() != data.getRecordList().size()) {
			logger.log(Level.SEVERE, job.getType() + " record count does not tally.");
			
			// Email CORE Technical Team
			EmailUtil.sendRecordCountNotTally (job, new Date ());
			
			// Generate Acknowledgement file
			File ackFile = ackFileWriter.recordCountNotTally(data);
			
			// Acknowledgement file will be null for NSPAM and HTVMS
			if (ackFile != null) {
				// Copy to outbound folder
				FileUtil.copyFile(ackFile, outputFile);
				// Archive the acknowledgement file
				FileUtil.archiveFile(ackFile, archiveFolder);
			}
			
			return;
		}
		
		Set<CodeInfo> codeErrors = new HashSet<>();
		List<RecordError> recordErrors = new ArrayList<>();
		List<ServiceTypeChange> srvTypeChangeRecords = new ArrayList<>();
		int successCount = 0;
		int failCount = 0;

		logger.log(Level.INFO, "Records to process: " + data.getRecordList().size());
		
		for (IRecord record : data.getRecordList()) {
			RecordResult result;
			try {
				logger.log(Level.INFO, "Processing record " + record.getNric());
				result = processRecord (record, job.getType());
			} 
			catch (PersonnelServiceException | ProcessServiceException | AccessDeniedException e) {
				logger.log(Level.SEVERE, "Internal error while processing " + job.getType() + " inbound file.", e);
				EmailUtil.sendInternalError (job, e, new Date ());
				return;
			}
			
			if (result.isSuccess()) {
				successCount++;
				
				// Added to the list to email on the service type change.
				if (result.isServiceTypeChanged()) {
					ServiceTypeChange serviceTypeChange = new ServiceTypeChange();
					serviceTypeChange.setName(record.getName());
					serviceTypeChange.setNric(record.getNric());
					serviceTypeChange.setServiceTypeDesc(record.getServiceType().getDescription());
					srvTypeChangeRecords.add(serviceTypeChange);
				}
			}
			else {
				failCount++;
				codeErrors.addAll(result.getNonMatchingCodes());
				recordErrors.addAll(result.getRecordErrors());
			}
		}
		
		// Email CORE Technical Team of status
		EmailUtil.sendSuccessStatus(job, successCount, failCount, new Date ());
		
		// Email CORE Technical Team of non-matching codes and invalid values
		if (codeErrors.size() > 0 || recordErrors.size() > 0) {
			EmailUtil.sendNonMatchingCodesAndInvalidValues(job, codeErrors, recordErrors);
		}
		
		// Email CORE Technical Team on the change in service type
		if (!srvTypeChangeRecords.isEmpty()) {
			EmailUtil.sendServiceTypeChange(srvTypeChangeRecords);
		}
		
		// Generate Acknowledgement file
		File ackFile = ackFileWriter.processStatus(data, recordErrors);
		// Copy to outbound folder
		FileUtil.copyFile(ackFile, outputFile);
		// Archive the acknowledgement file
		FileUtil.archiveFile(ackFile, archiveFolder);
	}
	
	
	private RecordResult processRecord (IRecord record, JobType jobType) throws PersonnelServiceException, ProcessServiceException, AccessDeniedException {
		
		Logger logger = Logger.getLogger(JobProcessor.class.getName());
		
		logger.log(Level.INFO, "Processing record with nric:" + record.getNric());
		
		RecordResult result = new RecordResult();
		
		// Validate the record
		List<RecordError> errors = RecordValidator.validate(jobType, record);
		if (errors.size() > 0) {
			logger.log(Level.WARNING, "Validation failed for record with nric:" + record.getNric());
			for (RecordError error : errors) {
				logger.log(Level.WARNING, "\t" + error.getDesc());
			}
			
			result.setSuccess(false);
			result.setRecordErrors(errors);
			result.setNonMatchingCodes(new HashSet<CodeInfo>());
			
			return result;
		}
		
		// Convert external to internal code
		Set<CodeInfo> codeErrors = record.convertToInternalCode();
		if (codeErrors.size () > 0) {
			logger.log(Level.WARNING, "Code conversion failed for record with nric:" + record.getNric());
			
			result.setSuccess(false);
			result.setNonMatchingCodes(codeErrors);
			result.setRecordErrors(convertToRecordErrors (record.getNric(), codeErrors));
			
			return result;
		}
		
		// Check the dates.
		// Check here instead of earlier, cause need the convert to internal code
		// to be done first before can check if is active employment.
		if (PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
			errors = RecordValidator.validateDates (jobType, record);
			if (errors.size() > 0) {
				logger.log(Level.WARNING, "Date validation failed for record with nric:" + record.getNric());
				for (RecordError error : errors) {
					logger.log(Level.WARNING, "\t" + error.getDesc());
				}
				
				result.setSuccess(false);
				result.setRecordErrors(errors);
				result.setNonMatchingCodes(new HashSet<CodeInfo>());
				
				return result;
			}
		}
		
		// Ignore all active PNSMEN record.
		if (PersonnelUtils.isPNSMEN(record.getServiceType().getValue()) &&
				PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
			logger.log(Level.WARNING, "PNSMEN record ignored as it has active employment status:" + record.getNric());
			result.setSuccess(true);
			return result;
		}
		
		IPersonnelService personnelService = PersonnelServiceFactory.getPersonnelService();
		boolean isNew = false;
		
		// Retrieve 
		PersonalDetail personalDetail = personnelService.getPersonal(record.getNric());
		HRProcessingInfo processingInfo = personnelService.getHRProcessingInfo(record.getNric());
		
		if (processingInfo == null) {
			processingInfo = new HRProcessingInfo(record.getNric(), null);
		}
		
		if (personalDetail == null) {
			
			// No existing record. Ignore PNSMEN record.
			if (PersonnelUtils.isPNSMEN(record.getServiceType().getValue())) {
				logger.log(Level.WARNING, "PNSMEN record ignored as no existing personal record exist:" + record.getNric());
				result.setSuccess(true);
				return result;
			}
			
			isNew = true;
			personalDetail = new PersonalDetail(record.getNric());
		}
		else if (PersonnelUtils.isNonSPFOfficer(personalDetail.getEmployment().getServiceType())) {
			
			// Ignore PNSMEN record.
			if (PersonnelUtils.isPNSMEN(record.getServiceType().getValue())) {
				logger.log(Level.WARNING, "PNSMEN record ignored as non-SPF record exist in DB with nric:" + record.getNric());
				result.setSuccess(true);
				return result;
			}
			
			
			// If is non-spf officer. Can only update if the existing record employment status is non-active.
			// Reject (return error) if new record has a active employment status,
			// else ignore (return success but don't update db.
			if (PersonnelUtils.isActiveEmployment(personalDetail.getEmployment().getEmploymentStatus())) {
				if (PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
					logger.log(Level.WARNING, "Record rejected as active non-SPF record exist in DB with nric:" + record.getNric());
					
					result.setSuccess(false);
					result.setRecordErrors(activeEmploymentErrors (record.getNric()));
					result.setNonMatchingCodes(new HashSet<>());
					
					return result;
				}
				else {
					logger.log(Level.WARNING, "Record ignored as active non-SPF record exist in DB with nric:" + record.getNric());
					result.setSuccess(true);
					return result;
				}
			}
		}
		else if (processingInfo.getSystemType() == ExternalSystemType.HRHUB && jobType != JobType.HRHUB) {
			
			// Ignore PNSMEN record.
			if (PersonnelUtils.isPNSMEN(record.getServiceType().getValue())) {
				logger.log(Level.WARNING, "PNSMEN record ignored as HRHUB record exist in DB with nric:" + record.getNric());
				result.setSuccess(true);
				return result;
			}
			
			
			// Ignore all inactive incoming record.
			if (!PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
				logger.log(Level.WARNING, "Inactive record ignored as HRHUB record exist in DB with nric:" + record.getNric());
				result.setSuccess(true);
				return result;
			}
			else {
				// Reject active incoming record if existing HR Hub data is active.
				if (PersonnelUtils.isActiveEmployment(personalDetail.getEmployment().getEmploymentStatus())) {
					logger.log(Level.WARNING, "Record rejected as active HRHUB record exist in DB with nric:" + record.getNric());
					
					result.setSuccess(false);
					result.setRecordErrors(activeEmploymentErrors (record.getNric()));
					result.setNonMatchingCodes(new HashSet<>());
					
					return result;
				}
			}
		}	
		else if (processingInfo.getSystemType() == ExternalSystemType.HTVMS && jobType != JobType.HTVMS) {
			
			// Ignore PNSMEN record.
			if (PersonnelUtils.isPNSMEN(record.getServiceType().getValue())) {
				logger.log(Level.WARNING, "PNSMEN record ignored as HTVMS record exist in DB with nric:" + record.getNric());
				result.setSuccess(true);
				return result;
			}
			
			
			// If existing HTVMS record is inactive, allow Active and Inactive HRHUB record, and Active NSPAM record can override. 
			if (!PersonnelUtils.isActiveEmployment(personalDetail.getEmployment().getEmploymentStatus())) {
				if (jobType == JobType.NSPAM && !PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
					logger.log(Level.WARNING, "Inactive record ignored as HTVMS record exist in DB with nric:" + record.getNric());
					result.setSuccess(true);
					return result;
				}
			}
			else {
				// Ignore inactive HRHUB and NSPAM record.
				// Reject active NSPAM record
				if (!PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
					logger.log(Level.WARNING, "Inactive record ignored as active HTVMS record exist in DB with nric:" + record.getNric());
					result.setSuccess(true);
					return result;
				}
				else if (jobType == JobType.NSPAM && PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
					logger.log(Level.WARNING, "Record rejected as active HTVMS record exist in DB with nric:" + record.getNric());
					
					result.setSuccess(false);
					result.setRecordErrors(activeEmploymentErrors (record.getNric()));
					result.setNonMatchingCodes(new HashSet<>());
					
					return result;
				}
			}
		}
		else if ((processingInfo.getSystemType() == ExternalSystemType.NSPAM && jobType != JobType.NSPAM) &&
		(PersonnelUtils.isActiveEmployment(personalDetail.getEmployment().getEmploymentStatus()))) {
			// If existing record is active
			// Ignore inactive HRHUB and HTVMS record.
			if (!PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
				logger.log(Level.WARNING, "Inactive record ignored as active NSPAM record exist in DB with nric:" + record.getNric());
				result.setSuccess(true);
				return result;
			}

			// if NSPAM record is active and replace with active HRHUB, needed to indicate the service type
			// change, so that email will be send to inform tech team.
			if (jobType == JobType.HRHUB) {
				logger.log(Level.INFO, "Service type change for following nric:" + record.getNric());
				result.setServiceTypeChanged(true);
			}
		}
		
		// Populate the personal detail
		PersonalFieldUtil.populate (personalDetail, record);
		
		Date currentDate = new Date();
		
		// Save to DB
		if (isNew) {
			personnelService.addPersonal(personalDetail, INTERFACE_USER);
			logger.log(Level.INFO, "Added new personnel record with nric:" + record.getNric());
			
			// Save for FE CreateUser
			// if active and user not exists, create user.
			// Else, if non-active and user exists, delete user.
			logger.log(Level.INFO, String.format("Active user %s, save to DB create", record.getNric()));
			saveRecord(record, currentDate, OperationCode.CREATE);
		}
		else {
			personnelService.updatePersonal(personalDetail, INTERFACE_USER);
			logger.log(Level.INFO, "Updated personnel record with nric:" + record.getNric());
			
			logger.log(Level.INFO, String.format("User employment status? %s", record.getEmploymentStatus().getValue()));
			
			logger.log(Level.INFO, String.format("Active user? %s", PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())));
			
			// Save for FE CreateUser
			// if active and user not exists, create user.
			// Else, if non-active and user exists, delete user.
			if (!PersonnelUtils.isActiveEmployment(record.getEmploymentStatus().getValue())) {
				logger.log(Level.INFO, String.format("Non-active user %s, save to DB delete", record.getNric()));
				saveRecord(record, currentDate, OperationCode.DELETE);
			}
		}
		
		// Only HRHUB has extra employment info
		if (jobType == JobType.HRHUB) {
			ExtraEmploymentInfo info = new ExtraEmploymentInfo();
			PersonalFieldUtil.populate (info, record);
			
			personnelService.saveExtraEmploymentInfo(info);
			logger.log(Level.INFO, "Updated extra employment info for nric:" + record.getNric());
		}
		
		// Trigger BPM process to process membership
		IProcessService processService = ProcessServiceFactory.getInstance();
		processService.processMembership(record.getNric());
		
		result.setSuccess(true);
		
		// Update Change record
		HRChangeRecord changeRecord = new HRChangeRecord (personalDetail.getNric(), new Date());
		personnelService.saveHRChangeRecord(changeRecord);
		
		// Save HR Processing Info
		processingInfo.setSystemType(convertExtSysType(jobType));
		personnelService.saveHRProcessingInfo(processingInfo);
		
		// Send email to invite to apply for PCWF Membership
		// Only if the user is of service type Police Civilian (HQ) Personnel and 
		// the membership record doesn't exists 
		if (isNew && jobType == JobType.HRHUB && PersonnelUtils.isPoliceCivilianHQPersonnel(personalDetail.getEmployment().getServiceType())) {
			logger.log(Level.INFO, "Sending email to officer to apply for PCWF Membership:" + record.getNric());
			String email = record.getOfficeEmail();
			if (email == null) {
				email = record.getPersonalEmail();
			}
			
			if (email != null) {
				EmailUtil.sendApplyMembership(email, personalDetail.getName());
				if ( logger.isLoggable( Level.INFO ) ) {
					logger.info(String.format("Apply for PCWF Membership email sent to %s", email));
				}
			}
			else {
				logger.log(Level.WARNING, "Fail to send apply PCWF Membership email. Office email not found for officer:" + record.getNric());
			}
		}
		
		return result;
	}

	private void saveRecord (IRecord iRecord, Date currentDate, OperationCode code) {
		
		Logger logger = Logger.getLogger(JobProcessor.class.getName());

		logger.log(Level.INFO, "Save record with nric:" + iRecord.getNric());
		
		UserProcessingDetails details = new UserProcessingDetails();
		details.setMessageCreated(currentDate);
		details.setName(iRecord.getName());
		details.setNric(iRecord.getNric());
		details.setOperationCode(code);
		details.setProcessFlag(ProcessFlag.NOT_PROCESSED);

		logger.log(Level.INFO, "Saving record:" + details);
		try {
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.beginTransaction();
			}
			else {
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			dao.saveUserProcessingDetails(details);
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			logger.log(Level.INFO, "Fail to save user processing details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	private ExternalSystemType convertExtSysType (JobType jobType) {
		if (jobType == JobType.HRHUB) {
			return ExternalSystemType.HRHUB;
		}
		else if (jobType == JobType.HTVMS) {
			return ExternalSystemType.HTVMS;
		}
		else {
			return ExternalSystemType.NSPAM;
		}
	}
	
	private static List<RecordError> convertToRecordErrors (String nric, Set<CodeInfo> codeErrors) {
		
		List<RecordError> errors = new ArrayList<>();
		
		for (CodeInfo codeInfo : codeErrors) {
			String msg = ErrorConstants.CODE_TABLE_ERROR_MSG;
			msg = msg.replace("@Code@", codeInfo.getValue());
			msg = msg.replace("@Field@", codeInfo.getTag());
			
			RecordError recordError = new RecordError(nric, ErrorConstants.CODE_TABLE_ERROR_CODE, msg, codeInfo.getTag());
			errors.add(recordError);
		}
		return errors;
	}
	
	private static List<RecordError> activeEmploymentErrors (String nric) {
		List<RecordError> errors = new ArrayList<>();
		RecordError recordError = new RecordError(nric, ErrorConstants.ACTIVE_RECORD_ERROR_CODE, ErrorConstants.ACTIVE_RECORD_ERROR_MSG, ErrorConstants.ACTIVE_RECORD_TAG);
		
		errors.add(recordError);
		return errors;
	}
	
}
