package com.stee.spfcore.model.membership;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "\"MEMBERSHIP_DISCREPANCY_RECORD\"", schema = "\"SPFCORE\"" )
@Audited
public class DiscrepancyRecord {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column( name = "\"ID\"" )
    @XStreamOmitField
    private long id;

    @Column( name = "\"NRIC\"", length = 10 )
    private String nric;

    @Column( name = "\"MONTH\"" )
    private Integer month;

    @Column( name = "\"YEAR\"" )
    private Integer year;

    @Column( name = "\"FEE\"" )
    private Double fee;

    @Column( name = "\"BALANCE\"" )
    private Double balance;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "\"RECORD_ID\"" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< PaymentHistory > paymentHistories;

    @Column( name = "\"RANK\"", length = 50 )
    private String rank;

    @Column( name = "\"ORGANISATION_OR_DEPARTMENT\"", length = 50 )
    private String organisationOrDepartment;

    @Column( name = "\"SUBUNIT\"", length = 50 )
    private String subunit;

    // following fields are used for reconciliation report
    // and not used for persistence.
    @Transient
    private String name;

    @Transient
    private Date appointmentDate;

    @Transient
    private Double amountCollected;

    @Transient
    private Double offset;

    public DiscrepancyRecord() {
        super();
    }


    public DiscrepancyRecord( String nric, Integer month, Integer year, Double fee, Double balance, List< PaymentHistory > paymentHistories, String rank, String organisationOrDepartment, String subunit ) {
        super();
        this.nric = nric;
        this.month = month;
        this.year = year;
        this.fee = fee;
        this.balance = balance;
        this.paymentHistories = paymentHistories;
        this.rank = rank;
        this.organisationOrDepartment = organisationOrDepartment;
        this.subunit = subunit;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth( Integer month ) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear( Integer year ) {
        this.year = year;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee( Double fee ) {
        this.fee = fee;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance( Double balance ) {
        this.balance = balance;
    }

    public List< PaymentHistory > getPaymentHistories() {
        return paymentHistories;
    }

    public void setPaymentHistories( List< PaymentHistory > paymentHistories ) {
        this.paymentHistories = paymentHistories;
    }

    public long getId() {
        return id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank( String rank ) {
        this.rank = rank;
    }

    public String getOrganisationOrDepartment() {
        return organisationOrDepartment;
    }

    public void setOrganisationOrDepartment( String organisationOrDepartment ) {
        this.organisationOrDepartment = organisationOrDepartment;
    }

    public String getSubunit() {
        return subunit;
    }

    public void setSubunit( String subunit ) {
        this.subunit = subunit;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate( Date appointmentDate ) {
        this.appointmentDate = appointmentDate;
    }

    public Double getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected( Double amountCollected ) {
        this.amountCollected = amountCollected;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset( Double offset ) {
        this.offset = offset;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( balance == null ) ? 0 : balance.hashCode() );
        result = prime * result + ( ( fee == null ) ? 0 : fee.hashCode() );
        result = prime * result + ( int ) ( id ^ ( id >>> 32 ) );
        result = prime * result + ( ( month == null ) ? 0 : month.hashCode() );
        result = prime * result + ( ( nric == null ) ? 0 : nric.hashCode() );
        result = prime * result + ( ( organisationOrDepartment == null ) ? 0 : organisationOrDepartment.hashCode() );
        result = prime * result + ( ( paymentHistories == null ) ? 0 : paymentHistories.hashCode() );
        result = prime * result + ( ( rank == null ) ? 0 : rank.hashCode() );
        result = prime * result + ( ( subunit == null ) ? 0 : subunit.hashCode() );
        result = prime * result + ( ( year == null ) ? 0 : year.hashCode() );
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
        DiscrepancyRecord other = ( DiscrepancyRecord ) obj;
        if ( balance == null ) {
            if ( other.balance != null )
                return false;
        }
        else if ( !balance.equals( other.balance ) )
            return false;
        if ( fee == null ) {
            if ( other.fee != null )
                return false;
        }
        else if ( !fee.equals( other.fee ) )
            return false;
        if ( id != other.id )
            return false;
        if ( month == null ) {
            if ( other.month != null )
                return false;
        }
        else if ( !month.equals( other.month ) )
            return false;
        if ( nric == null ) {
            if ( other.nric != null )
                return false;
        }
        else if ( !nric.equals( other.nric ) )
            return false;
        if ( organisationOrDepartment == null ) {
            if ( other.organisationOrDepartment != null )
                return false;
        }
        else if ( !organisationOrDepartment.equals( other.organisationOrDepartment ) )
            return false;
        if ( paymentHistories == null ) {
            if ( other.paymentHistories != null )
                return false;
        }
        else if ( !paymentHistories.equals( other.paymentHistories ) )
            return false;
        if ( rank == null ) {
            if ( other.rank != null )
                return false;
        }
        else if ( !rank.equals( other.rank ) )
            return false;
        if ( subunit == null ) {
            if ( other.subunit != null )
                return false;
        }
        else if ( !subunit.equals( other.subunit ) )
            return false;
        if ( year == null ) {
            if ( other.year != null )
                return false;
        }
        else if ( !year.equals( other.year ) )
            return false;
        return true;
    }
}
