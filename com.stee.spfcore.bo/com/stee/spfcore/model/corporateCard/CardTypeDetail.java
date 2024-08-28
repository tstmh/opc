package com.stee.spfcore.model.corporateCard;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "CARD_TYPE_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "CardTypeDetail" )
@Audited
public class CardTypeDetail {
    @Id
    @GenericGenerator( name = "CardTypeDetailGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "CardTypeDetailGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "UPDATER" )
    private String updater;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "UPDATE_TIMESTAMP" )
    private Date updateDateTime;

    @Column( name = "NAME" )
    private String name;

    @Enumerated( EnumType.STRING )
    @Column( name = "TYPE" )
    private CardType type;

    @Column( name = "DEPARTMENT" )
    private String department;

    @Column( name = "ADV_BOOKING_PERIOD_MONTHS" )
    private Integer advancedBookingPeriodMonths;

    @Column( name = "ENTRIES_PER_CARD", length = 2000 )
    private String entriesPerCard;

    @Column( name = "MAX_CARDS_PER_BOOKING" )
    private Integer maxCardsPerBooking;

    @Column( name = "MAX_BOOKINGS_PER_MONTH" )
    private Integer maxBookingsPerMonth;

    @Enumerated( EnumType.STRING )
    @Column( name = "ALLOCATION_RULE" )
    private AllocationRule allocationRule;

    @Column( name = "RETENTION_DAYS" )
    private Integer retentionDays;

    @Enumerated(EnumType.STRING)
    @Column ( name = "STATUS" )
    private CardTypeDetailStatus status;

    @Column( name = "TERMS_AND_CONDITIONS", length = 5000 )
    private String termsAndConditions;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "CARD_TYPE_DETAILS_COSHARE_DEPARTMENTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "CARD_TYPE_DETAILS_ID" ) )
    @Column( name = "COSHARE_DEPARTMENT" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > coShareDepartments;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "CARD_TYPE_DETAILS_COLLECTION_DETAILS_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "CARD_TYPE_DETAILS_ID" ), inverseJoinColumns = @JoinColumn( name = "CARD_COLLECTION_DETAILS_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< CardCollectionDetail > collectionDetails;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinTable( name = "CARD_TYPE_DETAILS_CARD_DETAILS_JOINTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "CARD_TYPE_DETAILS_ID" ), inverseJoinColumns = @JoinColumn( name = "CARD_DETAILS_ID" ) )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< CardDetail > cardDetails;

    @Transient
    @XStreamOmitField
    private transient int processingDays;

    public int getProcessingDays() {
        return processingDays;
    }

    public void setProcessingDays( int processingDays ) {
        this.processingDays = processingDays;
    }

    @Transient
    @XStreamOmitField
    private transient Date visitDateStart;

    @Transient
    @XStreamOmitField
    private transient Date visitDateEnd;

    public CardTypeDetail() {
        // This method is intentionally left empty
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater( String updater ) {
        this.updater = updater;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime( Date updateDateTime ) {
        this.updateDateTime = updateDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public CardType getType() {
        return type;
    }

    public void setType( CardType type ) {
        this.type = type;
    }

    public Integer getAdvancedBookingPeriodMonths() {
        return advancedBookingPeriodMonths;
    }

    public void setAdvancedBookingPeriodMonths( Integer advancedBookingPeriodMonths ) {
        this.advancedBookingPeriodMonths = advancedBookingPeriodMonths;
    }

    public String getEntriesPerCard() {
        return entriesPerCard;
    }

    public void setEntriesPerCard( String entriesPerCard ) {
        this.entriesPerCard = entriesPerCard;
    }

    public Integer getMaxCardsPerBooking() {
        return maxCardsPerBooking;
    }

    public void setMaxCardsPerBooking( Integer maxCardsPerBooking ) {
        this.maxCardsPerBooking = maxCardsPerBooking;
    }

    public Integer getMaxBookingsPerMonth() {
        return maxBookingsPerMonth;
    }

    public void setMaxBookingsPerMonth( Integer maxBookingsPerMonth ) {
        this.maxBookingsPerMonth = maxBookingsPerMonth;
    }

    public AllocationRule getAllocationRule() {
        return allocationRule;
    }

    public void setAllocationRule( AllocationRule allocationRule ) {
        this.allocationRule = allocationRule;
    }

    public Integer getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays( Integer retentionDays ) {
        this.retentionDays = retentionDays;
    }

    public List< String > getCoShareDepartments() {
        return coShareDepartments;
    }

    public void setCoShareDepartments( List< String > coShareDepartments ) {
        this.coShareDepartments = coShareDepartments;
    }

    public List< CardCollectionDetail > getCollectionDetails() {
        return collectionDetails;
    }

    public void setCollectionDetails( List< CardCollectionDetail > collectionDetails ) {
        this.collectionDetails = collectionDetails;
    }

    public List< CardDetail > getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails( List< CardDetail > cardDetails ) {
        this.cardDetails = cardDetails;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department = department;
    }

    public Date getVisitDateStart() {
        return visitDateStart;
    }

    public void setVisitDateStart( Date visitDateStart ) {
        this.visitDateStart = visitDateStart;
    }

    public Date getVisitDateEnd() {
        return visitDateEnd;
    }

    public void setVisitDateEnd( Date visitDateEnd ) {
        this.visitDateEnd = visitDateEnd;
    }

    public CardTypeDetailStatus getStatus() {
        return status;
    }

    public void setStatus(CardTypeDetailStatus status) {
        this.status = status;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }



}
