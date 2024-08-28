package com.stee.spfcore.model.course;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Trainer {

	@Column(name = "\"TRAINER_NAME\"", length = 100)
	private String name;
	
	@Column(name = "\"TRAINER_DESIGNATION\"", length = 100)
	private String designation;
	
	@Column( name = "\"TRAINER_SALUTATION\"" )
  private String salutation;
	
	@Column(name = "\"TRAINER_EMAIL\"", length = 256)
	private String email;

	@Column(name = "\"TRAINER_OFFICE_NUMBER\"", length = 16)
	private String officeNumber;

	@Column(name = "\"TRAINER_MOBILE_NUMBER\"", length = 16)
	private String mobileNumber;

	@Column(name = "\"TRAINER_FAX_NUMBER\"", length = 16)
	private String faxNumber;

	@Column(name = "\"TRAINER_REMARK\"", length = 1000)
	private String remark;

	public Trainer() {
		super();
	}

	public Trainer(String name, String designation, String email, String officeNumber, String mobileNumber,
			String faxNumber, String remark) {
		super();
		this.name = name;
		this.designation = designation;
		this.email = email;
		this.officeNumber = officeNumber;
		this.mobileNumber = mobileNumber;
		this.faxNumber = faxNumber;
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOfficeNumber() {
		return officeNumber;
	}

	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
