package com.stee.spfcore.webapi.model.benefits;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"APPLICATION_APPROVAL_RECORD\"", schema = "\"SPFCORE\"")
@XStreamAlias("ApprovalRecord")
@Audited
public class ApprovalRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	@XStreamOmitField
	private long id;
	
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;
	
	@Column(name = "\"OFFICER_LEVEL\"", length = 50)
	private String officerLevel;
	
	@Column(name = "\"OFFICER_NRIC\"", length = 50)
	private String officerNric;
	
	@Column(name = "\"OFFICER_NAME\"", length = 100)
	private String officerName;
	
	@Column(name = "\"OFFICER_ACTION\"", length = 50)
	private String offcierAction;
	
	@Column(name = "\"OFFICER_COMMENT\"", length = 1000)
	private String officerComment;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"DATE_OF_COMPLETION\"")
	private Date dateOfCompletion;

	public ApprovalRecord() {
		super();
	}

	public ApprovalRecord(String referenceNumber, String officerLevel,
			String officerNric, String officerName, String officerAction,
			String officerComments, Date dateOfCompletion) {
		super();
		this.referenceNumber = referenceNumber;
		this.officerLevel = officerLevel;
		this.officerNric = officerNric;
		this.officerName = officerName;
		this.offcierAction = officerAction;
		this.officerComment = officerComments;
		this.dateOfCompletion = dateOfCompletion;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getOfficerLevel() {
		return officerLevel;
	}

	public void setOfficerLevel(String officerLevel) {
		this.officerLevel = officerLevel;
	}

	public String getOfficerNric() {
		return officerNric;
	}

	public void setOfficerNric(String officerNric) {
		this.officerNric = officerNric;
	}

	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	public String getOfficerAction() {
		return offcierAction;
	}

	public void setOfficerAction(String officerAction) {
		this.offcierAction = officerAction;
	}

	public String getOfficerComments() {
		return officerComment;
	}

	public void setOfficerComments(String officerComments) {
		this.officerComment = officerComments;
	}

	public Date getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}
	
	public void preSave () {
		
		if (officerName != null) {
			officerName = officerName.toUpperCase();
		}
		
		if (officerNric != null) {
			officerNric = officerNric.toUpperCase();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApprovalRecord other = (ApprovalRecord) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
