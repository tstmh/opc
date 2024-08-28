package com.stee.spfcore.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
@Table( name = "DRAFT_DEPARTMENTS_SESSIONS", schema = "SPFCORE" )
@XStreamAlias( "DraftDepartmentsSessions" )
@Audited
public class DraftDepartmentsSessions {
    @Id
    @GenericGenerator( name = "DraftDepartmentsSessionsGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "DraftDepartmentsSessionsGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Column( name = "AM_START_TIME_NAME" )
    private String amStartTimeName;

    @Column( name = "AM_START_TIME_VALUE" )
    private String amStartTimeValue;

    @Column( name = "PM_START_TIME_NAME" )
    private String pmStartTimeName;

    @Column( name = "PM_START_TIME_VALUE" )
    private String pmStartTimeValue;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "DRAFT_DEPARTMENTS_SESSIONS_DEPARTMENTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "DRAFT_DEPARTMENTS_SESSIONS_ID" ) )
    @Column( name = "DEPARTMENT" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > departments;

    public DraftDepartmentsSessions() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getAmStartTimeName() {
        return amStartTimeName;
    }

    public void setAmStartTimeName( String amStartTimeName ) {
        this.amStartTimeName = amStartTimeName;
    }

    public String getAmStartTimeValue() {
        return amStartTimeValue;
    }

    public void setAmStartTimeValue( String amStartTimeValue ) {
        this.amStartTimeValue = amStartTimeValue;
    }

    public String getPmStartTimeName() {
        return pmStartTimeName;
    }

    public void setPmStartTimeName( String pmStartTimeName ) {
        this.pmStartTimeName = pmStartTimeName;
    }

    public String getPmStartTimeValue() {
        return pmStartTimeValue;
    }

    public void setPmStartTimeValue( String pmStartTimeValue ) {
        this.pmStartTimeValue = pmStartTimeValue;
    }

    public List< String > getDepartments() {
        return departments;
    }

    public void setDepartments( List< String > departments ) {
        this.departments = departments;
    }
}
