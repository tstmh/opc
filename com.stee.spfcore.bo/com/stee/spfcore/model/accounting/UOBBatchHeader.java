package com.stee.spfcore.model.accounting;

import java.lang.reflect.Field;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import static com.ibm.java.diagnostics.utils.Context.logger;

@XStreamAlias("UOBBatchHeader")
public class UOBBatchHeader {
	private String recordType;
	private String paymentType;
	private String serviceType;
	private String processingMode;
	private String companyId;
	private String originatingBICCode;
	private String originatingAccountNoCurrency;
	private String originatingAccountNo;
	private String originatingAccountName;
	private Date creationDate;
	private Date valueDate;
	private String ultimateOriginatingCustomer;
	private String bulkCustomerReference;
	private String softwareLabel;
	
	public UOBBatchHeader() {
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

	public UOBBatchHeader(String recordType, String paymentType,
			String serviceType, String processingMode, String companyId,
			String originatingBICCode, String originatingAccountNoCurrency,
			String originatingAccountNo, String originatingAccountName,
			Date creationDate, Date valueDate,
			String ultimateOriginatingCustomer, String bulkCustomerReference,
			String softwareLabel) {
		super();
		this.recordType = recordType;
		this.paymentType = paymentType;
		this.serviceType = serviceType;
		this.processingMode = processingMode;
		this.companyId = companyId;
		this.originatingBICCode = originatingBICCode;
		this.originatingAccountNoCurrency = originatingAccountNoCurrency;
		this.originatingAccountNo = originatingAccountNo;
		this.originatingAccountName = originatingAccountName;
		this.creationDate = creationDate;
		this.valueDate = valueDate;
		this.ultimateOriginatingCustomer = ultimateOriginatingCustomer;
		this.bulkCustomerReference = bulkCustomerReference;
		this.softwareLabel = softwareLabel;
	}

	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getProcessingMode() {
		return processingMode;
	}
	public void setProcessingMode(String processingMode) {
		this.processingMode = processingMode;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getOriginatingBICCode() {
		return originatingBICCode;
	}
	public void setOriginatingBICCode(String originatingBICCode) {
		this.originatingBICCode = originatingBICCode;
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	public String getUltimateOriginatingCustomer() {
		return ultimateOriginatingCustomer;
	}
	public void setUltimateOriginatingCustomer(String ultimateOriginatingCustomer) {
		this.ultimateOriginatingCustomer = ultimateOriginatingCustomer;
	}
	public String getBulkCustomerReference() {
		return bulkCustomerReference;
	}
	public void setBulkCustomerReference(String bulkCustomerReference) {
		this.bulkCustomerReference = bulkCustomerReference;
	}
	public String getSoftwareLabel() {
		return softwareLabel;
	}
	public void setSoftwareLabel(String softwareLabel) {
		this.softwareLabel = softwareLabel;
	}
	public String getOriginatingAccountNoCurrency() {
		return originatingAccountNoCurrency;
	}
	public void setOriginatingAccountNoCurrency(String originatingAccountNoCurrency) {
		this.originatingAccountNoCurrency = originatingAccountNoCurrency;
	}
}
