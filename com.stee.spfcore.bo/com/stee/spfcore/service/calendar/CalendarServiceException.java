package com.stee.spfcore.service.calendar;

public class CalendarServiceException extends Exception {

	private static final long serialVersionUID = 3715498217867243141L;

	public CalendarServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CalendarServiceException(String message) {
		super(message);
	}
}
