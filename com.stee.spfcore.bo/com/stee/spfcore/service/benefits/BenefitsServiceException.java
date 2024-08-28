package com.stee.spfcore.service.benefits;

public class BenefitsServiceException extends Exception {

	private static final long serialVersionUID = 2871346360734000151L;

	public BenefitsServiceException (String message) {
		super (message);
	}
	
	public BenefitsServiceException (String message, Throwable cause) {
		super (message, cause);
	} 
}
