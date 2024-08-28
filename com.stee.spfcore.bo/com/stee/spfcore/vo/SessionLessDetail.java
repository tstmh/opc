package com.stee.spfcore.vo;

import java.util.Date;

import com.stee.spfcore.model.SessionDetail;
import com.stee.spfcore.model.SessionType;

public class SessionLessDetail {
    private Date sessionDate;
    private SessionType sessionType;

    public SessionLessDetail() {
    }

    public SessionLessDetail( SessionDetail sessionDetail ) {
        this.sessionDate = sessionDetail.getSessionDate();
        this.sessionType = sessionDetail.getSessionType();
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate( Date sessionDate ) {
        this.sessionDate = sessionDate;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType( SessionType sessionType ) {
        this.sessionType = sessionType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( sessionDate == null ) ? 0 : sessionDate.hashCode() );
        result = prime * result + ( ( sessionType == null ) ? 0 : sessionType.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        SessionLessDetail other = ( SessionLessDetail ) obj;
        if ( sessionDate == null ) {
            if ( other.sessionDate != null )
                return false;
        }
        else if ( !sessionDate.equals( other.sessionDate ) )
            return false;
        return sessionType == other.sessionType;
    }
}
