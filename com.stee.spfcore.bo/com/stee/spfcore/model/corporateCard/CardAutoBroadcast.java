package com.stee.spfcore.model.corporateCard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "CARD_AUTO_BROADCAST", schema = "SPFCORE" )
@XStreamAlias( "CardAutoBroadcast" )
@Audited
public class CardAutoBroadcast {
    @Id
    @GenericGenerator( name = "CardAutoBroadcastGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "CardAutoBroadcastGenerator" )
    @Column( name = "ID" )
    private String id;

    @Enumerated( EnumType.STRING )
    @Column( name = "TYPE" )
    private CardType type;

    @Column( name = "DEPARTMENT" )
    private String department;

    @Enumerated( EnumType.STRING )
    @Column( name = "FREQUENCY" )
    private BroadcastFrequency frequency;

    @Column( name = "MINIMUM_CARDS" )
    private Integer minimumCards;

    @Column( name = "ACTIVE" )
    private Boolean active;

    public CardAutoBroadcast() {
        // DO NOTHING
    }

    public BroadcastFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency( BroadcastFrequency frequency ) {
        this.frequency = frequency;
    }

    public Integer getMinimumCards() {
        return minimumCards;
    }

    public void setMinimumCards( Integer minimumCards ) {
        this.minimumCards = minimumCards;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public CardType getType() {
        return type;
    }

    public void setType( CardType type ) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department = department;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive( Boolean active ) {
        this.active = active;
    }
}
