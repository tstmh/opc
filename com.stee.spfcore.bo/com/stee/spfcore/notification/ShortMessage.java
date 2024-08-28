package com.stee.spfcore.notification;

public class ShortMessage {

	private String recipient;
	private String text;

	public ShortMessage() {
		super();
	}

	public ShortMessage(String recipient, String text) {
		super();
		this.recipient = recipient;
		this.text = text;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
