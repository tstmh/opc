package com.stee.spfcore.model.membership;

public enum MembershipType {
	
	ORDINARY ("Ordinary"), 
	ORDINARY_NON_PAYING ("Ordinary (Non-paying)"),
	ASSOCIATE ("Associate"), 
	HONORARY ("Honorary"),
	EXPIRED ("Expired"), 
	NON_MEMBER ("Non-member");
	
	private String type;
	
	private MembershipType (String type) {
		this.type = type;
	}
	
	public static MembershipType get (String type) {
		if("Ordinary".equals(type)) {
			return ORDINARY;
		} else if("Ordinary (Non-paying)".equals(type)) {
			return ORDINARY_NON_PAYING;
		} else if("Associate".equals(type)) {
			return ASSOCIATE;
		} else if("Honorary".equals(type)) {
			return HONORARY;
		} else if("Expired".equals(type)) {
			return EXPIRED;
		} else {
			return NON_MEMBER;
		}
	}

	@Override
	public String toString () {
		return this.type;
	}
	 
}
