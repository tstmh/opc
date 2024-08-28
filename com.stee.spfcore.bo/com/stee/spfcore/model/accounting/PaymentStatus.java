package com.stee.spfcore.model.accounting;

public enum PaymentStatus {
			
	//status from UOB
	SUCCESSFUL ("0"), 
	REJECTED ("1"),
	PENDING ("2"), //applicable to FAST only
	STOPPED ("3"),
	
	//created status
	REVERTED_TO_APPLICANT ("Reverted to Applicant"),
	RESUBMITED_TO_PO ("Resubmitted to PO"),
	RESEND_TO_UOB ("Resend to UOB"),
	NONE ("None");
		
	private String status;
	
	private PaymentStatus (String status) {
		this.status = status;
	}
	
	public static PaymentStatus get (String status) {
		
		switch (status) {
			case "0":
				return SUCCESSFUL;
			case "1":
				return REJECTED;
			case "2":
				return PENDING;
			case "3":
				return STOPPED;
			case "Reverted to Applicant":
				return REVERTED_TO_APPLICANT;
			case "Resubmitted to PO":
				return RESUBMITED_TO_PO;
			case "Resend to UOB":
				return RESEND_TO_UOB;
			default:
				return NONE;
		}
	}

	@Override
	public String toString () {
		return this.status;
	}
}
