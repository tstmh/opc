package com.stee.spfcore.model.marketing;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_RULES\"", schema = "\"SPFCORE\"")
@Inheritance(strategy = InheritanceType.JOINED)
@XStreamAlias("Rule")
@Audited
@SequenceDef (name="MarketingRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-R-%d", intranetFormat="BPM-R-%d")
public class Rule implements Serializable {

	private static final long serialVersionUID = -1700681023630411997L;

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	protected String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"FIELD_CATEGORY\"", length = 15)
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"FIELD\"", length = 25)
	private Field field;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"RULE_TYPE\"", length = 15)
	protected RuleType type;

	@Enumerated(EnumType.STRING)
	@Column(name = "\"OPERATOR\"", length = 30)
	private Operator operator;

	public Rule() {
		super();
	}

	public Rule(String id, Category category, Field field, Operator operator) {
		super();
		this.id = id;
		this.category = category;
		this.field = field;
		this.operator = operator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public RuleType getType() {
		return type;
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
		Rule other = (Rule) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
