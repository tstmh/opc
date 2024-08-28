package com.stee.spfcore.model.accounting;

import java.lang.reflect.Field;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import static com.ibm.java.diagnostics.utils.Context.logger;

@XStreamAlias("UOBBatchDetail")
public class UOBBatchDetail {
	private String recordType;
	private String receivingBICCode;
	private String receivingAccountNo;
	private String receivingAccountName;
	private String currency;
	private Double amount;
	private String endToEndId;
	private String mandateId;
	private String purposeCode;
	private String remittanceInformation;
	private String ultimatePayerBeneName;
	private String customerReference;
	private String returnCode;
	private PaymentStatus paymentStatus; //aka Clear Fate
	private String reasonOfNotSent;
	
	public UOBBatchDetail() {
		super();
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
				logger.severe(String.valueOf(e));
			}
			builder.append(newLine);
		}
		builder.append("}");
		return builder.toString();
	}

	public UOBBatchDetail(String recordType, String receivingBICCode,
			String receivingAccountNo, String receivingAccountName,
			String currency, Double amount, String endToEndId,
			String mandateId, String purposeCode, String remittanceInformation,
			String ultimatePayerBeneName, String customerReference, String returnCode, 
			PaymentStatus paymentStatus, String reasonOfNotSent) {
		super();
		this.recordType = recordType;
		this.receivingBICCode = receivingBICCode;
		this.receivingAccountNo = receivingAccountNo;
		this.receivingAccountName = receivingAccountName;
		this.currency = currency;
		this.amount = amount;
		this.endToEndId = endToEndId;
		this.mandateId = mandateId;
		this.purposeCode = purposeCode;
		this.remittanceInformation = remittanceInformation;
		this.ultimatePayerBeneName = ultimatePayerBeneName;
		this.customerReference = customerReference;
		this.returnCode = returnCode;
		this.paymentStatus = paymentStatus;
		this.reasonOfNotSent = reasonOfNotSent;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getReceivingBICCode() {
		return receivingBICCode;
	}

	public void setReceivingBICCode(String receivingBICCode) {
		this.receivingBICCode = receivingBICCode;
	}

	public String getReceivingAccountNo() {
		return receivingAccountNo;
	}

	public void setReceivingAccountNo(String receivingAccountNo) {
		this.receivingAccountNo = receivingAccountNo;
	}

	public String getReceivingAccountName() {
		return receivingAccountName;
	}

	public void setReceivingAccountName(String receivingAccountName) {
		this.receivingAccountName = receivingAccountName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getEndToEndId() {
		return endToEndId;
	}

	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}

	public String getMandateId() {
		return mandateId;
	}

	public void setMandateId(String mandateId) {
		this.mandateId = mandateId;
	}

	public String getPurposeCode() {
		return purposeCode;
	}

	public void setPurposeCode(String purposeCode) {
		this.purposeCode = purposeCode;
	}

	public String getRemittanceInformation() {
		return remittanceInformation;
	}

	public void setRemittanceInformation(String remittanceInformation) {
		this.remittanceInformation = remittanceInformation;
	}

	public String getUltimatePayerBeneName() {
		return ultimatePayerBeneName;
	}

	public void setUltimatePayerBeneName(String ultimatePayerBeneName) {
		this.ultimatePayerBeneName = ultimatePayerBeneName;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public String getReasonOfNotSent() {
		return reasonOfNotSent;
	}

	public void setReasonOfNotSent(String reasonOfNotSent) {
		this.reasonOfNotSent = reasonOfNotSent;
	}
	
	
	
}
