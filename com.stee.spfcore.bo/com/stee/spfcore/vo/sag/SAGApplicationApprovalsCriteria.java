package com.stee.spfcore.vo.sag;

import java.util.Date;

public class SAGApplicationApprovalsCriteria {
	
	private String referenceNumber;
	
	private String officerAction;
	
	private String officerLevel;
	
	private Date searchStartDate;
	
	private Date searchEndDate;

	public SAGApplicationApprovalsCriteria() {
		super();
	}

	public SAGApplicationApprovalsCriteria( String referenceNumber,
			String officerAction, String officerLevel, Date searchStartDate,
			Date searchEndDate ) {
		super();
		this.referenceNumber = referenceNumber;
		this.officerAction = officerAction;
		this.officerLevel = officerLevel;
		this.searchStartDate = searchStartDate;
		this.searchEndDate = searchEndDate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getOfficerAction() {
		return officerAction;
	}

	public void setOfficerAction( String officerAction ) {
		this.officerAction = officerAction;
	}

	public String getOfficerLevel() {
		return officerLevel;
	}

	public void setOfficerLevel( String officerLevel ) {
		this.officerLevel = officerLevel;
	}

	public Date getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate( Date searchStartDate ) {
		this.searchStartDate = searchStartDate;
	}

	public Date getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate( Date searchEndDate ) {
		this.searchEndDate = searchEndDate;
	}

	@Override
	public String toString() {
		return "SAGApprovalRecordsCriteria [referenceNumber=" + referenceNumber
				+ ", officerAction=" + officerAction + ", officerLevel="
				+ officerLevel + ", searchStartDate=" + searchStartDate
				+ ", searchEndDate=" + searchEndDate + "]";
	}
}
