package com.stee.spfcore.vo.marketing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.utils.DateUtils;

public class MemberGroupSummaryList implements Serializable {

	private static final long serialVersionUID = 747969961730589210L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"NAME\"", length = 256)
	private String name;

	@Column(name = "\"DESCRIPTION\"", length = 1000)
	private String description;

	@Column(name = "\"UPDATED_BY_NAME\"", length = 255)
	private String updatedByName;
	
	@Column(name = "\"IS_TEMPLATE\"")
	private boolean template;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"EFFECTIVE_DATE\"")
	private Date effectiveDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"OBSOLETE_DATE\"")
	private Date obsoleteDate;

	@Transient
	private boolean enabled;

	public MemberGroupSummaryList() {
		super();
	}

	public MemberGroupSummaryList(MemberGroup memberGroup) {
		super();
		this.id = memberGroup.getId();
		this.name = memberGroup.getName();
		this.description = memberGroup.getDescription();
		this.template = memberGroup.isTemplate();
		this.effectiveDate = memberGroup.getEffectiveDate();
		this.obsoleteDate = memberGroup.getObsoleteDate();
		this.updatedByName = memberGroup.getUpdatedByName();
		this.enabled = memberGroup.isEnabled();
	}
	
	public MemberGroupSummaryList(String id, String name, String description, boolean template, Date effectiveDate,
			Date obsoleteDate, String updatedByName) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.template = template;
		this.effectiveDate = effectiveDate;
		this.obsoleteDate = obsoleteDate;
		this.updatedByName = updatedByName;
		onLoad();

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
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
	
	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	@PostLoad
	protected void onLoad() {

		// Default is false
		enabled = false;

		Date today = new Date();

		// Only enabled if the effective date is not after today (include today)
		// and obsolete date is null or is not before today
		if ((effectiveDate != null && !DateUtils.isAfterDay(effectiveDate, today)) && (obsoleteDate == null || DateUtils.isAfterDay(obsoleteDate, today))){
			enabled = true;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MemberGroupSummaryList other = (MemberGroupSummaryList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	

}
