package com.stee.spfcore.security;

public class AccessDeniedException extends Exception {
	
	private static final long serialVersionUID = 1462754549084978338L;

	public AccessDeniedException (String message) {
		super (message);
	}
	
	public AccessDeniedException (String message, Throwable cause) {
		super (message, cause);
	} 

}
