package com.stee.spfcore.service.membership;

public class MembershipServiceException extends Exception {

	private static final long serialVersionUID = 1951806351047946011L;

	public MembershipServiceException(String message) {
		super(message);
	}

	public MembershipServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
