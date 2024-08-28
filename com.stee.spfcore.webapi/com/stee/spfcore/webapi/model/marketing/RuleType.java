package com.stee.spfcore.webapi.model.marketing;

public enum RuleType {

	NUMBER ("number"), 
	STRING ("string"), 
	BOOLEAN ("boolean"), 
	DATE ("date"), 
	DURATION ("duration"), 
	CODE ("code"), 
	LIST ("list");
	
	private String value;
	
	private RuleType (String value) {
		this.value = value;
	}
	
	public String toString () {
		return value;
	}
	
	public static RuleType getRuleType (String value) {
		RuleType [] ruleTypes = RuleType.values();
		
		for (RuleType type : ruleTypes) {
			if (type.value.equals(value)) {
				return type;
			}
		}
		
		return null;
	}
}


