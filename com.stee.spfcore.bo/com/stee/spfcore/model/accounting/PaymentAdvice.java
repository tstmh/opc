package com.stee.spfcore.model.accounting;

import java.util.List;

public class PaymentAdvice {
	
	String paymentAdviceHeader;
	List<PaymentAdviceDetail> paymentAdviceDetailList;
	String payerName;
	
	public PaymentAdvice() {
		super();
	}
	
	public PaymentAdvice(String paymentAdviceHeader,
			List<PaymentAdviceDetail> paymentAdviceDetailList, String payerName) {
		super();
		this.paymentAdviceHeader = paymentAdviceHeader;
		this.paymentAdviceDetailList = paymentAdviceDetailList;
		this.payerName = payerName;
	}

	public String getPaymentAdviceHeader() {
		return paymentAdviceHeader;
	}

	public void setPaymentAdviceHeader(String paymentAdviceHeader) {
		this.paymentAdviceHeader = paymentAdviceHeader;
	}

	public List<PaymentAdviceDetail> getPaymentAdviceDetailList() {
		return paymentAdviceDetailList;
	}

	public void setPaymentAdviceDetailList(List<PaymentAdviceDetail> paymentAdviceDetailList) {
		this.paymentAdviceDetailList = paymentAdviceDetailList;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	
}
