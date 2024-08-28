package com.stee.spfcore.webapi.service.notification.impl;

import com.stee.spfcore.webapi.notification.ShortMessage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SMS")
public class SmsRequest {
	
	@XStreamAlias("SMSREF")
	private String reference;

	@XStreamAlias("DEST")
	private String destination;

	@XStreamAlias("MSG")
	private String message;

	public SmsRequest(ShortMessage message) {
		this.reference = "UATSMSA00001";
		this.destination = message.getRecipient();
		this.message = message.getText();
	}
}
