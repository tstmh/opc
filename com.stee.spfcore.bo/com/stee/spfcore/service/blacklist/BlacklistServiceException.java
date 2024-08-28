package com.stee.spfcore.service.blacklist;

public class BlacklistServiceException extends Exception {

	private static final long serialVersionUID = -3369695900547071787L;

	public BlacklistServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlacklistServiceException(String message) {
		super(message);
	}

}
