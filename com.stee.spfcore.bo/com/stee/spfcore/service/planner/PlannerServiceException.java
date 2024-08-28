package com.stee.spfcore.service.planner;

public class PlannerServiceException extends Exception {
    private static final long serialVersionUID = -3768954518413741706L;

    public PlannerServiceException( String message ) {
        super( message );
    }

    public PlannerServiceException( String message, Throwable cause ) {
        super( message, cause );
    }

}
