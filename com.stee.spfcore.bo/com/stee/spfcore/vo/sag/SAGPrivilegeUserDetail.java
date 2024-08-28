package com.stee.spfcore.vo.sag;

public class SAGPrivilegeUserDetail {
	
	private String privilegeId;
	
	private String nric;
	
	private String name;
	
	private String financialYear;
	
	private String departmentId;
	
	private String departmentDescription;
	
	private String comments;
	
	private String preferredEmail;
	
	private String preferredContactNumber;

	public SAGPrivilegeUserDetail() {
		super();
	}

	public SAGPrivilegeUserDetail( String privilegeId, String nric,
			String name, String financialYear, String departmentId,
			String departmentDescription, String comments,
			String preferredEmail, String preferredContactNumber ) {
		super();
		this.privilegeId = privilegeId;
		this.nric = nric;
		this.name = name;
		this.financialYear = financialYear;
		this.departmentId = departmentId;
		this.departmentDescription = departmentDescription;
		this.comments = comments;
		this.preferredEmail = preferredEmail;
		this.preferredContactNumber = preferredContactNumber;
	}

	public String getNric() {
		return nric;
	}

	public void setNric( String nric ) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId( String departmentId ) {
		this.departmentId = departmentId;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription( String departmentDescription ) {
		this.departmentDescription = departmentDescription;
	}

	public String getComments() {
		return comments;
	}

	public void setComments( String comments ) {
		this.comments = comments;
	}

	public String getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId( String privilegeId ) {
		this.privilegeId = privilegeId;
	}

	public String getPreferredEmail() {
		return preferredEmail;
	}

	public void setPreferredEmail( String preferredEmail ) {
		this.preferredEmail = preferredEmail;
	}

	public String getPreferredContactNumber() {
		return preferredContactNumber;
	}

	public void setPreferredContactNumber( String preferredContactNumber ) {
		this.preferredContactNumber = preferredContactNumber;
	}

	@Override
	public String toString() {
		return "SAGPrivilegeUserDetail [privilegeId=" + privilegeId + ", nric="
				+ nric + ", name=" + name + ", financialYear=" + financialYear
				+ ", departmentId=" + departmentId + ", departmentDescription="
				+ departmentDescription + ", comments=" + comments + "]";
	}
	
}
