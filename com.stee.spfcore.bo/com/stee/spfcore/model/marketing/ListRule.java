package com.stee.spfcore.model.marketing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"MARKETING_LIST_RULES\"", schema = "\"SPFCORE\"")
@PrimaryKeyJoinColumn(name = "\"ID\"")
@XStreamAlias("ListRule")
@Audited
@SequenceDef (name="MarketingListRuleId_SEQ", schema = "SPFCORE", internetFormat="FEB-LR-%d", intranetFormat="BPM-LR-%d")
public class ListRule extends Rule {

	private static final long serialVersionUID = -8614934903075593222L;

	private static final List<Operator> ALLOWED_OPERATORS = new ArrayList<>();

	static {
		ALLOWED_OPERATORS.add(Operator.IN);
		ALLOWED_OPERATORS.add(Operator.NOT_IN);
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"LIST_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<ListRuleValue> values;

	public ListRule() {
		super();
		this.type = RuleType.LIST;
		this.values = new ArrayList<ListRuleValue>();
	}

	public ListRule(String id, Category category, Field field, Operator operator, List<ListRuleValue> values) {
		super(id, category, field, operator);
		this.type = RuleType.LIST;
		this.values = values;

		if (!ALLOWED_OPERATORS.contains(operator)) {
			throw new IllegalArgumentException("Operator '" + operator.toString() + "' is not supported by List Rule.");
		}

		if (!field.isApplicable(RuleType.LIST)) {
			throw new IllegalArgumentException("Field '" + field.toString() + "' is not supported by List Rule.");
		}

		if (this.values == null) {
			this.values = new ArrayList<ListRuleValue>();
		}
	}

	public List<ListRuleValue> getValues() {
		return values;
	}

	public void setValues(List<ListRuleValue> values) {
		this.values = values;
	}

}
