package com.stee.spfcore.service.hrps.impl;

import static com.stee.spfcore.service.hrps.impl.FieldLengthConstants.*;

public class Record {
	
	private String recordType;
	private String nric;
	private String startDate;
	private String endDate;
	private String wageCode;
	private String amount;
	private String payrollMonth;
	private String reference;
	private String status;
	private String reason;
	
	public Record (String outboundDetail) {
		extract(outboundDetail);
	}
	
	public void extract (String outboundDetail) {
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_RECORD_TYPE_END) {
			recordType = outboundDetail.substring(OUTBOUND_DETAIL_RECORD_TYPE_START - 1, OUTBOUND_DETAIL_RECORD_TYPE_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_ID_END) {
			nric = outboundDetail.substring(OUTBOUND_DETAIL_ID_START - 1, OUTBOUND_DETAIL_ID_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_STARTDATE_END) {
			startDate = outboundDetail.substring(OUTBOUND_DETAIL_STARTDATE_START - 1,  OUTBOUND_DETAIL_STARTDATE_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_ENDDATE_END) {
			endDate = outboundDetail.substring(OUTBOUND_DETAIL_ENDDATE_START - 1, OUTBOUND_DETAIL_ENDDATE_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_WAGE_TYPE_END) {
			wageCode = outboundDetail.substring(OUTBOUND_DETAIL_WAGE_TYPE_START - 1, OUTBOUND_DETAIL_WAGE_TYPE_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_AMOUNT_END) {
			amount = outboundDetail.substring(OUTBOUND_DETAIL_AMOUNT_START - 1, OUTBOUND_DETAIL_AMOUNT_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_PAYROLL_MONTH_END) {
			payrollMonth = outboundDetail.substring(OUTBOUND_DETAIL_PAYROLL_MONTH_START - 1, OUTBOUND_DETAIL_PAYROLL_MONTH_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_REFERENCE_END) {
			reference = outboundDetail.substring(OUTBOUND_DETAIL_REFERENCE_START - 1, OUTBOUND_DETAIL_REFERENCE_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_STATUS_END) {
			status = outboundDetail.substring(OUTBOUND_DETAIL_STATUS_START - 1, OUTBOUND_DETAIL_STATUS_END).trim();
		}
		
		if (outboundDetail.length() >= OUTBOUND_DETAIL_REASON_END) {
			reason = outboundDetail.substring(OUTBOUND_DETAIL_REASON_START - 1).trim();
		}
		
	}
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getWageCode() {
		return wageCode;
	}
	public void setWageCode(String wageCode) {
		this.wageCode = wageCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPayrollMonth() {
		return payrollMonth;
	}
	public void setPayrollMonth(String payrollMonth) {
		this.payrollMonth = payrollMonth;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
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
		return String.format("Record Type=%s, NRIC=%s, Start Date:%s, End Date=%s, "
				+ "Wage Code=%s, Amount=%s, Payroll Month=%s, Reference=%s, " 
				+ "Status=%s, Reason=%s", this.recordType, this.nric, this.startDate
				, this.endDate, this.wageCode, this.amount, this.payrollMonth, this.reference
				, this.status, this.reason);
	}
	
}
