package com.stee.spfcore.model.marketingContent.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MARKETING_CONTENT_SMS_LOGS", schema = "SPFCORE")
public class SmsLog {
	
	@Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column( name = "\"ID\"" )
	private long id;

	@Column( name = "\"PHONE_NUMBER\"", length = 16 )
  private String number;

	@Column(name = "\"CONTENT_ID\"")
	private String contentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"SENT_DATE\"")
	private Date sentDate;
	
	@Column(name = "\"STATUS_CODE\"", length=10)
	private String statusCode;

	public SmsLog() {
		super();
	}

	public SmsLog(long id, String number, String contentId, Date sentDate, String statusCode) {
		super();
		this.id = id;
		this.number = number;
		this.contentId = contentId;
		this.sentDate = sentDate;
		this.statusCode = statusCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber (String number) {
		this.number = number;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
}
