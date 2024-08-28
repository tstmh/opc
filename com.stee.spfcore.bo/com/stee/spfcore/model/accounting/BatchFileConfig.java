package com.stee.spfcore.model.accounting;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"BATCH_FILE_CONFIG\"", schema = "\"SPFCORE\"")
@XStreamAlias("BatchFileConfig")
@Audited
@SequenceDef (name="BATCH_FILE_CONFIG_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class BatchFileConfig {
	
	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column(name="\"FINANCIAL_YEAR\"", length=10)
	private String financialYear;
	
	@Column(name="\"PAYMENT_TYPE\"", length=1)
	private String paymentType; //P-Payment, R-Payroll, C-Collection(not applicable for PayNow)
	
	/** 
	 * processing mode depends on paymentModeGiro, paymentModePaynow, processingType
	 * 'I'-GIRO+FAST; 'B'-GIRO+BATCH; 'F'-PayNow+FAST; 'G'-PayNow+BATCH; 
	**/ 
	
	@Column(name="\"PROCESSING_TYPE\"", length=10)
	private String processingType; //FAST-transfer in same working day; BATCH-transfer in 2 working days
	
	@Column(name="\"ORIGINATING_ACCOUNT_NO\"", length=34)
	private String originatingAccountNo;
	
	@Column(name="\"ORIGINATING_ACCOUNT_NAME\"", length=140)
	private String originatingAccountName;
	
	@Column(name="\"HAS_PAYMENT_ADVICE\"")
	private boolean hasPaymentAdvice;
	
	@Column(name="\"DELIVERY_MODE_EMAIL\"") 
	private boolean deliveryModeEmail; // use if paymentAdviceIndicator is true
	
	@Column(name="\"DELIVERY_MODE_POST\"") 
	private boolean deliveryModePost; // use if paymentAdviceIndicator is true
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"VALUE_DATE\"")
	private Date valueDate;
	
	@Column(name="\"BULK_CUSTOMER_REF\"", length=16)
	private String bulkCustomerReference; // displayed on Originating UOB Bank Account Statement 
	
	@Column(name="\"PURPOSE_CODE\"", length=4)
	private String purposeCode;
	
	@Column(name="\"ULT_ORIGINATING_CUSTOMER\"", length=140)
	private String ultimateOriginatingCustomer;
	
	@Column(name="\"REMITTANCE_INFORMATION\"", length=140)
	private String remittanceInformation; // additional payment details
	
	@Column(name="\"UPDATED_BY\"", length=100)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedDate;
	
	//payment advice
	@Column(name="\"PAYER_NAME\"", length=70)
	private String payerName;
	
	@Column(name="\"PAYMENT_ADVICE_HEADER\"")
	private String paymentAdviceHeader; 
	
	@Column(name="\"PAYMENT_ADVICE_DETAILS\"", length=2000)
	private String paymentAdviceDetails; 
	
	@Column(name = "\"INBOUND_FOLDER\"")
	private String inboundFolder = "c:\\UOB\\inbound";
	
	@Column(name = "\"OUTBOUND_FOLDER\"")
	private String outboundFolder = "c:\\UOB\\outbound";
	
	public BatchFileConfig() {
		super();
	}

	public BatchFileConfig(String id, String financialYear, String paymentType,
			String processingType, String originatingAccountNo,
			String originatingAccountName, boolean hasPaymentAdvice,
			boolean deliveryModeEmail, boolean deliveryModePost,
			Date valueDate, String bulkCustomerReference, String purposeCode,
			String ultimateOriginatingCustomer, String remittanceInformation,
			String updatedBy, Date updatedDate, String payerName,
			String paymentAdviceHeader, String paymentAdviceDetails,
			String inboundFolder, String outboundFolder) {
		super();
		this.id = id;
		this.financialYear = financialYear;
		this.paymentType = paymentType;
		this.processingType = processingType;
		this.originatingAccountNo = originatingAccountNo;
		this.originatingAccountName = originatingAccountName;
		this.hasPaymentAdvice = hasPaymentAdvice;
		this.deliveryModeEmail = deliveryModeEmail;
		this.deliveryModePost = deliveryModePost;
		this.valueDate = valueDate;
		this.bulkCustomerReference = bulkCustomerReference;
		this.purposeCode = purposeCode;
		this.ultimateOriginatingCustomer = ultimateOriginatingCustomer;
		this.remittanceInformation = remittanceInformation;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.payerName = payerName;
		this.paymentAdviceHeader = paymentAdviceHeader;
		this.paymentAdviceDetails = paymentAdviceDetails;
		this.inboundFolder = inboundFolder;
		this.outboundFolder = outboundFolder;
	}

	public BatchFileConfig(String id, String financialYear, String paymentType,
			String processingType, String originatingAccountNo,
			String originatingAccountName, boolean hasPaymentAdvice,
			boolean deliveryModeEmail, boolean deliveryModePost,
			Date valueDate, String bulkCustomerReference, String purposeCode,
			String ultimateOriginatingCustomer, String remittanceInformation,
			String updatedBy, Date updatedDate, String payerName,
			String paymentAdviceHeader, String paymentAdviceDetails) {
		super();
		this.id = id;
		this.financialYear = financialYear;
		this.paymentType = paymentType;
		this.processingType = processingType;
		this.originatingAccountNo = originatingAccountNo;
		this.originatingAccountName = originatingAccountName;
		this.hasPaymentAdvice = hasPaymentAdvice;
		this.deliveryModeEmail = deliveryModeEmail;
		this.deliveryModePost = deliveryModePost;
		this.valueDate = valueDate;
		this.bulkCustomerReference = bulkCustomerReference;
		this.purposeCode = purposeCode;
		this.ultimateOriginatingCustomer = ultimateOriginatingCustomer;
		this.remittanceInformation = remittanceInformation;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.payerName = payerName;
		this.paymentAdviceHeader = paymentAdviceHeader;
		this.paymentAdviceDetails = paymentAdviceDetails;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}

	public String getOriginatingAccountNo() {
		return originatingAccountNo;
	}

	public void setOriginatingAccountNo(String originatingAccountNo) {
		this.originatingAccountNo = originatingAccountNo;
	}

	public String getOriginatingAccountName() {
		return originatingAccountName;
	}

	public void setOriginatingAccountName(String originatingAccountName) {
		this.originatingAccountName = originatingAccountName;
	}

	public boolean isHasPaymentAdvice() {
		return hasPaymentAdvice;
	}

	public void setHasPaymentAdvice(boolean hasPaymentAdvice) {
		this.hasPaymentAdvice = hasPaymentAdvice;
	}

	public boolean isDeliveryModeEmail() {
		return deliveryModeEmail;
	}

	public void setDeliveryModeEmail(boolean deliveryModeEmail) {
		this.deliveryModeEmail = deliveryModeEmail;
	}

	public boolean isDeliveryModePost() {
		return deliveryModePost;
	}

	public void setDeliveryModePost(boolean deliveryModePost) {
		this.deliveryModePost = deliveryModePost;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public String getBulkCustomerReference() {
		return bulkCustomerReference;
	}

	public void setBulkCustomerReference(String bulkCustomerReference) {
		this.bulkCustomerReference = bulkCustomerReference;
	}

	public String getPurposeCode() {
		return purposeCode;
	}

	public void setPurposeCode(String purposeCode) {
		this.purposeCode = purposeCode;
	}

	public String getUltimateOriginatingCustomer() {
		return ultimateOriginatingCustomer;
	}

	public void setUltimateOriginatingCustomer(String ultimateOriginatingCustomer) {
		this.ultimateOriginatingCustomer = ultimateOriginatingCustomer;
	}

	public String getRemittanceInformation() {
		return remittanceInformation;
	}

	public void setRemittanceInformation(String remittanceInformation) {
		this.remittanceInformation = remittanceInformation;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPaymentAdviceHeader() {
		return paymentAdviceHeader;
	}

	public void setPaymentAdviceHeader(String paymentAdviceHeader) {
		this.paymentAdviceHeader = paymentAdviceHeader;
	}

	public String getPaymentAdviceDetails() {
		return paymentAdviceDetails;
	}

	public void setPaymentAdviceDetails(String paymentAdviceDetails) {
		this.paymentAdviceDetails = paymentAdviceDetails;
	}

	public String getInboundFolder() {
		return inboundFolder;
	}

	public void setInboundFolder(String inboundFolder) {
		this.inboundFolder = inboundFolder;
	}

	public String getOutboundFolder() {
		return outboundFolder;
	}

	public void setOutboundFolder(String outboundFolder) {
		this.outboundFolder = outboundFolder;
	}
}
