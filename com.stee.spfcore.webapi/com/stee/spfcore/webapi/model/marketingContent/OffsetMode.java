package com.stee.spfcore.webapi.model.marketingContent;


public enum OffsetMode {

	BEFORE ("BEFORE"),
	AFTER ("AFTER");
	
	private String value;
	
	private OffsetMode (String value) {
		this.value = value;
	}
	
	public String toString () {
		return value;
	}
	
	public static OffsetMode getOffsetMode (String value) {
		OffsetMode [] modes = OffsetMode.values();
		
		for (OffsetMode mode : modes) {
			if (mode.value.equals(value)) {
				return mode;
			}
		}
		
		return null;
	}
	
}

