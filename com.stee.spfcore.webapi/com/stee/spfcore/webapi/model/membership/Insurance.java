package com.stee.spfcore.webapi.model.membership;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Embeddable
@XStreamAlias("Insurance")
@Audited
public class Insurance {

	@Temporal(TemporalType.DATE)
	@Column(name="\"INSURANCE_SUBMISSION_DATE\"")
	private Date submissionDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"INSURANCE_CREATED_DATE\"")
	private Date insuranceCreatedDate;
	
	@Column(name="\"INSURANCE_CREATED_BY\"", length=255)
	private String insuranceCreatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"INSURANCE_UPDATED_DATE\"")
	private Date insuranceUpdatedOn;
	
	@Column(name="\"INSURANCE_UPDATED_BY\"", length=255)
	private String insuranceUpdatedBy;
	
	@Column(name="\"INSURANCE_REMARKS\"", length=2000)
	private String remark;
	
	@Enumerated(EnumType.STRING)
	@Column(name="\"NOMINATION_STATUS\"", length=10)
	private ApplicationStatus nominationStatus;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="\"MEMBER_NRIC\"")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Nominee> nominees;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"INSURANCE_PAYOUT_DATE\"")
	private Date payoutDate;
	
	@Column(name = "\"INSURANCE_AMT_DISBURSED\"")
	private double amountDisbursed;

	public Insurance() {
		super();
	}

	public Insurance( Date submissionDate, Date insuranceCreatedDate,
			String insuranceCreatedBy, Date insuranceUpdatedOn,
			String insuranceUpdatedBy, String remark,
			ApplicationStatus nominationStatus, List<Nominee> nominees,
			Date payoutDate, double amountDisbursed ) {
		super();
		this.submissionDate = submissionDate;
		this.insuranceCreatedDate = insuranceCreatedDate;
		this.insuranceCreatedBy = insuranceCreatedBy;
		this.insuranceUpdatedOn = insuranceUpdatedOn;
		this.insuranceUpdatedBy = insuranceUpdatedBy;
		this.remark = remark;
		this.nominationStatus = nominationStatus;
		this.nominees = nominees;
		this.payoutDate = payoutDate;
		this.amountDisbursed = amountDisbursed;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public Date getInsuranceCreatedDate() {
		return insuranceCreatedDate;
	}

	public void setInsuranceCreatedDate( Date insuranceCreatedDate ) {
		this.insuranceCreatedDate = insuranceCreatedDate;
	}

	public String getInsuranceCreatedBy() {
		return insuranceCreatedBy;
	}

	public void setInsuranceCreatedBy( String insuranceCreatedBy ) {
		this.insuranceCreatedBy = insuranceCreatedBy;
	}

	public Date getInsuranceUpdatedOn() {
		return insuranceUpdatedOn;
	}

	public void setInsuranceUpdatedOn( Date insuranceUpdatedOn ) {
		this.insuranceUpdatedOn = insuranceUpdatedOn;
	}

	public String getInsuranceUpdatedBy() {
		return insuranceUpdatedBy;
	}

	public void setInsuranceUpdatedBy( String insuranceUpdatedBy ) {
		this.insuranceUpdatedBy = insuranceUpdatedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public ApplicationStatus getNominationStatus() {
		return nominationStatus;
	}

	public void setNominationStatus(ApplicationStatus nominationStatus) {
		this.nominationStatus = nominationStatus;
	}

	public List<Nominee> getNominees() {
		return nominees;
	}

	public void setNominees(List<Nominee> nominees) {
		this.nominees = nominees;
	}

	public Date getPayoutDate() {
		return payoutDate;
	}

	public void setPayoutDate(Date payoutDate) {
		this.payoutDate = payoutDate;
	}

	public double getAmountDisbursed() {
		return amountDisbursed;
	}

	public void setAmountDisbursed(double amountDisbursed) {
		this.amountDisbursed = amountDisbursed;
	}
	
	
	public void preSave () {
		if (insuranceCreatedBy != null) {
			insuranceCreatedBy = insuranceCreatedBy.toUpperCase();
		}
		
		if (insuranceUpdatedBy != null) {
			insuranceUpdatedBy = insuranceUpdatedBy.toUpperCase();
		}
		
		if (nominees != null) {
			for (Nominee nominee : nominees) {
				nominee.preSave ();
			}
		}
		
		
	}

}
