package com.stee.spfcore.model.genericEvent;

public enum GenericEventStatus {
	
	DRAFT ("Draft"),
	ACTIVATED ("Activated"),
	CANCELLED ("Cancelled");
	
	private String value;
	
	private GenericEventStatus (String value) {
		this.value = value;
	}

	@Override
	public String toString () {
		return value;
	}
	
	public static GenericEventStatus getEventStatus (String value) {
		
		GenericEventStatus [] statuses = GenericEventStatus.values();
		for (GenericEventStatus status : statuses) {
			if (status.value.equals(value)) {
				return status;
			}
		}
		
		return null;
	}
	
}
