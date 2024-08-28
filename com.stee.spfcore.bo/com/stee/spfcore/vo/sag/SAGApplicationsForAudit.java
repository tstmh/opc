package com.stee.spfcore.vo.sag;

import java.util.Date;

public class SAGApplicationsForAudit {

	private String referenceNumber;
	
	private String awardType;
	
	private String awardTypeDescription;
	
	private String memberName;
	
	private String memberNric;
	
	private String childName;
	
	private String childNric;
	
	private String departmentId;
	
	private String departmentDescription;
	
	private Date dateOfSubmission;
	
	private Double perCapitaIncome;

	public SAGApplicationsForAudit() {
		super();
	}

	public SAGApplicationsForAudit( String referenceNumber, String awardType,
			String awardTypeDescription, String memberName, String memberNric,
			String childName, String childNric, String departmentId,
			String departmentDescription, Date dateOfSubmission,
			Double perCapitaIncome ) {
		super();
		this.referenceNumber = referenceNumber;
		this.awardType = awardType;
		this.awardTypeDescription = awardTypeDescription;
		this.memberName = memberName;
		this.memberNric = memberNric;
		this.childName = childName;
		this.childNric = childNric;
		this.departmentId = departmentId;
		this.departmentDescription = departmentDescription;
		this.dateOfSubmission = dateOfSubmission;
		this.perCapitaIncome = perCapitaIncome;
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

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId( String departmentId ) {
		this.departmentId = departmentId;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription( String departmentDescription ) {
		this.departmentDescription = departmentDescription;
	}

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission( Date dateOfSubmission ) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public Double getPerCapitaIncome() {
		return perCapitaIncome;
	}

	public void setPerCapitaIncome( Double perCapitaIncome ) {
		this.perCapitaIncome = perCapitaIncome;
	}

	@Override
	public String toString() {
		return "SAGApplicationsForAudit [referenceNumber=" + referenceNumber
				+ ", awardType=" + awardType + ", awardTypeDescription="
				+ awardTypeDescription + ", memberName=" + memberName
				+ ", memberNric=" + memberNric + ", childName=" + childName
				+ ", childNric=" + childNric + ", departmentId=" + departmentId
				+ ", departmentDescription=" + departmentDescription
				+ ", dateOfSubmission=" + dateOfSubmission
				+ ", perCapitaIncome=" + perCapitaIncome + "]";
	}
	
	
}
