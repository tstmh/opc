package com.stee.spfcore.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class TimeStartEnd {
    @Temporal( TemporalType.TIME )
    private Date start;

    @Temporal( TemporalType.TIME )
    private Date end;

    public TimeStartEnd() {
    }

    public TimeStartEnd( Date start, Date end ) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart( Date start ) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd( Date end ) {
        this.end = end;
    }
}
