package com.stee.spfcore.model.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name="\"MEMBERSHIP_PAYMENT_RECORD\"", schema="\"SPFCORE\"")
@XStreamAlias("PaymentRecord")
@Audited
public class PaymentRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	@XStreamOmitField
	private long id;
	
	@Column(name="\"PAID_INDICATOR\"", length=1)
	private String paidIndicator;
	
	@Column(name="\"AMOUNT_PAID\"")
	private Double amountPaid;
	
	@Column(name="\"ACCRUAL_MONTH\"", length=8)
	private String accrualMonth;
	
	@Column(name="\"REFERENCE_NUMBER\"", length=50)
	private String paymentRefNumber;
	
	@Column(name="\"RANK\"", length=10)
	private String rank;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"BILLING_DATE\"")
	private Date billingDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPLOADED_DATE\"")
	private Date uploadedDate;
	
	@Column(name="\"CREDIT_AMOUNT\"")
	private Double creditAmount;
	
	@Column(name="\"BALANCE\"")
	private Double balance;
	
	@Column(name = "\"ADJUSTED_AMOUNT\"")
    private Double adjustedAmount;

    @Column(name = "\"ADJUSTED_DATE\"")
    private Date adjustedDate;

    @Column(name = "\"ADJUSTED_BY\"")
    private String adjustedBy;

    @Column(name = "\"ADJUSTED_REMARKS\"")
    private String adjustedRemarks;

	public PaymentRecord() {
		super();
	}

    public PaymentRecord(String paidIndicator, Double amountPaid,
            String accrualMonth, String paymentRefNumber, String rank,
            Date billingDate, Date uploadedDate, Double creditAmount,
            Double balance, Double adjustedAmount, Date adjustedDate,
            String adjustedBy, String adjustedRemarks) {
        
        super();
        this.paidIndicator = paidIndicator;
        this.amountPaid = amountPaid;
        this.accrualMonth = accrualMonth;
        this.paymentRefNumber = paymentRefNumber;
        
        this.rank = rank;
        this.billingDate = billingDate;
        this.uploadedDate = uploadedDate;
        this.creditAmount = creditAmount;
        this.balance = balance;
        this.adjustedAmount = adjustedAmount;
        this.adjustedDate = adjustedDate;
        this.adjustedBy = adjustedBy;
        this.adjustedRemarks = adjustedRemarks;
    }



    public String getPaidIndicator() {
		return paidIndicator;
	}

	public void setPaidIndicator(String paidIndicator) {
		this.paidIndicator = paidIndicator;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getAccrualMonth() {
		return accrualMonth;
	}

	public void setAccrualMonth(String accrualMonth) {
		this.accrualMonth = accrualMonth;
	}

	public String getPaymentRefNumber() {
		return paymentRefNumber;
	}

	public void setPaymentRefNumber( String paymentRefNumber ) {
		this.paymentRefNumber = paymentRefNumber;
	}

	public String getRank() {
		return rank;
	}

	public void setRank( String rank ) {
		this.rank = rank;
	}

	public Date getBillingDate() {
		return billingDate;
	}

	public void setBillingDate( Date billingDate ) {
		this.billingDate = billingDate;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate( Date uploadedDate ) {
		this.uploadedDate = uploadedDate;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount( Double creditAmount ) {
		this.creditAmount = creditAmount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance( Double balance ) {
		this.balance = balance;
	}
	

    public Double getAdjustedAmount() {
        return adjustedAmount;
    }

    public void setAdjustedAmount(Double adjustedAmount) {
        this.adjustedAmount = adjustedAmount;
    }

    public Date getAdjustedDate() {
        return adjustedDate;
    }

    public void setAdjustedDate(Date adjustedDate) {
        this.adjustedDate = adjustedDate;
    }

    public String getAdjustedBy() {
        return adjustedBy;
    }

    public void setAdjustedBy(String adjustedBy) {
        this.adjustedBy = adjustedBy;
    }

    public String getAdjustedRemarks() {
        return adjustedRemarks;
    }

    public void setAdjustedRemarks(String adjustedRemarks) {
        this.adjustedRemarks = adjustedRemarks;
    }
    
    public void preSave () {
    	if (paymentRefNumber != null) {
    		paymentRefNumber = paymentRefNumber.toUpperCase();
    	}
    	
    	if (adjustedBy != null) {
    		adjustedBy = adjustedBy.toUpperCase();
    	}
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentRecord other = (PaymentRecord) obj;
		return id == other.id;
	}

    @Override
    public String toString() {
        return "PaymentRecord [id=" + id + ", paidIndicator=" + paidIndicator
                + ", amountPaid=" + amountPaid + ", accrualMonth="
                + accrualMonth + ", paymentRefNumber=" + paymentRefNumber
                + ", rank=" + rank + ", billingDate=" + billingDate
                + ", uploadedDate=" + uploadedDate + ", creditAmount="
                + creditAmount + ", balance=" + balance + ", adjustedAmount="
                + adjustedAmount + ", adjustedDate=" + adjustedDate
                + ", adjustedBy=" + adjustedBy + ", adjustedRemarks="
                + adjustedRemarks + "]";
    }
	
}
