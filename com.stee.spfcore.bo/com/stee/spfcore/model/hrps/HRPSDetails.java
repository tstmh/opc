package com.stee.spfcore.model.hrps;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "HRPS_DETAILS", schema = "SPFCORE")
@XStreamAlias("HRPDetails")
@Audited
public class HRPSDetails {
	
	@Id
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"RECORD_TYPE\"", length = 10)
	private String recordType;
	
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"START_DATE\"")
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"END_DATE\"")
	private Date endDate;
	
	@Column(name = "\"WAGE_CODE\"", length = 10)
	private String wageCode;
	
	@Column(name = "\"AMOUNT\"")
	private Double amount;
	
	@Column(name = "\"PAYROLL_MONTH\"", length = 10)
	private String payrollMonth;
	
	@Column(name = "\"REFERENCE\"", length = 255)
	private String reference;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"STATUS\"", length = 15)
	private RecordStatus status;
	
	@Column(name = "\"REASON\"", length = 255)
	private String reason;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedDate;
	
	@Column(name = "\"REMARKS\"", length = 255)
	private String remarks;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"POST_STATUS\"", length = 15)
	private RecordStatus postStatus;
	
	@Column(name = "\"POST_REASON\"", length = 255)
	private String postReason;
	
	public HRPSDetails () {
		super();
	}
	
	public HRPSDetails(String id, String recordType, String nric,
			Date startDate, Date endDate, String wageCode, Double amount,
			String payrollMonth, String reference, RecordStatus status,
			String reason, Date updatedDate, String remarks, 
			RecordStatus postStatus, String postReason) {
		super();
		this.id = id;
		this.recordType = recordType;
		this.nric = nric;
		this.startDate = startDate;
		this.endDate = endDate;
		this.wageCode = wageCode;
		this.amount = amount;
		this.payrollMonth = payrollMonth;
		this.reference = reference;
		this.status = status;
		this.reason = reason;
		this.updatedDate = updatedDate;
		this.remarks = remarks;
		this.postStatus = postStatus;
		this.postReason = postReason;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getWageCode() {
		return wageCode;
	}
	public void setWageCode(String wageCode) {
		this.wageCode = wageCode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
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
	public RecordStatus getStatus() {
		return status;
	}
	public void setStatus(RecordStatus status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public RecordStatus getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(RecordStatus postStatus) {
		this.postStatus = postStatus;
	}

	public String getPostReason() {
		return postReason;
	}

	public void setPostReason(String postReason) {
		this.postReason = postReason;
	}


}
