package com.stee.spfcore.service.hrps.impl;

import static com.stee.spfcore.service.hrps.impl.FieldLengthConstants.*;

public class PostRecord {
	
	private String recordType;
	private String payrollMonth;
	private String nric;
	private String reference;
	private String amount;
	private String status;
	private String reason;
	
	public PostRecord(String outboundPostDetail) {
		extract(outboundPostDetail);
	}
	
	public void extract(String outboundPostDetail) {
		if (outboundPostDetail.length() >= OUTBOUND_POST_DETAIL_RECORD_TYPE_END) {
			recordType = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_RECORD_TYPE_START - 1, OUTBOUND_POST_DETAIL_RECORD_TYPE_END).trim();
		}
		
		if (outboundPostDetail.length() >= OUTBOUND_POST_DETAIL_PAYROLL_MOTNH_END) {
			payrollMonth = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_PAYROLL_MONTH_START - 1, OUTBOUND_POST_DETAIL_PAYROLL_MOTNH_END).trim();
		}
		
		if (outboundPostDetail.length() >= OUTBOUND_POST_DETAIL_ID_END) {
			nric = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_ID_START - 1, OUTBOUND_POST_DETAIL_ID_END).trim();
		}
		
		if (outboundPostDetail.length() >= OUTBOUND_POST_DETAIL_REFERENCE_END) {
			reference = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_REFERENCE_START - 1, OUTBOUND_POST_DETAIL_REFERENCE_END).trim();
		}
		
		if (outboundPostDetail.length() >= OUTBOUND_POST_DETAIL_AMOUNT_END) {
			amount = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_AMOUNT_START - 1, OUTBOUND_POST_DETAIL_AMOUNT_END).trim();
		}
		
		if (outboundPostDetail.length() >= OUTBOUND_POST_DETAIL_STATUS_END) {
			status = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_STATUS_START - 1, OUTBOUND_POST_DETAIL_STATUS_END).trim();
		}

		if (outboundPostDetail.length() > OUTBOUND_POST_DETAIL_STATUS_END) {
			reason = outboundPostDetail.substring(OUTBOUND_POST_DETAIL_REASON_START -1);
		}
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getPayrollMonth() {
		return payrollMonth;
	}

	public void setPayrollMonth(String payrollMonth) {
		this.payrollMonth = payrollMonth;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String toString() {
		return String.format("Record Type=%s, Payroll Month=%s, NRIC:%s, Reference=%s, "
				+ "Amount=%s, Status=%s, Reason=%s", this.recordType, this.payrollMonth,
				this.nric, this.reference, this.amount, this.status, this.reason);
	}

}
