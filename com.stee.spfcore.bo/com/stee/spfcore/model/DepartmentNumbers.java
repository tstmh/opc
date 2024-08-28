package com.stee.spfcore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "DEPARTMENT_NUMBERS", schema = "SPFCORE" )
@XStreamAlias( "DepartmentNumbers" )
@Audited
public class DepartmentNumbers {
    @Id
    @GenericGenerator( name = "DepartmentNumbersGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "DepartmentNumbersGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Column( name = "DEPARTMENT" )
    private String department;

    @Column( name = "NUM_MEMBERS" )
    private int numberOfMembers;

    @Column( name = "NUM_SESSIONS" )
    private int numberOfSessions;

    public DepartmentNumbers() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department = department;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers( int numberOfMembers ) {
        this.numberOfMembers = numberOfMembers;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions( int numberOfSessions ) {
        this.numberOfSessions = numberOfSessions;
    }
}
