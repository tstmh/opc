package com.stee.spfcore.webapi.notification;

import java.util.List;

public class BatchShortMessage {

	private List<String> recipients;
	private String text;

	public BatchShortMessage() {
		super();
	}

	public BatchShortMessage(List<String> recipients, String text) {
		super();
		this.recipients = recipients;
		this.text = text;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
