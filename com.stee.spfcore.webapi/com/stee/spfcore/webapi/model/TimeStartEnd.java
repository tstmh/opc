package com.stee.spfcore.webapi.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Embeddable
public class TimeStartEnd {
    @Temporal( TemporalType.TIME )
    @JsonFormat(pattern="HH:mm:ss")
    private Date start;

    @Temporal( TemporalType.TIME )
    @JsonFormat(pattern="HH:mm:ss")
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
