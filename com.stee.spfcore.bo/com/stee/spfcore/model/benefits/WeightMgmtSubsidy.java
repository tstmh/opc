package com.stee.spfcore.model.benefits;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.ApplicationStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"WEIGHT_MGMT_SUBSIDY\"", schema = "\"SPFCORE\"")
@XStreamAlias("WeightMgmtSubsidy")
@Audited
public class WeightMgmtSubsidy {

	@Id
	@Column(name = "\"REFERENCE_NUMBER\"", length = 50)
	private String referenceNumber;

	@Column(name = "\"MEMBER_NRIC\"", length = 10)
	@ColumnTransformer(write = "UPPER(?)")
	private String nric;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"APPLICATION_STATUS\"", length = 10)
	private ApplicationStatus applicationStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"APPLICATION_DATE\"")
	private Date dateOfApplication;
	
	@Column(name="\"HEIGHT\"")
	private Double height;
	
	@Column(name="\"WEIGHT\"")
	private Double weight;

	@Column(name="\"BMI\"")
	private Double bmi;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "\"SERVICE_PROVIDER\"")
	private HealthCareProvider healthCareProvider;

	@Column(name = "\"FEEDBACK_CONSENT\"")
	private Boolean feedbackConsent;
	
	@Column(name="\"INFORMATION_CONSENT\"")
	private Boolean informationConsent;

	
	public WeightMgmtSubsidy() {
		super();
	}

    public WeightMgmtSubsidy(String referenceNumber, String nric,
            ApplicationStatus applicationStatus, Date dateOfApplication,
            Double height, Double weight, Double bmi,
            HealthCareProvider healthCareProvider, Boolean feedbackConsent,
            Boolean informationConsent) {
        super();
        this.referenceNumber = referenceNumber;
        this.nric = nric;
        this.applicationStatus = applicationStatus;
        this.dateOfApplication = dateOfApplication;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.healthCareProvider = healthCareProvider;
        this.feedbackConsent = feedbackConsent;
        this.informationConsent = informationConsent;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Date getDateOfApplication() {
        return dateOfApplication;
    }

    public void setDateOfApplication(Date dateOfApplication) {
        this.dateOfApplication = dateOfApplication;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public HealthCareProvider getHealthCareProvider() {
        return healthCareProvider;
    }

    public void setHealthCareProvider(HealthCareProvider healthCareProvider) {
        this.healthCareProvider = healthCareProvider;
    }

    public Boolean getFeedbackConsent() {
        return feedbackConsent;
    }

    public void setFeedbackConsent(Boolean feedbackConsent) {
        this.feedbackConsent = feedbackConsent;
    }

    public Boolean getInformationConsent() {
        return informationConsent;
    }

    public void setInformationConsent(Boolean informationConsent) {
        this.informationConsent = informationConsent;
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
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
		WeightMgmtSubsidy other = (WeightMgmtSubsidy) obj;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WeightMgmtSubsidy [referenceNumber=" + referenceNumber
				+ ", nric=" + nric + ", applicationStatus=" + applicationStatus
				+ ", dateOfApplication=" + dateOfApplication + ", height="
				+ height + ", weight=" + weight + ", bmi=" + bmi
				+ ", healthCareProvider=" + healthCareProvider
				+ ", feedbackConsent=" + feedbackConsent
				+ ", informationConsent=" + informationConsent + "]";
	}
}
