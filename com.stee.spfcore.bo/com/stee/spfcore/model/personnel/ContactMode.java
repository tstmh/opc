package com.stee.spfcore.model.personnel;

public enum ContactMode {

    SMS( "SMS" ), EMAIL( "Email" ), OTHERS( "Others" );

    private String value;

    private ContactMode( String mode ) {
        this.value = mode;
    }

    public static ContactMode get( String mode ) {
    	
    	if("SMS".equals(mode)){
    		return SMS;
    	} else if("Email".equals(mode)){
    		return EMAIL;
    	} else {
    		return OTHERS;
    	}
    	
    }

    @Override
    public String toString() {
        return value;
    }

}
