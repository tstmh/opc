package com.stee.spfcore.model.announcement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ANNOUNCEMENT_SENDERS", schema = "SPFCORE")
public class AnnouncementSender {

	@Id
	@Column(name = "\"SENDER_EMAIL\"", length=256)
	private String senderEmail;
	
	@Column(name = "\"SENDER_EMAIL_PASSWORD\"", length=100)
	private String senderEmailPassword;

	public AnnouncementSender() {
		super();
	}

	public AnnouncementSender(String senderEmail, String senderEmailPassword) {
		super();
		this.senderEmail = senderEmail;
		this.senderEmailPassword = senderEmailPassword;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderEmailPassword() {
		return senderEmailPassword;
	}

	public void setSenderEmailPassword(String senderEmailPassword) {
		this.senderEmailPassword = senderEmailPassword;
	}
}
