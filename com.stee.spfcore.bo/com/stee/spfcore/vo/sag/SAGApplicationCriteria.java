package com.stee.spfcore.vo.sag;

import java.util.Date;

public class SAGApplicationCriteria {

	private String referenceNumber;
	
	private String awardType;
	
	private String applicantId;
	
	private String applicantName;
	
	private String childName;
	
	private String applicationStatus; // Comma Separated ApplicationStatus values
	
	private String approvalRecordStatus;
	
	private String financialYear;
	
	private Date chequeUpdatedDate;
	
	//Comma-separated string [Recommended,Successful] to specifically search for Recommended/NotRecommended and/or Successful/Unsuccessful
	private String recommendOrSuccessfulApplications;

	public SAGApplicationCriteria() {
		super();
	}
	
	public SAGApplicationCriteria( String referenceNumber, String awardType,
			String applicantId, String applicantName, String childName,
			String applicationStatus, String approvalRecordStatus,
			String financialYear, Date chequeUpdatedDate,
			String recommendOrSuccessfulApplications ) {
		super();
		this.referenceNumber = referenceNumber;
		this.awardType = awardType;
		this.applicantId = applicantId;
		this.applicantName = applicantName;
		this.childName = childName;
		this.applicationStatus = applicationStatus;
		this.approvalRecordStatus = approvalRecordStatus;
		this.financialYear = financialYear;
		this.chequeUpdatedDate = chequeUpdatedDate;
		this.recommendOrSuccessfulApplications = recommendOrSuccessfulApplications;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType( String awardType ) {
		this.awardType = awardType;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId( String applicantId ) {
		this.applicantId = applicantId;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName( String applicantName ) {
		this.applicantName = applicantName;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus( String applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public String getApprovalRecordStatus() {
		return approvalRecordStatus;
	}

	public void setApprovalRecordStatus( String approvalRecordStatus ) {
		this.approvalRecordStatus = approvalRecordStatus;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getRecommendOrSuccessfulApplications() {
		return recommendOrSuccessfulApplications;
	}

	public void setRecommendOrSuccessfulApplications(
			String recommendOrSuccessfulApplications ) {
		this.recommendOrSuccessfulApplications = recommendOrSuccessfulApplications;
	}

	public Date getChequeUpdatedDate() {
		return chequeUpdatedDate;
	}

	public void setChequeUpdatedDate( Date chequeUpdatedDate ) {
		this.chequeUpdatedDate = chequeUpdatedDate;
	}

	@Override
	public String toString() {
		return "SAGApplicationCriteria [referenceNumber=" + referenceNumber
				+ ", awardType=" + awardType + ", applicantId=" + applicantId
				+ ", applicantName=" + applicantName + ", childName="
				+ childName + ", applicationStatus=" + applicationStatus
				+ ", approvalRecordStatus=" + approvalRecordStatus
				+ ", financialYear=" + financialYear + ", chequeUpdatedDate="
				+ chequeUpdatedDate + ", recommendOrSuccessfulApplications="
				+ recommendOrSuccessfulApplications + "]";
	}

}
