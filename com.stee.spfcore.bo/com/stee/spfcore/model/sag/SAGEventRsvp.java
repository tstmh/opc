package com.stee.spfcore.model.sag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_EVENT_RSVP\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGEventRsvp")
@Audited
@SequenceDef (name="SAGEventRsvp_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGEventRsvp {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column( name = "\"FINANCIAL_YEAR\"", length = 10 )
	private String financialYear;
	
	@Column( name = "\"EVENT_ID\"", length = 100)
	private String eventId;
	
	@Column( name = "\"SEQ_NUMBER_REFERENCE\"", length = 100)
	private String sequenceNumberReference; // Comma-separated sequence Number(s) for Reference
	
	@Column( name = "\"ATTENDEE_NAME\"", length = 100 )
	private String attendeeName;
	
	@Column( name = "\"ATTENDEE_ID\"", length = 50 )
	private String attendeeId;
	
	@Column( name = "\"IS_CHILD\"" )
	private Boolean isAttendeeChild = Boolean.FALSE;
	
	@Column( name = "\"IS_APPLICANT\"" )
	private Boolean isAttendeeApplicant = Boolean.FALSE;
	
	@Column( name = "\"RSVP\"" )
	private Boolean rsvp = Boolean.FALSE;
	
	@Column( name = "\"REASON_FOR_ABSENCE\"", length=255 )
	private String reasonForAbsence;
	
	@Column( name = "\"IS_VEGETARIAN\"" )
	private Boolean isVegetarian = Boolean.FALSE;
	
	@Column( name = "\"RSVP_SUBMITTED_BY\"", length=10 )
	private String rsvpSubmittedBy;
	
	@Column( name = "\"RSVP_SUBMISSION_DATE\"" )
	private Date dateOfRsvpSubmission;
	
	@Column( name = "\"RSVP_SUBMISSION_STATUS\"", length=50 )
	private String rsvpSubmissionStatus;
	
	@Column(name = "\"LIST_ORDER\"")
	private Integer order;
	
	public SAGEventRsvp() {
		super();
	}

	public SAGEventRsvp( String financialYear, String eventId,
			String sequenceNumberReference, String attendeeName,
			String attendeeId, Boolean isAttendeeChild,
			Boolean isAttendeeApplicant, Boolean rsvp, String reasonForAbsence,
			Boolean isVegetarian, String rsvpSubmittedBy,
			Date dateOfRsvpSubmission, String rsvpSubmissionStatus,
			Integer order ) {
		super();
		this.financialYear = financialYear;
		this.eventId = eventId;
		this.sequenceNumberReference = sequenceNumberReference;
		this.attendeeName = attendeeName;
		this.attendeeId = attendeeId;
		this.isAttendeeChild = isAttendeeChild;
		this.isAttendeeApplicant = isAttendeeApplicant;
		this.rsvp = rsvp;
		this.reasonForAbsence = reasonForAbsence;
		this.isVegetarian = isVegetarian;
		this.rsvpSubmittedBy = rsvpSubmittedBy;
		this.dateOfRsvpSubmission = dateOfRsvpSubmission;
		this.rsvpSubmissionStatus = rsvpSubmissionStatus;
		this.order = order;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId( String eventId ) {
		this.eventId = eventId;
	}

	public String getSequenceNumberReference() {
		return sequenceNumberReference;
	}

	public void setSequenceNumberReference( String sequenceNumberReference ) {
		this.sequenceNumberReference = sequenceNumberReference;
	}

	public String getAttendeeName() {
		return attendeeName;
	}

	public void setAttendeeName( String attendeeName ) {
		this.attendeeName = attendeeName;
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId( String attendeeId ) {
		this.attendeeId = attendeeId;
	}

	public Boolean getIsAttendeeChild() {
		return isAttendeeChild;
	}

	public void setIsAttendeeChild( Boolean isAttendeeChild ) {
		this.isAttendeeChild = isAttendeeChild;
	}

	public Boolean getIsAttendeeApplicant() {
		return isAttendeeApplicant;
	}

	public void setIsAttendeeApplicant( Boolean isAttendeeApplicant ) {
		this.isAttendeeApplicant = isAttendeeApplicant;
	}

	public Boolean getRsvp() {
		return rsvp;
	}

	public void setRsvp( Boolean rsvp ) {
		this.rsvp = rsvp;
	}

	public String getReasonForAbsence() {
		return reasonForAbsence;
	}

	public void setReasonForAbsence( String reasonForAbsence ) {
		this.reasonForAbsence = reasonForAbsence;
	}

	public Boolean getIsVegetarian() {
		return isVegetarian;
	}

	public void setIsVegetarian( Boolean isVegetarian ) {
		this.isVegetarian = isVegetarian;
	}

	public String getRsvpSubmittedBy() {
		return rsvpSubmittedBy;
	}

	public void setRsvpSubmittedBy( String rsvpSubmittedBy ) {
		this.rsvpSubmittedBy = rsvpSubmittedBy;
	}

	public Date getDateOfRsvpSubmission() {
		return dateOfRsvpSubmission;
	}

	public void setDateOfRsvpSubmission( Date dateOfRsvpSubmission ) {
		this.dateOfRsvpSubmission = dateOfRsvpSubmission;
	}

	public String getRsvpSubmissionStatus() {
		return rsvpSubmissionStatus;
	}

	public void setRsvpSubmissionStatus( String rsvpSubmissionStatus ) {
		this.rsvpSubmissionStatus = rsvpSubmissionStatus;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder( Integer order ) {
		this.order = order;
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
		SAGEventRsvp other = (SAGEventRsvp) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}

}
