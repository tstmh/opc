package com.stee.spfcore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Based class for all model object 
 * @author tbs
 */
@MappedSuperclass
public abstract class AuditBase implements Serializable {
	
	private static final long serialVersionUID = -5726540456795530513L;

	@Column(name="\"UPDATED_BY\"", length=256)
	protected String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"UPDATED_DATE\"")
	protected Date updatedOn;

	
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
