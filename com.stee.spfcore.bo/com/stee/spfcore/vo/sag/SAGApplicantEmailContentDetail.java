package com.stee.spfcore.vo.sag;

import java.util.List;

import com.stee.spfcore.model.personnel.ContactMode;

public class SAGApplicantEmailContentDetail {
	
	private String submittedById;
	
	private String submittedByName;
	
	private ContactMode preferredContactMode;
		
	private String preferredEmail;
	
	private String workEmail;
	
	private String paymentAdviceEmail;
	
	private String preferredContactNumber;
	
	private List<SAGApplicationDetailForEmail> applicationDetailForEmailList;
	
	private SAGOnBehalfDetailForEmail onBehalfDetailForEmail;

	public SAGApplicantEmailContentDetail() {
		super();
	}

	public SAGApplicantEmailContentDetail( String submittedById,
			String submittedByName, ContactMode preferredContactMode,
			String preferredEmail, String workEmail, String paymentAdviceEmail,
			String preferredContactNumber,
			List<SAGApplicationDetailForEmail> applicationDetailForEmailList,
			SAGOnBehalfDetailForEmail onBehalfDetailForEmail ) {
		super();
		this.submittedById = submittedById;
		this.submittedByName = submittedByName;
		this.preferredContactMode = preferredContactMode;
		this.preferredEmail = preferredEmail;
		this.workEmail = workEmail;
		this.paymentAdviceEmail = paymentAdviceEmail;
		this.preferredContactNumber = preferredContactNumber;
		this.applicationDetailForEmailList = applicationDetailForEmailList;
		this.onBehalfDetailForEmail = onBehalfDetailForEmail;
	}

	public String getSubmittedById() {
		return submittedById;
	}

	public void setSubmittedById( String submittedById ) {
		this.submittedById = submittedById;
	}

	public String getSubmittedByName() {
		return submittedByName;
	}

	public void setSubmittedByName( String submittedByName ) {
		this.submittedByName = submittedByName;
	}

	public ContactMode getPreferredContactMode() {
		return preferredContactMode;
	}

	public void setPreferredContactMode( ContactMode preferredContactMode ) {
		this.preferredContactMode = preferredContactMode;
	}

	public String getPreferredEmail() {
		return preferredEmail;
	}

	public void setPreferredEmail( String preferredEmail ) {
		this.preferredEmail = preferredEmail;
	}

	public String getPaymentAdviceEmail() {
		return paymentAdviceEmail;
	}

	public void setPaymentAdviceEmail( String paymentAdviceEmail ) {
		this.paymentAdviceEmail = paymentAdviceEmail;
	}
	
	public String getWorkEmail() {
		return workEmail;
	}

	public void setWorkEmail( String workEmail ) {
		this.workEmail = workEmail;
	}

	public String getPreferredContactNumber() {
		return preferredContactNumber;
	}

	public void setPreferredContactNumber( String preferredContactNumber ) {
		this.preferredContactNumber = preferredContactNumber;
	}

	public List<SAGApplicationDetailForEmail> getApplicationDetailForEmailList() {
		return applicationDetailForEmailList;
	}

	public void setApplicationDetailForEmailList(
			List<SAGApplicationDetailForEmail> applicationDetailForEmailList ) {
		this.applicationDetailForEmailList = applicationDetailForEmailList;
	}

	public SAGOnBehalfDetailForEmail getOnBehalfDetailForEmail() {
		return onBehalfDetailForEmail;
	}

	public void setOnBehalfDetailForEmail(
			SAGOnBehalfDetailForEmail onBehalfDetailForEmail ) {
		this.onBehalfDetailForEmail = onBehalfDetailForEmail;
	}

	@Override
	public String toString() {
		return "SAGApplicantEmailContentDetail [submittedById=" + submittedById
				+ ", submittedByName=" + submittedByName
				+ ", preferredContactMode=" + preferredContactMode
				+ ", preferredEmail=" + preferredEmail + ", workEmail="
				+ workEmail + ", paymentAdviceEmail=" + paymentAdviceEmail + ", preferredContactNumber="
				+ preferredContactNumber + ", applicationDetailForEmailList="
				+ applicationDetailForEmailList + ", onBehalfDetailForEmail="
				+ onBehalfDetailForEmail + "]";
	}
}
