package com.stee.spfcore.model.category;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.stee.spfcore.utils.DateUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "CATEGORY_SUBCATEGORIES", schema = "SPFCORE" )
@XStreamAlias( "SubCategory" )
@Audited
public class SubCategory implements Serializable {

    private static final long serialVersionUID = 8236124500918328279L;

    @Id
    @Column( name = "\"ID\"", length = 10 )
    private String id;

    @Column( name = "\"NAME\"", length = 100 )
    private String name;

    @Column( name = "\"DESCRIPTION\"", length = 2000 )
    private String description;

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

    public SubCategory() {
        super();
    }

    public SubCategory( String id, String name, String description, Date effectiveDate, Date obsoleteDate, String updatedBy, Date updatedOn, String remark ) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
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
        if ( effectiveDate != null && !DateUtils.isAfterDay( effectiveDate, today ) && obsoleteDate == null || DateUtils.isAfterDay( obsoleteDate, today ) ) {
            enabled = true;
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
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

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( description == null ) ? 0 : description.hashCode() );
        result = prime * result + ( ( effectiveDate == null ) ? 0 : effectiveDate.hashCode() );
        result = prime * result + ( enabled ? 1231 : 1237 );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( obsoleteDate == null ) ? 0 : obsoleteDate.hashCode() );
        result = prime * result + ( ( remark == null ) ? 0 : remark.hashCode() );
        result = prime * result + ( ( updatedBy == null ) ? 0 : updatedBy.hashCode() );
        result = prime * result + ( ( updatedOn == null ) ? 0 : updatedOn.hashCode() );
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
        SubCategory other = ( SubCategory ) obj;
        if ( description == null ) {
            if ( other.description != null )
                return false;
        }
        else if ( !description.equals( other.description ) )
            return false;
        if ( effectiveDate == null ) {
            if ( other.effectiveDate != null )
                return false;
        }
        else if ( !effectiveDate.equals( other.effectiveDate ) )
            return false;
        if ( enabled != other.enabled )
            return false;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        }
        else if ( !id.equals( other.id ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        }
        else if ( !name.equals( other.name ) )
            return false;
        if ( obsoleteDate == null ) {
            if ( other.obsoleteDate != null )
                return false;
        }
        else if ( !obsoleteDate.equals( other.obsoleteDate ) )
            return false;
        if ( remark == null ) {
            if ( other.remark != null )
                return false;
        }
        else if ( !remark.equals( other.remark ) )
            return false;
        if ( updatedBy == null ) {
            if ( other.updatedBy != null )
                return false;
        }
        else if ( !updatedBy.equals( other.updatedBy ) )
            return false;
        if ( updatedOn == null ) {
            if ( other.updatedOn != null )
                return false;
        }
        else if ( !updatedOn.equals( other.updatedOn ) )
            return false;
        return true;
    }
}
