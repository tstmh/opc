package com.stee.spfcore.model;

public enum ApplicationStatus {
	
	OPEN ("Open"), 
	PENDING ("Pending"),
	REJECTED ("Rejected"), 
	APPROVED ("Approved"),
	DRAFT("Draft"),
	REVERTED("Reverted"),
	WAIT_LIST ("WaitList"),
	WITHDRAWN ("Withdrawn"),
	AMENDED ("Amended"),
	ATTENDED ("Attended"),
	PENDING_APPROVAL("Pending Approval"),
	SUCCESSFUL("Successful"),
	UNSUCCESSFUL("Unsuccessful"),
	RECOMMENDED("Recommended"),
	NOT_RECOMMENDED("Not Recommended"),
	SUPPORTED("Supported"),
	VERIFIED("Verified"),
	REVERTED_TO_PO("Reverted to PO"),
	NONE ("None"),
	EXPIRED ("Expired"),
	FOVERIFIED("Fo Verified"),
	ENDORSED("Endorsed");
	//Add New Application Status
	
	private String status;
	
	private ApplicationStatus (String status) {
		this.status = status;
	}
	
	public static ApplicationStatus get (String status) {
		
		ApplicationStatus [] statusList = ApplicationStatus.values();
		for (ApplicationStatus applicationStatus : statusList) {
			if (applicationStatus.status.equals(status)) {
				return applicationStatus;
			}
		}
		return NONE;
	}

	@Override
	public String toString () {
		return this.status;
	}
}
