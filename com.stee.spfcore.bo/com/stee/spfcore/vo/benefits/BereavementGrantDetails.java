package com.stee.spfcore.vo.benefits;

import java.util.Date;

public class BereavementGrantDetails extends CommonBenefitsDetails {
	
	private String nameOfDeceased;
	
	private Date deceaseDate;
	
	private String deathCertificateNo;
	
	private String relationship;
	
	private Date paymentDate;
	
	public String getNameOfDeceased() {
		return nameOfDeceased;
	}

	public void setNameOfDeceased( String nameOfDeceased ) {
		this.nameOfDeceased = nameOfDeceased;
	}

	public Date getDeceaseDate() {
		return deceaseDate;
	}

	public void setDeceaseDate( Date deceaseDate ) {
		this.deceaseDate = deceaseDate;
	}

	public String getDeathCertificateNo() {
		return deathCertificateNo;
	}

	public void setDeathCertificateNo( String deathCertificateNo ) {
		this.deathCertificateNo = deathCertificateNo;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship( String relationship ) {
		this.relationship = relationship;
	}

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
