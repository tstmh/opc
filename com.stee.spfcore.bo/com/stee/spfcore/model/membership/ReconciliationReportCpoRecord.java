package com.stee.spfcore.model.membership;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "RECON_REPORTS_CPO_RECORDS", schema = "SPFCORE" )
@XStreamAlias( "ReconciliationReportCpoRecord" )
@Audited
public class ReconciliationReportCpoRecord {
    @Id
    @GenericGenerator( name = "ReconciliationReportCpoRecordGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "ReconciliationReportCpoRecordGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "DESCRIPTION" )
    private String description;

    @Column( name = "CPO_OFFICER_NUM" )
    private int cpoOfficerNum;

    @Column( name = "CPO_AMOUNT_COLLECTED" )
    private double cpoAmountCollected;

    @Column( name = "DB_OFFICER_NUM" )
    private int dbOfficerNum;

    @Column( name = "DB_AMOUNT_DUE" )
    private double dbAmountDue;

    @Column( name = "OFFSET" )
    private double offset;

    @Column( name = "DIFFERENCE" )
    private double difference;

    public ReconciliationReportCpoRecord() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public int getCpoOfficerNum() {
        return cpoOfficerNum;
    }

    public void setCpoOfficerNum( int cpoOfficerNum ) {
        this.cpoOfficerNum = cpoOfficerNum;
    }

    public double getCpoAmountCollected() {
        return cpoAmountCollected;
    }

    public void setCpoAmountCollected( double cpoAmountCollected ) {
        this.cpoAmountCollected = cpoAmountCollected;
    }

    public int getDbOfficerNum() {
        return dbOfficerNum;
    }

    public void setDbOfficerNum( int dbOfficerNum ) {
        this.dbOfficerNum = dbOfficerNum;
    }

    public double getDbAmountDue() {
        return dbAmountDue;
    }

    public void setDbAmountDue( double dbAmountDue ) {
        this.dbAmountDue = dbAmountDue;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset( double offset ) {
        this.offset = offset;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference( double difference ) {
        this.difference = difference;
    }
}
