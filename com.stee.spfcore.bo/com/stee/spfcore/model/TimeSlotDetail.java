package com.stee.spfcore.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "TIME_SLOT_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "TimeSlotDetail" )
@Audited
public class TimeSlotDetail {
    @Id
    @GenericGenerator( name = "TimeSlotDetailGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "TimeSlotDetailGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "START_TIME" ) ), @AttributeOverride( name = "end", column = @Column( name = "END_TIME" ) ) } )
    private TimeStartEnd timeStartEnd;

    public TimeSlotDetail() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public TimeStartEnd getTimeStartEnd() {
        return timeStartEnd;
    }

    public void setTimeStartEnd( TimeStartEnd timeStartEnd ) {
        this.timeStartEnd = timeStartEnd;
    }
}
