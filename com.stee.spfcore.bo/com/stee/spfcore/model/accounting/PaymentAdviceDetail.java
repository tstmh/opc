package com.stee.spfcore.model.accounting;

public class PaymentAdviceDetail {

	int spacingLinesBefore;
	String details;
	
	public PaymentAdviceDetail(){
		super();
	}
	
	public PaymentAdviceDetail(int spacingLinesBefore, String details) {
		super();
		this.spacingLinesBefore = spacingLinesBefore;
		this.details = details;
	}

	public int getSpacingLinesBefore() {
		return spacingLinesBefore;
	}

	public void setSpacingLinesBefore(int spacingLinesBefore) {
		this.spacingLinesBefore = spacingLinesBefore;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
}
