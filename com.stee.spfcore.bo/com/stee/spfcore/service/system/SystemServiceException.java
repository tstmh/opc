package com.stee.spfcore.service.system;

public class SystemServiceException extends Exception {

	private static final long serialVersionUID = -4392764220035803484L;

	public SystemServiceException (String message) {
		super (message);
	}
	
	public SystemServiceException (String message, Throwable cause) {
		super (message, cause);
	} 
}
