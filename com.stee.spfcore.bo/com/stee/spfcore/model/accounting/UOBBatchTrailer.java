package com.stee.spfcore.model.accounting;

import java.lang.reflect.Field;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import static com.ibm.java.diagnostics.utils.Context.logger;

@XStreamAlias("UOBBatchTrailer")
public class UOBBatchTrailer {

	private String recordType;
	private Double totalAmount;
	private int totalNoOfTransaction;
	private Double totalAcceptedAmount;
	private int totalAcceptedNoOfTransaction;
	private Double totalRejectedAmount;
	private int totalRejectedNoOfTransaction;
	private Double totalPendingAmount;
	private int totalPendingNoOfTransaction;
	private Double totalStoppedAmount;
	private int totalStoppedNoOfTransaction;
	
	public UOBBatchTrailer() {
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

	
	public UOBBatchTrailer(String recordType, Double totalAmount,
			int totalNoOfTransaction, Double totalAcceptedAmount,
			int totalAcceptedNoOfTransaction, Double totalRejectedAmount,
			int totalRejectedNoOfTransaction, Double totalPendingAmount,
			int totalPendingNoOfTransaction, Double totalStoppedAmount,
			int totalStoppedNoOfTransaction) {
		super();
		this.recordType = recordType;
		this.totalAmount = totalAmount;
		this.totalNoOfTransaction = totalNoOfTransaction;
		this.totalAcceptedAmount = totalAcceptedAmount;
		this.totalAcceptedNoOfTransaction = totalAcceptedNoOfTransaction;
		this.totalRejectedAmount = totalRejectedAmount;
		this.totalRejectedNoOfTransaction = totalRejectedNoOfTransaction;
		this.totalPendingAmount = totalPendingAmount;
		this.totalPendingNoOfTransaction = totalPendingNoOfTransaction;
		this.totalStoppedAmount = totalStoppedAmount;
		this.totalStoppedNoOfTransaction = totalStoppedNoOfTransaction;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getTotalNoOfTransaction() {
		return totalNoOfTransaction;
	}

	public void setTotalNoOfTransaction(int totalNoOfTransaction) {
		this.totalNoOfTransaction = totalNoOfTransaction;
	}

	public Double getTotalAcceptedAmount() {
		return totalAcceptedAmount;
	}

	public void setTotalAcceptedAmount(Double totalAcceptedAmount) {
		this.totalAcceptedAmount = totalAcceptedAmount;
	}

	public int getTotalAcceptedNoOfTransaction() {
		return totalAcceptedNoOfTransaction;
	}

	public void setTotalAcceptedNoOfTransaction(int totalAcceptedNoOfTransaction) {
		this.totalAcceptedNoOfTransaction = totalAcceptedNoOfTransaction;
	}

	public Double getTotalRejectedAmount() {
		return totalRejectedAmount;
	}

	public void setTotalRejectedAmount(Double totalRejectedAmount) {
		this.totalRejectedAmount = totalRejectedAmount;
	}

	public int getTotalRejectedNoOfTransaction() {
		return totalRejectedNoOfTransaction;
	}

	public void setTotalRejectedNoOfTransaction(int totalRejectedNoOfTransaction) {
		this.totalRejectedNoOfTransaction = totalRejectedNoOfTransaction;
	}

	public Double getTotalPendingAmount() {
		return totalPendingAmount;
	}

	public void setTotalPendingAmount(Double totalPendingAmount) {
		this.totalPendingAmount = totalPendingAmount;
	}

	public int getTotalPendingNoOfTransaction() {
		return totalPendingNoOfTransaction;
	}

	public void setTotalPendingNoOfTransaction(int totalPendingNoOfTransaction) {
		this.totalPendingNoOfTransaction = totalPendingNoOfTransaction;
	}

	public Double getTotalStoppedAmount() {
		return totalStoppedAmount;
	}

	public void setTotalStoppedAmount(Double totalStoppedAmount) {
		this.totalStoppedAmount = totalStoppedAmount;
	}

	public int getTotalStoppedNoOfTransaction() {
		return totalStoppedNoOfTransaction;
	}

	public void setTotalStoppedNoOfTransaction(int totalStoppedNoOfTransaction) {
		this.totalStoppedNoOfTransaction = totalStoppedNoOfTransaction;
	}
	
}
