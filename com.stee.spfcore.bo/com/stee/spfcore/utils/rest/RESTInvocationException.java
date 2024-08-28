package com.stee.spfcore.utils.rest;

public class RESTInvocationException extends Exception {

	private static final long serialVersionUID = 4971957811836520109L;

	
	public RESTInvocationException(String message) {
		super(message);
	}


	public RESTInvocationException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
