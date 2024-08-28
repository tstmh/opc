package com.stee.spfcore.model.marketing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@Entity
@Table(name = "\"MARKETING_STRING_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("StringRule")
@Audited
@SequenceDef (name="MarketingStringRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-SR-%d", intranetFormat="BPM-SR-%d")
public class StringRule extends Rule {

	private static final long serialVersionUID = -3878003953766922944L;

	private static final List<Operator> ALLOWED_OPERATORS = new ArrayList<>();
	
	static {
		ALLOWED_OPERATORS.add(Operator.EQUAL);
		ALLOWED_OPERATORS.add(Operator.CONTAINS);
	}
	
	@Column(name = "\"STRING_VALUE\"")
	private String value;

	public StringRule() {
		super();
		this.type = RuleType.STRING;
	}

	public StringRule(String id, Category category, Field field, Operator operator, String value) {
		super(id, category, field, operator);
		this.type = RuleType.STRING;
		this.value = value;
		
		if (!ALLOWED_OPERATORS.contains(operator)) {
			throw new IllegalArgumentException("Operator '" + operator.toString() + "' is not supported by String Rule.");
		}
		
		if (!field.isApplicable(RuleType.STRING)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by String Rule.");
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
