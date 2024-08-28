package com.stee.spfcore.model.survey;

public enum SurveyStatus {
	
	DRAFT ("Draft"),
	ACTIVATED ("Activated"),
	CANCELLED ("Cancelled");
	
	private String value;
	
	private SurveyStatus (String value) {
		this.value = value;
	}
	@Override
	public String toString () {
		return this.value;
	}
	
	public static SurveyStatus getSurveyStatus (String value) {
		
		SurveyStatus [] statuses = SurveyStatus.values();
		
		for (SurveyStatus status : statuses) {
			if (status.value.equals(value)) {
				return status;
			}
		}
		
		return null;
	}
	

}
