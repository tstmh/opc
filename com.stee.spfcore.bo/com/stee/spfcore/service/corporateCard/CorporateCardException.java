package com.stee.spfcore.service.corporateCard;

public class CorporateCardException extends Exception {

    private static final long serialVersionUID = 1L;

    public CorporateCardException( String message, Throwable cause ) {
        super( message, cause );
    }

    public CorporateCardException( String message ) {
        super( message );
    }
}
