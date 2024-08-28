package com.stee.spfcore.model.membership;

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

@Entity
@Table( name = "RECON_REPORTS", schema = "SPFCORE" )
@XStreamAlias( "ReconciliationReport" )
@Audited
public class ReconciliationReport {
    @Id
    @GenericGenerator( name = "ReconciliationReportGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "ReconciliationReportGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "TITLE" )
    private String title;

    @Column( name = "YEAR" )
    private int year;

    @Column( name = "MONTH" )
    private int month;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "REVISION_TIMESTAMP" )
    private Date revisionTimestamp;

    @Column( name = "UPDATER" )
    private String updater;

    @Column( name = "LOG_FILE_PATH" )
    private String logFilePath;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "RECON_REPORTS_CPO_RECORDS_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "RECON_REPORTS_ID" ), inverseJoinColumns = @JoinColumn( name = "RECON_REPORTS_CPO_RECORDS_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< ReconciliationReportCpoRecord > cpoRecords;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "RECON_REPORTS_CPO_CURRENT_MONTH_DISCREPANCY_RECORDS_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "RECON_REPORTS_ID" ), inverseJoinColumns = @JoinColumn( name = "RECON_REPORTS_DISCREPANCY_RECORDS_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< ReconciliationReportDiscrepancyRecord > cpoCurrentMonthDiscrepancyRecords;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "RECON_REPORTS_CPO_PAST_FUTURE_MONTH_DISCREPANCY_RECORDS_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "RECON_REPORTS_ID" ), inverseJoinColumns = @JoinColumn( name = "RECON_REPORTS_DISCREPANCY_RECORDS_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< ReconciliationReportDiscrepancyRecord > cpoPastFutureMonthsDiscrepancyRecords;

    public ReconciliationReport() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear( int year ) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth( int month ) {
        this.month = month;
    }

    public Date getRevisionTimestamp() {
        return revisionTimestamp;
    }

    public void setRevisionTimestamp( Date revisionTimestamp ) {
        this.revisionTimestamp = revisionTimestamp;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater( String updater ) {
        this.updater = updater;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath( String logFilePath ) {
        this.logFilePath = logFilePath;
    }

    public List< ReconciliationReportCpoRecord > getCpoRecords() {
        return cpoRecords;
    }

    public void setCpoRecords( List< ReconciliationReportCpoRecord > cpoRecords ) {
        this.cpoRecords = cpoRecords;
    }

    public List< ReconciliationReportDiscrepancyRecord > getCpoCurrentMonthDiscrepancyRecords() {
        return cpoCurrentMonthDiscrepancyRecords;
    }

    public void setCpoCurrentMonthDiscrepancyRecords( List< ReconciliationReportDiscrepancyRecord > cpoCurrentMonthDiscrepancyRecords ) {
        this.cpoCurrentMonthDiscrepancyRecords = cpoCurrentMonthDiscrepancyRecords;
    }

    public List< ReconciliationReportDiscrepancyRecord > getCpoPastFutureMonthsDiscrepancyRecords() {
        return cpoPastFutureMonthsDiscrepancyRecords;
    }

    public void setCpoPastFutureMonthsDiscrepancyRecords( List< ReconciliationReportDiscrepancyRecord > cpoPastFutureMonthsDiscrepancyRecords ) {
        this.cpoPastFutureMonthsDiscrepancyRecords = cpoPastFutureMonthsDiscrepancyRecords;
    }
}
