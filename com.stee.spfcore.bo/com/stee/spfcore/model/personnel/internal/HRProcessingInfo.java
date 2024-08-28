package com.stee.spfcore.model.personnel.internal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.stee.spfcore.model.code.ExternalSystemType;


/**
 * This model is temporary and will be remove once
 * the Personnel support multiple Employments record.
 * 
 * This model is needed to keep track of current personnel 
 * data is from which external system. 
 *   
 */
@Entity
@Table(name = "\"HR_PROCESSING_INFO\"", schema = "\"SPFCORE\"")
public class HRProcessingInfo implements Serializable {

	private static final long serialVersionUID = 717316349240524048L;

	@Id
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;

	@Enumerated(EnumType.STRING)
	@Column(name="\"EXT_SYSTEM_TYPE\"", length=5)
	private ExternalSystemType systemType;

	
	public HRProcessingInfo() {
		super();
	}

	public HRProcessingInfo(String nric, ExternalSystemType systemType) {
		super();
		this.nric = nric;
		this.systemType = systemType;
	}

	public String getNric() {
		return nric;
	}

	public ExternalSystemType getSystemType() {
		return systemType;
	}

	public void setSystemType(ExternalSystemType systemType) {
		this.systemType = systemType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nric == null) ? 0 : nric.hashCode());
		result = prime * result + ((systemType == null) ? 0 : systemType.hashCode());
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
		HRProcessingInfo other = (HRProcessingInfo) obj;
		if (nric == null) {
			if (other.nric != null)
				return false;
		} else if (!nric.equals(other.nric))
			return false;
		return systemType == other.systemType;
	}
}
