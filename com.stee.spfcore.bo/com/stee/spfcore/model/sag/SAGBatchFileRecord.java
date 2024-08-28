package com.stee.spfcore.model.sag;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.stee.spfcore.service.corporateCard.impl.CorporateCardService;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.accounting.PaymentStatus;
import com.stee.spfcore.model.accounting.ReturnCode;
import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_BATCH_FILE_RECORD\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGBatchFileRecord")
@Audited
public class SAGBatchFileRecord {
	private static final Logger logger = Logger.getLogger( SAGBatchFileRecord.class.getName() );
	@Id
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;

	@Column(name="\"FINANCIAL_YEAR\"", length=10)
	private String financialYear;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedDate;

	@Enumerated(EnumType.STRING)
	@Column(name="\"PAYMENT_STATUS\"", length=50) //'Clear Fate' Field
	private PaymentStatus paymentStatus;

	@Column(name="\"RETURN_REASON\"", length=100)
	private String returnReason;

	@Column(name="\"REASON_OF_NOT_SENT\"", length=50)
	private String reasonOfNotSent;

	@Column(name="\"COMMENTS\"", length=2000)
	private String comments;

	@Formula("(select sag.BANK_RECIPIENT_NAME from SPFCORE.SAG_APPLICATION sag where sag.REFERENCE_NUMBER = REFERENCE_NUMBER)")
	@NotAudited
	private String bankRecipientName;

	@Formula("(select pd.NAME from SPFCORE.PERSONAL_DETAILS pd where pd.nric in (select sag.MEMBER_NRIC from SPFCORE.SAG_APPLICATION sag where sag.REFERENCE_NUMBER = REFERENCE_NUMBER))")
	@NotAudited
	private String officerName;

	public SAGBatchFileRecord() {
		super();
	}

	public SAGBatchFileRecord(String referenceNumber,
							  String financialYear, Date updatedDate,
							  PaymentStatus paymentStatus, String returnReason,
							  String reasonOfNotSent, String comments) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.updatedDate = updatedDate;
		this.paymentStatus = paymentStatus;
		this.returnReason = returnReason;
		this.reasonOfNotSent = reasonOfNotSent;
		this.comments = comments;
	}

	public SAGBatchFileRecord(String referenceNumber,
							  String financialYear, Date updatedDate,
							  PaymentStatus paymentStatus, String returnReason,
							  String reasonOfNotSent) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.updatedDate = updatedDate;
		this.paymentStatus = paymentStatus;
		this.returnReason = returnReason;
		this.reasonOfNotSent = reasonOfNotSent;
	}

	public SAGBatchFileRecord(String referenceNumber, String financialYear,
							  Date updatedDate, PaymentStatus paymentStatus, String returnReason,
							  String reasonOfNotSent, String comments, String bankRecipientName,
							  String officerName) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.updatedDate = updatedDate;
		this.paymentStatus = paymentStatus;
		this.returnReason = returnReason;
		this.reasonOfNotSent = reasonOfNotSent;
		this.comments = comments;
		this.bankRecipientName = bankRecipientName;
		this.officerName = officerName;
	}

	public SAGBatchFileRecord(String referenceNumber, String financialYear,
							  Date updatedDate, PaymentStatus paymentStatus, String returnReason,
							  String reasonOfNotSent, String comments, String bankRecipientName) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.updatedDate = updatedDate;
		this.paymentStatus = paymentStatus;
		this.returnReason = returnReason;
		this.reasonOfNotSent = reasonOfNotSent;
		this.comments = comments;
		this.bankRecipientName = bankRecipientName;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getReasonOfNotSent() {
		return reasonOfNotSent;
	}

	public void setReasonOfNotSent(String reasonOfNotSent) {
		this.reasonOfNotSent = reasonOfNotSent;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBankRecipientName() {
		return bankRecipientName;
	}

	public void setBankRecipientName(String bankRecipientName) {
		this.bankRecipientName = bankRecipientName;
	}

	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}


	public String toString(){

		StringBuilder builder = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		builder.append(this.getClass().getName());
		builder.append("Object {");
		builder.append(newLine);

		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields){
			builder.append(" ");
			try {
				builder.append(field.getName());
				builder.append(": ");
				builder.append(field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.log(Level.SEVERE, "error", e);
			}
			builder.append(newLine);
		}
		builder.append("}");
		return builder.toString();
	}

}
