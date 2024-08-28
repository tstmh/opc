package com.stee.spfcore.webapi.model.course;

public enum ParticipantStatus {
	
	NOMINATED ("Nominated"),
	SUCCESSFUL ("Successful"),
	UNSUCCESSFUL ("Unsuccessful"),
	REGISTERED ("Registered"),
	WITHDRAWN ("Withdrawn"),
	WAIT_LIST ("Waitlist"),
	SELECTED ("Selected");
	
	private String value;
	
	private ParticipantStatus (String value) {
		this.value = value;
	}
	
	public String toString () {
		return value;
	}
	
	public static ParticipantStatus getParticipantStatus (String value) {
		
		ParticipantStatus [] statuses = ParticipantStatus.values();
		
		for (ParticipantStatus status : statuses) {
			if (status.value.equals(value)) {
				return status;
			}
		}
		
		return null;
	}

}
