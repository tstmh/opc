package com.stee.spfcore.vo.sag;

import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.personnel.ContactMode;

public class SAGApplicationResult {

	private String referenceNumber;
	
	private String awardType;
	
	private String applicantName;
	
	private String applicantId;
	
	private String childName;
	
	private ApplicationStatus applicationStatus;
	
	private String submittedBy;
	
	private String submittedByName;
	
	private String officerAction;
	
	private String childNewEduLevel;
	
	private String childHighestEduLevel;
	
	private Integer listOrder;
	
	private String submittedByPrefEmail;

	private String submittedByPaymentAdviceEmail;
	
	private ContactMode submittedByPrefContactMode;
	
	private String submittedByWorkEmail;
	
	private String submittedByPrefContactNumber;
	
	private String sequenceNumber;
	
	private String childPsleScore;
	
	private Double childCgpa;
	
	// added for searching for task
	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public SAGApplicationResult() {
		super();
	}

	public SAGApplicationResult( String referenceNumber, String awardType,
			String applicantName, String applicantId, String childName,
			ApplicationStatus applicationStatus, String submittedBy,
			String submittedByName, String officerAction,
			String childNewEduLevel, String childHighestEduLevel,
			Integer listOrder, String submittedByPrefEmail, String submittedByPaymentAdviceEmail,
			ContactMode submittedByPrefContactMode,
			String submittedByWorkEmail, String submittedByPrefContactNumber,
			String sequenceNumber, String childPsleScore, Double childCgpa, String taskId ) {
		super();
		this.referenceNumber = referenceNumber;
		this.awardType = awardType;
		this.applicantName = applicantName;
		this.applicantId = applicantId;
		this.childName = childName;
		this.applicationStatus = applicationStatus;
		this.submittedBy = submittedBy;
		this.submittedByName = submittedByName;
		this.officerAction = officerAction;
		this.childNewEduLevel = childNewEduLevel;
		this.childHighestEduLevel = childHighestEduLevel;
		this.listOrder = listOrder;
		this.submittedByPrefEmail = submittedByPrefEmail;
		this.submittedByPaymentAdviceEmail = submittedByPaymentAdviceEmail;
		this.submittedByPrefContactMode = submittedByPrefContactMode;
		this.submittedByWorkEmail = submittedByWorkEmail;
		this.submittedByPrefContactNumber = submittedByPrefContactNumber;
		this.sequenceNumber = sequenceNumber;
		this.childPsleScore = childPsleScore;
		this.childCgpa = childCgpa;
		this.taskId = taskId;
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

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName( String applicantName ) {
		this.applicantName = applicantName;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId( String applicantId ) {
		this.applicantId = applicantId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus( ApplicationStatus applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy( String submittedBy ) {
		this.submittedBy = submittedBy;
	}

	public String getSubmittedByName() {
		return submittedByName;
	}

	public void setSubmittedByName( String submittedByName ) {
		this.submittedByName = submittedByName;
	}

	public String getOfficerAction() {
		return officerAction;
	}

	public void setOfficerAction( String officerAction ) {
		this.officerAction = officerAction;
	}

	public String getChildNewEduLevel() {
		return childNewEduLevel;
	}

	public void setChildNewEduLevel( String childNewEduLevel ) {
		this.childNewEduLevel = childNewEduLevel;
	}

	public String getChildHighestEduLevel() {
		return childHighestEduLevel;
	}

	public void setChildHighestEduLevel( String childHighestEduLevel ) {
		this.childHighestEduLevel = childHighestEduLevel;
	}

	public Integer getListOrder() {
		return listOrder;
	}

	public void setListOrder( Integer listOrder ) {
		this.listOrder = listOrder;
	}

	public String getSubmittedByPrefEmail() {
		return submittedByPrefEmail;
	}

	public void setSubmittedByPrefEmail( String submittedByPrefEmail ) {
		this.submittedByPrefEmail = submittedByPrefEmail;
	}
	
	public String getSubmittedByPaymentAdviceEmail() {
		return submittedByPaymentAdviceEmail;
	}

	public void setSubmittedByPaymentAdviceEmail( String submittedByPaymentAdviceEmail ) {
		this.submittedByPaymentAdviceEmail = submittedByPaymentAdviceEmail;
	}

	public ContactMode getSubmittedByPrefContactMode() {
		return submittedByPrefContactMode;
	}

	public void setSubmittedByPrefContactMode(
			ContactMode submittedByPrefContactMode ) {
		this.submittedByPrefContactMode = submittedByPrefContactMode;
	}

	public String getSubmittedByWorkEmail() {
		return submittedByWorkEmail;
	}

	public void setSubmittedByWorkEmail( String submittedByWorkEmail ) {
		this.submittedByWorkEmail = submittedByWorkEmail;
	}

	public String getSubmittedByPrefContactNumber() {
		return submittedByPrefContactNumber;
	}

	public void setSubmittedByPrefContactNumber( String submittedByPrefContactNumber ) {
		this.submittedByPrefContactNumber = submittedByPrefContactNumber;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber( String sequenceNumber ) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getChildPsleScore() {
		return childPsleScore;
	}

	public void setChildPsleScore( String childPsleScore ) {
		this.childPsleScore = childPsleScore;
	}

	public Double getChildCgpa() {
		return childCgpa;
	}

	public void setChildCgpa( Double childCgpa ) {
		this.childCgpa = childCgpa;
	}

	@Override
	public String toString() {
		return "SAGApplicationResult [referenceNumber=" + referenceNumber
				+ ", awardType=" + awardType + ", applicantName="
				+ applicantName + ", applicantId=" + applicantId
				+ ", childName=" + childName + ", applicationStatus="
				+ applicationStatus + ", submittedBy=" + submittedBy
				+ ", submittedByName=" + submittedByName + ", officerAction="
				+ officerAction + ", childNewEduLevel=" + childNewEduLevel
				+ ", childHighestEduLevel=" + childHighestEduLevel
				+ ", listOrder=" + listOrder + ", submittedByPrefEmail="
				+ submittedByPrefEmail + ", submittedByPaymentAdviceEmail=" + submittedByPaymentAdviceEmail + ", submittedByPrefContactMode="
				+ submittedByPrefContactMode + ", submittedByWorkEmail="
				+ submittedByWorkEmail + ", submittedByPrefContactNumber="
				+ submittedByPrefContactNumber + ", sequenceNumber="
				+ sequenceNumber + ", childPsleScore=" + childPsleScore
				+ ", childCgpa=" + childCgpa + ", taskId=" + taskId + "]";
	}

}
