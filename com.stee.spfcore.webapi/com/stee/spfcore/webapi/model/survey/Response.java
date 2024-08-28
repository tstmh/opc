package com.stee.spfcore.webapi.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"SURVEY_RESPONSES\"", schema = "\"SPFCORE\"")
@XStreamAlias("SurveyResponse")
@Audited
@SequenceDef (name="SurveyResponseId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Response implements Serializable {

	private static final long serialVersionUID = 8960092183654728354L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"SURVEY_ID\"")
	private String surveyId;
	
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;
	
	@Formula("(select c.NAME from SPFCORE.PERSONAL_DETAILS c where c.NRIC = NRIC)")
	@NotAudited
	@XStreamOmitField
	private String name;

	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.ORGANISATION_OR_DEPARTMENT and c.CODE_TYPE = 'UNIT_DEPARTMENT' and e.NRIC = NRIC)")
	@NotAudited
	@XStreamOmitField
	private String department;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"SUBMITTED_ON\"")
	private Date submittedOn;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"RESPONSE_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<Answer> answers;

	public Response() {
		super();
		
		answers = new ArrayList<Answer>();
	}

	public Response(String id, String surveyId, String nric, Date submittedOn,
			List<Answer> answers) {
		super();
		this.id = id;
		this.surveyId = surveyId;
		this.nric = nric;
		this.submittedOn = submittedOn;
		this.answers = answers;
		
		if (this.answers == null) {
			this.answers = new ArrayList<Answer>();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public String getName() {
		return name;
	}

	public String getDepartment() {
		return department;
	}
	
	@Override
	public String toString() {
		return "Response [id=" + id + ", surveyId=" + surveyId + ", nric="
				+ nric + ", name=" + name + ", department=" + department
				+ ", submittedOn=" + submittedOn + ", answers=" + answers + "]";
	}
}
