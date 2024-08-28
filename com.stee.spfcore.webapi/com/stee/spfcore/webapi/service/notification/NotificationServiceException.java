package com.stee.spfcore.webapi.service.notification;

public class NotificationServiceException extends Exception {

	private static final long serialVersionUID = 9100342741735135510L;

	public NotificationServiceException(String message) {
		super(message);
	}
	
	public NotificationServiceException(String message, Throwable cause) {
		super(message, cause);
	} 
}