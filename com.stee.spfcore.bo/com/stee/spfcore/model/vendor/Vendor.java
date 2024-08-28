package com.stee.spfcore.model.vendor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.Address;
import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.stee.spfcore.utils.DateUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "VENDORS", schema = "SPFCORE" )
@XStreamAlias( "Vendor" )
@Audited
@SequenceDef( name = "Vendor_SEQ", schema = "SPFCORE", internetFormat = "FEB-%d", intranetFormat = "BPM-%d" )
public class Vendor implements Serializable {

    private static final long serialVersionUID = -2945042415538359644L;

    @Id
    @GeneratedId
    @Column( name = "\"ID\"" )
    private String id;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"VENDOR_ID\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< CategoryInfo > categoryInfos;

    @Column( name = "\"NAME\"", length = 100 )
    private String name;

    @Embedded
    private Address address;

    @Column( name = "\"CONTACT_NUMBER\"", length = 16 )
    private String contactNumber;

    @Column( name = "\"EMAIL\"", length = 256 )
    private String email;

    @Column( name = "\"FAX_NUMBER\"", length = 16 )
    private String faxNumber;

    @Column( name = "\"FIRST_POC_SALUTATION\"" )
    private String firstPocSalutation;

    @Column( name = "\"FIRST_POC_NAME\"", length = 100 )
    private String firstPocName;

    @Column( name = "\"FIRST_POC_CONTACT_NUMBER\"", length = 16 )
    private String firstPocContactNumber;

    @Column( name = "\"FIRST_POC_EMAIL\"", length = 256 )
    private String firstPocEmail;

    @Column( name = "\"SECOND_POC_SALUTATION\"" )
    private String secondPocSalutation;

    @Column( name = "\"SECOND_POC_NAME\"", length = 100 )
    private String secondPocName;

    @Column( name = "\"SECOND_POC_CONTACT_NUMBER\"", length = 16 )
    private String secondPocContactNumber;

    @Column( name = "\"SECOND_POC_EMAIL\"", length = 256 )
    private String secondPocEmail;

    @Column( name = "\"DESCRIPTION\"", length = 2000 )
    private String description;

    @Column( name = "\"VENDOR_REMARK\"", length = 100 )
    private String vendorRemark;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"EFFECTIVE_DATE\"" )
    private Date effectiveDate;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"OBSOLETE_DATE\"" )
    private Date obsoleteDate;

    @Column( name = "\"UPDATED_BY\"", length = 30 )
    private String updatedBy;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"UPDATED_DATE\"" )
    private Date updatedOn;

    @Column( name = "\"REMARK\"", length = 100 )
    private String remark;

    @Transient
    private boolean enabled;

    @Column( name = "\"VENDOR_CODE\"", length = 100 )
    private String vendorCode;

    public Vendor() {
        super();
    }

    public Vendor( String id, List< CategoryInfo > categoryInfos, String name, String vendorCode, Address address, String contactNumber, String email, String faxNumber, String firstPocName, String firstPocContactNumber,
            String firstPocEmail, String secondPocName, String secondPocContactNumber, String secondPocEmail, String description, String vendorRemark, Date effectiveDate, Date obsoleteDate, String updatedBy, Date updatedOn, String remark ) {
        super();
        this.id = id;
        this.categoryInfos = categoryInfos;
        this.name = name;
        this.vendorCode = vendorCode;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.faxNumber = faxNumber;
        this.firstPocName = firstPocName;
        this.firstPocContactNumber = firstPocContactNumber;
        this.firstPocEmail = firstPocEmail;
        this.secondPocName = secondPocName;
        this.secondPocContactNumber = secondPocContactNumber;
        this.secondPocEmail = secondPocEmail;
        this.description = description;
        this.vendorRemark = vendorRemark;
        this.effectiveDate = effectiveDate;
        this.obsoleteDate = obsoleteDate;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
        this.remark = remark;
    }

    @PostLoad
    protected void onLoad() {

        // Default is false
        enabled = false;

        Date today = new Date();

        // Only enabled if the effective date is not after today (include today)
        // and obsolete date is null or is not before today
        if (( effectiveDate != null && !DateUtils.isAfterDay( effectiveDate, today ) ) && ( obsoleteDate == null || DateUtils.isAfterDay( obsoleteDate, today ) )) {
            enabled = true;
        }
    }

    @PrePersist
    protected void onPersist() {
        if ( address != null ) {
            address.buildResidentialAddress();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if ( address != null ) {
            address.buildResidentialAddress();
        }
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public List< CategoryInfo > getCategoryIds() {
        return categoryInfos;
    }

    public void setCategoryIds( List< CategoryInfo > categoryIds ) {
        this.categoryInfos = categoryIds;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress( Address address ) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber( String contactNumber ) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber( String faxNumber ) {
        this.faxNumber = faxNumber;
    }

    public String getFirstPocName() {
        return firstPocName;
    }

    public void setFirstPocName( String firstPocName ) {
        this.firstPocName = firstPocName;
    }

    public String getFirstPocContactNumber() {
        return firstPocContactNumber;
    }

    public void setFirstPocContactNumber( String firstPocContactNumber ) {
        this.firstPocContactNumber = firstPocContactNumber;
    }

    public String getFirstPocEmail() {
        return firstPocEmail;
    }

    public void setFirstPocEmail( String firstPocEmail ) {
        this.firstPocEmail = firstPocEmail;
    }

    public String getSecondPocName() {
        return secondPocName;
    }

    public void setSecondPocName( String secondPocName ) {
        this.secondPocName = secondPocName;
    }

    public String getSecondPocContactNumber() {
        return secondPocContactNumber;
    }

    public void setSecondPocContactNumber( String secondPocContactNumber ) {
        this.secondPocContactNumber = secondPocContactNumber;
    }

    public String getSecondPocEmail() {
        return secondPocEmail;
    }

    public void setSecondPocEmail( String secondPocEmail ) {
        this.secondPocEmail = secondPocEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getVendorRemark() {
        return vendorRemark;
    }

    public void setVendorRemark( String vendorRemark ) {
        this.vendorRemark = vendorRemark;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate( Date effectiveDate ) {
        this.effectiveDate = effectiveDate;
    }

    public Date getObsoleteDate() {
        return obsoleteDate;
    }

    public void setObsoleteDate( Date obsoleteDate ) {
        this.obsoleteDate = obsoleteDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy( String updatedBy ) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn( Date updatedOn ) {
        this.updatedOn = updatedOn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark( String remark ) {
        this.remark = remark;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode( String vendorCode ) {
        this.vendorCode = vendorCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Vendor other = ( Vendor ) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        }
        else if ( !id.equals( other.id ) )
            return false;
        return true;
    }

    public String getFirstPocSalutation() {
        return firstPocSalutation;
    }

    public void setFirstPocSalutation( String firstPocSalutation ) {
        this.firstPocSalutation = firstPocSalutation;
    }

    public String getSecondPocSalutation() {
        return secondPocSalutation;
    }

    public void setSecondPocSalutation( String secondPocSalutation ) {
        this.secondPocSalutation = secondPocSalutation;
    }
}
