package com.stee.spfcore.vo.sag;

import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.benefits.ApprovalRecord;

public class SAGStatusApprovalRecordDetail {

	private String referenceNumber;
	
	private ApplicationStatus applicationStatus;
	
	private ApprovalRecord approvalRecord;
	
	private String sequenceNumber; // Set only when application Status is Successful
	
	private String remarks;

	public SAGStatusApprovalRecordDetail() {
		super();
	}

	public SAGStatusApprovalRecordDetail( String referenceNumber,
			ApplicationStatus applicationStatus, ApprovalRecord approvalRecord,
			String sequenceNumber, String remarks ) {
		super();
		this.referenceNumber = referenceNumber;
		this.applicationStatus = applicationStatus;
		this.approvalRecord = approvalRecord;
		this.sequenceNumber = sequenceNumber;
		this.remarks = remarks;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public ApplicationStatus getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus( ApplicationStatus applicationStatus ) {
		this.applicationStatus = applicationStatus;
	}

	public ApprovalRecord getApprovalRecord() {
		return approvalRecord;
	}

	public void setApprovalRecord( ApprovalRecord approvalRecord ) {
		this.approvalRecord = approvalRecord;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber( String sequenceNumber ) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks( String remarks ) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "SAGStatusApprovalRecordDetail [referenceNumber="
				+ referenceNumber + ", applicationStatus=" + applicationStatus
				+ ", approvalRecord=" + approvalRecord + ", sequenceNumber="
				+ sequenceNumber + ", remarks=" + remarks + "]";
	}
}
