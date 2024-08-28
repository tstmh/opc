package com.stee.spfcore.vo.sag;

public class SAGApplicationChildDetail {

	private String childName;
	
	private String childNric;
	
	private String childEmail;
	
	private String referenceNumber;
	
	private String financialYear;
	
	private String memberNric;

	public SAGApplicationChildDetail() {
		super();
	}

	public SAGApplicationChildDetail( String childName, String childNric,
			String childEmail, String referenceNumber, String financialYear,
			String memberNric ) {
		super();
		this.childName = childName;
		this.childNric = childNric;
		this.childEmail = childEmail;
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
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

	public String getChildEmail() {
		return childEmail;
	}

	public void setChildEmail( String childEmail ) {
		this.childEmail = childEmail;
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

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric( String memberNric ) {
		this.memberNric = memberNric;
	}

	@Override
	public String toString() {
		return "SAGApplicationChildDetail [childName=" + childName
				+ ", childNric=" + childNric + ", childEmail=" + childEmail
				+ ", referenceNumber=" + referenceNumber + ", financialYear="
				+ financialYear + ", memberNric=" + memberNric + "]";
	}
	
}
