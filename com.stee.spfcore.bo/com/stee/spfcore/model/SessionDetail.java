package com.stee.spfcore.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "SESSION_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "SessionDetail" )
@Audited
public class SessionDetail {
    @Id
    @GenericGenerator( name = "SessionDetailGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "SessionDetailGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Temporal( TemporalType.DATE )
    @Column( name = "SESSION_DATE" )
    private Date sessionDate;

    @Column( name = "SESSION_TYPE" )
    private SessionType sessionType;

    @Column( name = "DEPARTMENT" )
    private String department;

    @Column( name = "VENUE" )
    private String venue;

    @Column( name = "TITLE" )
    private String tableTitle;

    @Column( name = "SESSION" )
    private String sessionTitle;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "SESSION_DETAILS_TIME_SLOTS_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "SESSION_DETAILS_ID" ), inverseJoinColumns = @JoinColumn( name = "TIME_SLOT_DETAILS_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< TimeSlotDetail > timeSlots;

    public SessionDetail() {
    }
    
    public SessionDetail( SessionDetail sessionDetail ) {
    	this.id = sessionDetail.getId();
    	this.department = sessionDetail.getDepartment();
    	this.sessionDate = sessionDetail.getSessionDate();
    	this.sessionTitle = sessionDetail.getSessionTitle();
    	this.sessionType = sessionDetail.getSessionType();
        this.tableTitle = sessionDetail.getTableTitle();
        this.setTimeSlots( sessionDetail.getTimeSlots() );
        this.venue = sessionDetail.getVenue();
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
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

    public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getVenue() {
        return venue;
    }

    public void setVenue( String venue ) {
        this.venue = venue;
    }

    public List< TimeSlotDetail > getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots( List< TimeSlotDetail > timeSlots ) {
        if ( this.timeSlots == null ) {
            this.timeSlots = new ArrayList<>();
        }
        this.timeSlots.clear();
        if ( timeSlots != null ) {
            this.timeSlots.addAll( timeSlots );
        }
    }

    public String getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle( String tableTitle ) {
        this.tableTitle = tableTitle;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle( String sessionTitle ) {
        this.sessionTitle = sessionTitle;
    }
}
