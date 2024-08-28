package com.stee.spfcore.vo.membership;

import java.util.Date;

import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.membership.MembershipType;

/**
 * Criteria Object to set the search parameters.
 * @author 
 *
 */
public class MembershipCriteria {
	
	private String name;
	
	private String nric;
	
	private String orgOrDeptCode;
	
	private Date searchEffStartDate;
	
	private Date searchEffEndDate;
	
	private Date searchExpStartDate;
	
	private Date searchExpEndDate;
	
	private Boolean searchExpDateIncludeNull;
	
	private ApplicationStatus nominationStatus;
	
	private ApplicationStatus withdrawalStatus;
	
	private MembershipType membershipType;
	
	private ActivationStatus membershipStatus;
	
	private Date searchSubmissionStartDate;

	private Date searchSubmissionEndDate;
	
	private Date searchCessationStartDate;
	
	private Date searchCessationEndDate;
	

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getOrgOrDeptCode() {
		return orgOrDeptCode;
	}

	public void setOrgOrDeptCode(String orgOrDeptCode) {
		this.orgOrDeptCode = orgOrDeptCode;
	}

	public Date getSearchEffStartDate() {
		return searchEffStartDate;
	}

	public void setSearchEffStartDate(Date searchEffStartDate) {
		this.searchEffStartDate = searchEffStartDate;
	}

	public Date getSearchEffEndDate() {
		return searchEffEndDate;
	}

	public void setSearchEffEndDate(Date searchEffEndDate) {
		this.searchEffEndDate = searchEffEndDate;
	}

	public Date getSearchExpStartDate() {
		return searchExpStartDate;
	}

	public void setSearchExpStartDate(Date searchExpStartDate) {
		this.searchExpStartDate = searchExpStartDate;
	}

	public Date getSearchExpEndDate() {
		return searchExpEndDate;
	}

	public void setSearchExpEndDate(Date searchExpEndDate) {
		this.searchExpEndDate = searchExpEndDate;
	}
	
    public Boolean getSearchExpDateIncludeNull() {
        return searchExpDateIncludeNull;
    }

    public void setSearchExpDateIncludeNull( Boolean searchExpDateIncludeNull ) {
        this.searchExpDateIncludeNull = searchExpDateIncludeNull;
    }

	public ApplicationStatus getNominationStatus() {
		return nominationStatus;
	}

	public void setNominationStatus(ApplicationStatus nominationStatus) {
		this.nominationStatus = nominationStatus;
	}

	public ApplicationStatus getWithdrawalStatus() {
		return withdrawalStatus;
	}

	public void setWithdrawalStatus(ApplicationStatus withdrawalStatus) {
		this.withdrawalStatus = withdrawalStatus;
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

	public Date getSearchSubmissionStartDate() {
		return searchSubmissionStartDate;
	}

	public void setSearchSubmissionStartDate( Date searchSubmissionStartDate ) {
		this.searchSubmissionStartDate = searchSubmissionStartDate;
	}

	public Date getSearchSubmissionEndDate() {
		return searchSubmissionEndDate;
	}

	public void setSearchSubmissionEndDate( Date searchSubmissionEndDate ) {
		this.searchSubmissionEndDate = searchSubmissionEndDate;
	}
	
	public Date getSearchCessationStartDate() {
		return searchCessationStartDate;
	}

	public void setSearchCessationStartDate(Date searchCessationStartDate) {
		this.searchCessationStartDate = searchCessationStartDate;
	}

	public Date getSearchCessationEndDate() {
		return searchCessationEndDate;
	}

	public void setSearchCessationEndDate(Date searchCessationEndDate) {
		this.searchCessationEndDate = searchCessationEndDate;
	}

	// Auto-generated Code
    @Override
    public String toString() {
        return "MembershipCriteria [name=" + name + ", nric=" + nric
                + ", orgOrDeptCode=" + orgOrDeptCode + ", searchEffStartDate="
                + searchEffStartDate + ", searchEffEndDate=" + searchEffEndDate
                + ", searchExpStartDate=" + searchExpStartDate
                + ", searchExpEndDate=" + searchExpEndDate
                + ", searchExpDateIncludeNull=" + searchExpDateIncludeNull
                + ", nominationStatus=" + nominationStatus
                + ", withdrawalStatus=" + withdrawalStatus
                + ", membershipType=" + membershipType + ", membershipStatus="
                + membershipStatus + ", searchSubmissionStartDate="
                + searchSubmissionStartDate + ", searchSubmissionEndDate="
                + searchSubmissionEndDate + ", searchCessationStartDate="
                + searchCessationStartDate + ", searchCessationEndDate="
                + searchCessationEndDate + "]";
    }
    
}
