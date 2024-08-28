package com.stee.spfcore.model.hrps.internal;

public class BenefitsDetails {
	
	private String referenceNumber;
	
	private String memberNric;
	
	private double amountToBePaid;
	
	private String submittedBy;
	
	public BenefitsDetails() {
		super();
	}

	public BenefitsDetails(String referenceNumber, String memberNric,
			double amountToBePaid) {
		super();
		this.referenceNumber = referenceNumber;
		this.memberNric = memberNric;
		this.amountToBePaid = amountToBePaid;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric(String memberNric) {
		this.memberNric = memberNric;
	}

	public double getAmountToBePaid() {
		return amountToBePaid;
	}

	public void setAmountToBePaid(double amountToBePaid) {
		this.amountToBePaid = amountToBePaid;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

}
