package com.stee.spfcore.model.accounting;

import java.lang.reflect.Field;
import java.util.logging.Level;

import com.stee.spfcore.utils.Util;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import static com.ibm.java.diagnostics.utils.Context.logger;

@XStreamAlias("ACCPACDetail")
public class AccpacDetail {

	private String batchID;
	private String entryNumber;
	private String accCode;
	private String batchDesc;
	private String paymentMode;
	private String paymentReference;
	private String entryDescription;
	
	private String destinationBankBranch;
	private String destinationAccNo;
	private String destinationAccName;
	private String lineDescription;
	private String amount;
	private String gstAmount;
	private String gstInclusive;
	private String gstRate;
	private String transactionDate;
	private String currency;
	private String exRate;
	private String bankCode;
	private String transactionType;
	
	public AccpacDetail() {
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

	public AccpacDetail(String batchID, String entryNumber, String accCode,
			String batchDesc, String paymentMode, String paymentReference,
			String entryDescription, String destinationBankBranch,
			String destinationAccNo, String destinationAccName,
			String lineDescription, String amount, String gstAmount,
			String gstInclusive, String gstRate, String transactionDate,
			String currency, String exRate, String bankCode,
			String transactionType) {
		super();
		this.batchID = batchID;
		this.entryNumber = entryNumber;
		this.accCode = accCode;
		this.batchDesc = batchDesc;
		this.paymentMode = paymentMode;
		this.paymentReference = paymentReference;
		this.entryDescription = entryDescription;
		this.destinationBankBranch = destinationBankBranch;
		this.destinationAccNo = destinationAccNo;
		this.destinationAccName = destinationAccName;
		this.lineDescription = lineDescription;
		this.amount = amount;
		this.gstAmount = gstAmount;
		this.gstInclusive = gstInclusive;
		this.gstRate = gstRate;
		this.transactionDate = transactionDate;
		this.currency = currency;
		this.exRate = exRate;
		this.bankCode = bankCode;
		this.transactionType = transactionType;
	}

	public String getBatchID() {
		return batchID;
	}

	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}

	public String getEntryNumber() {
		return entryNumber;
	}

	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}

	public String getAccCode() {
		return accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	public String getBatchDesc() {
		return batchDesc;
	}

	public void setBatchDesc(String batchDesc) {
		this.batchDesc = batchDesc;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public String getEntryDescription() {
		return entryDescription;
	}

	public void setEntryDescription(String entryDescription) {
		this.entryDescription = entryDescription;
	}

	public String getDestinationBankBranch() {
		return destinationBankBranch;
	}

	public void setDestinationBankBranch(String destinationBankBranch) {
		this.destinationBankBranch = destinationBankBranch;
	}

	public String getDestinationAccNo() {
		return destinationAccNo;
	}

	public void setDestinationAccNo(String destinationAccNo) {
		this.destinationAccNo = destinationAccNo;
	}

	public String getDestinationAccName() {
		return destinationAccName;
	}

	public void setDestinationAccName(String destinationAccName) {
		this.destinationAccName = destinationAccName;
	}

	public String getLineDescription() {
		return lineDescription;
	}

	public void setLineDescription(String lineDescription) {
		this.lineDescription = lineDescription;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(String gstAmount) {
		this.gstAmount = gstAmount;
	}

	public String getGstInclusive() {
		return gstInclusive;
	}

	public void setGstInclusive(String gstInclusive) {
		this.gstInclusive = gstInclusive;
	}

	public String getGstRate() {
		return gstRate;
	}

	public void setGstRate(String gstRate) {
		this.gstRate = gstRate;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExRate() {
		return exRate;
	}

	public void setExRate(String exRate) {
		this.exRate = exRate;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
