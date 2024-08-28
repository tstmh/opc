package com.stee.spfcore.model.hrps.internal;

import java.util.Date;

public class BenefitsReport {
	
	private String referenceNumber;
	private String memberName;
	private String memberNric;
	private String submittedBy;
	private String serviceType;
	private String unit;
	private String subunit;
	private String deceasedRelation;
	private String typeOfBenefits;
	private String name;
	private String nric;
	private Date date;
	private double amount;
	private String officerAction;
	
	public BenefitsReport() {
		super();
	}
	
	public BenefitsReport(String referenceNumber, String memberName,
			String memberNric, String submittedBy, String serviceType, String unit, String subunit,
			String deceasedRelation, String typeOfBenefits, String name,
			String nric, Date date, double amount, String officerAction) {
		super();
		this.referenceNumber = referenceNumber;
		this.memberName = memberName;
		this.memberNric = memberNric;
		this.serviceType = serviceType;
		this.unit = unit;
		this.subunit = subunit;
		this.deceasedRelation = deceasedRelation;
		this.typeOfBenefits = typeOfBenefits;
		this.name = name;
		this.nric = nric;
		this.date = date;
		this.amount = amount;
		this.officerAction = officerAction;
		this.submittedBy = submittedBy;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberNric() {
		return memberNric;
	}
	public void setMemberNric(String memberNric) {
		this.memberNric = memberNric;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSubunit() {
		return subunit;
	}
	public void setSubunit(String subunit) {
		this.subunit = subunit;
	}
	public String getDeceasedRelation() {
		return deceasedRelation;
	}
	public void setDeceasedRelation(String deceasedRelation) {
		this.deceasedRelation = deceasedRelation;
	}
	public String getTypeOfBenefits() {
		return typeOfBenefits;
	}
	public void setTypeOfBenefits(String typeOfBenefits) {
		this.typeOfBenefits = typeOfBenefits;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getOfficerAction() {
		return officerAction;
	}
	public void setOfficerAction(String officerAction) {
		this.officerAction = officerAction;
	}
	public String getSubmittedBy() {
		return submittedBy;
	}
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}
}
