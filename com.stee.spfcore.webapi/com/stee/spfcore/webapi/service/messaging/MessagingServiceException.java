package com.stee.spfcore.webapi.service.messaging;

public class MessagingServiceException extends Exception {

	private static final long serialVersionUID = -9168900319335035129L;

	public MessagingServiceException(String message) {
		super(message);
	}
	
	public MessagingServiceException(String message, Throwable cause) {
		super(message, cause);
	} 
}
