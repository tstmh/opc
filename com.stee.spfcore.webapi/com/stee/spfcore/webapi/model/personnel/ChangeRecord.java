package com.stee.spfcore.webapi.model.personnel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Track the date that user change his/her personal detail.
 * Needed for the filter to force user to change detail
 * during first time login and also after configured period of
 * time.
 */
@Entity
@Table(name="\"USER_CHANGE_DETAIL_RECORD\"", schema="\"SPFCORE\"")
public class ChangeRecord {

	@Id
	@Column(name="\"NRIC\"", length=10)
	private String nric;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"UPDATED_DATE\"")
	private Date updatedOn;

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	public void preSave () {
		this.nric = nric.toUpperCase();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nric == null) ? 0 : nric.hashCode());
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
		ChangeRecord other = (ChangeRecord) obj;
		if (nric == null) {
			if (other.nric != null)
				return false;
		} else if (!nric.equals(other.nric))
			return false;
		return true;
	}
	
}
