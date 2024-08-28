package com.stee.spfcore.service.hr.impl.input;

public class DataException extends Exception {

	private static final long serialVersionUID = 5655311017930857392L;

	public DataException (String message) {
		super (message);
	}
	
	public DataException (String message, Throwable cause) {
		super (message, cause);
	}
	
}
