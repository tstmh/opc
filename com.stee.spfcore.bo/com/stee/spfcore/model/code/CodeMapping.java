package com.stee.spfcore.model.code;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.AuditBase;

@Entity
@Table(name="\"CODE_MAPPING\"", schema="\"SPFCORE\"")
@Audited
public class CodeMapping extends AuditBase {

	private static final long serialVersionUID = 3287367964541859776L;
	
	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="\"EXT_SYSTEM_TYPE\"", length=5)
	private ExternalSystemType systemType;
	
	@Id
	@Enumerated(EnumType.STRING)
	@Column(name="\"CODE_TYPE\"", length=50)
	private CodeType codeType;
	
	@Id
	@Column(name="\"EXTERNAL_ID\"", length=256)
	private String externalId;
	
	@Column(name="\"INTERNAL_ID\"", length=50)
	private String internalId;
	
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

	@Formula("(select c.DESCRIPTION from SPFCORE.CODES c where c.CODE_TYPE = CODE_TYPE and c.ID = INTERNAL_ID)")
	@NotAudited
	private String description;
	
	public CodeMapping() {
		super();
	}

	public CodeMapping(ExternalSystemType systemType, CodeType codeType,
			String externalId, String internalId, Date effectiveDate,
			Date obsoleteDate, String remarks, boolean enabled,
			String description, String updatedBy, Date updatedOn) {
		super();
		this.systemType = systemType;
		this.codeType = codeType;
		this.externalId = externalId;
		this.internalId = internalId;
		this.effectiveDate = effectiveDate;
		this.obsoleteDate = obsoleteDate;
		this.remarks = remarks;
		this.enabled = enabled;
		this.description = description;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	public ExternalSystemType getSystemType() {
		return systemType;
	}

	public void setSystemType(ExternalSystemType systemType) {
		this.systemType = systemType;
	}

	public CodeType getCodeType() {
		return codeType;
	}

	public void setCodeType(CodeType codeType) {
		this.codeType = codeType;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getDescription() {
		return description;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate( Date effectiveDate ) {
		this.effectiveDate = effectiveDate;
	}

	public Date getObsoleteDate() {
		return obsoleteDate;
	}

	public void setObsoleteDate( Date obsoleteDate ) {
		this.obsoleteDate = obsoleteDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks( String remarks ) {
		this.remarks = remarks;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeType == null) ? 0 : codeType.hashCode());
		result = prime * result + ((externalId == null) ? 0 : externalId.hashCode());
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
		CodeMapping other = (CodeMapping) obj;
		if (codeType != other.codeType)
			return false;
		if (externalId == null) {
			if (other.externalId != null)
				return false;
		} else if (!externalId.equals(other.externalId))
			return false;
		return (systemType == other.systemType);
	}
	
}
