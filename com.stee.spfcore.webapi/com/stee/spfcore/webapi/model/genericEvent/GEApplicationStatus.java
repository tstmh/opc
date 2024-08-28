package com.stee.spfcore.webapi.model.genericEvent;

public enum GEApplicationStatus {

	REGISTERED ("Registered"),
	SUCCESSFUL ("Successful"),
	UNSUCCESSFUL ("Unsuccessful"),
	WITHDRAWN ("Withdrawn");
	
	private String value;
	
	private GEApplicationStatus (String value) {
		this.value = value;
	}
	
	public String toString () {
		return value;
	}
	
	public static GEApplicationStatus getApplicationStatus (String value) {
		
		GEApplicationStatus [] statuses = GEApplicationStatus.values();
		for (GEApplicationStatus status : statuses) {
			if (status.value.equals(value)) {
				return status;
			}
		}
		
		return null;
	}
	
	
}
