package com.stee.spfcore.model.blacklist;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.stee.spfcore.utils.DateUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"BLACKLISTEES\"", schema = "\"SPFCORE\"")
@XStreamAlias("Blacklistee")
@Audited
@SequenceDef (name="BlacklisteeId_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class Blacklistee implements Serializable {

	private static final long serialVersionUID = -8317249891413780012L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"NRIC\"", length = 10)
	private String nric;

	@Column(name = "\"MODULE\"", length = 100)
	private String module;

	@Formula("(select c.NAME from SPFCORE.PERSONAL_DETAILS c where c.NRIC = NRIC)")
	@NotAudited
	@XStreamOmitField
	private String name;

	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c, SPFCORE.PERSONAL_EMPLOYMENT_DETAILS e where "
			+ "c.ID = e.ORGANISATION_OR_DEPARTMENT and c.CODE_TYPE = 'UNIT_DEPARTMENT' and e.NRIC = NRIC)")
	@NotAudited
	@XStreamOmitField
	private String department;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"EFFECTIVE_DATE\"")
	private Date effectiveDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"OBSOLETE_DATE\"")
	private Date obsoleteDate;

	@Column(name = "\"UPDATED_BY\"", length = 30)
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedOn;

	@Column(name = "\"REMARK\"", length = 2000)
	private String remark;

	@Transient
	private boolean enabled;

	@PostLoad
	protected void onLoad() {

		// Default is false
		enabled = false;

		Date today = new Date();

		// Only enabled if the effective date is not after today (include today)
		// and obsolete date is null or is not before today
		if (effectiveDate != null && !DateUtils.isAfterDay(effectiveDate, today) && obsoleteDate == null || DateUtils.isAfterDay(obsoleteDate, today)) {
			enabled = true;
		}
	}

	public Blacklistee() {
		super();
	}

	public Blacklistee(String nric, String module, Date effectiveDate, Date obsoleteDate,
			String updatedBy, Date updatedOn, String remark) {
		super();
		this.nric = nric;
		this.module = module;
		this.effectiveDate = effectiveDate;
		this.obsoleteDate = obsoleteDate;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.remark = remark;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public String getDepartment() {
		return department;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
