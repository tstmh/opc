package com.stee.spfcore.webapi.model.membership;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.ActivationStatus;
import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.stee.spfcore.webapi.model.AuditBase;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MEMBERSHIP_DETAILS\"", schema = "\"SPFCORE\"")
@XStreamAlias("Membership")
@Audited
public class Membership extends AuditBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6119404388802579393L;

	@Id
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"MEMBERSHIP_TYPE\"", length = 30)
	private MembershipType membershipType;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"MEMBERSHIP_STATUS\"", length = 10)
	private ActivationStatus membershipStatus;

	@Column(name = "\"SPRA_MEMBER\"")
	private boolean isSPRAMember;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"EFFECTIVE_DATE\"")
	private Date effectiveDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"EXPIRY_DATE\"")
	private Date expiryDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"NS_CARD_ISSUED_DATE\"")
	private Date dateOfNSCardIssued;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"MEMBERSHIP_CARD_ISSUED_DATE\"")
	private Date dateOfMembershipCardIssued;

	@Column(name = "\"HAS_INSURANCE_COVERAGE\"")
	private boolean hasInsuranceCoverage;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"COVERAGE_EFFECTIVE_DATE\"")
	private Date effectiveDateOfCoverage;

	@Column(name = "\"REMARKS\"", length = 2000)
	private String remarks;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"MEMBERSHIP_NRIC\"")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<PaymentRecord> paymentRecords;

	@Embedded
	private Insurance insurance;

	// Membership Withdrawal
	@Enumerated(EnumType.STRING)
	@Column(name = "\"WITHDRAWAL_STATUS\"", length = 10)
	private ApplicationStatus withdrawalStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"WITHDRAWAL_REQUEST_DATE\"")
	private Date dateOfWithdrawalRequest;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"TERMINATION_REQUEST_DATE\"")
	private Date requestedDateOfTermination;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"CESSATION_DATE\"")
	private Date dateOfCessation;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"CESSATION_DATE_UPDATED_ON\"")
	private Date cessationDateUpdatedOn;

	@Column(name = "\"CESSATION_DATE_UPDATED_BY\"", length = 255)
	private String cessationDateUpdatedBy;

	@Column(name = "\"FINANCE_OFFICER_REMARKS\"", length = 2000)
	private String financeOfficerRemarks;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"WELFARE_AD_UPDATED_ON\"")
	private Date welfareADUpdatedOn;

	@Column(name = "\"WELFARE_AD_NAME\"", length = 255)
	private String welfareADName;

	@Column(name = "\"WELFARE_AD_REMARKS\"", length = 2000)
	private String welfareADRemarks;

	@Column(name = "\"CAN_APPLY\"")
	private boolean canApplyMembership;

	@Column(name = "\"CAN_WITHDRAW\"")
	private boolean canWithdrawMembership;

	public Membership() {
		super();
	}

	public Membership( String nric, MembershipType membershipType,
			ActivationStatus membershipStatus, boolean isSPRAMember,
			Date effectiveDate, Date expiryDate, Date dateOfNSCardIssued,
			Date dateOfMembershipCardIssued, boolean hasInsuranceCoverage,
			Date effectiveDateOfCoverage, String remarks, Insurance insurance,
			ApplicationStatus withdrawalStatus, Date dateOfWithdrawalRequest,
			Date requestedDateOfTermination, Date dateOfCessation,
			Date cessationDateUpdatedOn, String cessationDateUpdatedBy,
			String financeOfficerRemarks, Date welfareADUpdatedOn,
			String welfareADName, String welfareADRemarks,
			boolean canApplyMembership, boolean canWithdrawMembership ) {
		super();
		this.nric = nric;
		this.membershipType = membershipType;
		this.membershipStatus = membershipStatus;
		this.isSPRAMember = isSPRAMember;
		this.effectiveDate = effectiveDate;
		this.expiryDate = expiryDate;
		this.dateOfNSCardIssued = dateOfNSCardIssued;
		this.dateOfMembershipCardIssued = dateOfMembershipCardIssued;
		this.hasInsuranceCoverage = hasInsuranceCoverage;
		this.effectiveDateOfCoverage = effectiveDateOfCoverage;
		this.remarks = remarks;
		this.insurance = insurance;
		this.withdrawalStatus = withdrawalStatus;
		this.dateOfWithdrawalRequest = dateOfWithdrawalRequest;
		this.requestedDateOfTermination = requestedDateOfTermination;
		this.dateOfCessation = dateOfCessation;
		this.cessationDateUpdatedOn = cessationDateUpdatedOn;
		this.cessationDateUpdatedBy = cessationDateUpdatedBy;
		this.financeOfficerRemarks = financeOfficerRemarks;
		this.welfareADUpdatedOn = welfareADUpdatedOn;
		this.welfareADName = welfareADName;
		this.welfareADRemarks = welfareADRemarks;
		this.canApplyMembership = canApplyMembership;
		this.canWithdrawMembership = canWithdrawMembership;
	}

	public String getNric() {
		return nric;
	}

	public void setNric( String nric ) {
		this.nric = nric;
	}

	public MembershipType getMembershipType() {
		return membershipType;
	}

	public void setMembershipType( MembershipType membershipType ) {
		this.membershipType = membershipType;
	}

	public ActivationStatus getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus( ActivationStatus membershipStatus ) {
		this.membershipStatus = membershipStatus;
	}

	public boolean isSPRAMember() {
		return isSPRAMember;
	}

	public void setIsSPRAMember( boolean isSPRAMember ) {
		this.isSPRAMember = isSPRAMember;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate( Date effectiveDate ) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate( Date expiryDate ) {
		this.expiryDate = expiryDate;
	}

	public Date getDateOfNSCardIssued() {
		return dateOfNSCardIssued;
	}

	public void setDateOfNSCardIssued( Date dateOfNSCardIssued ) {
		this.dateOfNSCardIssued = dateOfNSCardIssued;
	}

	public Date getDateOfMembershipCardIssued() {
		return dateOfMembershipCardIssued;
	}

	public void setDateOfMembershipCardIssued( Date dateOfMembershipCardIssued ) {
		this.dateOfMembershipCardIssued = dateOfMembershipCardIssued;
	}

	public boolean hasInsuranceCoverage() {
		return hasInsuranceCoverage;
	}

	public void setHasInsuranceCoverage( boolean hasInsuranceCoverage ) {
		this.hasInsuranceCoverage = hasInsuranceCoverage;
	}

	public Date getEffectiveDateOfCoverage() {
		return effectiveDateOfCoverage;
	}

	public void setEffectiveDateOfCoverage( Date effectiveDateOfCoverage ) {
		this.effectiveDateOfCoverage = effectiveDateOfCoverage;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks( String remarks ) {
		this.remarks = remarks;
	}

	public List<PaymentRecord> getPaymentRecords() {
		return paymentRecords;
	}

	public void setPaymentRecords( List<PaymentRecord> paymentRecords ) {
		this.paymentRecords = paymentRecords;
	}

	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance( Insurance insurance ) {
		this.insurance = insurance;
	}

	public ApplicationStatus getWithdrawalStatus() {
		return withdrawalStatus;
	}

	public void setWithdrawalStatus( ApplicationStatus withdrawalStatus ) {
		this.withdrawalStatus = withdrawalStatus;
	}

	public Date getDateOfWithdrawalRequest() {
		return dateOfWithdrawalRequest;
	}

	public void setDateOfWithdrawalRequest( Date dateOfWithdrawalRequest ) {
		this.dateOfWithdrawalRequest = dateOfWithdrawalRequest;
	}

	public Date getRequestedDateOfTermination() {
		return requestedDateOfTermination;
	}

	public void setRequestedDateOfTermination( Date requestedDateOfTermination ) {
		this.requestedDateOfTermination = requestedDateOfTermination;
	}

	public Date getDateOfCessation() {
		return dateOfCessation;
	}

	public void setDateOfCessation( Date dateOfCessation ) {
		this.dateOfCessation = dateOfCessation;
	}

	public Date getCessationDateUpdatedOn() {
		return cessationDateUpdatedOn;
	}

	public void setCessationDateUpdatedOn( Date cessationDateUpdatedOn ) {
		this.cessationDateUpdatedOn = cessationDateUpdatedOn;
	}

	public String getCessationDateUpdatedBy() {
		return cessationDateUpdatedBy;
	}

	public void setCessationDateUpdatedBy( String cessationDateUpdatedBy ) {
		this.cessationDateUpdatedBy = cessationDateUpdatedBy;
	}

	public String getFinanceOfficerRemarks() {
		return financeOfficerRemarks;
	}

	public void setFinanceOfficerRemarks( String financeOfficerRemarks ) {
		this.financeOfficerRemarks = financeOfficerRemarks;
	}

	public Date getWelfareADUpdatedOn() {
		return welfareADUpdatedOn;
	}

	public void setWelfareADUpdatedOn( Date welfareADUpdatedOn ) {
		this.welfareADUpdatedOn = welfareADUpdatedOn;
	}

	public String getWelfareADName() {
		return welfareADName;
	}

	public void setWelfareADName( String welfareADName ) {
		this.welfareADName = welfareADName;
	}

	public String getWelfareADRemarks() {
		return welfareADRemarks;
	}

	public void setWelfareADRemarks( String welfareADRemarks ) {
		this.welfareADRemarks = welfareADRemarks;
	}

	public boolean isHasInsuranceCoverage() {
		return hasInsuranceCoverage;
	}

	public void setSPRAMember( boolean isSPRAMember ) {
		this.isSPRAMember = isSPRAMember;
	}

	public boolean isCanApplyMembership() {
		return canApplyMembership;
	}

	public void setCanApplyMembership( boolean canApplyMembership ) {
		this.canApplyMembership = canApplyMembership;
	}

	public boolean isCanWithdrawMembership() {
		return canWithdrawMembership;
	}

	public void setCanWithdrawMembership( boolean canWithdrawMembership ) {
		this.canWithdrawMembership = canWithdrawMembership;
	}
	
	public void preSave () {
		
		if (insurance !=  null) {
			insurance.preSave();
		}
		
		if (nric != null) {
			nric = nric.toUpperCase();
		}
		
		if (welfareADName != null) {
			welfareADName = welfareADName.toUpperCase();
		}
		
		if (paymentRecords != null) {
			for (PaymentRecord paymentRecord : paymentRecords) {
				paymentRecord.preSave();
			}
		}
		
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( nric == null ) ? 0 : nric.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		Membership other = (Membership) obj;
		if ( nric == null ) {
			if ( other.nric != null )
				return false;
		} else if ( !nric.equals( other.nric ) )
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Membership [nric=" + nric + ", membershipType="
                + membershipType + ", membershipStatus=" + membershipStatus
                + ", isSPRAMember=" + isSPRAMember + ", effectiveDate="
                + effectiveDate + ", expiryDate=" + expiryDate
                + ", dateOfNSCardIssued=" + dateOfNSCardIssued
                + ", dateOfMembershipCardIssued=" + dateOfMembershipCardIssued
                + ", hasInsuranceCoverage=" + hasInsuranceCoverage
                + ", effectiveDateOfCoverage=" + effectiveDateOfCoverage
                + ", remarks=" + remarks + ", paymentRecords=" + paymentRecords
                + ", insurance=" + insurance + ", withdrawalStatus="
                + withdrawalStatus + ", dateOfWithdrawalRequest="
                + dateOfWithdrawalRequest + ", requestedDateOfTermination="
                + requestedDateOfTermination + ", dateOfCessation="
                + dateOfCessation + ", cessationDateUpdatedOn="
                + cessationDateUpdatedOn + ", cessationDateUpdatedBy="
                + cessationDateUpdatedBy + ", financeOfficerRemarks="
                + financeOfficerRemarks + ", welfareADUpdatedOn="
                + welfareADUpdatedOn + ", welfareADName=" + welfareADName
                + ", welfareADRemarks=" + welfareADRemarks
                + ", canApplyMembership=" + canApplyMembership
                + ", canWithdrawMembership=" + canWithdrawMembership + "]";
    }
	
}
