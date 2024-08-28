package com.stee.spfcore.vo.sag;

public class SAGRsvpTemplate {
	
	private int rowNumber;
	
	private String referenceNumber;
	
	private String applicantId;
	
	private String attendeeId;
	
	private String attendeeName;
	
	private String attendeeIsRecipient;
	
	private String recipientSequenceNumber;
	
	private String recipientAmendedName;
	
	private String attendance;
	
	private String reasonForAbsence;
	
	private String vegMealRequired;

	public SAGRsvpTemplate() {
		super();
	}

	public SAGRsvpTemplate( int rowNumber, String referenceNumber, String applicantId,
			String attendeeId, String attendeeName, String attendeeIsRecipient,
			String recipientSequenceNumber, String recipientAmendedName,
			String attendance, String reasonForAbsence, String vegMealRequired ) {
		super();
		this.rowNumber = rowNumber;
		this.referenceNumber = referenceNumber;
		this.applicantId = applicantId;
		this.attendeeId = attendeeId;
		this.attendeeName = attendeeName;
		this.attendeeIsRecipient = attendeeIsRecipient;
		this.recipientSequenceNumber = recipientSequenceNumber;
		this.recipientAmendedName = recipientAmendedName;
		this.attendance = attendance;
		this.reasonForAbsence = reasonForAbsence;
		this.vegMealRequired = vegMealRequired;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber( String referenceNumber ) {
		this.referenceNumber = referenceNumber;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId( String applicantId ) {
		this.applicantId = applicantId;
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId( String attendeeId ) {
		this.attendeeId = attendeeId;
	}

	public String getAttendeeName() {
		return attendeeName;
	}

	public void setAttendeeName( String attendeeeName ) {
		this.attendeeName = attendeeeName;
	}

	public String getAttendeeIsRecipient() {
		return attendeeIsRecipient;
	}

	public void setAttendeeIsRecipient( String attendeeIsRecipient ) {
		this.attendeeIsRecipient = attendeeIsRecipient;
	}

	public String getRecipientSequenceNumber() {
		return recipientSequenceNumber;
	}

	public void setRecipientSequenceNumber( String recipientSequenceNumber ) {
		this.recipientSequenceNumber = recipientSequenceNumber;
	}

	public String getRecipientAmendedName() {
		return recipientAmendedName;
	}

	public void setRecipientAmendedName( String recipientAmendedName ) {
		this.recipientAmendedName = recipientAmendedName;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance( String attendance ) {
		this.attendance = attendance;
	}

	public String getReasonForAbsence() {
		return reasonForAbsence;
	}

	public void setReasonForAbsence( String reasonForAbsence ) {
		this.reasonForAbsence = reasonForAbsence;
	}

	public String getVegMealRequired() {
		return vegMealRequired;
	}

	public void setVegMealRequired( String vegMealRequired ) {
		this.vegMealRequired = vegMealRequired;
	}

	@Override
	public String toString() {
		return "SAGRsvpTemplate [referenceNumber=" + referenceNumber
				+ ", applicantId=" + applicantId + ", attendeeId=" + attendeeId
				+ ", attendeeeName=" + attendeeName + ", attendeeIsRecipient="
				+ attendeeIsRecipient + ", recipientSequenceNumber="
				+ recipientSequenceNumber + ", recipientAmendedName="
				+ recipientAmendedName + ", attendance=" + attendance
				+ ", reasonForAbsence=" + reasonForAbsence
				+ ", vegMealRequired=" + vegMealRequired + "]";
	}
	
	
}
