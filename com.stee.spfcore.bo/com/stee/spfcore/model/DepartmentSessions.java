package com.stee.spfcore.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "DEPARTMENT_SESSIONS", schema = "SPFCORE" )
@XStreamAlias( "DepartmentSessions" )
@Audited
public class DepartmentSessions {
    @Id
    @GenericGenerator( name = "DepartmentSessionsGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "DepartmentSessionsGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "DEPARTMENT_SESSIONS_DEPARTMENTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "DEPARTMENT_SESSIONS_ID" ) )
    @Column( name = "DEPARTMENT" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > departments;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "MORNING_START" ) ), @AttributeOverride( name = "end", column = @Column( name = "MORNING_END" ) ) } )
    private TimeStartEnd morningSession;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "AFTERNOON_START" ) ), @AttributeOverride( name = "end", column = @Column( name = "AFTERNOON_END" ) ) } )
    private TimeStartEnd afternoonSession;
    
    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "EVENING_START" ) ), @AttributeOverride( name = "end", column = @Column( name = "EVENING_END" ) ) } )
    private TimeStartEnd eveningSession;

    public DepartmentSessions() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public List< String > getDepartment() {
        return departments;
    }

    public void setDepartments( List< String > departments ) {
        if ( this.departments == null ) {
            this.departments = new ArrayList<>();
        }
        this.departments.clear();
        if ( departments != null ) {
            this.departments.addAll( departments );
        }
    }

	public TimeStartEnd getMorningSession() {
		return morningSession;
	}

	public void setMorningSession(TimeStartEnd morningSession) {
		this.morningSession = morningSession;
	}

	public TimeStartEnd getAfternoonSession() {
		return afternoonSession;
	}

	public void setAfternoonSession(TimeStartEnd afternoonSession) {
		this.afternoonSession = afternoonSession;
	}

	public TimeStartEnd getEveningSession() {
		return eveningSession;
	}

	public void setEveningSession(TimeStartEnd eveningSession) {
		this.eveningSession = eveningSession;
	}

}
