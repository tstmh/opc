package com.stee.spfcore.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SYSTEM_STATUS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SystemStatus")
public class SystemStatus {
	
	@Id
	@Column(name = "\"SYSTEM_NAME\"", length = 20)
	private String systemName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"LAST_UPDATED_TIME\"")
	private Date lastUpdatedTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "\"LAST_REPORT_TIME\"")
	private Date lastReportTime;

	@Column(name = "\"IS_SERVICEABLE\"")
	private boolean isServiceable;

	public SystemStatus() {
		super();
	}

	public SystemStatus(String systemName, Date now) {
		super();
		
		this.systemName = systemName;
		this.lastUpdatedTime = now;
		this.lastReportTime = now;
		this.isServiceable = true;
	}

	public String getSystemName() {
		return systemName;
	}
	
	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	
	public Date getLastReportTime() {
		return lastReportTime;
	}

	public void setLastReportTime(Date lastReportTime) {
		this.lastReportTime = lastReportTime;
	}
	
	public boolean isServiceable() {
		return isServiceable;
	}
	
	public boolean isNotServiceable() {
		return !isServiceable;
	}
	
	public void setServiceable(boolean isServiceable) {
		this.isServiceable = isServiceable;
	}
}
