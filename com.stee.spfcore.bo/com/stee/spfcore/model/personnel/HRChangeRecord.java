package com.stee.spfcore.model.personnel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "\"PERSONAL_HR_CHANGE_RECORDS\"", schema = "\"SPFCORE\"")
public class HRChangeRecord implements Serializable {

	private static final long serialVersionUID = 3024653285081476319L;

	@Id
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CHANGE_DATE\"")
	private Date changeDate;

	public HRChangeRecord() {
	}
	
	public HRChangeRecord (String nric, Date changeDate) {
		this.nric = nric;
		this.changeDate = changeDate;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changeDate == null) ? 0 : changeDate.hashCode());
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
		HRChangeRecord other = (HRChangeRecord) obj;
		if (changeDate == null) {
			if (other.changeDate != null)
				return false;
		} else if (!changeDate.equals(other.changeDate))
			return false;
		if (nric == null) {
			if (other.nric != null)
				return false;
		} else if (!nric.equals(other.nric))
			return false;
		return true;
	}
	
}
