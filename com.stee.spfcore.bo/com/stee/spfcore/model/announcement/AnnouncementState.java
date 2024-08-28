package com.stee.spfcore.model.announcement;

public enum AnnouncementState {

	DRAFT ("Draft"),
	ACTIVATED ("Activated"),
	DEACTIVATED ("Deactivated");
	
	private String value;
	
	private AnnouncementState (String value) {
		this.value = value;
	}

	@Override
	public String toString () {
		return value;
	}
	
	public static AnnouncementState getAnnouncementState (String value) {
		
		AnnouncementState [] states = AnnouncementState.values();
		
		for (AnnouncementState state : states) {
			if (state.value.equals(value)) {
				return state;
			}
		}
		
		return null;
	}
	
}
