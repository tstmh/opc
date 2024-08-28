package com.stee.spfcore.model.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_BOOLEAN_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("BooleanRule")
@Audited
@SequenceDef (name="MarketingBooleanRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-BR-%d", intranetFormat="BPM-BR-%d")
public class BooleanRule extends Rule {

	private static final long serialVersionUID = -1404998620671220888L;
	
	@Column(name = "\"BOOLEAN_VALUE\"")
	private Boolean value;

	public BooleanRule() {
		super();
		this.type = RuleType.BOOLEAN;
	}

	public BooleanRule(String id, Category category, Field field, Boolean value) {
		super(id, category, field, Operator.EQUAL);
		this.type = RuleType.BOOLEAN;
		this.value = value;
		
		if (!field.isApplicable(RuleType.BOOLEAN)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by Boolean Rule.");
		}
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	
}
