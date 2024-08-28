package com.stee.spfcore.model.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Table( name = "SSRS_REPORT_ENVIRONMENT", schema = "SPFCORE" )
@Audited
public class SsrsReportEnvironment {
    private static final String URL_FORMAT = "%s://%s:%s%s";

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "ID" )
    private int id;

    @Column( name = "ENABLED" )
    private Boolean enabled;

    @Column( name = "ENVIRONMENT_NAME" )
    private String environmentName;

    @Column( name = "PROTOCOL" )
    private String protocol;

    @Column( name = "HOSTNAME" )
    private String hostName;

    @Column( name = "MANAGER_PORT" )
    private Integer managerPort;

    @Column( name = "SERVICE_PORT" )
    private Integer servicePort;

    @Column( name = "MANAGER_VDIR" )
    private String managerVdir;

    @Column( name = "SERVICE_VDIR" )
    private String serviceVdir;

    @Transient
    private String serviceBaseUrl;

    @Transient
    private String managerBaseUrl;

    public SsrsReportEnvironment() {
        // DO NOTHING
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled( Boolean enabled ) {
        this.enabled = enabled;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName( String environmentName ) {
        this.environmentName = environmentName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol( String protocol ) {
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName( String hostName ) {
        this.hostName = hostName;
    }

    public Integer getManagerPort() {
        return managerPort;
    }

    public void setManagerPort( Integer managerPort ) {
        this.managerPort = managerPort;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort( Integer servicePort ) {
        this.servicePort = servicePort;
    }

    public String getManagerVdir() {
        return managerVdir;
    }

    public void setManagerVdir( String managerVdir ) {
        this.managerVdir = managerVdir;
    }

    public String getServiceVdir() {
        return serviceVdir;
    }

    public void setServiceVdir( String serviceVdir ) {
        this.serviceVdir = serviceVdir;
    }

    public String getServiceBaseUrl() {
        return serviceBaseUrl;
    }

    public String getManagerBaseUrl() {
        return managerBaseUrl;
    }

    @PrePersist
    @PreUpdate
    @PostLoad
    protected void buildUrls() {
        // build service base url
        this.serviceBaseUrl = String.format( URL_FORMAT, this.protocol, this.hostName, this.servicePort, this.serviceVdir );

        // build manager base url
        this.managerBaseUrl = String.format( URL_FORMAT, this.protocol, this.hostName, this.managerPort, this.managerVdir );
    }
}
