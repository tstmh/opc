package com.stee.spfcore.vo.benefits;

import java.util.Date;

public class NewBornGiftDetails extends CommonBenefitsDetails {

	private String childName;

	private Date childDateOfBirth;

	private String birthCertificateNo;
	
	private Date paymentDate;
	
	public String getChildName() {
		return childName;
	}

	public void setChildName( String childName ) {
		this.childName = childName;
	}

	public Date getChildDateOfBirth() {
		return childDateOfBirth;
	}

	public void setChildDateOfBirth( Date childDateOfBirth ) {
		this.childDateOfBirth = childDateOfBirth;
	}

	public String getBirthCertificateNo() {
		return birthCertificateNo;
	}

	public void setBirthCertificateNo( String birthCertificateNo ) {
		this.birthCertificateNo = birthCertificateNo;
	}
	
	

	public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
	public String toString() {
		return "NewBornGiftDetails [childName=" + childName
				+ ", childDateOfBirth=" + childDateOfBirth
				+ ", birthCertificateNo=" + birthCertificateNo
				+ ", getReferenceNumber()=" + getReferenceNumber()
				+ ", getMemberNric()=" + getMemberNric() + ", getMemberName()="
				+ getMemberName() + ", getApplicationStatus()="
				+ getApplicationStatus() + ", getDateOfSubmission()="
				+ getDateOfSubmission() + ", getSubmittedBy()="
				+ getSubmittedBy() + ", getAmountToBePaid()="
				+ getAmountToBePaid() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
