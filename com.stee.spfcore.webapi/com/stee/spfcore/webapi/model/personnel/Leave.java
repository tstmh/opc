package com.stee.spfcore.webapi.model.personnel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "\"PERSONAL_LEAVE_DETAILS\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "Leave" )
@Audited
public class Leave {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "\"ID\"" )
    @XStreamOmitField
    private long id;

    @Column( name = "\"LEAVE_TYPE\"", length = 50 )
    private String leaveType;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"START_DATE\"" )
    private Date startDate;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"END_DATE\"" )
    private Date endDate;

    public Leave() {
        super();
    }

    public Leave( String leaveType, Date startDate, Date endDate ) {
        super();
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType( String leaveType ) {
        this.leaveType = leaveType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }

    public String toString() {
        return String.format( "[leaveType=%s,startDate=%s,endDate=%s]", this.leaveType, this.startDate, this.endDate );
    }
}
