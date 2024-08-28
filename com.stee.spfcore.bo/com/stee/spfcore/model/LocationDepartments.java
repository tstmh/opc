package com.stee.spfcore.model;

import java.util.ArrayList;
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
@Table( name = "LOCATION_DEPARTMENTS", schema = "SPFCORE" )
@XStreamAlias( "LocationDepartments" )
@Audited
public class LocationDepartments {
    @Id
    @GenericGenerator( name = "LocationDepartmentsGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "LocationDepartmentsGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Column( name = "LOCATION", length = 32 )
    private String location;

    @Column( name = "BLOCK_NUMBER", length = 10 )
    private String blockNumber;

    @Column( name = "STREET_NAME", length = 32 )
    private String streetName;

    @Column( name = "POSTAL_CODE", length = 6 )
    private String postalCode;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "LOCATION_DEPARTMENTS_DEPTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "LOCATION_DEPARTMENTS_ID" ) )
    @Column( name = "DEPT" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > departments;
    
    @Column( name = "DEPT_IN_CHARGE", length = 255 )
    private String deptInCharge;
    
    @Column( name = "IS_SESSION_DONE_IN_SETUP" )
    private boolean isSessionDoneInSetup;

    public LocationDepartments() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation( String location ) {
        this.location = location;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber( String blockNumber ) {
        this.blockNumber = blockNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName( String streetName ) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode( String postalCode ) {
        this.postalCode = postalCode;
    }

    public List< String > getDepartments() {
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
    
    public String getDeptInCharge() {
    	return deptInCharge;
    }
    
    public void setDeptInCharge(String deptInCharge) {
    	this.deptInCharge = deptInCharge;
    }
    
    public boolean getIsSessionDoneInSetup() {
    	return this.isSessionDoneInSetup;
    }
    
    public void setIsSessionDoneInSetup(boolean isSessionDoneInSetup) {
    	this.isSessionDoneInSetup = isSessionDoneInSetup;
    }
}
