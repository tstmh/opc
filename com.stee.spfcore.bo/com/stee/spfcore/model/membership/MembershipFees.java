package com.stee.spfcore.model.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "\"MEMBERSHIP_FEES\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "MembershipFees" )
@Audited
public class MembershipFees {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "\"ID\"" )
    @XStreamOmitField
    private long id;

    @Column( name = "\"RANK_TYPE\"", length = 50 )
    private String rankType;

    @Column( name = "\"RANK\"", length = 50 )
    private String rank;

    @Column( name = "\"BASE_FEE\"" )
    private Double baseFee;

    @Column( name = "\"WITH_INSURANCE_FEE\"" )
    private Double withInsuranceFee;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"EFFECTIVE_DATE\"" )
    private Date effectiveDate;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"OBSOLETE_DATE\"" )
    private Date obsoleteDate;

    @Column( name = "\"UPDATED_BY\"", length = 50 )
    private String updatedBy;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"UPDATED_DATE\"", length = 50 )
    private Date updatedOn;

    public MembershipFees() {
        super();
    }

    public MembershipFees( String rankType, String rank, Double baseFee, Double withInsuranceFee, Date effectiveDate, Date obsoleteDate, String updatedBy, Date updatedOn ) {
        super();
        this.rankType = rankType;
        this.rank = rank;
        this.baseFee = baseFee;
        this.withInsuranceFee = withInsuranceFee;
        this.effectiveDate = effectiveDate;
        this.obsoleteDate = obsoleteDate;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getRankType() {
        return rankType;
    }

    public void setRankType( String rankType ) {
        this.rankType = rankType;
    }

    public String getRank() {
        return rank;
    }

    public void setRank( String rank ) {
        this.rank = rank;
    }

    public Double getBaseFee() {
        return baseFee;
    }

    public void setBaseFee( Double baseFee ) {
        this.baseFee = baseFee;
    }

    public Double getWithInsuranceFee() {
        return withInsuranceFee;
    }

    public void setWithInsuranceFee( Double withInsuranceFee ) {
        this.withInsuranceFee = withInsuranceFee;
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

    @Override
    public String toString() {
        return "MembershipFees [id=" + id + ", rankType=" + rankType + ", rank=" + rank + ", baseFee=" + baseFee + ", withInsuranceFee=" + withInsuranceFee + ", effectiveDate=" + effectiveDate + ", obsoleteDate=" + obsoleteDate
                + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
    }

}
