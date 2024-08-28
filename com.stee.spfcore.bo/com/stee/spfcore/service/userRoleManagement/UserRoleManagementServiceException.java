package com.stee.spfcore.service.userRoleManagement;

public class UserRoleManagementServiceException extends Exception {
    private static final long serialVersionUID = 866729453580027988L;

    public UserRoleManagementServiceException( String message, Throwable cause ) {
        super( message, cause );
    }

    public UserRoleManagementServiceException( String message ) {
        super( message );
    }
}
