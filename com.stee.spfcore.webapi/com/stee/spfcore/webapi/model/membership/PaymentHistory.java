package com.stee.spfcore.webapi.model.membership;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"MEMBERSHIP_PAYMENT_HISTORY\"", schema = "\"SPFCORE\"")
@Audited
public class PaymentHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "\"ID\"")
	@XStreamOmitField
	private long id;

	@Column(name = "\"SOURCE_MONTH\"")
	private Integer sourceMonth;

	@Column(name = "\"SOURCE_YEAR\"")
	private Integer sourceYear;

	@Column(name = "\"AMOUNT\"")
	private Double amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"SOURCE\"", length = 10)
	private PaymentDataSource source;

	@Column(name = "\"OFFICER_COMMENT\"", length = 1000)
	private String officerComment;

	@Column(name = "\"PAYMENT_INDICATOR\"", length = 10)
	private String paymentIndicator;

	@Column(name = "\"REFERENCE_NUMBER\"", length = 100)
	private String referenceNumber;

	public PaymentHistory() {
		super();
	}

	public PaymentHistory(Integer sourceMonth, Integer sourceYear, Double amount, PaymentDataSource source,
			String officerComment, String paymentIndicator, String referenceNumber) {
		super();
		this.sourceMonth = sourceMonth;
		this.sourceYear = sourceYear;
		this.amount = amount;
		this.source = source;
		this.officerComment = officerComment;
		this.paymentIndicator = paymentIndicator;
		this.referenceNumber = referenceNumber;
	}

	public Integer getSourceMonth() {
		return sourceMonth;
	}

	public void setSourceMonth(Integer sourceMonth) {
		this.sourceMonth = sourceMonth;
	}

	public Integer getSourceYear() {
		return sourceYear;
	}

	public void setSourceYear(Integer sourceYear) {
		this.sourceYear = sourceYear;
	}

	public String getPaymentIndicator() {
		return paymentIndicator;
	}

	public void setPaymentIndicator(String paymentIndicator) {
		this.paymentIndicator = paymentIndicator;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public PaymentDataSource getSource() {
		return source;
	}

	public void setSource(PaymentDataSource source) {
		this.source = source;
	}

	public String getOfficerComment() {
		return officerComment;
	}

	public void setOfficerComment(String officerComment) {
		this.officerComment = officerComment;
	}

	public long getId() {
		return id;
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
		PaymentHistory other = (PaymentHistory) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
