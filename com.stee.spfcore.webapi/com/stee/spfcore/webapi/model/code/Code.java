package com.stee.spfcore.webapi.model.code;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.AuditBase;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name="\"CODES\"", schema="\"SPFCORE\"")
@XStreamAlias("Code")
@Audited
public class Code extends AuditBase {

	private static final long serialVersionUID = -8171310039816198143L;

	@Id
	@Column(name="\"ID\"", length=50)
	private String id;
	
	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="\"CODE_TYPE\"", length=50)
	private CodeType type;
	
	@Column(name="\"DESCRIPTION\"", length=256)
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name="\"EFFECTIVE_DATE\"")
	private Date effectiveDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="\"OBSOLETE_DATE\"")
	private Date obsoleteDate;
	
	@Column(name="\"REMARKS\"", length=2000)
	private String remarks;
	
	@Column(name="\"ENABLED\"")
	private boolean enabled = false;
	
	@Column(name="\"DISPLAY_ORDER\"")
	private int order;
	
	
	/**
	 * No-argument constructor required by Hibernate, 
	 */
	public Code () {
		super ();
	}
	
	public Code(String id, CodeType type) {
		super();
		this.id = id;
		this.type = type;
	}

	public Code(String id, CodeType type, String description,
			Date effectiveDate, Date obsoleteDate, String remarks,
			boolean enabled, int order, String updatedBy, Date updatedOn) {
		super();
		this.id = id;
		this.type = type;
		this.description = description;
		this.effectiveDate = effectiveDate;
		this.obsoleteDate = obsoleteDate;
		this.remarks = remarks;
		this.enabled = enabled;
		this.order = order;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CodeType getType() {
		return type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Code other = (Code) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Code [id=" + id + ", type=" + type + ", description=" + description + ", effectiveDate=" + effectiveDate
				+ ", obsoleteDate=" + obsoleteDate + ", remarks=" + remarks + ", enabled=" + enabled + ", updatedBy="
				+ updatedBy + ", updatedOn=" + updatedOn + "]";
	}
	
}
