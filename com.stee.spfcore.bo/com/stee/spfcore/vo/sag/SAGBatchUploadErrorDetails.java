package com.stee.spfcore.vo.sag;

public class SAGBatchUploadErrorDetails {
	private int rowNumber;
	private String applicantId;
	private String attendeeId;
	private String errorMessage;
	
	public SAGBatchUploadErrorDetails()
	{
		super();
	}

	public SAGBatchUploadErrorDetails(int rowNumber, String applicantId, String attendeeId, String errorMessage)
	{
		super();
		this.rowNumber = rowNumber;
		this.applicantId = applicantId;
		this.attendeeId = attendeeId;
		this.errorMessage = errorMessage;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
