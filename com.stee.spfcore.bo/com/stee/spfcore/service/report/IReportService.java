package com.stee.spfcore.service.report;

import java.util.Map;

import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.report.SsrsReport;
import com.stee.spfcore.model.report.SsrsReportEnvironment;

public interface IReportService {
    public SsrsReportEnvironment getReportEnvironment( Boolean environmentEnabled, String environmentName ) throws ReportServiceException;

    public SsrsReport getReport( Boolean environmentEnabled, String environmentName, Boolean reportEnabled, Boolean isAdhoc, String rootDir, String reportName ) throws ReportServiceException;

    public void downloadReport (String reportName, String reportFile, Map<String,String> parameters) throws ReportServiceException;
    
    public BinaryFile downloadReport (String reportName, Map<String,String> parameters) throws ReportServiceException;
    
    public String getReportUrl (String reportPath, Map<String, String> parameters) throws ReportServiceException;

}
