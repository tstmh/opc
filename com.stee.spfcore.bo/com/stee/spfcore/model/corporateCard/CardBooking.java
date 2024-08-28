package com.stee.spfcore.model.corporateCard;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.DateStartEnd;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.vo.corporateCard.CardCollectionLessDetail;
import com.stee.spfcore.vo.corporateCard.CardNextHolderPreferredContacts;
import com.stee.spfcore.vo.corporateCard.CardPreviousHolderPreferredContacts;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "CARD_BOOKING", schema = "SPFCORE" )
@XStreamAlias( "CardBooking" )
@Audited
public class CardBooking {
    @Id
    @GenericGenerator( name = "CardBookingGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "CardBookingGenerator" )
    @Column( name = "ID" )
    private String id;

    @Column( name = "UPDATER" )
    private String updater;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "UPDATE_TIMESTAMP" )
    private Date updateDateTime;

    @Column( name = "SUBMITTER" )
    private String submitter;

    @Column( name = "CARD_TYPE_ID" )
    private String cardTypeId;

    @Column( name = "NRIC" )
    private String nric;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "DATE_START" ) ), @AttributeOverride( name = "end", column = @Column( name = "DATE_END" ) ) } )
    private DateStartEnd dateStartEnd;

    @Column( name = "NUM_OF_CARDS" )
    private Integer numberOfCards;

    @Enumerated( EnumType.STRING )
    @Column( name = "STATUS" )
    private CardBookingStatus status;

    @Column( name = "CANCEL_REASONS", length = 1000 )
    private String cancelReasons;

    @Column( name = "CANCELLED_BY" )
    private String cancelledBy;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "CARD_BOOKING_ALLOCATED_CARD_SERIAL_NOS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "CARD_BOOKING_ID" ) )
    @Column( name = "ALLOCATED_CARD_SERIAL_NO" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > allocatedCardSerialNumbers;

    @Column( name = "REMARKS" )
    private String remarks;

    @Transient
    @XStreamOmitField
    private int numberOfPreviousApprovedBookings;

    @Transient
    @XStreamOmitField
    private String department;

    @Transient
    @XStreamOmitField
    private PersonalPreferredContacts personalPreferredContacts;

    @Transient
    @XStreamOmitField
    private List< PersonalPreferredContacts > onBehalfUposPreferredContacts;

    @Transient
    @XStreamOmitField
    private List< CardNextHolderPreferredContacts > nextHolderPreferredContacts;

    @Transient
    @XStreamOmitField
    private List< String > cardSerialNumbersToHandoverToCollectionCenter;

    @Transient
    @XStreamOmitField
    private List< CardPreviousHolderPreferredContacts > previousHolderPreferredContacts;

    @Transient
    @XStreamOmitField
    private List< String > cardSerialNumbersToTakeoverFromCollectionCenter;

    @Transient
    @XStreamOmitField
    private CardTypeDetail cardTypeDetail;

    @Transient
    @XStreamOmitField
    private List< CardCollectionLessDetail > cardCollectionDetails;

    @Transient
    @XStreamOmitField
    private String cardTypeName;

    // ========================
    // following are deprecated
    // to be remove in future after changes in bpm apps
    // use personalPreferredContacts instead
    // ========================

    @Transient
    @XStreamOmitField
    private String name;

    @Transient
    @XStreamOmitField
    private ContactMode preferredContactMode;

    @Transient
    @XStreamOmitField
    private String preferredMobile;

    @Transient
    @XStreamOmitField
    private String preferredEmail;

    // ========================

    public CardBooking() {
        // DO NOTHING
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

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards( Integer numberOfCards ) {
        this.numberOfCards = numberOfCards;
    }

    public CardBookingStatus getStatus() {
        return status;
    }

    public void setStatus( CardBookingStatus status ) {
        this.status = status;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public DateStartEnd getDateStartEnd() {
        return dateStartEnd;
    }

    public void setDateStartEnd( DateStartEnd dateStartEnd ) {
        this.dateStartEnd = dateStartEnd;
    }

    public String getCancelReasons() {
        return cancelReasons;
    }

    public void setCancelReasons( String cancelReasons ) {
        this.cancelReasons = cancelReasons;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId( String cardTypeId ) {
        this.cardTypeId = cardTypeId;
    }

    public List< String > getAllocatedCardSerialNumbers() {
        return allocatedCardSerialNumbers;
    }

    public void setAllocatedCardSerialNumbers( List< String > allocatedCardSerialNumbers ) {
        this.allocatedCardSerialNumbers = allocatedCardSerialNumbers;
    }

    public int getNumberOfPreviousApprovedBookings() {
        return numberOfPreviousApprovedBookings;
    }

    public void setNumberOfPreviousApprovedBookings( int numberOfPreviousApprovedBookings ) {
        this.numberOfPreviousApprovedBookings = numberOfPreviousApprovedBookings;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department = department;
    }

    public ContactMode getPreferredContactMode() {
        return preferredContactMode;
    }

    public void setPreferredContactMode( ContactMode preferredContactMode ) {
        this.preferredContactMode = preferredContactMode;
    }

    public String getPreferredMobile() {
        return preferredMobile;
    }

    public void setPreferredMobile( String preferredMobile ) {
        this.preferredMobile = preferredMobile;
    }

    public String getPreferredEmail() {
        return preferredEmail;
    }

    public void setPreferredEmail( String preferredEmail ) {
        this.preferredEmail = preferredEmail;
    }

    public List< CardPreviousHolderPreferredContacts > getPreviousHolderPreferredContacts() {
        return previousHolderPreferredContacts;
    }

    public void setPreviousHolderPreferredContacts( List< CardPreviousHolderPreferredContacts > previousHolderPreferredContacts ) {
        this.previousHolderPreferredContacts = previousHolderPreferredContacts;
    }

    public PersonalPreferredContacts getPersonalPreferredContacts() {
        return personalPreferredContacts;
    }

    public void setPersonalPreferredContacts( PersonalPreferredContacts personalPreferredContacts ) {
        this.personalPreferredContacts = personalPreferredContacts;
    }

    public List< PersonalPreferredContacts > getOnBehalfUposPreferredContacts() {
        return onBehalfUposPreferredContacts;
    }

    public void setOnBehalfUposPreferredContacts( List< PersonalPreferredContacts > onBehalfUposPreferredContacts ) {
        this.onBehalfUposPreferredContacts = onBehalfUposPreferredContacts;
    }

    public List< CardNextHolderPreferredContacts > getNextHolderPreferredContacts() {
        return nextHolderPreferredContacts;
    }

    public void setNextHolderPreferredContacts( List< CardNextHolderPreferredContacts > nextHolderPreferredContacts ) {
        this.nextHolderPreferredContacts = nextHolderPreferredContacts;
    }

    public CardTypeDetail getCardTypeDetail() {
        return cardTypeDetail;
    }

    public void setCardTypeDetail( CardTypeDetail cardTypeDetail ) {
        this.cardTypeDetail = cardTypeDetail;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter( String submitter ) {
        this.submitter = submitter;
    }

    public List< String > getCardSerialNumbersToHandoverToCollectionCenter() {
        return cardSerialNumbersToHandoverToCollectionCenter;
    }

    public void setCardSerialNumbersToHandoverToCollectionCenter( List< String > cardSerialNumbersToHandoverToCollectionCenter ) {
        this.cardSerialNumbersToHandoverToCollectionCenter = cardSerialNumbersToHandoverToCollectionCenter;
    }

    public List< String > getCardSerialNumbersToTakeoverFromCollectionCenter() {
        return cardSerialNumbersToTakeoverFromCollectionCenter;
    }

    public void setCardSerialNumbersToTakeoverFromCollectionCenter( List< String > cardSerialNumbersToTakeoverFromCollectionCenter ) {
        this.cardSerialNumbersToTakeoverFromCollectionCenter = cardSerialNumbersToTakeoverFromCollectionCenter;
    }

    public List< CardCollectionLessDetail > getCardCollectionDetails() {
        return cardCollectionDetails;
    }

    public void setCardCollectionDetails( List< CardCollectionLessDetail > cardCollectionDetails ) {
        this.cardCollectionDetails = cardCollectionDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks( String remarks ) {
        this.remarks = remarks;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName( String cardTypeName ) {
        this.cardTypeName = cardTypeName;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy( String cancelledBy ) {
        this.cancelledBy = cancelledBy;
    }
}
