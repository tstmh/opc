package com.stee.spfcore.webapi.model.marketing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_DURATION_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("DurationRule")
@Audited
@SequenceDef (name="MarketingDurationRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-DUR-%d", intranetFormat="BPM-DUR-%d")
public class DurationRule extends Rule {

	private static final long serialVersionUID = -5915242899547146579L;

	private static final List<Operator> ALLOWED_OPERATORS = new ArrayList<>();
	
	static {
		ALLOWED_OPERATORS.add(Operator.EQUAL);
		ALLOWED_OPERATORS.add(Operator.NOT_EQUAL);
		ALLOWED_OPERATORS.add(Operator.GREATER_THAN);
		ALLOWED_OPERATORS.add(Operator.LESS_THAN);
		ALLOWED_OPERATORS.add(Operator.GREATER_THAN_OR_EQUAL_TO);
		ALLOWED_OPERATORS.add(Operator.LESS_THAN_OR_EQUAL_TO);
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"DURATION_UNIT\"", length = 30)
	private DurationUnit unit;
	
	@Column(name = "\"DURATION_VALUE\"")
	private int value;
	
	@Column(name = "\"REFERENCE_DATE\"")
	private Date reference;
	

	public DurationRule() {
		super();
		this.type = RuleType.DURATION;
	}

	public DurationRule(String id, Category category, Field field, Operator operator, DurationUnit unit, int value, Date reference) {
		super(id, category, field, operator);
		this.type = RuleType.DURATION;
		this.unit = unit;
		this.value = value;
		this.reference = reference;
		
		if (!ALLOWED_OPERATORS.contains(operator)) {
			throw new IllegalArgumentException("Operator '" + operator.toString() + "' is not supported by Duration Rule.");
		}
		
		if (!field.isApplicable(RuleType.DURATION)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by Duration Rule.");
		}
	}

	public DurationUnit getUnit() {
		return unit;
	}

	public void setUnit(DurationUnit unit) {
		this.unit = unit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getReference() {
		return reference;
	}

	public void setReference(Date reference) {
		this.reference = reference;
	}
}

