package com.stee.spfcore.vo.benefits;

import java.util.Date;

public class BenefitsCriteria {
	
	private String referenceNumber;
	
	private String officerAction;
	
	private String officerLevel;
	
	private Date searchStartDate;
	
	private Date searchEndDate;
	
	private String nric;
	
	private String applicantName;
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getOfficerAction() {
		return officerAction;
	}

	public void setOfficerAction(String officerAction) {
		this.officerAction = officerAction;
	}

	public String getOfficerLevel() {
		return officerLevel;
	}

	public void setOfficerLevel(String officerLevel) {
		this.officerLevel = officerLevel;
	}

	public Date getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate( Date searchStartDate ) {
		this.searchStartDate = searchStartDate;
	}

	public Date getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate( Date searchEndDate ) {
		this.searchEndDate = searchEndDate;
	}

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    @Override
    public String toString() {
        return "BenefitsCriteria [referenceNumber=" + referenceNumber
                + ", officerAction=" + officerAction + ", officerLevel="
                + officerLevel + ", searchStartDate=" + searchStartDate
                + ", searchEndDate=" + searchEndDate + ", nric=" + nric
                + ", applicantName=" + applicantName + "]";
    }

}
