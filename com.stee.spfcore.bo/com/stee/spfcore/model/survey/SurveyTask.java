package com.stee.spfcore.model.survey;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"SURVEY_TASKS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyTask")
@Audited
@SequenceDef (name="SurveytaskId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SurveyTask {

	@Id
	@Column(name = "\"ID\"")
	@GeneratedId
	private String id;
	
	@Formula("(select c.NAME from SPFCORE.SURVEYS c where c.ID = SURVEY_ID)")
	@NotAudited
	@XStreamOmitField
	private String name;
	
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;
	
	@Column(name = "\"SURVEY_ID\"")
	private String surveyId;
	
	@Column(name = "\"COMPLETED\"")
	private boolean completed;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"CREATED_DATE_TIME\"")
	private Date createdOn;
	
	public SurveyTask() {
		super();
	}

	public SurveyTask(String id, String nric, String surveyId, boolean completed, Date createdOn) {
		super();
		this.id = id;
		this.nric = nric;
		this.surveyId = surveyId;
		this.completed = completed;
		this.createdOn = createdOn;
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

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getName() {
		return name;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}
