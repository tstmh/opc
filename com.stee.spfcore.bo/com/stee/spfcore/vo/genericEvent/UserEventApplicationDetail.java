package com.stee.spfcore.vo.genericEvent;

import java.util.Date;
import java.util.List;

public class UserEventApplicationDetail {

	private String nric;
	private String name;
	private String department;
	private String serviceType;
	private List<UserChoice> userChoices;
	private String status;
	private Date registeredOn;
	

	public UserEventApplicationDetail() {
		super();
	}

	public UserEventApplicationDetail(String nric, String name, String department, String status, String serviceType,
			List<UserChoice> userChoices, Date registeredOn) {
		super();
		this.nric = nric;
		this.name = name;
		this.department = department;
		this.serviceType = serviceType;
		this.userChoices = userChoices;
		this.status = status;
		this.registeredOn = registeredOn;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<UserChoice> getUserChoices() {
		return userChoices;
	}

	public void setUserChoices(List<UserChoice> userChoices) {
		this.userChoices = userChoices;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}
	
	
}
