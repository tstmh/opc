package com.stee.spfcore.service.hrps;

public class HRPSServiceException extends Exception {

	private static final long serialVersionUID = -8114548043874458122L;
	
	public HRPSServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HRPSServiceException(String message) {
		super(message);
	}
}
