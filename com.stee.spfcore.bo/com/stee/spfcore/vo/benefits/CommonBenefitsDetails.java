package com.stee.spfcore.vo.benefits;

import java.util.Date;

import com.stee.spfcore.model.ApplicationStatus;

/**
 * Common benefits Module VO. Extend for each module specific VO.
 * @author 
 */
public abstract class CommonBenefitsDetails {
	
	private String referenceNumber;
	
	private String memberNric;
	
	private String memberName;
	
	private ApplicationStatus applicationStatus;
	
	private Date dateOfSubmission;
	
	private String submittedBy;
	
	private Double amountToBePaid;
	
	private Date dateOfTaskCompletion;

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public String getMemberName() {
		return memberName;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}
	
	public Date getDateOfTaskCompletion() {
		return dateOfTaskCompletion;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	public void setMemberName( String memberName ) {
		this.memberName = memberName;
	}

	public void setApplicationStatus( ApplicationStatus applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public void setDateOfSubmission( Date dateOfSubmission ) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public void setSubmittedBy( String submittedBy ) {
		this.submittedBy = submittedBy;
	}

	public Double getAmountToBePaid() {
		return amountToBePaid;
	}

	public void setAmountToBePaid( Double amountToBePaid ) {
		this.amountToBePaid = amountToBePaid;
	}

}
