package com.stee.spfcore.service.department;

public class DepartmentException extends Exception {

    private static final long serialVersionUID = 1L;

    public DepartmentException( String message, Throwable cause ) {
        super( message, cause );
    }

    public DepartmentException( String message ) {
        super( message );
    }
}
