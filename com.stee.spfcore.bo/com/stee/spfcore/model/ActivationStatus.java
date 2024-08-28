package com.stee.spfcore.model;

public enum ActivationStatus {
	
	ACTIVE ("Active"), 
	INACTIVE ("In-active"),
	NONE ("None");
	
	private String status;
	
	private ActivationStatus (String status) {
		this.status = status;
	}
	
	public static ActivationStatus get (String status) {
		if("Active".equals(status)){
			return ACTIVE;
		} else if("In-active".equals(status)){
			return INACTIVE;
		} else {
			return NONE;
		}
	}

	@Override
	public String toString () {
		return this.status;
	}
}
