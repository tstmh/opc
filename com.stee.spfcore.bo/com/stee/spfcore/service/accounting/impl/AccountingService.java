package com.stee.spfcore.service.accounting.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.model.accounting.*;
import net.lingala.zip4j.exception.ZipException;

import com.stee.spfcore.dao.AccountingDAO;
import com.stee.spfcore.dao.BenefitsDAO;
import com.stee.spfcore.dao.CodeDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SAGApplicationDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.internal.SAGAwardType;
import com.stee.spfcore.model.internal.SAGPaymentType;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.model.sag.SAGConfigSetup;
import com.stee.spfcore.model.sag.SAGDateConfigType;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.accounting.AccountingServiceException;
import com.stee.spfcore.service.accounting.IAccountingService;
import com.stee.spfcore.utils.ConvertUtil;
import com.stee.spfcore.utils.zip.ZipUtil;
import com.stee.spfcore.vo.benefits.BenefitsCriteria;
import com.stee.spfcore.vo.benefits.BereavementGrantDetails;
import com.stee.spfcore.vo.benefits.NewBornGiftDetails;
import com.stee.spfcore.vo.benefits.WeddingGiftDetails;
import com.stee.spfcore.vo.sag.SAGApplicationCriteria;
import com.stee.spfcore.vo.sag.SAGApplicationsApprovedForAwardWithPayment;

public class AccountingService implements IAccountingService{
	private static final Logger LOGGER = Logger.getLogger(AccountingService.class.getName());
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String SPACE = " ";
	
	private SAGApplicationDAO sagDAO;
	private AccountingDAO accountingDAO;
	private BenefitsDAO benefitsDAO;
	private PersonnelDAO personnelDAO;
	private CodeDAO codeDAO;
	
	public AccountingService() {
		sagDAO = new SAGApplicationDAO();
		accountingDAO = new AccountingDAO();
		benefitsDAO = new BenefitsDAO();
		personnelDAO = new PersonnelDAO();
		codeDAO = new CodeDAO();
	}
	
	@Override
    public List< BatchFileConfig > searchBatchFileConfigByYear( String financialYear ) throws AccountingServiceException {
        List< BatchFileConfig > batchFileConfigList = null;
        LOGGER.log( Level.INFO, "Search BatchFileConfig by year" );
        try {
            SessionFactoryUtil.beginTransaction();

            batchFileConfigList = accountingDAO.searchBatchFileConfigByYear( financialYear );
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.warning(String.format("Failed to search BatchFileConfig by year: %s %s", financialYear, e.getMessage() ));

            SessionFactoryUtil.rollbackTransaction();
        }
        return batchFileConfigList;
    }

    @Override
    public void saveBatchFileConfig( BatchFileConfig batchFileConfig, String requestor ) throws AccountingServiceException {
        LOGGER.log( Level.INFO, "Save BatchFileConfig" );
        try {
            if ( null != batchFileConfig ) {
            	SessionFactoryUtil.beginTransaction();
                accountingDAO.saveBatchFileConfig( batchFileConfig, requestor );
                SessionFactoryUtil.commitTransaction();
            }
        }
        catch ( Exception e ) {
            LOGGER.warning(String.format("Failed to Save BatchFileConfig: %s", e.getMessage() ));
            SessionFactoryUtil.rollbackTransaction();
        }
	}
    
    @Override
    public List< BankInformation > getBankInformation() throws AccountingServiceException{
        List< BankInformation > bankInformationList = null;
        LOGGER.log( Level.INFO, "get Bank Information" );
        try {
            SessionFactoryUtil.beginTransaction();

            bankInformationList = accountingDAO.getBankInformation();
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "Failed to get Bank Information " );

            SessionFactoryUtil.rollbackTransaction();
    		throw new AccountingServiceException("Failed to get Bank Information ", e);
        }
        return bankInformationList;
    }
    
    @Override
    public List< BankInformation > getBankInformationFromCode(String bankCode) throws AccountingServiceException{
        List< BankInformation > bankInformationList = null;
        LOGGER.log( Level.INFO, "get Bank Information from bank code" );
        try {
            SessionFactoryUtil.beginTransaction();

            bankInformationList = accountingDAO.getBankInformationFromCode(bankCode);
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "Failed to get Bank Information from bank code" );

            SessionFactoryUtil.rollbackTransaction();
    		throw new AccountingServiceException("Failed to get Bank Information from bank code ", e);
        }
        return bankInformationList;
    }
	
    @Override
	public String generateFateFile(List<String> referenceNumberList,int numOfFilesToGenerate, String downloadPath,
			 List<String> paymentStatusList, String requestor) throws AccountingServiceException{

		Calendar todayDate = Calendar.getInstance();
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("start generateFateFile: %s", todayDate.getTime()));
		}
		SessionFactoryUtil.beginTransaction();
		
		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("financialYear:%s", financialYear));
		}
		//get config for FY
		List<BatchFileConfig> configList = accountingDAO.searchBatchFileConfigByYear(financialYear);
		if (configList == null || configList.size() == 0){
			if ( LOGGER.isLoggable( Level.WARNING ) ) {
				LOGGER.warning(String.format("No batch file config found for FY %s ", financialYear));
			}
            SessionFactoryUtil.rollbackTransaction();
    		throw new AccountingServiceException("No batch file configurations created for Financial Year " + financialYear);
		}
		BatchFileConfig config = configList.get(0);
		
		//check value date
		if (!AccountingUtil.checkValueDate(config.getValueDate())){
			LOGGER.warning( String.format( "generateFateFile error: Invalid value date %s", config.getValueDate() ) );
			SessionFactoryUtil.rollbackTransaction();
			throw new AccountingServiceException("Fail to generate file, invalid value date.");	
		}
		
		//check if its new generation or regeneration
		boolean generateFromList = false;
    	if (referenceNumberList != null && !referenceNumberList.isEmpty()){
    		generateFromList = true;
    	}

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("referenceNumberList:%s paymentStatusList:%s generateFromList:%s",
					((referenceNumberList == null) ? null : referenceNumberList.size()),
					((paymentStatusList == null) ? null : paymentStatusList.size()), generateFromList));
		}
    	//get payment advice from config
		boolean hasPaymentAdvice = config.isHasPaymentAdvice();
		
		//get inbound folder path from config
		String inboundFolderPath = config.getInboundFolder();
		if (inboundFolderPath == null){
			if ( LOGGER.isLoggable( Level.WARNING ) ) {
				LOGGER.warning(String.format("generateFateFile error: inboundFolderPath %s ", inboundFolderPath));
			}
			SessionFactoryUtil.rollbackTransaction();
			throw new AccountingServiceException("Fail to generate file, null inbound folder path in config.");	
		}
		
		//create download folder
		FileWriter writer = null;
		File downloadFolder = new File(downloadPath, AccountingUtil.getFateFolderName(todayDate.getTime()));
		
		if (downloadFolder.exists()){
			AccountingUtil.deleteFiles(downloadFolder);
		}
		downloadFolder.mkdirs();
		if (downloadFolder!=null && LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info(String.format("download folder in %s , files %s", downloadFolder.getPath(), downloadFolder.listFiles().length));
		}
		//get payment mode from configurations
		SAGConfigSetup paymentModeConfigSetup =  sagDAO.getConfigSetup(financialYear, SAGDateConfigType.PAYMENT_MODE);
		List<String> paymentModes = AccountingUtil.getPaymentModeList(paymentModeConfigSetup.getStringValue());
		
		int fileNum = 0;
		for (String paymentMode: paymentModes){
			
			boolean giro = paymentMode == SAGPaymentType.GIRO.toString();
			if (!giro && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("paymentMode:%s giro:%s", paymentMode, giro));
			}
			
			//get applications with payment mode
			SAGApplicationCriteria criteria = new SAGApplicationCriteria();
			criteria.setApplicationStatus(ApplicationStatus.SUCCESSFUL.toString());
			criteria.setFinancialYear(financialYear);
			
			List<SAGApplicationsApprovedForAwardWithPayment> allApplicationList = new ArrayList<>();
			if (!generateFromList){
				allApplicationList = sagDAO.searchSAGApplicationsApprovedForAwardWithPayment(criteria, paymentMode, paymentStatusList);
			} else {
				allApplicationList = sagDAO.searchSAGApplicationsApprovedForAwardWithPaymentByRefNum(criteria, paymentMode, referenceNumberList, paymentStatusList);
			}
			
			//check if have applications
			int listSize = allApplicationList.size();
			if (listSize>0 && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("%s application size:%s", paymentMode, listSize));
			}
			if (listSize == 0){
				if ( LOGGER.isLoggable( Level.WARNING ) ) {
					LOGGER.warning(String.format("generateFateFile error: No applications to generate file for payment mode %s", paymentMode));
				}
				continue;
			}
			
			//split applications by numFileToGen
			int splitNum = (int) Math.ceil((float) listSize/numOfFilesToGenerate);
			for (int i=0; i<listSize; i += splitNum){
				fileNum ++;
				int end = i+splitNum;
				end = (allApplicationList.size() >= end) ?  end : allApplicationList.size();
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("start:%s , end:%s", i, end));
				}
				//get sub application list 
				List<SAGApplicationsApprovedForAwardWithPayment> subApplicationList = allApplicationList.subList(i, end);
				LOGGER.log(Level.INFO, "subApplicationlist " + fileNum +": "+subApplicationList.size());
				
				//create batch file
				String fileName = AccountingUtil.getFateFileName(hasPaymentAdvice, todayDate.getTime(), fileNum);
				if (fileName == null){
					SessionFactoryUtil.rollbackTransaction();
					throw new AccountingServiceException("Fail to generate file, more than 99 files generated.");	
				}
				
				File inboundFile = new File(downloadFolder, fileName + ".txt");
				if (!inboundFile.exists()){
					try {
						boolean created = inboundFile.createNewFile();
						if ( LOGGER.isLoggable( Level.INFO ) ) {
							LOGGER.info(String.format("Created file? %s", created));
						}
					} catch (IOException e1) {
						LOGGER.info(String.format( "Fail to generate file: %s %s", inboundFile.getAbsolutePath() ,e1));
						continue;
					}
				}

				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("Fate file %s paymentMode %s in:%s", fileNum, paymentMode, inboundFile.getAbsolutePath()));
				}
				//generate batch header
				String processingMode = AccountingUtil.getProcessingMode(giro, config.getProcessingType());
				String originatingAccountName = AccountingUtil.replaceSpecialChar(config.getOriginatingAccountName(), SPACE);
				String bulkCustomerReference = AccountingUtil.replaceSpecialChar(config.getBulkCustomerReference(), SPACE);
				
				String batchHeader = AccountingUtil.generateBatchHeader(hasPaymentAdvice, fileName, config.getPaymentType(), 
						FieldLengthConstants.SERVICE_TYPE, processingMode, FieldLengthConstants.ORIGINATING_BIC_CODE,
						config.getOriginatingAccountNo(), originatingAccountName, config.getValueDate(), 
						config.getUltimateOriginatingCustomer(), bulkCustomerReference, 
						config.getPaymentAdviceHeader());
				if (batchHeader!=null && LOGGER.isLoggable(Level.INFO)) {
					LOGGER.info(String.format("generated batchHeader size:%s", batchHeader.length()));
				}
				
				//calculate total payment amount from batch details. 
				float totalPaymentAmount = 0;
				
				//generate batch details
				int detailCount = 0;
				List<String> batchDetailList = new ArrayList<>();
				for (SAGApplicationsApprovedForAwardWithPayment application: subApplicationList){
					detailCount++;
					
					if (!AccountingUtil.checkAmount(application.getAwardAmount(), config.getProcessingType())){
						LOGGER.warning( String.format( "generateBatchDetails error: Invalid amount allowed for application %s", application.getReferenceNumber() ) );
						continue;
					}
					
					String beneficiaryPostal = null;//mandatory if send payment advice via post 
					String beneficiaryCity = null;
					String beneficiaryMailingAddress = null; //printed on advice
					String mandateID = null;//(DDA reference)- mandatory if payment type is Collection 'C'
					String amount = AccountingUtil.getAmountFormatFromDouble(application.getAwardAmount()); //format amount, *100 to remove decimal.
					String payerName = config.getPayerName(); //shown in advice
					
					String endToEndID = AccountingUtil.replaceSpecialChar(application.getReferenceNumber(), SPACE);//printed on receiving bank A/C statement & advice
					String customerRef = AccountingUtil.replaceSpecialChar(application.getSequenceNumber(), SPACE);//internal reference use - printed on advice	
					 
					boolean sentPaymentAdvice = config.isHasPaymentAdvice();
					
					//get officer preferred email - send advice to this email if isDeliveryModeEmail
					String beneficiaryEmailAddress = application.getSubmittedByPaymentAdviceEmail();
					LOGGER.log(Level.INFO, "paymentAdviceEmailAddress:" + application.getReferenceNumber() + application.getSubmittedByPaymentAdviceEmail());
					if (beneficiaryEmailAddress == null || beneficiaryEmailAddress.equals("")){
						beneficiaryEmailAddress = application.getSubmittedByWorkEmail(); //send to work email if no preferred email
					}
					if ((config.isDeliveryModeEmail()) && (beneficiaryEmailAddress == null || beneficiaryEmailAddress.equals(""))){
						LOGGER.warning( String.format( "generateBatchDetails error: No emails for application %s", application.getReferenceNumber() ) );
					}
					
					//set payment information
					String bicCodeProxyType = application.getBicCodeProxyType();
					
					//format accNoProxyValue
					String accNoProxyValue = application.getAccNoProxyValue();
					if (application.getPreferredPaymentMode().equals(FieldLengthConstants.SAG_PAYMENT_MODE_GIRO)){
						//giro payment type, remove all but digits in account no.
						accNoProxyValue = AccountingUtil.removeAllButDigits(accNoProxyValue);
						if (accNoProxyValue!=null && LOGGER.isLoggable(Level.INFO)) {
							LOGGER.info(String.format("%s GIRO payment type, accountNoFormatted >> %s ", detailCount, accNoProxyValue));
						}
					} else if ((application.getPreferredPaymentMode().equals(FieldLengthConstants.SAG_PAYMENT_MODE_PAYNOW)) && (Objects.equals(application.getBicCodeProxyType(), FieldLengthConstants.PAYNOW_PROXY_TYPE_MOBILE))){
						accNoProxyValue = AccountingUtil.appendCountryCodeToMobileNo(accNoProxyValue, FieldLengthConstants.MOBILE_COUNTRY_CODE_SG);
					}
					
					String accName = AccountingUtil.replaceSpecialChar(application.getAccName(), SPACE); 
					if (accName!=null && LOGGER.isLoggable( Level.INFO )) {
						LOGGER.info(String.format("%s name>> %s bicCodeProxyType>> %s accNoProxyValue>> %s", detailCount, accName, bicCodeProxyType, accNoProxyValue));
					}
					String ultiPayerBeneName = application.getChildName();
					
					String batchDetails = AccountingUtil.generateBatchDetails(sentPaymentAdvice, config.isDeliveryModePost(),
							config.isDeliveryModeEmail(), endToEndID, mandateID, config.getPurposeCode(),
							config.getRemittanceInformation(), ultiPayerBeneName, customerRef, 
							payerName, bicCodeProxyType, accNoProxyValue, accName, 
							amount, beneficiaryCity, beneficiaryPostal, 
							beneficiaryEmailAddress, beneficiaryMailingAddress);
					
					totalPaymentAmount += application.getAwardAmount();

					if (batchDetails!=null && LOGGER.isLoggable( Level.INFO )) {
						LOGGER.info(String.format("%s batchDetails size:%s", detailCount, batchDetails.length()));
					}
					batchDetailList.add(batchDetails);
				}
				
				//generate payment advice format
				List<String> paymentAdviceFormatList = new ArrayList<>();
				if (hasPaymentAdvice){
					List<PaymentAdviceDetail> paymentAdviceDetailList = AccountingUtil.getPaymentAdviceDetailsFromString(config.getPaymentAdviceDetails());
					for (PaymentAdviceDetail paymentAdviceDetails : paymentAdviceDetailList){
						paymentAdviceFormatList.add(AccountingUtil.generatePaymentAdviceFormat(paymentAdviceDetails.getSpacingLinesBefore(), paymentAdviceDetails.getDetails()));
					}
				}
				
				String formattedTotalPaymentAmount = AccountingUtil.getAmountFormatFromDouble(totalPaymentAmount);
				
				//generate batch trailer
				String batchTrailer = AccountingUtil.generateBatchTrailer(hasPaymentAdvice, formattedTotalPaymentAmount, 
						FieldLengthConstants.ORIGINATING_BIC_CODE, config.getOriginatingAccountNo(), config.getOriginatingAccountName(), 
						config.getPaymentType(), subApplicationList, config.getPurposeCode());
				if (batchTrailer!=null && LOGGER.isLoggable( Level.INFO )) {
					LOGGER.info(String.format("batchTrailer size:%s", batchTrailer.length()));
				}
				//write into batch file
				try {
					writer = new FileWriter(inboundFile);
					//write header
					writer.write(batchHeader);
					writer.write(NEW_LINE);
					//write details
					for (String batchDetails: batchDetailList){
						writer.write(batchDetails);
						writer.write(NEW_LINE);
						//write advice if hasPaymentAdvice
						if(hasPaymentAdvice){
							for (String advice: paymentAdviceFormatList){
								writer.write(advice);
								writer.write(NEW_LINE);
							}
						}
					}
					//write trailer
					writer.write(batchTrailer);
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("end generateFateFile: %s", new Date().getTime()));
					}
					
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Fail to generate file: {}" , inboundFile.getAbsolutePath() + e);
					
					SessionFactoryUtil.rollbackTransaction();
				}
				//close writer
				finally {
					try {
						if (writer != null) 
							writer.close();
					} catch (IOException e) {
						LOGGER.log(Level.WARNING, "Fail to close the FileWriter", e);
					}
				}
			}
		}
		
		if (generateFromList){
			if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info( String.format( "isGenerateFromList. referenceNumberList:%s", referenceNumberList.size()));
			}
			List<SAGBatchFileRecord> batchFileRecordList = sagDAO.searchSAGBatchFileRecordByReferenceNumber(referenceNumberList);
			LOGGER.info( String.format( "batchFileRecordList from referenceNumberList count=%s", batchFileRecordList.size()));
			
			if (batchFileRecordList != null && !batchFileRecordList.isEmpty()){
				//update status of batchFileRecords if is regenerate for UOB
				for (SAGBatchFileRecord sagbatchfilerecord : batchFileRecordList){
					sagbatchfilerecord.setPaymentStatus(PaymentStatus.RESEND_TO_UOB);
					sagbatchfilerecord.setUpdatedDate(todayDate.getTime());
				}
				
				//split batchFileRecords to save in batches
				List< List< SAGBatchFileRecord >> allBatchFileRecordList = AccountingUtil.splitIntoBatches( batchFileRecordList, 100 );	
                for ( List< SAGBatchFileRecord > subBatchFileRecordList : allBatchFileRecordList ) {
                	sagDAO.saveSAGBatchFileRecordList(subBatchFileRecordList, requestor);
                	LOGGER.info( String.format( "save subBatchFileRecordList count=%s", subBatchFileRecordList.size() ) );
                }
			}
		}
		
		//zip the folder for download
		File downloadZipFolder = new File( downloadFolder.getParent(), downloadFolder.getName() + ".zip" );
		try {
			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("zipping folder %s", downloadZipFolder.getAbsolutePath()));
			}
			ZipUtil.zip( downloadFolder.getAbsolutePath(), downloadZipFolder.getAbsolutePath() );
		} catch ( ZipException e ) {
			LOGGER.log( Level.WARNING, "Fail to zip file for {}",downloadZipFolder.getAbsolutePath()+ e );
			SessionFactoryUtil.rollbackTransaction();
		}
		
		//generate another zip folder in inbound folder
		File inboundZipFolder = new File(inboundFolderPath, AccountingUtil.getFateFolderName(todayDate.getTime()) + ".zip");
		try {
			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("zipping folder %s", inboundZipFolder.getAbsolutePath()));
			}
			ZipUtil.zip( downloadFolder.getAbsolutePath(), inboundZipFolder.getAbsolutePath() );
		} catch ( ZipException e ) {
			LOGGER.warning(String.format( "Fail to zip file for %s %s",inboundZipFolder.getAbsolutePath(),e ));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		//delete the non-zip folder
		AccountingUtil.deleteFiles(downloadFolder);
		
		SessionFactoryUtil.commitTransaction();
		return downloadZipFolder.getAbsolutePath();
	}

	@Override
	public String generateMockReturnFile(List<SAGBatchFileRecord> recordList, int numOfFilesToGenerate,
			String downloadPath, List<String> paymentStatusList) throws AccountingServiceException{
		
		Calendar todayDate = Calendar.getInstance();
		if (todayDate!=null && LOGGER.isLoggable( Level.INFO )) {
			LOGGER.info(String.format("start generateMockReturnFile: %s", todayDate.getTime()));
		}
		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		if (financialYear!=null && LOGGER.isLoggable( Level.INFO )) {
			LOGGER.info(String.format("financialYear:%s", financialYear));
		}
		SessionFactoryUtil.beginTransaction();
		
		List<BatchFileConfig> configList = accountingDAO.searchBatchFileConfigByYear(financialYear);
		if (configList == null || configList.isEmpty()){
			if ( LOGGER.isLoggable( Level.WARNING ) ) {
				LOGGER.warning(String.format("No batchFileConfig found for financial year %s ", financialYear));
			}
			SessionFactoryUtil.rollbackTransaction();
			throw new AccountingServiceException("No batchFileConfig for financial year ");
		}
		BatchFileConfig config = configList.get(0);
		boolean hasPaymentAdvice = config.isHasPaymentAdvice();
		
		//create download folder
		FileWriter writer = null;
		File downloadFolder = new File(downloadPath, AccountingUtil.getFateFolderName(todayDate.getTime()));
		
		if (downloadFolder.exists()){
			AccountingUtil.deleteFiles(downloadFolder);
		}
		downloadFolder.mkdirs();
		if (downloadFolder!=null && LOGGER.isLoggable( Level.INFO )) {
			LOGGER.info(String.format("download folder in %s , files %s", downloadFolder.getPath(), downloadFolder.listFiles().length));
		}
		
		//get payment mode from config
		SAGConfigSetup paymentModeConfigSetup =  sagDAO.getConfigSetup(financialYear, SAGDateConfigType.PAYMENT_MODE);
		List<String> paymentModes = AccountingUtil.getPaymentModeList(paymentModeConfigSetup.getStringValue());
			
		int fileNum = 0;
		for (String paymentMode: paymentModes){
		
			boolean giro = paymentMode == SAGPaymentType.GIRO.toString();
			if (!giro && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("paymentMode:%s giro:%s", paymentMode, giro));
			}
			//get applications with payment mode
			SAGApplicationCriteria criteria = new SAGApplicationCriteria();
			criteria.setApplicationStatus(ApplicationStatus.SUCCESSFUL.toString());
			criteria.setFinancialYear(financialYear);
			
			List<SAGApplicationsApprovedForAwardWithPayment> allApplicationList = 
					sagDAO.searchSAGApplicationsApprovedForAwardWithPayment(criteria, paymentMode, paymentStatusList);
			
			//check if have applications
			int listSize = allApplicationList.size();
			if (listSize>0 && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("%s application size:%s", paymentMode, listSize));
			}
			if (listSize == 0){
				if ( LOGGER.isLoggable( Level.WARNING ) ) {
					LOGGER.warning(String.format("generateMockReturnFile error: No applications to generate file for payment mode %s", paymentMode));
				}
				continue;
			}
			
			//split applications by numFileToGen
			int splitNum = (int) Math.ceil((float) listSize/numOfFilesToGenerate);
			for (int i=0; i<listSize; i += splitNum){
				fileNum ++;
				int end = i+splitNum;
				end = (allApplicationList.size() >= end) ?  end : allApplicationList.size();
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("start:%s , end:%s", i, end));
				}
				//get sub application list 
				List<SAGApplicationsApprovedForAwardWithPayment> subApplicationList = allApplicationList.subList(i, end);
				LOGGER.log(Level.INFO, "subApplicationlist " + fileNum +": "+subApplicationList.size());
				
				//create batch file
				String fileName = AccountingUtil.getReturnFateFileNameBIBPlus(hasPaymentAdvice, todayDate.getTime(), fileNum);
				if (fileName == null){
					SessionFactoryUtil.rollbackTransaction();
					throw new AccountingServiceException("Fail to generate file, more than 99 files generated.");	
				}
				
				File inboundFile = new File(downloadFolder, fileName + ".txt");
				if (!inboundFile.exists()){
					try {
						boolean created = inboundFile.createNewFile();
						if ( LOGGER.isLoggable( Level.INFO ) ) {
							LOGGER.info(String.format("Created file? %s", created));
						}
					} catch (IOException e1) {
						LOGGER.info(String.format("Fail to generate file: %s %s " ,inboundFile.getAbsolutePath(), e1));
						continue;
					}
				}
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("Fate file %s paymentMode %s in:%s", fileNum, paymentMode, inboundFile.getAbsolutePath()));
				}
				//generate batch header
				String processingMode = AccountingUtil.getProcessingMode(giro, config.getProcessingType());
				String originatingAccountName = AccountingUtil.replaceSpecialChar(config.getOriginatingAccountName(), SPACE);
				String bulkCustomerReference = AccountingUtil.replaceSpecialChar(config.getBulkCustomerReference(), SPACE);
				
				StringBuilder batchHeaderBuilder = new StringBuilder();
				batchHeaderBuilder.append(Integer.toString(FieldLengthConstants.HEADER_RECORD_TYPE));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(config.getPaymentType(), FieldLengthConstants.UOB_HEADER_PAYMENT_TYPE_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(FieldLengthConstants.SERVICE_TYPE, FieldLengthConstants.UOB_HEADER_SERVICE_TYPE_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(processingMode, FieldLengthConstants.UOB_HEADER_PROCESSING_MODE_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_HEADER_COMPANY_ID_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(FieldLengthConstants.ORIGINATING_BIC_CODE, FieldLengthConstants.UOB_HEADER_BIC_CODE_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(FieldLengthConstants.CURRENCY, FieldLengthConstants.UOB_HEADER_AC_NO_CURRENCY_LEN));
				
				String accountNo = AccountingUtil.removeAllButDigits(config.getOriginatingAccountNo());
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(accountNo, FieldLengthConstants.UOB_HEADER_AC_NO_LEN));
				
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(originatingAccountName, FieldLengthConstants.UOB_HEADER_AC_NAME_LEN));
				batchHeaderBuilder.append(AccountingUtil.getDateTimeFormatFromDate(new Date()));
				batchHeaderBuilder.append(AccountingUtil.getDateTimeFormatFromDate(config.getValueDate()));
				String ultiOriginatingCustomer = config.getUltimateOriginatingCustomer();
				ultiOriginatingCustomer = (ultiOriginatingCustomer == config.getOriginatingAccountName()) ? null : ultiOriginatingCustomer; 
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(ultiOriginatingCustomer, FieldLengthConstants.UOB_HEADER_ULTI_ORIGINATING_CUSTOMER_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(bulkCustomerReference, FieldLengthConstants.UOB_HEADER_BULK_CUSTOMER_REFERENCE_LEN));
				batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_HEADER_SOFTWARE_LABEL_LEN));
				if (!hasPaymentAdvice){
					batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_HEADER_FILLER_LEN));
				} else {
					batchHeaderBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_HEADER_PAYMENT_ADVICE_FILLER_LEN));
				}
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("generated mock batchHeader size:%s", batchHeaderBuilder.length()));
				}
				
				//calculate total payment amount from batch details. 
				float totalPaymentAmount = 0;
				
				//generate batch details
				int detailCount = 0;
				List<String> batchDetailList = new ArrayList<>();
				for (SAGApplicationsApprovedForAwardWithPayment application: subApplicationList){
					detailCount++;
					
					StringBuilder batchDetailsBuilder = new StringBuilder();
					
					//set payment information
					String bicCodeProxyType = application.getBicCodeProxyType();
					
					String accNoProxyValue = application.getAccNoProxyValue();
					if (application.getPreferredPaymentMode().equals(FieldLengthConstants.SAG_PAYMENT_MODE_GIRO)){
						//giro payment type, remove all but digits in account no.
						accNoProxyValue = AccountingUtil.removeAllButDigits(accNoProxyValue);
						if ( LOGGER.isLoggable( Level.INFO ) ) {
							LOGGER.info(String.format("%s GIRO payment type, accountNoFormatted >> %s ", detailCount, accNoProxyValue));
						}
					} else if (application.getPreferredPaymentMode().equals(FieldLengthConstants.SAG_PAYMENT_MODE_PAYNOW) && application.getBicCodeProxyType() == FieldLengthConstants.PAYNOW_PROXY_TYPE_MOBILE){
						//paynow payment type, if proxy type is mobile, append +65 to mobileNo
						accNoProxyValue = AccountingUtil.appendCountryCodeToMobileNo(accNoProxyValue, FieldLengthConstants.MOBILE_COUNTRY_CODE_SG);
					}
					
					String accName = AccountingUtil.replaceSpecialChar(application.getAccName(), SPACE);
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format(" %s name>> %s bicCodeProxyType>> %s accNoProxyValue>> %s", detailCount, accName, bicCodeProxyType, accNoProxyValue));
					}
					batchDetailsBuilder.append(Integer.toString(FieldLengthConstants.DETAILS_RECORD_TYPE));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(bicCodeProxyType, FieldLengthConstants.UOB_DETAILS_BIC_CODE_PROXY_TYPE_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(accNoProxyValue, FieldLengthConstants.UOB_DETAILS_AC_NO_PROXY_VALUE_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(accName, FieldLengthConstants.UOB_DETAILS_AC_NAME_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(FieldLengthConstants.CURRENCY, FieldLengthConstants.UOB_DETAILS_AC_NO_CURRENCY_LEN));
					batchDetailsBuilder.append(AccountingUtil.leftPadZeros(AccountingUtil.getAmountFormatFromDouble(application.getAwardAmount()), FieldLengthConstants.UOB_DETAILS_AMOUNT_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(application.getReferenceNumber(), FieldLengthConstants.UOB_DETAILS_END_TO_END_ID_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_DETAILS_MANDATE_ID_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(config.getPurposeCode(), FieldLengthConstants.UOB_DETAILS_PURPOSE_CODE_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(config.getRemittanceInformation(), FieldLengthConstants.UOB_DETAILS_REMITTANCE_INFORMATION_LEN));
					
					String ultiPayerBeneName = config.getUltimateOriginatingCustomer();
					ultiPayerBeneName = (ultiPayerBeneName == application.getAccName())? null : ultiPayerBeneName;
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(ultiPayerBeneName, FieldLengthConstants.UOB_DETAILS_ULT_PAYER_BENEFICIARY_NAME_LEN));
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(application.getSequenceNumber(), FieldLengthConstants.UOB_DETAILS_CUSTOMER_REFERENCE_LEN));
					
					SAGBatchFileRecord recordWithMockStatus = AccountingUtil.getRecordFromList(application.getReferenceNumber(), recordList);
					
					String returnCode = (recordWithMockStatus == null) ? null : recordWithMockStatus.getReturnReason();
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces( returnCode, FieldLengthConstants.UOB_DETAILS_RETURN_CODE_LEN));
					
					String paymentStatus = (recordWithMockStatus == null) ? null : recordWithMockStatus.getPaymentStatus().toString();
					batchDetailsBuilder.append(AccountingUtil.rightPadSpaces( paymentStatus , FieldLengthConstants.UOB_DETAILS_CLEAR_FATE_LEN));
					
					if (!hasPaymentAdvice){
						batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_DETAILS_NON_PAYMENT_ADVICE_FILLER_LEN));

						if ( LOGGER.isLoggable( Level.INFO ) ) {
							LOGGER.info(String.format("%s batchDetails paymentStatus:%s returnCode:%s", detailCount, paymentStatus, returnCode));
						}
					} else {
						String reasonOfNotSent = (recordWithMockStatus == null) ? null : recordWithMockStatus.getReasonOfNotSent();
						batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(reasonOfNotSent , FieldLengthConstants.UOB_DETAILS_REASON_NOT_SENT_LEN));
						batchDetailsBuilder.append(AccountingUtil.rightPadSpaces(null , FieldLengthConstants.UOB_DETAILS_PAYMENT_ADVICE_FILLER_LEN));
						if ( LOGGER.isLoggable( Level.INFO ) ) {
							LOGGER.info(String.format("%s batchDetails paymentStatus:%s returnCode:%s reasonOfNotSent:%s", detailCount, paymentStatus, returnCode, reasonOfNotSent));
						}
					}
					totalPaymentAmount += application.getAwardAmount();

					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("%d batchDetails size:%s", detailCount, batchDetailsBuilder.length()));
					}
					batchDetailList.add(batchDetailsBuilder.toString());
				}
				
				//set payment status from list 
				subApplicationList = AccountingUtil.populateMockPaymentStatusListFromList(subApplicationList, recordList);
				
				//generate batch trailer
				String batchTrailer;
				StringBuilder batchTrailerBuilder = new StringBuilder();
				batchTrailerBuilder.append(Integer.toString(FieldLengthConstants.TRAILER_RECORD_TYPE));
				batchTrailerBuilder.append(AccountingUtil.leftPadZeros(AccountingUtil.getAmountFormatFromDouble(totalPaymentAmount), FieldLengthConstants.UOB_TRAILER_TOTAL_AMOUNT_LEN));
				batchTrailerBuilder.append(AccountingUtil.leftPadZeros(Integer.toString(subApplicationList.size()), FieldLengthConstants.UOB_TRAILER_TOTAL_NO_TRANSACTION_LEN));
				
				String totalAcceptedAmount = AccountingUtil.getTotalAmountByStatus(subApplicationList, PaymentStatus.SUCCESSFUL.name());
				if (totalAcceptedAmount!=null) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalAcceptedAmount:%s", totalAcceptedAmount));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalAcceptedAmount, FieldLengthConstants.UOB_TRAILER_TOTAL_ACCEPTED_AMOUNT_LEN));
				}
				String totalAcceptedNo = AccountingUtil.getApplicationCountByStatus(subApplicationList, PaymentStatus.SUCCESSFUL.name());
				if (totalAcceptedNo!=null) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalAcceptedNo:%s", totalAcceptedNo));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalAcceptedNo, FieldLengthConstants.UOB_TRAILER_TOTAL_ACCEPTED_NO_TRANSACTION_LEN));
				}
				String totalRejectedAmount = AccountingUtil.getTotalAmountByStatus(subApplicationList, PaymentStatus.REJECTED.name());
				if (totalRejectedAmount!=null) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalRejectedAmount:%s", totalRejectedAmount));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalRejectedAmount, FieldLengthConstants.UOB_TRAILER_TOTAL_REJECTED_AMOUNT_LEN));
				}
				String totalRejectedNo = AccountingUtil.getApplicationCountByStatus(subApplicationList, PaymentStatus.REJECTED.name());
				if (totalRejectedNo!=null) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalRejectedNo:%s", totalRejectedNo));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalRejectedNo, FieldLengthConstants.UOB_TRAILER_TOTAL_REJECTED_NO_TRANSACTION_LEN));
				}
				String totalPendingAmount = AccountingUtil.getTotalAmountByStatus(subApplicationList, PaymentStatus.PENDING.name());
				if (totalPendingAmount!=null) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalPendingAmount:%s", totalPendingAmount));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalPendingAmount, FieldLengthConstants.UOB_TRAILER_TOTAL_PENDING_AMOUNT_LEN));
				}
				String totalPendingNo = AccountingUtil.getApplicationCountByStatus(subApplicationList, PaymentStatus.PENDING.name());
				if (totalPendingNo!=null) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalPendingNo:%s", totalPendingNo));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalPendingNo, FieldLengthConstants.UOB_TRAILER_TOTAL_PENDING_NO_TRANSACTION_LEN));
				}
				String totalStoppedAmount = AccountingUtil.getTotalAmountByStatus(subApplicationList, PaymentStatus.STOPPED.name());
				if (totalStoppedAmount!=null && LOGGER.isLoggable( Level.INFO )) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("totalStoppedAmount:%s", totalStoppedAmount));
					}
					batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalStoppedAmount, FieldLengthConstants.UOB_TRAILER_TOTAL_STOPPED_AMOUNT_LEN));
				}
				String totalStoppedNo = AccountingUtil.getApplicationCountByStatus(subApplicationList, PaymentStatus.STOPPED.name());
				if (totalStoppedNo!=null && LOGGER.isLoggable( Level.INFO )) {
					LOGGER.info(String.format("totalStoppedNo:%s", totalStoppedNo));
				}
				batchTrailerBuilder.append(AccountingUtil.leftPadZeros(totalStoppedNo, FieldLengthConstants.UOB_TRAILER_TOTAL_STOPPED_NO_TRANSACTION_LEN));
				
				if (hasPaymentAdvice){
					batchTrailerBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_TRAILER_PAYMENT_ADVICE_FILLER_LEN));
				} else {
					batchTrailerBuilder.append(AccountingUtil.rightPadSpaces(null, FieldLengthConstants.UOB_TRAILER_NON_PAYMENT_ADVICE_FILLER_LEN));
				}
				batchTrailer = batchTrailerBuilder.toString();
				if (batchTrailer!=null && LOGGER.isLoggable( Level.INFO )) {
					LOGGER.info(String.format("batchTrailer size:%s", batchTrailer.length()));
				}
				
				//write into batch file
				try {
					writer = new FileWriter(inboundFile);
					//write header
					writer.write(batchHeaderBuilder.toString());
					writer.write(NEW_LINE);
					//write details
					for (String batchDetails: batchDetailList){
						writer.write(batchDetails);
						writer.write(NEW_LINE);
					}
					//write trailer
					writer.write(batchTrailer);
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("end generateFateFile: %s", new Date().getTime()));
					}
					
				} catch (IOException e) {
					LOGGER.warning(String.format("Fail to generate file: %s %s", inboundFile.getAbsolutePath(),e));
					SessionFactoryUtil.rollbackTransaction();
				}
				//close writer
				finally {
					try {
						if (writer != null) 
							writer.close();
					} catch (IOException e) {
						LOGGER.log(Level.WARNING, "Fail to close the FileWriter", e);
					}
				}
			}
		}	
		
		//zip the folder for download
		File downloadZipFolder = new File( downloadFolder.getParent(), downloadFolder.getName() + ".zip" );
		try {
			if (downloadZipFolder != null) {
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("zipping folder %s", downloadZipFolder.getAbsolutePath()));
				}
				ZipUtil.zip(downloadFolder.getAbsolutePath(), downloadZipFolder.getAbsolutePath());
			}
		} catch ( ZipException e ) {
			LOGGER.warning(String.format("Fail to zip file for %s %s", downloadZipFolder.getAbsolutePath(),e ));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		//delete the non-zip folder
		AccountingUtil.deleteFiles(downloadFolder);
		
		SessionFactoryUtil.commitTransaction();
		return downloadZipFolder.getAbsolutePath();
	}
	
	@Override
	public List<SAGBatchFileRecord> processFateFile(String filePath, String requestor) throws AccountingServiceException{
		
		LOGGER.log(Level.INFO, "Start Process Fate File" );
		
		SessionFactoryUtil.beginTransaction();
		
		//get config for FY
		Calendar todayDate = Calendar.getInstance();
		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		List<BatchFileConfig> configList = accountingDAO.searchBatchFileConfigByYear(financialYear);
		if (configList == null || configList.size() == 0){
			if ( LOGGER.isLoggable( Level.WARNING ) ) {
				LOGGER.warning(String.format("No batch file config found for FY %s ", financialYear));
			}
            SessionFactoryUtil.rollbackTransaction();
    		throw new AccountingServiceException("No batch file configurations created for Financial Year " + financialYear);
		}
		BatchFileConfig config = configList.get(0);
		
		//create outbound folder
		String outboundFolderPath = config.getOutboundFolder();
		if (outboundFolderPath == null){
			if ( LOGGER.isLoggable( Level.WARNING ) ) {
				LOGGER.warning(String.format("processFateFile error: outboundFolderPath %s ", outboundFolderPath));
			}
			SessionFactoryUtil.rollbackTransaction();
			throw new AccountingServiceException("Fail to process file, null outbound folder path in config.");	
		}
		File outboundFolder = new File(outboundFolderPath, AccountingUtil.getFateFolderName(todayDate.getTime()));
		outboundFolder.mkdirs();
		if (outboundFolder!=null && LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info(String.format("outbound folder %s", outboundFolder.getPath()));
		}
		
		UOBBatchHeader batchHeader = new UOBBatchHeader();
        List<UOBBatchDetail> batchDetailsList = new ArrayList<>();

        try {

			if (filePath!=null && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("filePath %s", filePath));
			}

			File file = new File(filePath);
			
			Scanner scanner = new Scanner(file);
			
			String fileName = file.getName();
			if (fileName!=null && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("fate file name:%s", fileName));
			}
			
			boolean hasPaymentAdvice = AccountingUtil.getPaymentAdviceFromFileName(file.getName());
			if (!hasPaymentAdvice && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("payment advice:%s", hasPaymentAdvice));
			}
			
			String infinityRefNo = AccountingUtil.getRefNoFromFileName(fileName);
			if (infinityRefNo!=null && LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("infinityRefNo:%s", infinityRefNo));
			}
			
			while (scanner.hasNextLine()){
				
				String data = scanner.nextLine();
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("data: %s", data));
				}
				
				switch (data.charAt(0)){
					case FieldLengthConstants.HEADER_RECORD_TYPE+'0':
						//header 1
						batchHeader = AccountingUtil.processBatchHeader(data);
						break;
					case FieldLengthConstants.DETAILS_RECORD_TYPE+'0':
						//details 1..n
						batchDetailsList.add(AccountingUtil.processBatchDetails(data, hasPaymentAdvice));
						break;
					case FieldLengthConstants.TRAILER_RECORD_TYPE+'0':
						//trailer 1
                        AccountingUtil.processBatchTrailer(data);
                        break;
					default:
						LOGGER.warning(data);
						break;
				}
			}
			scanner.close();
			
			Date valueDate = null;
			if (batchHeader != null){
				valueDate = batchHeader.getValueDate();
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("batchHeader valueDate: %s", valueDate.toString()));
				}
			}
			
			List<SAGBatchFileRecord> batchFileRecordList = new ArrayList<>();
			
			LOGGER.info( String.format( "batchDetailsList length: %s", batchDetailsList.size() ) );
			for (UOBBatchDetail batchDetail: batchDetailsList){
				
				String refNumber = batchDetail.getEndToEndId();
				
				List<String> officerNameList = accountingDAO.getOfficerNameByReferenceNumber(refNumber);

				String officerName = "";
				
				if(officerNameList != null){
					officerName = officerNameList.get(0);					
				}else{
					LOGGER.info("officer name not found");
				}
								
				PaymentStatus paymentStatus = batchDetail.getPaymentStatus();
				
				ReturnCode returnCode = new ReturnCode();
				String returnReason = "";
				
				if (batchDetail.getReturnCode() != null){
					returnReason = returnCode.getReturnCode(batchDetail.getReturnCode().trim());
				}
				LOGGER.info( String.format( "batchDetail returnCode:%s returnReason:%s", batchDetail.getReturnCode(), returnReason ) );
				
				String reasonOfNotSent = batchDetail.getReasonOfNotSent();
				
				SAGBatchFileRecord batchFileRecord = new SAGBatchFileRecord();
				batchFileRecord.setFinancialYear(financialYear);
				batchFileRecord.setPaymentStatus(paymentStatus);
				batchFileRecord.setReasonOfNotSent(reasonOfNotSent);
				batchFileRecord.setReferenceNumber(refNumber);
				batchFileRecord.setReturnReason(returnReason);
				batchFileRecord.setUpdatedDate(todayDate.getTime());
				batchFileRecord.setOfficerName(officerName);
				
				batchFileRecordList.add(batchFileRecord);
				
				LOGGER.info( String.format( "batchFileRecord", batchFileRecord.toString() ) );
				
				//save value date & amount in sagApplication 
				if (paymentStatus == PaymentStatus.SUCCESSFUL && valueDate != null){
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("refNumber %s is successful! retrieving application... ", refNumber));
					}
					try {
					SAGApplication application = sagDAO.getSAGApplication(refNumber);
					application.setChequeValueDate(valueDate);
					application.setChequeUpdatedDate(new Date());

					application.setAmountPaid(batchDetail.getAmount());

					LOGGER.info( String.format( "saving application with valueDate %s" , application.getChequeValueDate()) );
					sagDAO.saveSAGApplication(application, requestor, false);

					} catch (AccessDeniedException e1) {
						LOGGER.log(Level.WARNING, "Access Denied for " + refNumber , e1);
					}
				}
			}
			
			if ( batchFileRecordList != null ) {
                List< List< SAGBatchFileRecord >> batchList = AccountingUtil.splitIntoBatches( batchFileRecordList, 100 );
                
                int totalCount = 0;
                for ( List< SAGBatchFileRecord > tempList : batchList ) {
                	sagDAO.saveSAGBatchFileRecordList(tempList, requestor);
                	LOGGER.info( String.format( "batch count=%s, total=%s", tempList.size(), totalCount ) );
                    totalCount += tempList.size();
                }
            }
			
			//move the file from upload folder to outbound folder
			File outboundFile = new File(outboundFolder, fileName);
			if (file.renameTo(outboundFile)){
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("move file %s to %s", file.getPath(), outboundFile.getPath()));
				}
				AccountingUtil.deleteFiles(file);
			}
			
			SessionFactoryUtil.commitTransaction();

			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("batchFileRecordList: %s", batchFileRecordList.size()));
				LOGGER.info(String.format("end processFateFile: %s", new Date().getTime()));
			}
			return batchFileRecordList;
				
		} catch (FileNotFoundException e) {
			LOGGER.warning(String.format( "Fail to find file %s", e.getMessage()));
			SessionFactoryUtil.rollbackTransaction();
		} catch (ParseException e) {
			LOGGER.warning(String.format( "Fail to parse in processHeader %s", e.getMessage()));
			SessionFactoryUtil.rollbackTransaction();
		}
        return Collections.emptyList();
    }
	
	@Override
	public String generateAccpacCsvFile(String downloadPath, String fileName, List<AccpacDetail> accpacDetailList) throws FileNotFoundException, AccountingServiceException{
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("start generateAccpacFile: %s", new Date().getTime()));
		}
		//create csv file
		File csvFile = new File(downloadPath, fileName + ".csv");
		if (!csvFile.exists()){
			try {
				boolean created = csvFile.createNewFile();
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("Created file: %s", created));
				}
			} catch (IOException e1) {
				LOGGER.info(String.format("Fail to generate csv file: %s %s", csvFile.getAbsolutePath(),e1));
			}
		}
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("Accpac SAG file in: %s", csvFile.getAbsolutePath()));
		}
		StringBuilder sb = new StringBuilder();
		
		String header = AccountingUtil.getAccpacHeaderAsCsvString();
		sb.append(header);
		sb.append(NEW_LINE);
		
		for (AccpacDetail detail : accpacDetailList){
			String sDetail = AccountingUtil.getAccpacDetailAsCsvString(detail);	
			sb.append(sDetail);
			sb.append(NEW_LINE);
		}
		//write into batch file
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(csvFile);
			writer.write(sb.toString());

			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("end generate accpac file: %s", new Date().getTime()));
			}
		} catch (IOException e) {
			LOGGER.log (Level.SEVERE, "Fail to generate accpac csv file");
			return null;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				LOGGER.log (Level.SEVERE, "Fail to close PrintWriter");
			}
		}
		return csvFile.getAbsolutePath();
		
	}
	
	@Override
	public String getAccpacSAGCsvFileDetails(String downloadPath, List<String> paymentStatusList) throws AccountingServiceException{
	
		Calendar todayDate = Calendar.getInstance();
		if (todayDate!=null && LOGGER.isLoggable( Level.INFO )) {
			LOGGER.info(String.format("start generateAccpacFile: %s", todayDate.getTime()));
		}
		SessionFactoryUtil.beginTransaction();
		
		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		if (financialYear!=null && LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info(String.format("financialYear:%s", financialYear));
		}
		String fileName = AccountingUtil.getAccpacSAGFolderName(todayDate.getTime());
		if (fileName!=null && LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info(String.format("accpac sag file name :%s", fileName));
		}
		
		SAGApplicationCriteria criteria = new SAGApplicationCriteria();
		criteria.setApplicationStatus(ApplicationStatus.SUCCESSFUL.toString());
		criteria.setFinancialYear(financialYear);
		criteria.setChequeUpdatedDate(todayDate.getTime());
		
		LOGGER.info( String.format( "search SAG criteria: %s", criteria.toString() ) );
		
		List<SAGApplicationsApprovedForAwardWithPayment> allApplicationList = 
				sagDAO.searchSAGApplicationsApprovedForAwardWithPayment(criteria, null, paymentStatusList);
		
		//check if have applications
		int listSize = allApplicationList.size();
		if (listSize>0 && LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info(String.format("application size:%s", listSize));
		}

		if (listSize == 0){
			LOGGER.log(Level.WARNING, "generateAccpacFile error: No applications to generate SPFLee ACCPAC report" );
			SessionFactoryUtil.rollbackTransaction();
			return "";
		}
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
				
		for (SAGApplicationsApprovedForAwardWithPayment app : allApplicationList){
			
			SAGApplication sagApplication = null;
			try {
				sagApplication = sagDAO.getSAGApplication(app.getReferenceNumber());
			} catch (AccessDeniedException e) {
				LOGGER.log(Level.WARNING, "generateAccpacFile getSAGApplication error: Access Denied {}", e.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			}
			
			AccpacDetail accpacDetail = new AccpacDetail();
			accpacDetail.setBatchID("");
			accpacDetail.setEntryNumber("");
			
			String entryDescEdu = "";
			
			if (app.getAwardType().trim().equals(SAGAwardType.STUDY_AWARD.toString())){
				accpacDetail.setAccCode(FieldLengthConstants.ACCPAC_SAG_ACC_CODE_SA);
				accpacDetail.setBatchDesc(FieldLengthConstants.ACCPAC_SAG_BATCH_DESC_SA);
				
				entryDescEdu = sagApplication.getChildNewEduLevel().trim();
				
				LOGGER.info( String.format( "awardType SA!!, accCode %s, batchDesc %s, entryDescEdu %s", app.getAwardType(),
						accpacDetail.getAccCode(), accpacDetail.getBatchDesc(), entryDescEdu));
			} else if (app.getAwardType().trim().equals(SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD.toString())){
				accpacDetail.setAccCode(FieldLengthConstants.ACCPAC_SAG_ACC_CODE_SAA);
				accpacDetail.setBatchDesc(FieldLengthConstants.ACCPAC_SAG_BATCH_DESC_SAA);
				
				entryDescEdu = sagApplication.getChildHighestEduLevel().trim();
				
				LOGGER.info( String.format( "awardType SAA!!, accCode %s, batchDesc %s, entryDescEdu %s", app.getAwardType(),
						accpacDetail.getAccCode(), accpacDetail.getBatchDesc(), entryDescEdu));
			} else if (app.getAwardType().trim().equals(SAGAwardType.STUDY_GRANT.toString())){
				accpacDetail.setAccCode(FieldLengthConstants.ACCPAC_SAG_ACC_CODE_SG);
				accpacDetail.setBatchDesc(FieldLengthConstants.ACCPAC_SAG_BATCH_DESC_SG);
				
				entryDescEdu = "Special School";
				
				LOGGER.info( String.format( "awardType SG!!, accCode %s, batchDesc %s, entryDescEdu %s", app.getAwardType(),
						accpacDetail.getAccCode(), accpacDetail.getBatchDesc(), entryDescEdu));
			}
			
			boolean giro = false;
			if (app.getPreferredPaymentMode() != null){
				if (app.getPreferredPaymentMode().trim().equals(SAGPaymentType.GIRO.toString())){
					accpacDetail.setPaymentMode(FieldLengthConstants.ACCPAC_SAG_PAYMENT_MODE_GIRO);
					giro = true;
				} else if (app.getPreferredPaymentMode().trim().equals(SAGPaymentType.PAYNOW.toString())){
					accpacDetail.setPaymentMode(FieldLengthConstants.ACCPAC_SAG_PAYMENT_MODE_PAYNOW);
				} 
			} else {
				accpacDetail.setPaymentMode(FieldLengthConstants.ACCPAC_SAG_PAYMENT_MODE_CHEQUE);
			}
			LOGGER.info( String.format( "preferredPaymentMode %s , setPaymentMode %s", app.getPreferredPaymentMode(),accpacDetail.getPaymentMode()));
			
			if(app.getReferenceNumber() != null){
				accpacDetail.setPaymentReference(app.getReferenceNumber().trim());
			}
			
			String entryDesc = app.getChildName() + " - " + entryDescEdu;
			accpacDetail.setEntryDescription(entryDesc);
			
			LOGGER.info( String.format( "giro %s , bicCodeProxyType %s", giro, app.getBicCodeProxyType()));
			if (giro){
				List<BankInformation> bankList = accountingDAO.getBankInformationFromSwiftCode(app.getBicCodeProxyType());
				
				if (bankList != null && !bankList.isEmpty()){
					BankInformation bank = bankList.get(0);
					accpacDetail.setDestinationBankBranch(bank.getBankName());
				}
			} else {
				accpacDetail.setDestinationBankBranch(app.getBicCodeProxyType());
			}
			
			
			if (app.getAccNoProxyValue() != null){
				accpacDetail.setDestinationAccNo(app.getAccNoProxyValue().trim());
			}
			
			accpacDetail.setDestinationAccName(app.getAccName());
			
			accpacDetail.setLineDescription(entryDesc);
			
			String amount = (sagApplication.getAmountPaid() == null) ? "0" : sagApplication.getAmountPaid().toString();
			accpacDetail.setAmount(amount);
			
			accpacDetail.setGstAmount(FieldLengthConstants.ACCPAC_SAG_GST_AMOUNT);
			accpacDetail.setGstInclusive(FieldLengthConstants.ACCPAC_SAG_GST_INCLUSIVE);
			accpacDetail.setGstRate(FieldLengthConstants.ACCPAC_SAG_GST_RATE);
			
			Date valueDate = sagApplication.getChequeValueDate();
			String transactionDate = new SimpleDateFormat("dd/MM/yyyy").format(valueDate);
			accpacDetail.setTransactionDate(transactionDate);
			
			accpacDetail.setCurrency(FieldLengthConstants.ACCPAC_SAG_CURRENCY);
			accpacDetail.setExRate(FieldLengthConstants.ACCPAC_SAG_EX_RATE);
			accpacDetail.setBankCode(FieldLengthConstants.ACCPAC_SAG_BANK_CODE);
			accpacDetail.setTransactionType(app.getAwardType());
			
			accpacDetailList.add(accpacDetail);
			 
		}
		
		LOGGER.info( String.format( "accpacDetailList size %s ", accpacDetailList.size()));
		
		//write into batch file
		String csvFilePath = null;
		try {
			csvFilePath = generateAccpacCsvFile(downloadPath, fileName, accpacDetailList);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
			SessionFactoryUtil.rollbackTransaction();
		} catch (AccountingServiceException el) {
			LOGGER.log(Level.WARNING, "generateAccpacFile error {}", el.getMessage());
			SessionFactoryUtil.rollbackTransaction();
		} 
		
		SessionFactoryUtil.commitTransaction();
		return csvFilePath;
		
	}
	
	@Override
	public String getAccpacSAGCsvFileDetailsByDate (Date startDate, Date endDate, String downloadPath, List<String> paymentStatusList) throws AccountingServiceException{
	
		Calendar todayDate = Calendar.getInstance();
		if (todayDate!=null && LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info(String.format("start generateAccpacFile: %s", todayDate.getTime()));
		}
		
		SessionFactoryUtil.beginTransaction();
		
		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		if (financialYear!=null && LOGGER.isLoggable( Level.INFO )){
		LOGGER.info( String.format( "financialYear:%s", financialYear) );}
		
		String fileName = AccountingUtil.getAccpacSAGFolderName(todayDate.getTime());	
		if (fileName!=null && LOGGER.isLoggable( Level.INFO )){
		LOGGER.info( String.format( "accpac sag file name :%s", fileName ) );}
		
		SAGApplicationCriteria criteria = new SAGApplicationCriteria();
		criteria.setApplicationStatus(ApplicationStatus.SUCCESSFUL.toString());
		criteria.setFinancialYear(financialYear);
		
		LOGGER.info( String.format( "search SAG criteria: %s", criteria.toString() ) );
		
		List<SAGApplicationsApprovedForAwardWithPayment> allApplicationList = 
				sagDAO.searchSAGApplicationsApprovedForAwardWithPaymentByDate(startDate, endDate, criteria, null, paymentStatusList);
		
		//check if have applications
		int listSize = allApplicationList.size();
		if (listSize<0 && LOGGER.isLoggable( Level.INFO )){
		LOGGER.info( String.format( "application size:%s", listSize ) );}

		if (listSize == 0){
			LOGGER.log(Level.WARNING, "generateAccpacFile error: No applications to generate SPFLee ACCPAC report" );
			SessionFactoryUtil.rollbackTransaction();
			return "";
		}
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
				
		for (SAGApplicationsApprovedForAwardWithPayment app : allApplicationList){
			
			SAGApplication sagApplication = null;
			try {
				sagApplication = sagDAO.getSAGApplication(app.getReferenceNumber());
			} catch (AccessDeniedException e) {
				LOGGER.log(Level.WARNING, "generateAccpacFile getSAGApplication error: Access Denied {}", e.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			}
			
			AccpacDetail accpacDetail = new AccpacDetail();
			accpacDetail.setBatchID("");
			accpacDetail.setEntryNumber("");
			
			String entryDescEdu = "";
			
			if (app.getAwardType().trim().equals(SAGAwardType.STUDY_AWARD.toString())){
				accpacDetail.setAccCode(FieldLengthConstants.ACCPAC_SAG_ACC_CODE_SA);
				accpacDetail.setBatchDesc(FieldLengthConstants.ACCPAC_SAG_BATCH_DESC_SA);
				
				entryDescEdu = sagApplication.getChildNewEduLevel().trim();
				
				LOGGER.info( String.format( "awardType SA!!, accCode %s, batchDesc %s, entryDescEdu %s", app.getAwardType(),
						accpacDetail.getAccCode(), accpacDetail.getBatchDesc(), entryDescEdu));
			} else if (app.getAwardType().trim().equals(SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD.toString())){
				accpacDetail.setAccCode(FieldLengthConstants.ACCPAC_SAG_ACC_CODE_SAA);
				accpacDetail.setBatchDesc(FieldLengthConstants.ACCPAC_SAG_BATCH_DESC_SAA);
				
				entryDescEdu = sagApplication.getChildHighestEduLevel().trim();
				
				LOGGER.info( String.format( "awardType SAA!!, accCode %s, batchDesc %s, entryDescEdu %s", app.getAwardType(),
						accpacDetail.getAccCode(), accpacDetail.getBatchDesc(), entryDescEdu));
			} else if (app.getAwardType().trim().equals(SAGAwardType.STUDY_GRANT.toString())){
				accpacDetail.setAccCode(FieldLengthConstants.ACCPAC_SAG_ACC_CODE_SG);
				accpacDetail.setBatchDesc(FieldLengthConstants.ACCPAC_SAG_BATCH_DESC_SG);
				
				entryDescEdu = "Special School";
				
				LOGGER.info( String.format( "awardType SG!!, accCode %s, batchDesc %s, entryDescEdu %s", app.getAwardType(),
						accpacDetail.getAccCode(), accpacDetail.getBatchDesc(), entryDescEdu));
			}
			
			boolean giro = false;
			if (app.getPreferredPaymentMode() != null){
				if (app.getPreferredPaymentMode().trim().equals(SAGPaymentType.GIRO.toString())){
					accpacDetail.setPaymentMode(FieldLengthConstants.ACCPAC_SAG_PAYMENT_MODE_GIRO);
					giro = true;
				} else if (app.getPreferredPaymentMode().trim().equals(SAGPaymentType.PAYNOW.toString())){
					accpacDetail.setPaymentMode(FieldLengthConstants.ACCPAC_SAG_PAYMENT_MODE_PAYNOW);
				} 
			} else {
				accpacDetail.setPaymentMode(FieldLengthConstants.ACCPAC_SAG_PAYMENT_MODE_CHEQUE);
			}
			LOGGER.info( String.format( "preferredPaymentMode %s , setPaymentMode %s", app.getPreferredPaymentMode(),accpacDetail.getPaymentMode()));
			
			if(app.getReferenceNumber() != null){
				accpacDetail.setPaymentReference(app.getReferenceNumber().trim());
			}
			
			String entryDesc = app.getChildName() + " - " + entryDescEdu;
			accpacDetail.setEntryDescription(entryDesc);
			
			LOGGER.info( String.format( "giro %s , bicCodeProxyType %s", giro, app.getBicCodeProxyType()));
			if (giro){
				List<BankInformation> bankList = accountingDAO.getBankInformationFromSwiftCode(app.getBicCodeProxyType());
				
				if (bankList != null && !bankList.isEmpty()){
					BankInformation bank = bankList.get(0);
					accpacDetail.setDestinationBankBranch(bank.getBankName());
				}
			} else {
				accpacDetail.setDestinationBankBranch(app.getBicCodeProxyType());
			}
			
			
			if (app.getAccNoProxyValue() != null){
				accpacDetail.setDestinationAccNo(app.getAccNoProxyValue().trim());
			}
			
			accpacDetail.setDestinationAccName(app.getAccName());
			
			accpacDetail.setLineDescription(entryDesc);
			
			String amount = (sagApplication.getAmountPaid() == null) ? "0" : sagApplication.getAmountPaid().toString();
			accpacDetail.setAmount(amount);
			
			accpacDetail.setGstAmount(FieldLengthConstants.ACCPAC_SAG_GST_AMOUNT);
			accpacDetail.setGstInclusive(FieldLengthConstants.ACCPAC_SAG_GST_INCLUSIVE);
			accpacDetail.setGstRate(FieldLengthConstants.ACCPAC_SAG_GST_RATE);
			
			Date valueDate = sagApplication.getChequeValueDate();
			String transactionDate = new SimpleDateFormat("dd/MM/yyyy").format(valueDate);
			accpacDetail.setTransactionDate(transactionDate);
			
			accpacDetail.setCurrency(FieldLengthConstants.ACCPAC_SAG_CURRENCY);
			accpacDetail.setExRate(FieldLengthConstants.ACCPAC_SAG_EX_RATE);
			accpacDetail.setBankCode(FieldLengthConstants.ACCPAC_SAG_BANK_CODE);
			accpacDetail.setTransactionType(app.getAwardType());
			
			accpacDetailList.add(accpacDetail);
			 
		}
		
		LOGGER.info( String.format( "accpacDetailList size %s ", accpacDetailList.size()));
		
		//write into batch file
		String csvFilePath = null;
		try {
			csvFilePath = generateAccpacCsvFile(downloadPath, fileName, accpacDetailList);
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
			SessionFactoryUtil.rollbackTransaction();
		} catch (AccountingServiceException el) {
			LOGGER.log(Level.WARNING, "generateAccpacFile error {}", el.getMessage());
			SessionFactoryUtil.rollbackTransaction();
		} 
		
		SessionFactoryUtil.commitTransaction();
		return csvFilePath;
		
	}
	
	private String getCodeDescription( CodeType codeType, String codeId ) {
        String codeDescription = null;
        if ( codeId != null ) {
            Code code = codeDAO.getCode( codeType, codeId );
            if ( code != null ) {
                codeDescription = code.getDescription();
            }
        }
        return codeDescription;
    }
	
	private List<AccpacDetail> getAccpacDetailFromBereavementList(List<BereavementGrantDetails> bereavementList) throws Exception {
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
		
		if (bereavementList.size() == 0){
			LOGGER.log(Level.INFO, "NO bereavement grants after removing !!");
		} else {
			for (BereavementGrantDetails brvGrant : bereavementList){
				String nric = brvGrant.getMemberNric();
				String referenceNo = brvGrant.getReferenceNumber();
				
				try{
					PersonalDetail personnel = personnelDAO.getPersonal(nric);
					if (personnel == null){
						if ( LOGGER.isLoggable( Level.WARNING ) ) {
							LOGGER.warning(String.format("Unable to find personnel for %s", nric));
						}
						continue;
					}
					boolean isExtMHA = this.isExtMHA(personnel.getEmployment());
					
					String name = personnel.getName();
					String entryDesc = nric + "-" + name;
				
					BereavementGrant brv = benefitsDAO.getBereavementGrant(referenceNo);
					
					String amount = (brvGrant.getAmountToBePaid() == null) ? "" : brvGrant.getAmountToBePaid().toString();
					String transactionDate = AccountingUtil.getAccpacTransactionDatefromApprovalRecords(brv.getApprovalRecords());
					
					AccpacDetail ap = new AccpacDetail();
					ap.setAccCode(FieldLengthConstants.ACCPAC_BENE_ACC_CODE_BEREAVEMENT);
					ap.setBatchDesc(FieldLengthConstants.ACCPAC_BENE_BATCH_DESC_BEREAVEMENT);
					
					ap = this.populateAccpacDetail(ap, referenceNo, isExtMHA, brv.getBranchCode(), 
							brv.getAccountNo(), name, entryDesc, amount, transactionDate);
					
					accpacDetailList.add(ap);
				} catch (Exception e) {
					LOGGER.log( Level.WARNING, "Exception in getAccpacDetailFromBereavementList {}" , e.getMessage() );
				}
			}
			LOGGER.info( String.format( "bereavement accpacDetailList size %s ", accpacDetailList.size()));
		}
		return accpacDetailList;
	}
	
	private List<AccpacDetail> getAccpacDetailFromNewbornList(List<NewBornGiftDetails> newbornList) throws Exception {
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
		
		if (newbornList.isEmpty()){
			LOGGER.log(Level.INFO, "NO newborn gifts !!");
		} else {
			for (NewBornGiftDetails nbGrant : newbornList){
				String nric = nbGrant.getMemberNric();
				String referenceNo = nbGrant.getReferenceNumber();
				
				try{
					PersonalDetail personnel = personnelDAO.getPersonal(nric);
					if (personnel == null){
						if ( LOGGER.isLoggable( Level.WARNING ) ) {
							LOGGER.warning(String.format("Unable to find personnel for %s", nric));
						}
						continue;
					}
					boolean isExtMHA = this.isExtMHA(personnel.getEmployment());
					
					String name = personnel.getName();
					String entryDesc = nric + "-" + name;
				
					NewBornGift nb = benefitsDAO.getNewBorn(referenceNo);
					
					String amount = (nbGrant.getAmountToBePaid() == null) ? "" : nbGrant.getAmountToBePaid().toString();
					String transactionDate = AccountingUtil.getAccpacTransactionDatefromApprovalRecords(nb.getApprovalRecords());
					
					AccpacDetail ap = new AccpacDetail();
					ap.setAccCode(FieldLengthConstants.ACCPAC_BENE_ACC_CODE_NEWBORN);
					ap.setBatchDesc(FieldLengthConstants.ACCPAC_BENE_BATCH_DESC_NEWBORN);
					
					ap = this.populateAccpacDetail(ap, referenceNo, isExtMHA, nb.getBranchCode(), 
							nb.getAccountNo(), name, entryDesc, amount, transactionDate);
					
					accpacDetailList.add(ap);
				} catch (Exception e) {
					LOGGER.log( Level.WARNING, "Exception in getAccpacDetailFromNewbornList {}" , e.getMessage() );
				}
				
			}
			LOGGER.info( String.format( "newborn accpacDetailList size %s ", accpacDetailList.size()));
		}
		return accpacDetailList;
	}
	
	private List<AccpacDetail> getAccpacDetailFromWeddingList(List<WeddingGiftDetails> weddingList) throws Exception{
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
		
		if (weddingList.isEmpty()){
			LOGGER.log(Level.INFO, "NO wedding gifts !!");
		} else {
			for (WeddingGiftDetails wedGrant : weddingList){
				String nric = wedGrant.getMemberNric();
				String referenceNo = wedGrant.getReferenceNumber();
				
				try {
					PersonalDetail personnel = personnelDAO.getPersonal(nric);
					if (personnel == null){
						if ( LOGGER.isLoggable( Level.WARNING ) ) {
							LOGGER.warning(String.format("Unable to find personnel for %s", nric));
						}
						continue;
					}
					boolean isExtMHA = this.isExtMHA(personnel.getEmployment());
					
					String name = personnel.getName();
					String entryDesc = nric + "-" + name;
				
					WeddingGift wed = benefitsDAO.getWeddingGift(referenceNo);
					
					String amount = (wedGrant.getAmountToBePaid() == null) ? "" : wedGrant.getAmountToBePaid().toString();
					String transactionDate = AccountingUtil.getAccpacTransactionDatefromApprovalRecords(wed.getApprovalRecords());
					
					AccpacDetail ap = new AccpacDetail();
					ap.setAccCode(FieldLengthConstants.ACCPAC_BENE_ACC_CODE_WEDDING);
					ap.setBatchDesc(FieldLengthConstants.ACCPAC_BENE_BATCH_DESC_WEDDING);
					
					ap = this.populateAccpacDetail(ap, referenceNo, isExtMHA, wed.getBranchCode(), 
							wed.getAccountNo(), name, entryDesc, amount, transactionDate);
					
					accpacDetailList.add(ap);
				
				} catch (Exception e) {
					LOGGER.warning(String.format("Exception in getAccpacDetailFromWeddingList %s" , e.getMessage() ));
				}
				
			}
			LOGGER.info( String.format( "wedding accpacDetailList size %s ", accpacDetailList.size()));
		}
		return accpacDetailList;
	}
	
	private AccpacDetail populateAccpacDetail (AccpacDetail ap, String referenceNo, boolean isExtMHA, String branchCode, String accNo, 
			String name, String entryDesc, String amount, String transactionDate) {
		
		if (isExtMHA){
			ap.setPaymentMode(FieldLengthConstants.ACCPAC_BENE_PAYMENT_MODE_DEPT_EXT_MHA);
			ap.setDestinationBankBranch(FieldLengthConstants.ACCPAC_BENE_DEST_BANK_GIRO + branchCode);
			ap.setDestinationAccNo(accNo);
			ap.setDestinationAccName(name);
		} else {
			ap.setPaymentMode(FieldLengthConstants.ACCPAC_BENE_PAYMENT_MODE);
			ap.setDestinationBankBranch(FieldLengthConstants.ACCPAC_BENE_DEST_BANK_BRANCH_NON_GIRO);
			ap.setDestinationAccNo(FieldLengthConstants.ACCPAC_BENE_DEST_ACC_NO_NON_GIRO);
			ap.setDestinationAccName(FieldLengthConstants.ACCPAC_BENE_DEST_ACC_NAME_NON_GIRO);
		}
		
		ap.setPaymentReference(referenceNo);
		ap.setEntryDescription(entryDesc);
		ap.setLineDescription(entryDesc);
		ap.setAmount(amount);
		ap.setTransactionDate(transactionDate);
		
		//default
		ap.setGstAmount(FieldLengthConstants.ACCPAC_BENE_GST_AMOUNT);
		ap.setGstInclusive(FieldLengthConstants.ACCPAC_BENE_GST_INCLUSIVE);
		ap.setGstRate(FieldLengthConstants.ACCPAC_BENE_GST_RATE);
		ap.setCurrency(FieldLengthConstants.ACCPAC_BENE_CURRENCY);
		ap.setExRate(FieldLengthConstants.ACCPAC_BENE_EX_RATE);
		ap.setBankCode(FieldLengthConstants.ACCPAC_BENE_BANK_CODE);
		ap.setTransactionType(FieldLengthConstants.ACCPAC_BENE_TRANSACTION_TYPE);
		
		return ap;
	}
	
	private boolean isExtMHA (Employment emp) {
		
		boolean isExtMHA = false;
		if (emp == null){
			return isExtMHA;
		}
		
		String dept = this.getCodeDescription( CodeType.UNIT_DEPARTMENT, emp.getOrganisationOrDepartment() );
		String subUnit = this.getCodeDescription(CodeType.SUB_UNIT, emp.getSubunit() );
		if (dept!= null) {
			if (dept.equals("EXTERNAL AGENCIES") && subUnit != null && subUnit.equals("MHA")) {
				isExtMHA = true;
			}
			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("dept %s, subunit %s, isExtMHA %s ", dept, subUnit, isExtMHA));
			}
		}
		return isExtMHA;
	}

	public String getAccpacBeneCsvFileDetails(String downloadPath) throws AccountingServiceException{
		
		Calendar todayDate = Calendar.getInstance(); 
		if ((todayDate!=null) && ( LOGGER.isLoggable( Level.INFO ) )){
			LOGGER.info(String.format("start generateAccpacFile: %s", todayDate.getTime()));
		}
		SessionFactoryUtil.beginTransaction();
				
		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		if ((financialYear!=null) && ( LOGGER.isLoggable( Level.INFO ) )){
			LOGGER.info(String.format("financialYear:%s", financialYear));
		}
		String fileName = AccountingUtil.getAccpacBeneFolderName(todayDate.getTime());	
		if ((fileName!=null) && ( LOGGER.isLoggable( Level.INFO ) )){
			LOGGER.info(String.format("accpac bene file name :%s", fileName));
		}
		int day = todayDate.get(Calendar.DATE);
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.DATE, day - 1);
		startDate.set(Calendar.HOUR_OF_DAY, 18);
		startDate.set(Calendar.MINUTE, 30);
		
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DATE, day);
		endDate.set(Calendar.HOUR_OF_DAY, 18);
		endDate.set(Calendar.MINUTE, 30);
		if (startDate!=null && endDate!=null && LOGGER.isLoggable( Level.INFO )) {
			LOGGER.info(String.format("search startDate: %s endDate: %s", startDate.getTime(), endDate.getTime()));
		}
		BenefitsCriteria criteria = new BenefitsCriteria();
		criteria.setOfficerAction(ApplicationStatus.ENDORSED.name());
		criteria.setOfficerLevel("AD");
		criteria.setSearchStartDate(startDate.getTime());
		criteria.setSearchEndDate(endDate.getTime());
		
		LOGGER.info( String.format( "search benefits criteria: %s", criteria.toString() ) );
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
		
		//bereavement
		List< BereavementGrantDetails > bereavementList= benefitsDAO.searchBereavementGrantByApprovalsCriteria(criteria);
		if (bereavementList != null && !bereavementList.isEmpty()) {
			
			//remove member relationship
			LOGGER.info( String.format( "before remove member: bereavement list size %s", bereavementList.size() ) );
			Iterator<BereavementGrantDetails> i = bereavementList.iterator();
			while (i.hasNext()){
				BereavementGrantDetails brv = i.next();
				if (brv.getRelationship().equals("Member")){
					LOGGER.info( String.format( "remove member relationship in bereavement grant: %s", brv.getReferenceNumber() ) );
					i.remove();
				}
			}
			LOGGER.info( String.format( "after remove member: bereavement list size %s", bereavementList.size() ) );
			
			try {
				accpacDetailList.addAll(this.getAccpacDetailFromBereavementList(bereavementList));
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			}
			
		} else {
			LOGGER.log(Level.INFO, "NO bereavement grants !!");
		}
		
		LOGGER.info( String.format( "after bereavement : accpac list size %s", accpacDetailList.size() ) );
		
		try {
			//newborn
			List< NewBornGiftDetails > newbornList= benefitsDAO.searchNewBornByApprovalsCriteria(criteria);
			LOGGER.info( String.format( "newborn list size %s", (newbornList == null) ? null : newbornList.size() ) );
			accpacDetailList.addAll(this.getAccpacDetailFromNewbornList(newbornList));
			LOGGER.info( String.format( "after newborn : accpac list size %s", accpacDetailList.size() ) );
			
			//wedding
			List< WeddingGiftDetails > weddingList= benefitsDAO.searchWeddingByApprovalsCriteria(criteria);
			LOGGER.info( String.format( "wedding list size %s", (weddingList == null) ? null : weddingList.size() ) );
			accpacDetailList.addAll(this.getAccpacDetailFromWeddingList(weddingList));
			LOGGER.info( String.format( "after wedding : accpac list size %s", accpacDetailList.size() ) );
		} catch(Exception e){
			LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
			SessionFactoryUtil.rollbackTransaction();
		}
		
		String csvFilePath = "";
		if (accpacDetailList.size() > 0){
			//write into batch file
			try {
				csvFilePath = generateAccpacCsvFile(downloadPath, fileName, accpacDetailList);
			} catch (FileNotFoundException e) {
				LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			} catch (AccountingServiceException el) {
				LOGGER.log(Level.WARNING, "generateAccpacFile error {}", el.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			} 
		}
		
		SessionFactoryUtil.commitTransaction();
		return csvFilePath;
		
	}
	
public String getAccpacBeneCsvFileDetailsByDate(Date startDate, Date endDate, String downloadPath) throws AccountingServiceException{
		
		Calendar todayDate = Calendar.getInstance(); 
		if (todayDate!=null) {
			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("start generateAccpacFile: %s", todayDate.getTime()));
			}
			SessionFactoryUtil.beginTransaction();
		}

		String financialYear = ConvertUtil.convertToFinancialYearString(todayDate);
		if ((financialYear!=null) && ( LOGGER.isLoggable( Level.INFO ) )) {
			LOGGER.info(String.format("financialYear:%s", financialYear));
		}
		String fileName = AccountingUtil.getAccpacBeneFolderName(todayDate.getTime());
		if ((fileName!=null) && ( LOGGER.isLoggable( Level.INFO ) )){
			LOGGER.info(String.format("accpac bene file name :%s", fileName));
		}
		if ((startDate !=null && endDate !=null) && ( LOGGER.isLoggable( Level.INFO ) )){
			LOGGER.info(String.format("search startDate: %s endDate: %s", startDate, endDate));
		}

		BenefitsCriteria criteria = new BenefitsCriteria();
		criteria.setOfficerAction(ApplicationStatus.ENDORSED.name());
		criteria.setOfficerLevel("AD");
		criteria.setSearchStartDate(startDate);
		criteria.setSearchEndDate(endDate);
		
		LOGGER.info( String.format( "search benefits criteria: %s", criteria.toString() ) );
		
		List<AccpacDetail> accpacDetailList = new ArrayList<>();
		
		//bereavement
		List< BereavementGrantDetails > bereavementList= benefitsDAO.searchBereavementGrantByApprovalsCriteria(criteria);
		if (bereavementList != null && !bereavementList.isEmpty()) {
			
			//remove member relationship
			LOGGER.info( String.format( "before remove member: bereavement list size %s", bereavementList.size() ) );
			Iterator<BereavementGrantDetails> i = bereavementList.iterator();
			while (i.hasNext()){
				BereavementGrantDetails brv = i.next();
				if (brv.getRelationship().equals("Member")){
					LOGGER.info( String.format( "remove member relationship in bereavement grant: %s", brv.getReferenceNumber() ) );
					i.remove();
				}
			}
			LOGGER.info( String.format( "after remove member: bereavement list size %s", bereavementList.size() ) );
			
			try{
				accpacDetailList.addAll(this.getAccpacDetailFromBereavementList(bereavementList));
			} catch(Exception e){
				LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			}
			
		} else {
			LOGGER.log(Level.INFO, "NO bereavement grants !!");
		}
		
		LOGGER.info( String.format( "after bereavement : accpac list size %s", accpacDetailList.size() ) );
		
		try{
			//newborn
			List< NewBornGiftDetails > newbornList= benefitsDAO.searchNewBornByApprovalsCriteria(criteria);
			LOGGER.info( String.format( "newborn list size %s", (newbornList == null) ? null : newbornList.size() ) );
			accpacDetailList.addAll(this.getAccpacDetailFromNewbornList(newbornList));
			LOGGER.info( String.format( "after newborn : accpac list size %s", accpacDetailList.size() ) );
			
			//wedding
			List< WeddingGiftDetails > weddingList= benefitsDAO.searchWeddingByApprovalsCriteria(criteria);
			LOGGER.info( String.format( "wedding list size %s", (weddingList == null) ? null : weddingList.size() ) );
			accpacDetailList.addAll(this.getAccpacDetailFromWeddingList(weddingList));
			LOGGER.info( String.format( "after wedding : accpac list size %s", accpacDetailList.size() ) );
		} catch (Exception e){
			LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
			SessionFactoryUtil.rollbackTransaction();
		}
		
		String csvFilePath = "";
		if (accpacDetailList.size() > 0){
			//write into batch file
			try {
				csvFilePath = generateAccpacCsvFile(downloadPath, fileName, accpacDetailList);
			} catch (FileNotFoundException e) {
				LOGGER.log(Level.WARNING, "generateAccpacFile error {}", e.getMessage());
				SessionFactoryUtil.rollbackTransaction();
			} catch (AccountingServiceException el) {
				LOGGER.log(Level.WARNING, "Failed to generate Accpac file due to AccountingServiceException: {}", el.getMessage());
			}

		}
		
		SessionFactoryUtil.commitTransaction();
		return csvFilePath;
		
	}
}