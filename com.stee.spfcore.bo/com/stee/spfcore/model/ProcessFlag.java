package com.stee.spfcore.model;

public enum ProcessFlag {
	
	PROCESSED("P"),
    NOT_PROCESSED("N"),
    ERROR("E");
	
	private String code;
	
	ProcessFlag (String code) {
		this.code = code;
	}
	
	public static ProcessFlag fromValue(String value) {
        for (ProcessFlag flag : ProcessFlag.values()) {
            if (flag.code.equals(value)) {
                return flag;
            }
        }
        throw new IllegalArgumentException("Invalid ProcessFlag value: " + value);
    }
	
	public String getCode() {
        return code;
    }

}


