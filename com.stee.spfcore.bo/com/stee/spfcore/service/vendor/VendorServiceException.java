package com.stee.spfcore.service.vendor;

public class VendorServiceException extends Exception {

	private static final long serialVersionUID = 2056184373129375424L;

	public VendorServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public VendorServiceException(String message) {
		super(message);
	}
	
}
