package com.stee.spfcore.webapi.model.internal;

public enum ApplicationType {
	
	BEREAVEMENT_GRANT("BEREAVEMENT_GRANT"), 
	NEWBORN_GIFT("NEWBORN_GIFT"), 
	WEDDING_GIFT("WEDDING_GIFT"), 
	WEIGHT_MGMT_SUBSIDY("WEIGHT_MGMT_SUBSIDY"), 
	COURSE_REGISTRATION("COURSE_REGISTRATION"),
	SCHOLASTIC_ACHIEVEMENT_AWARD("SCHOLASTIC_ACHIEVEMENT_AWARD"),
	STUDY_AWARD("STUDY_AWARD"),
	STUDY_GRANT("STUDY_GRANT"),
	HIV_SCREENING_ACKNOWLEDGEMENT("HIV_SCREENING_ACKNOWLEDGEMENT"),
	OTHERS("OTHERS");

    private String value;

    private ApplicationType( String value ) {
        this.value = value;
    }

    public static ApplicationType get( String value ) {
    	    	
    	if("BEREAVEMENT_GRANT".equals(value)){
    		return BEREAVEMENT_GRANT;
    	} else if("NEWBORN_GIFT".equals(value)){
    		return NEWBORN_GIFT;
    	} else if("WEDDING_GIFT".equals(value)){
    		return WEDDING_GIFT;
    	} else if("WEIGHT_MGMT_SUBSIDY".equals(value)) {
    	    return WEIGHT_MGMT_SUBSIDY;
    	} else if("COURSE_REGISTRATION".equals(value)) {
    	    return COURSE_REGISTRATION;
    	} else if("SCHOLASTIC_ACHIEVEMENT_AWARD".equals(value)) {
    	    return SCHOLASTIC_ACHIEVEMENT_AWARD;
    	} else if("STUDY_AWARD".equals(value)) {
    	    return STUDY_AWARD;
    	} else if("STUDY_GRANT".equals(value)) {
    	    return STUDY_GRANT;
        } else if("HIV_SCREENING_ACKNOWLEDGEMENT".equals(value)) {
            return HIV_SCREENING_ACKNOWLEDGEMENT;
    	} else {
    		return OTHERS;
    	}
    	
    }

    public String toString() {
        return value;
    }

}
