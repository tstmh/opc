package com.stee.spfcore.service.accounting;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.accounting.AccpacDetail;
import com.stee.spfcore.model.accounting.BankInformation;
import com.stee.spfcore.model.accounting.BatchFileConfig;
import com.stee.spfcore.model.sag.SAGBatchFileRecord;

public interface IAccountingService {

	public String generateFateFile(List<String> referenceNumberList, int numOfFilesToGenerate, String downloadPath,
			 List<String> paymentStatusList, String requestor) throws AccountingServiceException;
	
	public List<SAGBatchFileRecord> processFateFile(String filePath, String requestor) throws AccountingServiceException;
	
	public String generateMockReturnFile(List<SAGBatchFileRecord> recordList, int numOfFilesToGenerate, 
			String downloadPath, List<String> paymentStatusList) throws AccountingServiceException;

	public List<BatchFileConfig> searchBatchFileConfigByYear(String financialYear) throws AccountingServiceException;

	void saveBatchFileConfig(BatchFileConfig batchFileConfig, String requestor) throws AccountingServiceException; 

	List<BankInformation> getBankInformation() throws AccountingServiceException;

	List<BankInformation> getBankInformationFromCode(String bankCode) throws AccountingServiceException;
	
	public String getAccpacSAGCsvFileDetails(String downloadPath, List<String> paymentStatusList) throws AccountingServiceException;
	
	public String getAccpacSAGCsvFileDetailsByDate(Date startDate, Date endDate, String downloadPath, List<String> paymentStatusList) throws AccountingServiceException;
	
	public String generateAccpacCsvFile(String downloadPath, String fileName, List<AccpacDetail> accpacDetailList) throws FileNotFoundException, AccountingServiceException;
	
	public String getAccpacBeneCsvFileDetails(String downloadPath) throws AccountingServiceException;

	public String getAccpacBeneCsvFileDetailsByDate(Date startDate, Date endDate, String downloadPath) throws AccountingServiceException;
}
