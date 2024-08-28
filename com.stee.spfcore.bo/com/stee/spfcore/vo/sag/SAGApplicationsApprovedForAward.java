package com.stee.spfcore.vo.sag;

import java.util.Date;

public class SAGApplicationsApprovedForAward {
	
	private String referenceNumber;
	
	private String financialYear;
	
	private String awardType;
	
	private String awardTypeDescription;
	
	private String memberNric;
	
	private String memberName;
	
	private String childNric;
	
	private String childName;
	
	private String childCurrentSchoolId;
	
	private String childCurrentSchoolDesc;
	
	private String childHighestEduLevelId;
	
	private String childHighestEduLevelDesc;
	
	private String childNewEduLevelId;
	
	private String childNewEduLevelDesc;
	
	private Double awardAmount;
	
	private Date chequeValueDate;
	
	private Date chequeUpdatedDate;
	
	private String sequenceNumber;

	public SAGApplicationsApprovedForAward() {
		super();
	}

	public SAGApplicationsApprovedForAward( String referenceNumber,
			String financialYear, String awardType,
			String awardTypeDescription, String memberNric, String memberName,
			String childNric, String childName, String childCurrentSchoolId,
			String childCurrentSchoolDesc, String childHighestEduLevelId,
			String childHighestEduLevelDesc, String childNewEduLevelId,
			String childNewEduLevelDesc, Double awardAmount,
			Date chequeValueDate, Date chequeUpdatedDate, String sequenceNumber ) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.awardType = awardType;
		this.awardTypeDescription = awardTypeDescription;
		this.memberNric = memberNric;
		this.memberName = memberName;
		this.childNric = childNric;
		this.childName = childName;
		this.childCurrentSchoolId = childCurrentSchoolId;
		this.childCurrentSchoolDesc = childCurrentSchoolDesc;
		this.childHighestEduLevelId = childHighestEduLevelId;
		this.childHighestEduLevelDesc = childHighestEduLevelDesc;
		this.childNewEduLevelId = childNewEduLevelId;
		this.childNewEduLevelDesc = childNewEduLevelDesc;
		this.awardAmount = awardAmount;
		this.chequeValueDate = chequeValueDate;
		this.chequeUpdatedDate = chequeUpdatedDate;
		this.sequenceNumber = sequenceNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
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

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName( String memberName ) {
		this.memberName = memberName;
	}

	public String getChildNric() {
		return childNric;
	}

	public void setChildNric( String childNric ) {
		this.childNric = childNric;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public String getChildCurrentSchoolId() {
		return childCurrentSchoolId;
	}

	public void setChildCurrentSchoolId( String childCurrentSchoolId ) {
		this.childCurrentSchoolId = childCurrentSchoolId;
	}

	public String getChildCurrentSchoolDesc() {
		return childCurrentSchoolDesc;
	}

	public void setChildCurrentSchoolDesc( String childCurrentSchoolDesc ) {
		this.childCurrentSchoolDesc = childCurrentSchoolDesc;
	}

	public String getChildHighestEduLevelId() {
		return childHighestEduLevelId;
	}

	public void setChildHighestEduLevelId( String childHighestEduLevelId ) {
		this.childHighestEduLevelId = childHighestEduLevelId;
	}

	public String getChildHighestEduLevelDesc() {
		return childHighestEduLevelDesc;
	}

	public void setChildHighestEduLevelDesc( String childHighestEduLevelDesc ) {
		this.childHighestEduLevelDesc = childHighestEduLevelDesc;
	}

	public String getChildNewEduLevelId() {
		return childNewEduLevelId;
	}

	public void setChildNewEduLevelId( String childNewEduLevelId ) {
		this.childNewEduLevelId = childNewEduLevelId;
	}

	public String getChildNewEduLevelDesc() {
		return childNewEduLevelDesc;
	}

	public void setChildNewEduLevelDesc( String childNewEduLevelDesc ) {
		this.childNewEduLevelDesc = childNewEduLevelDesc;
	}

	public Double getAwardAmount() {
		return awardAmount;
	}

	public void setAwardAmount( Double awardAmount ) {
		this.awardAmount = awardAmount;
	}

	public Date getChequeValueDate() {
		return chequeValueDate;
	}

	public void setChequeValueDate( Date chequeValueDate ) {
		this.chequeValueDate = chequeValueDate;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber( String sequenceNumber ) {
		this.sequenceNumber = sequenceNumber;
	}

	public Date getChequeUpdatedDate() {
		return chequeUpdatedDate;
	}

	public void setChequeUpdatedDate( Date chequeUpdatedDate ) {
		this.chequeUpdatedDate = chequeUpdatedDate;
	}

	@Override
	public String toString() {
		return "SAGApplicationsApprovedForAward [referenceNumber="
				+ referenceNumber + ", financialYear=" + financialYear
				+ ", awardType=" + awardType + ", awardTypeDescription="
				+ awardTypeDescription + ", memberNric=" + memberNric
				+ ", memberName=" + memberName + ", childNric=" + childNric
				+ ", childName=" + childName + ", childCurrentSchoolId="
				+ childCurrentSchoolId + ", childCurrentSchoolDesc="
				+ childCurrentSchoolDesc + ", childHighestEduLevelId="
				+ childHighestEduLevelId + ", childHighestEduLevelDesc="
				+ childHighestEduLevelDesc + ", childNewEduLevelId="
				+ childNewEduLevelId + ", childNewEduLevelDesc="
				+ childNewEduLevelDesc + ", awardAmount=" + awardAmount
				+ ", chequeValueDate=" + chequeValueDate
				+ ", chequeUpdatedDate=" + chequeUpdatedDate
				+ ", sequenceNumber=" + sequenceNumber + "]";
	}
	
}
