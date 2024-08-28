package com.stee.spfcore.webapi.model.sag;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.DateStartEnd;
import com.stee.spfcore.webapi.model.TimeStartEnd;
import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_EVENT_DETAIL\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGEventDetail")
@Audited
@SequenceDef (name="SAGEventDetail_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGEventDetail {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column( name = "\"FINANCIAL_YEAR\"", length=10 )
	private String financialYear;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column( name = "\"EVENT_DATE_TIME\"")
	private Date eventDateTime;
	
	@Column( name = "\"VENUE\"", length=100 )
	private String venue;
	
	@Column( name = "\"VENUE_ADDRESS\"", length=255 )
	private String venueAddress;
	
	@Column( name = "\"VENUE_POSTAL_CODE\"", length=10 )
	private String venuePostalCode;
	
	@Column( name = "\"GUEST_DESIGNATION\"", length=50 )
	private String guestOfHonorDesignation;
	
	@Column( name = "\"GUEST_TITLE\"", length=10 )
	private String guestOfHonorTitle;
	
	@Column( name = "\"GUEST_NAME\"", length=100 )
	private String guestOfHonorName;
	
	@AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "REGISTARTION_START_DATE" ) ), @AttributeOverride( name = "end", column = @Column( name = "REGISTRATION_END_DATE" ) ) } )
	private TimeStartEnd registration;
	
	@Column( name = "\"CHEQUE_COLLECT_LOCATION\"", length=100 )
	private String chequeCollectionLocation;
	
	@AttributeOverrides( { @AttributeOverride( name = "start", column = @Column( name = "CHEQUE_COLLECT_START_DATE" ) ), @AttributeOverride( name = "end", column = @Column( name = "CHEQUE_COLLECT_END_DATE" ) ) } )
	private DateStartEnd chequeCollection;
	
	@Column( name = "\"CONTACT_PERSON_TITLE\"", length=10 )
	private String contactPersonTitle;
	
	@Column( name = "\"CONTACT_PERSON_NAME\"", length=100 )
	private String contactPersonName;
	
	@Column( name = "\"CONTACT_NUMBER\"", length = 16 )
	private String contactPersonNumber;
	
	@Column( name = "\"CONTACT_EMAIL\"", length=256 )
	private String contactPersonEmail;
	
	@Column( name = "\"UPDATED_BY\"", length=100 )
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column( name = "\"UPDATED_DATE\"")
	private Date updatedDate;

	public SAGEventDetail() {
		super();
	}

	public SAGEventDetail( String financialYear, Date eventDateTime,
			String venue, String venueAddress, String venuePostalCode,
			String guestOfHonorDesignation, String guestOfHonorTitle,
			String guestOfHonorName, TimeStartEnd registration,
			String chequeCollectionLocation, DateStartEnd chequeCollection,
			String contactPersonTitle, String contactPersonName,
			String contactPersonNumber, String contactPersonEmail,
			String updatedBy, Date updatedDate ) {
		super();
		this.financialYear = financialYear;
		this.eventDateTime = eventDateTime;
		this.venue = venue;
		this.venueAddress = venueAddress;
		this.venuePostalCode = venuePostalCode;
		this.guestOfHonorDesignation = guestOfHonorDesignation;
		this.guestOfHonorTitle = guestOfHonorTitle;
		this.guestOfHonorName = guestOfHonorName;
		this.registration = registration;
		this.chequeCollectionLocation = chequeCollectionLocation;
		this.chequeCollection = chequeCollection;
		this.contactPersonTitle = contactPersonTitle;
		this.contactPersonName = contactPersonName;
		this.contactPersonNumber = contactPersonNumber;
		this.contactPersonEmail = contactPersonEmail;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public Date getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime( Date eventDateTime ) {
		this.eventDateTime = eventDateTime;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue( String venue ) {
		this.venue = venue;
	}

	public String getVenueAddress() {
		return venueAddress;
	}

	public void setVenueAddress( String venueAddress ) {
		this.venueAddress = venueAddress;
	}

	public String getVenuePostalCode() {
		return venuePostalCode;
	}

	public void setVenuePostalCode( String venuePostalCode ) {
		this.venuePostalCode = venuePostalCode;
	}

	public String getGuestOfHonorDesignation() {
		return guestOfHonorDesignation;
	}

	public void setGuestOfHonorDesignation( String guestOfHonorDesignation ) {
		this.guestOfHonorDesignation = guestOfHonorDesignation;
	}

	public String getGuestOfHonorTitle() {
		return guestOfHonorTitle;
	}

	public void setGuestOfHonorTitle( String guestOfHonorTitle ) {
		this.guestOfHonorTitle = guestOfHonorTitle;
	}

	public String getGuestOfHonorName() {
		return guestOfHonorName;
	}

	public void setGuestOfHonorName( String guestOfHonorName ) {
		this.guestOfHonorName = guestOfHonorName;
	}

	public TimeStartEnd getRegistration() {
		return registration;
	}

	public void setRegistration( TimeStartEnd registration ) {
		this.registration = registration;
	}

	public String getChequeCollectionLocation() {
		return chequeCollectionLocation;
	}

	public void setChequeCollectionLocation( String chequeCollectionLocation ) {
		this.chequeCollectionLocation = chequeCollectionLocation;
	}

	public DateStartEnd getChequeCollection() {
		return chequeCollection;
	}

	public void setChequeCollection( DateStartEnd chequeCollection ) {
		this.chequeCollection = chequeCollection;
	}

	public String getContactPersonTitle() {
		return contactPersonTitle;
	}

	public void setContactPersonTitle( String contactPersonTitle ) {
		this.contactPersonTitle = contactPersonTitle;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName( String contactPersonName ) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonNumber() {
		return contactPersonNumber;
	}

	public void setContactPersonNumber( String contactPersonNumber ) {
		this.contactPersonNumber = contactPersonNumber;
	}

	public String getContactPersonEmail() {
		return contactPersonEmail;
	}

	public void setContactPersonEmail( String contactPersonEmail ) {
		this.contactPersonEmail = contactPersonEmail;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy( String updatedBy ) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate( Date updatedDate ) {
		this.updatedDate = updatedDate;
	}

	public String getId() {
		return id;
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
		SAGEventDetail other = (SAGEventDetail) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}
	
}
