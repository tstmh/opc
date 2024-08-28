package com.stee.spfcore.model.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.stee.spfcore.model.ActivationStatus;

@Entity
@Table(name="\"MEMBERSHIP_STATUS_CHANGE_RECORD\"", schema="\"SPFCORE\"")
public class StatusChangeRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	private long id;
	
	@Column(name = "\"MEMBERSHIP_NRIC\"", length = 10)
	private String nric;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"UPDATED_DATE\"")
	private Date updatedOn;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"MEMBERSHIP_STATUS\"", length = 10)
	private ActivationStatus membershipStatus;

	public StatusChangeRecord() {
		super();
	}

	public StatusChangeRecord(String nric, Date updatedOn, ActivationStatus membershipStatus) {
		super();
		this.nric = nric;
		this.updatedOn = updatedOn;
		this.membershipStatus = membershipStatus;
	}

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

	public ActivationStatus getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(ActivationStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((membershipStatus == null) ? 0 : membershipStatus.hashCode());
		result = prime * result + ((nric == null) ? 0 : nric.hashCode());
		result = prime * result + ((updatedOn == null) ? 0 : updatedOn.hashCode());
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
		StatusChangeRecord other = (StatusChangeRecord) obj;
		if (id != other.id)
			return false;
		if (membershipStatus != other.membershipStatus)
			return false;
		if (nric == null) {
			if (other.nric != null)
				return false;
		} else if (!nric.equals(other.nric))
			return false;
		if (updatedOn == null) {
			if (other.updatedOn != null)
				return false;
		} else if (!updatedOn.equals(other.updatedOn))
			return false;
		return true;
	}
	
}
