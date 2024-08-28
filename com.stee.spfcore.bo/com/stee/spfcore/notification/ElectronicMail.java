package com.stee.spfcore.notification;

import java.util.ArrayList;
import java.util.List;

public class ElectronicMail {

	private String userAddress;
	private String userPassword;
	private String toAddress;
	private String ccAddress;
	private String subject;
	private String text;
	private boolean isHtmlContent = false;
	private List<String> attachments = new ArrayList<>();
	
	
	public ElectronicMail() {
		super();
	}

	public ElectronicMail(String userAddress, String userPassword,
			String toAddress, String ccAddress, String subject, String text) {
		super();
		this.userAddress = userAddress;
		this.userPassword = userPassword;
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		this.subject = subject;
		this.text = text;
	}
	
	public String getUserAddress() {
		return userAddress;
	}
	
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public String getToAddress() {
		return toAddress;
	}
	
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public boolean isHtmlContent() {
		return isHtmlContent;
	}

	public void setHtmlContent(boolean isHtmlContent) {
		this.isHtmlContent = isHtmlContent;
	}
	
	public void addAttachment (String file) {
		this.attachments.add(file);
	}
	
	public List<String> getAttachments () {
		return this.attachments;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}
	
}
