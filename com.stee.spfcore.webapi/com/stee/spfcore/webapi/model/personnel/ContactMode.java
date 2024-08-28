package com.stee.spfcore.webapi.model.personnel;

public enum ContactMode {

    SMS( "SMS" ), EMAIL( "Email" ), OTHERS( "Others" );

    private String value;

    private ContactMode( String mode ) {
        this.value = mode;
    }

    public static ContactMode get( String mode ) {
    	
        /*switch ( mode ) {
            case "SMS":
                return SMS;
            case "Email":
                return EMAIL;
            default:
                return OTHERS;
        }*/
    	
    	if("SMS".equals(mode)){
    		return SMS;
    	} else if("Email".equals(mode)){
    		return EMAIL;
    	} else {
    		return OTHERS;
    	}
    	
    }

    public String toString() {
        return value;
    }

}
