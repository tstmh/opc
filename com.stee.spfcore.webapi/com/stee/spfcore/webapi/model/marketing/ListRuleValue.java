package com.stee.spfcore.webapi.model.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_LIST_RULES_VALUE\"", schema = "\"SPFCORE\"")
@XStreamAlias("ListRuleValue")
@Audited
@SequenceDef (name="MarketingListRuleValueId_SEQ", schema = "SPFCORE", internetFormat="FEB-LRV-%d", intranetFormat="BPM-LRV-%d")
public class ListRuleValue {

	@Id
	@GeneratedId
	@Column(name="\"ID\"")
	private String id;
	
	@Column(name="\"LIST_VALUE\"", length=255)
	private String value;

	@Transient
	private String displayValue;
	
	public ListRuleValue() {
		super();
	}

	public ListRuleValue(String id, String vale) {
		super();
		this.id = id;
		this.value = vale;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String vale) {
		this.value = vale;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
}



