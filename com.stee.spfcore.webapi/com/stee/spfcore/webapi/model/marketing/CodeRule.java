package com.stee.spfcore.webapi.model.marketing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_CODE_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("CodeRule")
@Audited
@SequenceDef (name="MarketingCodeRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-CR-%d", intranetFormat="BPM-CR-%d")
public class CodeRule extends Rule {

	private static final long serialVersionUID = -4090384378980200010L;

	private static final List<Operator> ALLOWED_OPERATORS = new ArrayList<>();

	static {
		ALLOWED_OPERATORS.add(Operator.EQUAL);
		ALLOWED_OPERATORS.add(Operator.NOT_EQUAL);
	}

	@Column(name = "\"CODE_VALUE\"", length=50)
	private String value;

	@Transient
	private String description;

	public CodeRule() {
		super();
		this.type = RuleType.CODE;
	}

	public CodeRule(String id, Category category, Field field, Operator operator, String value, String description) {
		super (id, category, field, operator);
		this.value = value;
		this.description = description;
		this.type = RuleType.CODE;
		
		if (!ALLOWED_OPERATORS.contains(operator)) {
			throw new IllegalArgumentException("Operator '" + operator.toString() + "' is not supported by Code Table Rule.");
		}
		
		if (!field.isApplicable(RuleType.CODE)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by Code Table Rule.");
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

