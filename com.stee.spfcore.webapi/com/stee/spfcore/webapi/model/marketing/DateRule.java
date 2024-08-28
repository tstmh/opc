package com.stee.spfcore.webapi.model.marketing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_DATE_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("DateRule")
@Audited
@SequenceDef (name="MarketingDateRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-DR-%d", intranetFormat="BPM-DR-%d")
public class DateRule extends Rule {

	private static final long serialVersionUID = -6725855722457463727L;

	private static final List<Operator> ALLOWED_OPERATORS = new ArrayList<>();
	
	static {
		ALLOWED_OPERATORS.add(Operator.EQUAL);
		ALLOWED_OPERATORS.add(Operator.NOT_EQUAL);
		ALLOWED_OPERATORS.add(Operator.GREATER_THAN);
		ALLOWED_OPERATORS.add(Operator.LESS_THAN);
		ALLOWED_OPERATORS.add(Operator.GREATER_THAN_OR_EQUAL_TO);
		ALLOWED_OPERATORS.add(Operator.LESS_THAN_OR_EQUAL_TO);
	}
	
	@Column(name = "\"DATE_VALUE\"")
	private Date dateValue;

	public DateRule() {
		super();
		this.type = RuleType.DATE;
	}

	public DateRule (String id, Category category, Field field, Operator operator, Date value) {
		super(id, category, field, operator);
		this.dateValue = value;
		this.type = RuleType.DATE;
		
		if (!ALLOWED_OPERATORS.contains(operator)) {
			throw new IllegalArgumentException("Operator '" + operator.toString() + "' is not supported by Date Rule.");
		}
		
		if (!field.isApplicable(RuleType.DATE)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by Date Rule.");
		}
	}

	public Date getValue() {
		return dateValue;
	}

	public void setValue(Date value) {
		this.dateValue = value;
	}
	
}


