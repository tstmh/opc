package com.stee.spfcore.vo.membership;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.membership.MembershipType;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.Phone;

public class MembershipReport {

	private String nric;

	private String name;

	private String serviceType;

	private MembershipType membershipType;

	private ActivationStatus membershipStatus;

	private String rankOrGrade;

	private String organisationOrDepartment;

	private String schemeOfService;

	private List<Email> emailContacts;

	private List<Phone> phoneContacts;

	private ApplicationStatus nominationStatus;

	private Date insuranceCreatedDate;

	private Date insuranceUpdatedDate;

	private Date insuranceSubmissionDate;

	private String membershipModifiedBy;

	private Date effectiveDate;

	private Date expiryDate;

	private boolean hasInsuranceCoverage;

	private Date effectiveDateOfCoverage;
	
	private Date dateOfCessation;
	
	private ApplicationStatus withdrawalStatus;
	
	private Boolean isSPRAMember;
	
	private ContactMode preferredContactMode;

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public MembershipType getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(MembershipType membershipType) {
		this.membershipType = membershipType;
	}

	public ActivationStatus getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(ActivationStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public String getRankOrGrade() {
		return rankOrGrade;
	}

	public void setRankOrGrade(String rankOrGrade) {
		this.rankOrGrade = rankOrGrade;
	}

	public String getOrganisationOrDepartment() {
		return organisationOrDepartment;
	}

	public void setOrganisationOrDepartment(String organisationOrDepartment) {
		this.organisationOrDepartment = organisationOrDepartment;
	}

	public String getSchemeOfService() {
		return schemeOfService;
	}

	public void setSchemeOfService(String schemeOfService) {
		this.schemeOfService = schemeOfService;
	}

	public List<Email> getEmailContacts() {
		return emailContacts;
	}

	public void setEmailContacts(List<Email> emailContacts) {
		this.emailContacts = emailContacts;
	}

	public List<Phone> getPhoneContacts() {
		return phoneContacts;
	}

	public void setPhoneContacts(List<Phone> phoneContacts) {
		this.phoneContacts = phoneContacts;
	}

	public ApplicationStatus getNominationStatus() {
		return nominationStatus;
	}

	public void setNominationStatus(ApplicationStatus nominationStatus) {
		this.nominationStatus = nominationStatus;
	}

	public Date getInsuranceCreatedDate() {
		return insuranceCreatedDate;
	}

	public void setInsuranceCreatedDate(Date insuranceCreatedDate) {
		this.insuranceCreatedDate = insuranceCreatedDate;
	}

	public Date getInsuranceUpdatedDate() {
		return insuranceUpdatedDate;
	}

	public void setInsuranceUpdatedDate(Date insuranceUpdatedDate) {
		this.insuranceUpdatedDate = insuranceUpdatedDate;
	}

	public Date getInsuranceSubmissionDate() {
		return insuranceSubmissionDate;
	}

	public void setInsuranceSubmissionDate(Date insuranceSubmissionDate) {
		this.insuranceSubmissionDate = insuranceSubmissionDate;
	}

	public String getMembershipModifiedBy() {
		return membershipModifiedBy;
	}

	public void setMembershipModifiedBy(String membershipModifiedBy) {
		this.membershipModifiedBy = membershipModifiedBy;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isHasInsuranceCoverage() {
		return hasInsuranceCoverage;
	}

	public void setHasInsuranceCoverage(boolean hasInsuranceCoverage) {
		this.hasInsuranceCoverage = hasInsuranceCoverage;
	}

	public Date getEffectiveDateOfCoverage() {
		return effectiveDateOfCoverage;
	}

	public void setEffectiveDateOfCoverage(Date effectiveDateOfCoverage) {
		this.effectiveDateOfCoverage = effectiveDateOfCoverage;
	}

    public Date getDateOfCessation() {
        return dateOfCessation;
    }

    public void setDateOfCessation(Date dateOfCessation) {
        this.dateOfCessation = dateOfCessation;
    }

    public ApplicationStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(ApplicationStatus withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public Boolean getIsSPRAMember() {
        return isSPRAMember;
    }

    public void setIsSPRAMember(Boolean isSPRAMember) {
        this.isSPRAMember = isSPRAMember;
    }

    public ContactMode getPreferredContactMode() {
        return preferredContactMode;
    }

    public void setPreferredContactMode(ContactMode preferredContactMode) {
        this.preferredContactMode = preferredContactMode;
    }
	
}
