package com.stee.spfcore.service.report.impl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.ReportDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.report.SsrsReport;
import com.stee.spfcore.model.report.SsrsReportEnvironment;
import com.stee.spfcore.service.report.IReportService;
import com.stee.spfcore.service.report.ReportServiceException;

public class ReportService implements IReportService {
    private static final Logger LOGGER = Logger.getLogger( ReportService.class.getName() );

    private ReportDAO dao;
    private ReportUtil reportUtil;

    public ReportService() {
        dao = new ReportDAO();
        reportUtil = new ReportUtil();
    }

    public SsrsReportEnvironment getReportEnvironment( Boolean environmentEnabled, String environmentName ) throws ReportServiceException {
        SsrsReportEnvironment result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getReportEnvironment( environmentEnabled, environmentName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getReportEnvironment() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public SsrsReport getReport( Boolean environmentEnabled, String environmentName, Boolean reportEnabled, Boolean isAdhoc, String rootDir, String reportName ) throws ReportServiceException {
        SsrsReport result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getReport( environmentEnabled, environmentName, reportEnabled, isAdhoc, rootDir, reportName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getReport() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

	@Override
	public void downloadReport (String reportName, String reportFile, Map<String, String> parameters) throws ReportServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Object [] pathInfo = dao.getReportInfo (reportName);
			
			String reportPath = null;
			if (pathInfo == null) {
				throw new ReportServiceException("pathinfo is null.");
			}
			else {
				if(pathInfo.length == 2){
					reportPath = "/" + pathInfo [0] + "/" + pathInfo [1]; //sast
				
					reportUtil.downloadReport(reportPath, parameters, reportFile);
				} else {
					throw new ReportServiceException("pathinfo size is not 2");
				}
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (RuntimeException e) {
			LOGGER.log(Level.WARNING, "getReport() failed.", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		
	}
	
	public BinaryFile downloadReport (String reportName, Map<String, String> parameters) throws ReportServiceException {
		
		BinaryFile binaryFile = null;
		
		try {
			
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.beginTransaction();
			}
			Object [] pathInfo = dao.getReportInfo (reportName);
			
			String reportPath = null;
			if (pathInfo == null) {
				throw new ReportServiceException("pathinfo is null.");
			}
			else {
				if(pathInfo.length == 2){
					reportPath = "/" + pathInfo [0] + "/" + pathInfo [1];													
					binaryFile = reportUtil.downloadReport(reportPath, parameters);														
				} else {
					throw new ReportServiceException("pathinfo size is not 2");
				}
			}
			
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.commitTransaction();
			}
		}
		catch (RuntimeException e) {
			LOGGER.log(Level.WARNING, "getReport() failed.", e);
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.rollbackTransaction();
			}
		}
		
		return binaryFile;
	}
	
public String getReportUrl (String reportName, Map<String, String> parameters) throws ReportServiceException {
		
		String reportUrl = null;
		
		try {
			
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.beginTransaction();
			}
			Object [] pathInfo = dao.getReportInfo (reportName);
			
			String reportPath = null;
			if (pathInfo == null) {
				throw new ReportServiceException("pathinfo is null.");
			}
			else {
				if(pathInfo.length == 2){
					reportPath = "/" + pathInfo [0] + "/" + pathInfo [1];													
					reportUrl = reportUtil.getReportUrl(reportPath, parameters);														
				} else {
					throw new ReportServiceException("pathinfo size is not 2");
				}
			}
			
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.commitTransaction();
			}
		}
		catch (RuntimeException e) {
			LOGGER.log(Level.WARNING, "getReport() failed.", e);
			if (!SessionFactoryUtil.isTransactionActive()) {
				SessionFactoryUtil.rollbackTransaction();
			}
		}
		
		return reportUrl;
	}
}
