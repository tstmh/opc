package com.stee.spfcore.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DateStartEnd {
    @Temporal( TemporalType.DATE )
    private Date start;

    @Temporal( TemporalType.DATE )
    private Date end;

    public DateStartEnd() {
    }

    public DateStartEnd( Date start, Date end ) {
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
