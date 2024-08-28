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
@Table(name = "MARKETING_CONTENT_EMAIL_LOGS", schema = "SPFCORE")
public class EmailLog {

	@Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column( name = "\"ID\"" )
	private long id;

	@Column( name = "\"EMAIL_ADDRESS\"", length = 256 )
	private String email;

	@Column(name = "\"CONTENT_ID\"")
	private String contentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"SENT_DATE\"")
	private Date sentDate;

	public EmailLog() {
		super();
	}

	public EmailLog(long id, String email, String contentId, Date sentDate) {
		super();
		this.id = id;
		this.email = email;
		this.contentId = contentId;
		this.sentDate = sentDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
}
