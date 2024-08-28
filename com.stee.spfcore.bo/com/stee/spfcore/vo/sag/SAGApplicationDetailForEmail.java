package com.stee.spfcore.vo.sag;

import com.stee.spfcore.model.ApplicationStatus;

public class SAGApplicationDetailForEmail {

	private String referenceNumber;
	
	private String childName;
	
	private String awardTypeDescription;
	
	private String awardType;

	private ApplicationStatus applicationStatus;
	
	private String sequenceNumber;
	
	private String applicantId;

	public SAGApplicationDetailForEmail() {
		super();
	}

	public SAGApplicationDetailForEmail( String referenceNumber,
			String childName, String awardTypeDescription, String awardType,
			ApplicationStatus applicationStatus, String sequenceNumber,
			String applicantId) {
		super();
		this.referenceNumber = referenceNumber;
		this.childName = childName;
		this.awardTypeDescription = awardTypeDescription;
		this.awardType = awardType;
		this.applicationStatus = applicationStatus;
		this.sequenceNumber = sequenceNumber;
		this.applicantId = applicantId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public String getAwardTypeDescription() {
		return awardTypeDescription;
	}

	public void setAwardTypeDescription( String awardTypeDescription ) {
		this.awardTypeDescription = awardTypeDescription;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType( String awardType ) {
		this.awardType = awardType;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus( ApplicationStatus applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber( String sequenceNumber ) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	@Override
	public String toString() {
		return "SAGApplicationDetailForEmail [referenceNumber="
				+ referenceNumber + ", childName=" + childName
				+ ", awardTypeDescription=" + awardTypeDescription
				+ ", awardType=" + awardType + ", applicationStatus="
				+ applicationStatus + ", sequenceNumber=" + sequenceNumber
				+ ", applicantId=" + applicantId
				+ "]";
	}
}
