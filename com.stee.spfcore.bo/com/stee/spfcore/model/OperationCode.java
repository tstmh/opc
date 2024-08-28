package com.stee.spfcore.model;

public enum OperationCode {
	
	CREATE("C"),
	DELETE("D");
	
	private String code;
	
	OperationCode (String code) {
		this.code = code;
	}
	
	// Add a static method to convert from code to enum
    public static OperationCode fromCode(String code) {
        for (OperationCode operationCode : values()) {
            if (operationCode.code.equals(code)) {
                return operationCode;
            }
        }
        throw new IllegalArgumentException("Unknown OperationCode: " + code);
    }
	
	public String getCode() {
        return code;
    }
	
}

