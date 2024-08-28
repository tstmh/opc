package com.stee.spfcore.model.corporateCard;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.TimeStartEnd;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "CARD_COLLECTION_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "CardCollectionDetail" )
@Audited
public class CardCollectionDetail {
    @Id
    @GenericGenerator( name = "CardCollectionDetailGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "CardCollectionDetailGenerator" )
    @Column( name = "ID" )
    @XStreamOmitField
    private String id;

    @Column( name = "DAYS_DESCRIPTION" )
    private String daysDescription;

    @Embedded
    @AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "START_TIME" ) ), @AttributeOverride( name = "end", column = @Column( name = "END_TIME" ) ) } )
    private TimeStartEnd timeStartEnd;

    @Column( name = "LOCATION" )
    private String location;

    public CardCollectionDetail() {
        // DO NOTHING
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getDaysDescription() {
        return daysDescription;
    }

    public void setDaysDescription( String daysDescription ) {
        this.daysDescription = daysDescription;
    }

    public TimeStartEnd getTimeStartEnd() {
        return timeStartEnd;
    }

    public void setTimeStartEnd( TimeStartEnd timeStartEnd ) {
        this.timeStartEnd = timeStartEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation( String location ) {
        this.location = location;
    }
}
