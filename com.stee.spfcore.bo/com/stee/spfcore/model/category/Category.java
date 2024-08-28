package com.stee.spfcore.model.category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import com.stee.spfcore.utils.DateUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "CATEGORY_CATEGORIES", schema = "SPFCORE" )
@XStreamAlias( "Category" )
@Audited
@FilterDef( name = "subcategoryFilter", parameters = @ParamDef( name = "subcategoryFilterParam", type = "date" ) )
public class Category implements Serializable {

    private static final long serialVersionUID = -31649511767111873L;

    @Id
    @Column( name = "\"ID\"", length = 10 )
    private String id;

    @Column( name = "\"NAME\"", length = 100 )
    private String name;

    @Column( name = "\"DESCRIPTION\"", length = 2000 )
    private String description;

    @Column( name = "\"IS_SYSTEM\"" )
    private boolean system;

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

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "CATEGORY_CATEGORIES_SUBCATEGORIES_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "CATEGORY_ID" ), inverseJoinColumns = @JoinColumn( name = "SUBCATEGORY_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    @Filter( name = "subcategoryFilter", condition = " (EFFECTIVE_DATE IS NOT NULL and EFFECTIVE_DATE <= :subcategoryFilterParam) " + " and (OBSOLETE_DATE IS NULL or OBSOLETE_DATE > :subcategoryFilterParam)" )
    private List< SubCategory > subCategories;

    public Category() {
        super();
    }

    public Category( String id, String name, String description, boolean system, Date effectiveDate, Date obsoleteDate, String updatedBy, Date updatedOn, String remark, List< SubCategory > subCategories ) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.system = system;
        this.effectiveDate = effectiveDate;
        this.obsoleteDate = obsoleteDate;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
        this.remark = remark;
        this.subCategories = subCategories;
    }

    @PostLoad
    protected void onLoad() {

        // Default is false
        enabled = false;

        Date today = new Date();

        // Only enabled if the effective date is not after today (include today)
        // and obsolete date is null or is not before today
        if ( effectiveDate != null && !DateUtils.isAfterDay( effectiveDate, today ) && obsoleteDate == null || DateUtils.isAfterDay( obsoleteDate, today )) {
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

    public boolean isSystem() {
        return system;
    }

    public void setSystem( boolean system ) {
        this.system = system;
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

    public List< SubCategory > getSubCategories() {
        return subCategories;
    }

    public void setSubCategories( List< SubCategory > subCategories ) {
        if ( this.subCategories == null ) {
            this.subCategories = new ArrayList<>();
        }
        this.subCategories.clear();
        if ( subCategories != null ) {
            this.subCategories.addAll( subCategories );
        }
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
        Category other = ( Category ) obj;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        }
        else if ( !id.equals( other.id ) )
            return false;
        return true;
    }

}
