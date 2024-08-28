package com.stee.spfcore.service.personnel;

public class PersonnelServiceException extends Exception {

	private static final long serialVersionUID = -4731373166309839900L;

	public PersonnelServiceException (String message) {
		super (message);
	}
	
	public PersonnelServiceException (String message, Throwable cause) {
		super (message, cause);
	} 
	
}
