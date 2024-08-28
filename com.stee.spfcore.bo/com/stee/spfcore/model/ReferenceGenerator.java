package com.stee.spfcore.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.stee.spfcore.model.internal.ApplicationType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"REFERENCE_GENERATOR\"", schema = "\"SPFCORE\"")
@XStreamAlias("ReferenceGenerator")
public class ReferenceGenerator {
	
	@Id
	@Column(name = "\"APPLICATION_TYPE\"", length = 40)
	private String applicationType;

	@Column(name = "\"REFERENCE_NUMBER\"")
	private Integer referenceNumber;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"LAST_UPDATE\"")
	private Date lastUpdate;

	public ReferenceGenerator() {
		super();
	}

	public ReferenceGenerator(ApplicationType applicationType,
			Integer referenceNumber, Date lastUpdate) {
		super();
		this.applicationType = applicationType.toString();
		this.referenceNumber = referenceNumber;
		this.lastUpdate = lastUpdate;
	}

	public ApplicationType getApplicationType() {
		return ApplicationType.get(applicationType);
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType.toString();
	}

	public Integer getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(Integer referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationType == null) ? 0 : applicationType.hashCode());
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
		ReferenceGenerator other = (ReferenceGenerator) obj;
		if (applicationType == null) {
			if (other.applicationType != null)
				return false;
		} else if (!applicationType.equals(other.applicationType))
			return false;
		return true;
	}

}
