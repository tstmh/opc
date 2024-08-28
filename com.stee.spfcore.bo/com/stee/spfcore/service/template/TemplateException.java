package com.stee.spfcore.service.template;

public class TemplateException extends Exception {

    private static final long serialVersionUID = 1438488133301575215L;

    public TemplateException( String message, Throwable cause ) {
        super( message, cause );
    }

    public TemplateException( String message ) {
        super( message );
    }
}
