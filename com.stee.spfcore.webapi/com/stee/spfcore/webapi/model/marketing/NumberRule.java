package com.stee.spfcore.webapi.model.marketing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_NUMBER_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("NumberRule")
@Audited
@SequenceDef (name="MarketingNumberRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-NR-%d", intranetFormat="BPM-NR-%d")
public class NumberRule extends Rule {

	private static final long serialVersionUID = 5908129528866274202L;

	private static final List<Operator> ALLOWED_OPERATORS = new ArrayList<>();
	
	static {
		ALLOWED_OPERATORS.add(Operator.EQUAL);
		ALLOWED_OPERATORS.add(Operator.NOT_EQUAL);
		ALLOWED_OPERATORS.add(Operator.GREATER_THAN);
		ALLOWED_OPERATORS.add(Operator.LESS_THAN);
		ALLOWED_OPERATORS.add(Operator.GREATER_THAN_OR_EQUAL_TO);
		ALLOWED_OPERATORS.add(Operator.LESS_THAN_OR_EQUAL_TO);
	}
	
	@Column(name = "\"NUMBER_VALUE\"")
	private Integer value;

	public NumberRule() {
		super();
		this.type = RuleType.NUMBER;
	}

	public NumberRule(String id, Category category, Field field, Operator operator, Integer value) {
		super(id, category, field, operator);
		this.type = RuleType.NUMBER;
		this.value = value;
		
		if (!ALLOWED_OPERATORS.contains(operator)) {
			throw new IllegalArgumentException("Operator '" + operator.toString() + "' is not supported by Number Rule.");
		}
		
		if (!field.isApplicable(RuleType.NUMBER)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by Number Rule.");
		}
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
}

