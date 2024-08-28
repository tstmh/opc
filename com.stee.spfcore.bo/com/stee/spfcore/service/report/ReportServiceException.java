package com.stee.spfcore.service.report;

public class ReportServiceException extends Exception {
    private static final long serialVersionUID = 2811124446976028274L;

    public ReportServiceException( String message ) {
        super( message );
    }

    public ReportServiceException( String message, Throwable cause ) {
        super( message, cause );
    }
}
