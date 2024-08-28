package com.stee.spfcore.vo.personnel;

public class PersonalOnContinuousLeaveStatus {
    private String nric;
    private boolean onContinuousLeave;

    public PersonalOnContinuousLeaveStatus( String nric, boolean onContinuousLeave ) {
        this.nric = nric;
        this.onContinuousLeave = onContinuousLeave;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public boolean isOnContinuousLeave() {
        return onContinuousLeave;
    }

    public void setOnContinuousLeave( boolean onContinuousLeave ) {
        this.onContinuousLeave = onContinuousLeave;
    }
}
