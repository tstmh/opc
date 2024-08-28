package com.stee.spfcore.vo.benefits;

import java.util.Date;

public class WeddingGiftDetails extends CommonBenefitsDetails {

	private String spouseName;
	
	private Date dateOfMarriage;
	
	private String certificateNo;
	
	private Date paymentDate;
	
	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName( String spouseName ) {
		this.spouseName = spouseName;
	}

	public Date getDateOfMarriage() {
		return dateOfMarriage;
	}

	public void setDateOfMarriage( Date dateOfMarriage ) {
		this.dateOfMarriage = dateOfMarriage;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo( String certificateNo ) {
		this.certificateNo = certificateNo;
	}

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
	
	
}
