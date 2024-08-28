package com.stee.spfcore.vo.sag;

import java.util.Date;

import com.stee.spfcore.model.ApplicationStatus;

public class SAGApplicationDetailByApprovals {
	
	private String referenceNumber;
	
	private String awardTypeDescription;
	
	private String memberName;
	
	private String memberNric;
	
	private ApplicationStatus applicationStatus;
	
	private String childName;
	
	private String childNric;
	
	private Date dateOfSubmission;
	
	private String submittedBy;

	private String educationLevel;
	
	private String officerName;
	
	
	public SAGApplicationDetailByApprovals() {
		super();
	}

	public SAGApplicationDetailByApprovals( String referenceNumber,
			String awardTypeDescription, String memberName, String memberNric,
			ApplicationStatus applicationStatus, String childName,
			String childNric, Date dateOfSubmission, String submittedBy, 
			String educationLevel, String officerName) {
		super();
		this.referenceNumber = referenceNumber;
		this.awardTypeDescription = awardTypeDescription;
		this.memberName = memberName;
		this.memberNric = memberNric;
		this.applicationStatus = applicationStatus;
		this.childName = childName;
		this.childNric = childNric;
		this.dateOfSubmission = dateOfSubmission;
		this.submittedBy = submittedBy;
		this.educationLevel = educationLevel;
		this.officerName = officerName;
	}


	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getAwardTypeDescription() {
		return awardTypeDescription;
	}

	public void setAwardTypeDescription( String awardTypeDescription ) {
		this.awardTypeDescription = awardTypeDescription;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName( String memberName ) {
		this.memberName = memberName;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus( ApplicationStatus applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public String getChildNric() {
		return childNric;
	}

	public void setChildNric( String childNric ) {
		this.childNric = childNric;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission( Date dateOfSubmission ) {
		this.dateOfSubmission = dateOfSubmission;
	}
	
	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy( String submittedBy ) {
		this.submittedBy = submittedBy;
	}
	

	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel( String educationLevel ) {
		this.educationLevel = educationLevel;
	}
	
	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName( String officerName ) {
		this.officerName = officerName;
	}
	
	
	
	@Override
	public String toString() {
		return "SAGApplicationDetailByApprovals [referenceNumber="
				+ referenceNumber + ", awardTypeDescription="
				+ awardTypeDescription + ", memberName=" + memberName
				+ ", memberNric=" + memberNric + ", applicationStatus="
				+ applicationStatus + ", childName=" + childName
				+ ", childNric=" + childNric + ", dateOfSubmission="
				+ dateOfSubmission + ", submittedBy=" + submittedBy +
				", educationLevel="
				+ educationLevel + ", officerName=" + officerName
				+"]";
	}
}
