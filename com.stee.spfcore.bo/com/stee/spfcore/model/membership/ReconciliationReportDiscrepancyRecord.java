package com.stee.spfcore.model.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "RECON_REPORTS_DISCREPANCY_RECORDS", schema = "SPFCORE" )
@XStreamAlias( "ReconciliationReportDiscrepancyRecord" )
@Audited
public class ReconciliationReportDiscrepancyRecord {
    @Id
    @GenericGenerator( name = "ReconciliationReportDiscrepancyRecordGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "ReconciliationReportDiscrepancyRecordGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "NAME" )
    private String name;

    @Column( name = "NRIC" )
    private String nric;

    @Column( name = "RANK" )
    private String rank;

    @Column( name = "UNIT" )
    private String unit;

    @Temporal( TemporalType.DATE )
    @Column( name = "APPOINTMENT_DATE" )
    private Date appointmentDate;

    @Column( name = "ACCRUAL_YEAR" )
    private int accrualYear;

    @Column( name = "ACCRUAL_MONTH" )
    private int accrualMonth;

    @Column( name = "AMOUNT_COLLECTED" )
    private double amountCollected;

    @Column( name = "AMOUNT_DUE" )
    private double amountDue;

    @Column( name = "OFFSET" )
    private double offset;

    @Column( name = "DIFFERENCE" )
    private double difference;

    @Column( name = "REMARKS" )
    private String remarks;

    public ReconciliationReportDiscrepancyRecord() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public String getRank() {
        return rank;
    }

    public void setRank( String rank ) {
        this.rank = rank;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit( String unit ) {
        this.unit = unit;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate( Date appointmentDate ) {
        this.appointmentDate = appointmentDate;
    }

    public int getAccrualYear() {
        return accrualYear;
    }

    public void setAccrualYear( int accrualYear ) {
        this.accrualYear = accrualYear;
    }

    public int getAccrualMonth() {
        return accrualMonth;
    }

    public void setAccrualMonth( int accrualMonth ) {
        this.accrualMonth = accrualMonth;
    }

    public double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected( double amountCollected ) {
        this.amountCollected = amountCollected;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue( double amountDue ) {
        this.amountDue = amountDue;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }
}
