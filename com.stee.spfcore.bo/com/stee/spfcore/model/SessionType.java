package com.stee.spfcore.model;

public enum SessionType {
    MORNING( "MORNING" ), AFTERNOON( "AFTERNOON" ), EVENING( "EVENING" ), NONE( "None" );

    private String typeOfSession;

    private SessionType( String sessionType ) {
        this.typeOfSession = sessionType;
    }

    public static SessionType get( String sessionType ) {
        SessionType[] sessionTypeList = SessionType.values();
        for ( SessionType sessionTypeItem : sessionTypeList ) {
            if ( sessionTypeItem.typeOfSession.equals( sessionType ) ) {
                return sessionTypeItem;
            }
        }
        return NONE;
    }

    @Override
    public String toString() {
        return this.typeOfSession;
    }
}
