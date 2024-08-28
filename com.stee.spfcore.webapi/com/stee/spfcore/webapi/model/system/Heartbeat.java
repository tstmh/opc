package com.stee.spfcore.webapi.model.system;

import java.util.Date;

public class Heartbeat {

	private String systemName;
	private Date timestamp;

	public Heartbeat() {
	}

	public Heartbeat(String systemName, Date timestamp) {
		this.systemName = systemName;
		this.timestamp = timestamp;
	}
	
	public String getSystemName() {
		return systemName;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
