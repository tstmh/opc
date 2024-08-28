package com.stee.spfcore.model.hrps;

public enum RecordStatus {
	
	SUCCESSFUL ("SUC"), 
	REJECTED ("REJ"),
	PENDING ("PEN"),
	NONE ("NONE");
	
	private String status;
	
	private RecordStatus (String status) {
		this.status = status;
	}
	
	public static RecordStatus get (String status) {
		
		RecordStatus [] statusList = RecordStatus.values();
		for (RecordStatus recordStatus : statusList) {
			if (recordStatus.status.equals(status)) {
				return recordStatus;
			}
		}
		return NONE;
	}

	@Override
	public String toString () {
		return this.status;
	}
}
