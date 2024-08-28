package com.stee.spfcore.model.marketing;

public enum Operator {

	EQUAL ("equals", "="), 
	NOT_EQUAL ("notEquals", "!="),
	GREATER_THAN ("greaterThan", ">"), 
	LESS_THAN ("lessThan", "<"), 
	GREATER_THAN_OR_EQUAL_TO ("greaterThanOrEqualTo", ">="), 
	LESS_THAN_OR_EQUAL_TO ("lessThanOrEqualTo", "<="), 
	CONTAINS ("contains", ""), 
	IN ("in", "in"),
	NOT_IN ("notIn", "not in");
	
	private String value;
	private String expression;
	
	private Operator (String value, String expression) {
		this.value = value;
		this.expression = expression;
	}

	@Override
	public String toString () {
		return value;
	}
	
	public static Operator getOperator (String value) {
		Operator [] operators = Operator.values();
		
		for (Operator operator : operators) {
			if (operator.value.equals(value)) {
				return operator;
			}
		}
		
		return null;
	}
	
	public String toExpression () {
		return expression;
	}
	
}
