package com.stee.spfcore.vo.blacklist;

public class PersonalNameDepartment {

	private String name;
	private String department;

	public PersonalNameDepartment(String name, String department) {
		super();
		this.name = name;
		this.department = department;
	}

	public PersonalNameDepartment() {
		super();
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

}
