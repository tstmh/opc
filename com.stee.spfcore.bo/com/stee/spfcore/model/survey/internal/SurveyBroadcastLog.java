package com.stee.spfcore.model.survey.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SURVEY_BROADCAST_LOG", schema = "SPFCORE")
public class SurveyBroadcastLog {

	@Id
	@Column(name = "\"BROADCAST_ID\"")
	private String broadcastId;
	
	@Column(name = "\"SURVEY_ID\"")
	private String surveyId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"BROADCAST_DATE\"")
	private Date broadcastDate;

	public SurveyBroadcastLog(String broadcastId, String surveyId, Date broadcastDate) {
		super();
		this.broadcastId = broadcastId;
		this.surveyId = surveyId;
		this.broadcastDate = broadcastDate;
	}

	public SurveyBroadcastLog() {
		super();
	}

	public String getBroadcastId() {
		return broadcastId;
	}

	public void setBroadcastId(String broadcastId) {
		this.broadcastId = broadcastId;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public Date getBroadcastDate() {
		return broadcastDate;
	}

	public void setBroadcastDate(Date broadcastDate) {
		this.broadcastDate = broadcastDate;
	}
}
