package com.stee.spfcore.vo.sag;

import java.util.Date;
import java.util.List;

public class SAGEmailContentDetails {

	private Date rsvpCutOffDate;
	private Date chequeCollectionStartDate;
	private Date chequeCollectionEndDate;
	private String chequeCollectionLocation;
	
	private List<SAGApplicantEmailContentDetail> applicantEmailDetails;

	public SAGEmailContentDetails() {
		super();
	}

	public SAGEmailContentDetails( Date rsvpCutOffDate, Date chequeCollectionStartDate, Date chequeCollectionEndDate,
			String chequeCollectionLocation, List<SAGApplicantEmailContentDetail> applicantEmailDetails ) {
		super();
		this.rsvpCutOffDate = rsvpCutOffDate;
		this.chequeCollectionStartDate = chequeCollectionStartDate;
		this.chequeCollectionEndDate = chequeCollectionEndDate;
		this.chequeCollectionLocation = chequeCollectionLocation;
		this.applicantEmailDetails = applicantEmailDetails;
	}

	public Date getRsvpCutOffDate() {
		return rsvpCutOffDate;
	}

	public void setRsvpCutOffDate( Date rsvpCutOffDate ) {
		this.rsvpCutOffDate = rsvpCutOffDate;
	}

	public Date getChequeCollectionStartDate() {
		return chequeCollectionStartDate;
	}

	public void setChequeCollectionStartDate(Date chequeCollectionStartDate) {
		this.chequeCollectionStartDate = chequeCollectionStartDate;
	}

	public Date getChequeCollectionEndDate() {
		return chequeCollectionEndDate;
	}

	public void setChequeCollectionEndDate(Date chequeCollectionEndDate) {
		this.chequeCollectionEndDate = chequeCollectionEndDate;
	}

	public String getChequeCollectionLocation() {
		return chequeCollectionLocation;
	}

	public void setChequeCollectionLocation(String chequeCollectionLocation) {
		this.chequeCollectionLocation = chequeCollectionLocation;
	}

	public List<SAGApplicantEmailContentDetail> getApplicantEmailDetails() {
		return applicantEmailDetails;
	}

	public void setApplicantEmailDetails(
			List<SAGApplicantEmailContentDetail> applicantEmailDetails ) {
		this.applicantEmailDetails = applicantEmailDetails;
	}

	@Override
	public String toString() {
		return "SAGEmailContentDetails [eventDate=" + rsvpCutOffDate
				+ ", applicantEmailDetails=" + applicantEmailDetails + "]";
	}
	
}
