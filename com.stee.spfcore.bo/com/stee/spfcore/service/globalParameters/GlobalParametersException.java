package com.stee.spfcore.service.globalParameters;

public class GlobalParametersException extends Exception {

    private static final long serialVersionUID = 1L;

    public GlobalParametersException( String message, Throwable cause ) {
        super( message, cause );
    }

    public GlobalParametersException( String message ) {
        super( message );
    }
}
