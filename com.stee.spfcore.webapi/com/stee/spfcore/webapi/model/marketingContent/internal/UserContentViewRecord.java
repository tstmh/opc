package com.stee.spfcore.webapi.model.marketingContent.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "MARKETING_CONTENT_USER_VIEW_RECORDS", schema = "SPFCORE")
@XStreamAlias("UserContentViewRecord")
@SequenceDef(name = "MARKETING_CONTENT_USER_VIEW_RECORD_SEQ", schema = "SPFCORE", internetFormat = "FEB-%d", intranetFormat = "BPM-%d")
public class UserContentViewRecord {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"NRIC\"", length = 10)
	private String nric;

	@Column(name = "\"CONTENT_ID\"")
	private String contentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"VIEW_DATE\"")
	private Date viewOn;

	public UserContentViewRecord() {
		super();
	}

	public UserContentViewRecord(String id, String nric, String contentId, Date viewOn) {
		super();
		this.id = id;
		this.nric = nric;
		this.contentId = contentId;
		this.viewOn = viewOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Date getViewOn() {
		return viewOn;
	}

	public void setViewOn(Date viewOn) {
		this.viewOn = viewOn;
	}
}

