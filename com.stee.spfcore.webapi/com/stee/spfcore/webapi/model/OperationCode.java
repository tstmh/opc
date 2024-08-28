package com.stee.spfcore.webapi.model;

public enum OperationCode {
	
	CREATE("C"),
    DELETE("D");
	
	private String value;
	
	OperationCode (String value) {
		this.value = value;
	}
	
	public static OperationCode fromValue(String value) {
        for (OperationCode code : OperationCode.values()) {
            if (code.value.equals(value)) {
                return code;
            }
        }
        throw new IllegalArgumentException("Invalid OperationCode value: " + value);
    }
	
	public String toString () {
		return this.value;
	}
}
