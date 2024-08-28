package com.stee.spfcore.webapi.dao.dac;

public class DepartmentAndSubUnit {

	private String department;
	private String subunit;

	public DepartmentAndSubUnit () {
		super ();
	}

	public DepartmentAndSubUnit (String department, String subUnit) {
		this.department = department;
		this.subunit = subUnit;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSubunit() {
		return subunit;
	}

	public void setSubunit(String subunit) {
		this.subunit = subunit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((subunit == null) ? 0 : subunit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartmentAndSubUnit other = (DepartmentAndSubUnit) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (subunit == null) {
			if (other.subunit != null)
				return false;
		} else if (!subunit.equals(other.subunit))
			return false;
		return true;
	}
	
}
