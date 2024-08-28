package com.stee.spfcore.model.internal;

public enum SAGAwardType {

	SCHOLASTIC_ACHIEVEMENT_AWARD("SAA"),
	STUDY_AWARD("SA"),
	STUDY_GRANT("SG"),
	OTHERS("");
	
	private String value;

	private SAGAwardType( String value ) {
		this.value = value;
	}
	
	public static SAGAwardType getSAGAwardType( String value) {
		
		if("SAA".equals( value )) {
			return SCHOLASTIC_ACHIEVEMENT_AWARD;
		} else if("SA".equals( value )) {
			return STUDY_AWARD;
		} else if("SG".equals( value )) {
			return STUDY_GRANT;
		} else {
			return OTHERS;
		}
	}

	@Override
	public String toString() {
		return value;
	}
	
}
