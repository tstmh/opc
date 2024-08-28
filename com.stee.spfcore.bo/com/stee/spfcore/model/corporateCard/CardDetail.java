package com.stee.spfcore.model.corporateCard;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.DateStartEnd;
import com.stee.spfcore.utils.DateTimeUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "CARD_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "CardDetail" )
@Audited
public class CardDetail {
    @Id
    @GenericGenerator( name = "CardDetailGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "CardDetailGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Column( name = "SERIAL_NO" )
    private String serialNumber;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "START_DATE" ) ), @AttributeOverride( name = "end", column = @Column( name = "EXPIRY_DATE" ) ) } )
    private DateStartEnd validityPeriod;

    @Enumerated( EnumType.STRING )
    @Column( name = "STATUS" )
    private CardStatus cardStatus;

    @Transient
    private String cardDisplayStatus;

    public CardDetail() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber( String serialNumber ) {
        this.serialNumber = serialNumber;
    }

    public DateStartEnd getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod( DateStartEnd validityPeriod ) {
        this.validityPeriod = validityPeriod;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus( CardStatus cardStatus ) {
        this.cardStatus = cardStatus;
    }

    public String getCardDisplayStatus() {
        return cardDisplayStatus;
    }

    public void setCardDisplayStatus( String cardDisplayStatus ) {
        this.cardDisplayStatus = cardDisplayStatus;
    }

    @PostLoad
    protected void onLoad() {
        if ( this.cardStatus == CardStatus.AUTO_POPULATE ) {
            Date today = DateTimeUtil.getDateDaysAfterToday( 0 );
            Date cardStartDate = null;
            Date cardEndDate = null;
            if ( this.validityPeriod != null ) {
                cardStartDate = DateTimeUtil.getDateDaysAfter( this.validityPeriod.getStart(), 0 );
                cardEndDate = DateTimeUtil.getDateDaysAfter( this.validityPeriod.getEnd(), 1 );
            }
            String statusCard = "Expired";
            if ( cardStartDate != null && cardEndDate != null) {
                if ( today.getTime() < cardStartDate.getTime() ) {
                    statusCard = "Dormant";
                }
                else if ( today.getTime() < cardEndDate.getTime() ) {
                    statusCard = "Active";
                }
            }
            this.cardDisplayStatus = statusCard;
        }
        else if ( this.cardStatus == null ) {
            this.cardDisplayStatus = "";
        }
        else {
            this.cardDisplayStatus = this.cardStatus.toString();
        }
    }
}
