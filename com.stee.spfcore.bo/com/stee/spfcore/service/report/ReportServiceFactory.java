package com.stee.spfcore.service.report;

import com.stee.spfcore.service.report.impl.ReportService;

public class ReportServiceFactory {

    private ReportServiceFactory(){}
    private static IReportService service = null;

    public static synchronized IReportService getInstance() {
        if ( service == null ) {
            service = createReportService();
        }

        return service;
    }

    private static IReportService createReportService() {

    	return new ReportService();
        
    }

}
