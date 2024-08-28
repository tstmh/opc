package com.stee.spfcore.vo.sag;

public class SAGPrivilegePersonalDetails {
	
	private String name;
	
	private String nric;
	
	private String genderId;
	
	private String genderDescription;
	
	private String nationalityId;
	
	private String nationalityDescription;
	
	private String departmentId;
	
	private String departmentDescription;
	
	private String rankId;
	
	private String rankDescription;
	
	private String preferredEmail;
	
	private String preferredContactNumber;

	public SAGPrivilegePersonalDetails() {
		super();
	}

	public SAGPrivilegePersonalDetails( String name, String nric,
			String genderId, String genderDescription, String nationalityId,
			String nationalityDescription, String departmentId,
			String departmentDescription, String rankId,
			String rankDescription, String preferredEmail,
			String preferredContactNumber ) {
		super();
		this.name = name;
		this.nric = nric;
		this.genderId = genderId;
		this.genderDescription = genderDescription;
		this.nationalityId = nationalityId;
		this.nationalityDescription = nationalityDescription;
		this.departmentId = departmentId;
		this.departmentDescription = departmentDescription;
		this.rankId = rankId;
		this.rankDescription = rankDescription;
		this.preferredEmail = preferredEmail;
		this.preferredContactNumber = preferredContactNumber;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getNric() {
		return nric;
	}

	public void setNric( String nric ) {
		this.nric = nric;
	}

	public String getGenderId() {
		return genderId;
	}

	public void setGenderId( String genderId ) {
		this.genderId = genderId;
	}

	public String getGenderDescription() {
		return genderDescription;
	}

	public void setGenderDescription( String genderDescription ) {
		this.genderDescription = genderDescription;
	}

	public String getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId( String nationalityId ) {
		this.nationalityId = nationalityId;
	}

	public String getNationalityDescription() {
		return nationalityDescription;
	}

	public void setNationalityDescription( String nationalityDescription ) {
		this.nationalityDescription = nationalityDescription;
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

	public String getRankId() {
		return rankId;
	}

	public void setRankId( String rankId ) {
		this.rankId = rankId;
	}

	public String getRankDescription() {
		return rankDescription;
	}

	public void setRankDescription( String rankDescription ) {
		this.rankDescription = rankDescription;
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
		return "SAGPrivilegePersonalDetails [name=" + name + ", nric=" + nric
				+ ", genderId=" + genderId + ", genderDescription="
				+ genderDescription + ", nationalityId=" + nationalityId
				+ ", nationalityDescription=" + nationalityDescription
				+ ", departmentId=" + departmentId + ", departmentDescription="
				+ departmentDescription + ", rankId=" + rankId
				+ ", rankDescription=" + rankDescription + ", preferredEmail="
				+ preferredEmail + ", preferredContactNumber="
				+ preferredContactNumber + "]";
	}
}
