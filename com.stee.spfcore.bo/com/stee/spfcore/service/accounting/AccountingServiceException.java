package com.stee.spfcore.service.accounting;

public class AccountingServiceException extends Exception {
	
    private static final long serialVersionUID = 1L;

    public AccountingServiceException( String message, Throwable cause ) {
        super( message, cause );
    }

    public AccountingServiceException( String message ) {
        super( message );
    }
}
