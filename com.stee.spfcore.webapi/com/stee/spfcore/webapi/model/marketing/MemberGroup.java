package com.stee.spfcore.webapi.model.marketing;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.transform.ResultTransformer;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.stee.spfcore.webapi.utils.DateUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_MEMBER_GROUPS\"", schema = "\"SPFCORE\"")
@XStreamAlias("MemberGroup")
@Audited
@SequenceDef (name="MarketingMemberGroupId_SEQ", schema = "SPFCORE", internetFormat="FEB-MG-%d", intranetFormat="BPM-MG-%d")
public class MemberGroup implements Serializable {

	private static final long serialVersionUID = 747969961730589210L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"NAME\"", length = 256)
	private String name;

	@Column(name = "\"DESCRIPTION\"", length = 1000)
	private String description;

	@Column(name = "\"MODULE\"", length = 100)
	private String module;

	@Column(name = "\"IS_TEMPLATE\"")
	private boolean template;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"EFFECTIVE_DATE\"")
	private Date effectiveDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"OBSOLETE_DATE\"")
	private Date obsoleteDate;

	@Column(name = "\"UPDATED_BY\"", length = 30)
	private String updatedBy;

	@Column(name = "\"UPDATED_BY_NAME\"", length = 255)
	private String updatedByName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedOn;

	@Column(name = "\"REMARK\"", length = 100)
	private String remark;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"MEMBER_GROUP_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<RuleSet> ruleSets;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"ORDER_BY_FIELD_CATEGORY\"", length = 15)
	private Category orderByCategory;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"ORDER_BY_FIELD\"", length = 25)
	private Field orderByField;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"SORT_ORDER\"", length = 15)
	private SortOrder order;
	
	@Transient
	private boolean enabled;
	
	public MemberGroup() {
		super();
	}

	public MemberGroup(String id, String name, String description, String module, boolean template, Date effectiveDate,
			Date obsoleteDate, String updatedBy, String updatedByName, Date updatedOn, String remark, List<RuleSet> ruleSets, boolean enabled,
			Category orderByCategory, Field orderByField, SortOrder order) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.module = module;
		this.template = template;
		this.effectiveDate = effectiveDate;
		this.obsoleteDate = obsoleteDate;
		this.updatedBy = updatedBy;
		this.updatedByName = updatedByName;
		this.updatedOn = updatedOn;
		this.remark = remark;
		this.ruleSets = ruleSets;
		this.enabled = enabled;
		this.orderByCategory = orderByCategory;
		this.orderByField = orderByField;
		this.order = order;
	}

	@PostLoad
	protected void onLoad() {

		// Default is false
		enabled = false;

		Date today = new Date();

		// Only enabled if the effective date is not after today (include today)
		// and obsolete date is null or is not before today
		if (effectiveDate != null && !DateUtils.isAfterDay(effectiveDate, today)) {
			if (obsoleteDate == null || DateUtils.isAfterDay(obsoleteDate, today)) {
				enabled = true;
			}
		}
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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RuleSet> getRuleSets() {
		return ruleSets;
	}

	public void setRuleSets(List<RuleSet> ruleSets) {
		this.ruleSets = ruleSets;
	}

	public Category getOrderByCategory() {
		return orderByCategory;
	}

	public void setOrderByCategory(Category orderByCategory) {
		this.orderByCategory = orderByCategory;
	}

	public Field getOrderByField() {
		return orderByField;
	}

	public void setOrderByField(Field orderByField) {
		this.orderByField = orderByField;
	}

	public SortOrder getOrder() {
		return order;
	}

	public void setOrder(SortOrder order) {
		this.order = order;
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
		MemberGroup other = (MemberGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
