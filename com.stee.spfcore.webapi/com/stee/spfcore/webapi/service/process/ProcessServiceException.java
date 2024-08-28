package com.stee.spfcore.webapi.service.process;

public class ProcessServiceException extends Exception {

	private static final long serialVersionUID = -417706623416349668L;

	public ProcessServiceException(String message) {
		super(message);
	}
	
	public ProcessServiceException(String message, Throwable cause) {
		super(message, cause);
	} 
}

