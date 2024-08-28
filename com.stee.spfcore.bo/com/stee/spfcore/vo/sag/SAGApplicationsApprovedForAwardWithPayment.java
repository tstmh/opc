package com.stee.spfcore.vo.sag;

import com.stee.spfcore.model.accounting.PaymentStatus;

public class SAGApplicationsApprovedForAwardWithPayment {
	
	private String referenceNumber;
	
	private String financialYear;
	
	private String awardType;
	
	private String memberNric;
	
	private String memberName;
	
	private String childNric;
	
	private String childName;
	
	private Double awardAmount;
		
	private String sequenceNumber;
	
	private String submittedByWorkEmail;
	
	private String submittedByPrefEmail;
	
	private String submittedByPaymentAdviceEmail;
	
	private String preferredPaymentMode;
	
	private String bicCodeProxyType;
	
	private String accNoProxyValue;
	
	private String accName;
	
	private PaymentStatus paymentStatus;
	
	public SAGApplicationsApprovedForAwardWithPayment() {
		super();
	}
	 
	public SAGApplicationsApprovedForAwardWithPayment(
			String referenceNumber, String financialYear, String awardType,
			String memberNric, String memberName, String childNric,
			String childName, Double awardAmount, String sequenceNumber,
			String submittedByWorkEmail, String submittedByPrefEmail, String submittedByPaymentAdviceEmail,
			String preferredPaymentMode, String bicCodeProxyType,
			String accNoProxyValue, String accName) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.awardType = awardType;
		this.memberNric = memberNric;
		this.memberName = memberName;
		this.childNric = childNric;
		this.childName = childName;
		this.awardAmount = awardAmount;
		this.sequenceNumber = sequenceNumber;
		this.submittedByWorkEmail = submittedByWorkEmail;
		this.submittedByPrefEmail = submittedByPrefEmail;
		this.submittedByPaymentAdviceEmail = submittedByPaymentAdviceEmail;
		this.preferredPaymentMode = preferredPaymentMode;
		this.bicCodeProxyType = bicCodeProxyType;
		this.accNoProxyValue = accNoProxyValue;
		this.accName = accName;
	}

	public SAGApplicationsApprovedForAwardWithPayment(
			String referenceNumber, String financialYear, String awardType,
			String memberNric, String memberName, String childNric,
			String childName, Double awardAmount, String sequenceNumber,
			String submittedByWorkEmail, String submittedByPrefEmail, String submittedByPaymentAdviceEmail,
			String preferredPaymentMode, String bicCodeProxyType,
			String accNoProxyValue, String accName, PaymentStatus paymentStatus) {
		super();
		this.referenceNumber = referenceNumber;
		this.financialYear = financialYear;
		this.awardType = awardType;
		this.memberNric = memberNric;
		this.memberName = memberName;
		this.childNric = childNric;
		this.childName = childName;
		this.awardAmount = awardAmount;
		this.sequenceNumber = sequenceNumber;
		this.submittedByWorkEmail = submittedByWorkEmail;
		this.submittedByPrefEmail = submittedByPrefEmail;
		this.submittedByPaymentAdviceEmail = submittedByPaymentAdviceEmail;
		this.preferredPaymentMode = preferredPaymentMode;
		this.bicCodeProxyType = bicCodeProxyType;
		this.accNoProxyValue = accNoProxyValue;
		this.accName = accName;
		this.paymentStatus = paymentStatus;
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

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public String getMemberNric() {
		return memberNric;
	}

	public void setMemberNric(String memberNric) {
		this.memberNric = memberNric;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getChildNric() {
		return childNric;
	}

	public void setChildNric(String childNric) {
		this.childNric = childNric;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public Double getAwardAmount() {
		return awardAmount;
	}

	public void setAwardAmount(Double awardAmount) {
		this.awardAmount = awardAmount;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getPreferredPaymentMode() {
		return preferredPaymentMode;
	}

	public void setPreferredPaymentMode(String preferredPaymentMode) {
		this.preferredPaymentMode = preferredPaymentMode;
	}

	public String getSubmittedByWorkEmail() {
		return submittedByWorkEmail;
	}

	public void setSubmittedByWorkEmail(String submittedByWorkEmail) {
		this.submittedByWorkEmail = submittedByWorkEmail;
	}

	public String getSubmittedByPrefEmail() {
		return submittedByPrefEmail;
	}	

	public void setSubmittedByPrefEmail(String submittedByPrefEmail) {
		this.submittedByPrefEmail = submittedByPrefEmail;
	}
	
	public String getSubmittedByPaymentAdviceEmail() {
		return submittedByPaymentAdviceEmail;
	}
	
	public void setSubmittedByPaymentAdviceEmail(String submittedByPaymentAdviceEmail) {
		this.submittedByPaymentAdviceEmail = submittedByPaymentAdviceEmail;
	}

	public String getBicCodeProxyType() {
		return bicCodeProxyType;
	}

	public void setBicCodeProxyType(String bicCodeProxyType) {
		this.bicCodeProxyType = bicCodeProxyType;
	}

	public String getAccNoProxyValue() {
		return accNoProxyValue;
	}

	public void setAccNoProxyValue(String accNoProxyValue) {
		this.accNoProxyValue = accNoProxyValue;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}
	
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Override
	public String toString() {
		return "SAGApplicationsApprovedForAwardWithPayment [referenceNumber="
				+ referenceNumber + ", financialYear=" + financialYear
				+ ", awardType=" + awardType + ", memberNric=" + memberNric
				+ ", memberName=" + memberName + ", childNric=" + childNric
				+ ", childName=" + childName + ", awardAmount=" + awardAmount
				+ ", sequenceNumber=" + sequenceNumber + ", submittedByPrefEmail=" 
				+ submittedByPrefEmail + ", submittedByPaymentAdviceEmail=" + submittedByPaymentAdviceEmail + ", submittedByWorkEmail=" + submittedByWorkEmail 
				+ ", preferredPaymentMode=" + preferredPaymentMode + ", bicCodeProxyType=" + bicCodeProxyType 
				+ ", accNoProxyValue=" + accNoProxyValue + ", accName=" + accName 
				+ ", paymentStatus=" + paymentStatus
				+ "]";
	}
	
}
