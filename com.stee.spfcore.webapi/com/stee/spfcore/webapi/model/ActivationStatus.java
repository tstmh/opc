package com.stee.spfcore.webapi.model;

public enum ActivationStatus {
	
	ACTIVE ("Active"), 
	INACTIVE ("In-active"),
	NONE ("None");
	
	private String status;
	
	private ActivationStatus (String status) {
		this.status = status;
	}
	
	public static ActivationStatus get (String status) {
		/*switch (status) {
			case "Active":
				return ACTIVE;
			case "In-active":
				return INACTIVE;
			default:
				return NONE;
		}*/
		
		if("Active".equals(status)){
			return ACTIVE;
		} else if("In-active".equals(status)){
			return INACTIVE;
		} else {
			return NONE;
		}
	}
	
	public String toString () {
		return this.status;
	}
}
