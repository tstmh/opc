package com.stee.spfcore.model.report;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Table( name = "SSRS_REPORT", schema = "SPFCORE" )
@Audited
public class SsrsReport {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "ID" )
    private long id;

    @Column( name = "ENABLED" )
    private Boolean enabled;

    @Column( name = "IS_ADHOC" )
    private Boolean isAdhoc;

    @Column( name = "ROOT_DIR" )
    private String rootDir;

    @Column( name = "REPORT_NAME" )
    private String
            reportName;

    @Column( name = "TABLE_NAME" )
    private String tableName;

    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "REPORT_ENVIRONMENT_ID" )
    private SsrsReportEnvironment environment;

    @Transient
    private String serviceReportUrl;

    @Transient
    private String managerReportUrl;

    public SsrsReportEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment( SsrsReportEnvironment environment ) {
        this.environment = environment;
    }

    public SsrsReport() {
        // DO NOTHING
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled( Boolean enabled ) {
        this.enabled = enabled;
    }

    public Boolean getIsAdhoc() {
        return isAdhoc;
    }

    public void setIsAdhoc( Boolean isAdhoc ) {
        this.isAdhoc = isAdhoc;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir( String rootDir ) {
        this.rootDir = rootDir;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName( String reportName ) {
        this.reportName = reportName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName( String tableName ) {
        this.tableName = tableName;
    }

    public String getServiceReportUrl() {
        return serviceReportUrl;
    }

    public String getManagerReportUrl() {
        return managerReportUrl;
    }

    @PrePersist
    @PreUpdate
    @PostLoad
    protected void buildUrls() {
        // build service report url
        this.serviceReportUrl = ( this.environment == null ) ? null : this.environment.getServiceBaseUrl() + this.rootDir + "/" + this.reportName;

        // build manager report url
        this.managerReportUrl = ( this.environment == null ) ? null : this.environment.getManagerBaseUrl() + this.rootDir + "/" + this.reportName;
    }
}
