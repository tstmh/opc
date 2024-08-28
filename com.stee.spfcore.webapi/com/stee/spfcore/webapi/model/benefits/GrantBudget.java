package com.stee.spfcore.webapi.model.benefits;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"APPLICATION_GRANT_BUDGET\"", schema = "\"SPFCORE\"")
@XStreamAlias("GrantBudget")
@Audited
public class GrantBudget {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	@XStreamOmitField
	private long id;
	
	@Column(name = "\"GRANT_TYPE\"", length = 50)
	private String grantType;
	
	@Column(name = "\"GRANT_SUB_TYPE\"", length = 50)
	private String grantSubType;
	
	@Column(name = "\"GRANT_AMOUNT\"")
	private Double grantAmount;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"EFFECTIVE_DATE\"")
	private Date effectiveDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"OBSOLETE_DATE\"")
	private Date obsoleteDate;
	
	@Column(name = "\"UPDATED_BY\"", length = 50)
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"", length = 50)
	private Date updatedOn;
	
	public GrantBudget(){
		super();
	}
	
	public GrantBudget(String grantType, String grantSubType,
			Double grantAmount, Date effectiveDate, Date obsoleteDate,
			String updatedBy, Date updatedOn) {
		super();
		this.grantType = grantType;
		this.grantSubType = grantSubType;
		this.grantAmount = grantAmount;
		this.effectiveDate = effectiveDate;
		this.obsoleteDate = obsoleteDate;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getGrantSubType() {
		return grantSubType;
	}

	public void setGrantSubType(String grantSubType) {
		this.grantSubType = grantSubType;
	}

	public Double getGrantAmount() {
		return grantAmount;
	}

	public void setGrantAmount(Double grantAmount) {
		this.grantAmount = grantAmount;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getObsoleteDate() {
		return obsoleteDate;
	}

	public void setObsoleteDate(Date obsoleteDate) {
		this.obsoleteDate = obsoleteDate;
	}

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

	public void preSave () {
		if (updatedBy != null) {
			updatedBy = updatedBy.toUpperCase();
		}
	}
}
