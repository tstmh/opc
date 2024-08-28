package com.stee.spfcore.webapi.model;

public enum ProcessFlag {
	
	PROCESSED("P"),
    NOT_PROCESSED("N"),
    ERROR("E");
	
	private String value;
	
	ProcessFlag (String value) {
		this.value = value;
	}
	
	public static ProcessFlag fromValue(String value) {
        for (ProcessFlag flag : ProcessFlag.values()) {
            if (flag.value.equals(value)) {
                return flag;
            }
        }
        throw new IllegalArgumentException("Invalid ProcessFlag value: " + value);
    }
	
	public String toString () {
		return this.value;
	}
}
