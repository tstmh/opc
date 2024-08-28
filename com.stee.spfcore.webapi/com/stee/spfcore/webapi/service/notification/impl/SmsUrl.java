package com.stee.spfcore.webapi.service.notification.impl;

public class SmsUrl {
	
	private static String contextroot = "/sms/SendSMS";
	private static String contentType = "application/x-www-form-urlencoded";

	private String baseUrl;
	private String path;
	
	protected SmsUrl() {
		this.baseUrl = "https://" + "sms.sgmail.sgnet.gov.sg";
		this.path = contextroot;
	}
	
	public String getUrl() {
		return baseUrl + this.path;
	}
	
	public static String getContentType() {
		return contentType;
	}
}
