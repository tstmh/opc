package com.stee.spfcore.service.code;

public class CodeServiceException extends Exception {

	private static final long serialVersionUID = 167671467307095342L;

	public CodeServiceException(String message) {
		super(message);
	}

	public CodeServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
