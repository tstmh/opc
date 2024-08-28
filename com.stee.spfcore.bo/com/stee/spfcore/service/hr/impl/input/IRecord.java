package com.stee.spfcore.service.hr.impl.input;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IRecord {

	public String getNric ();
	
	public String getName ();
	
	public Date getDateOfBirth () throws ParseException;
	
	public CodeInfo getCitizenship ();
	
	public CodeInfo getNationality ();
	
	public CodeInfo getRace ();
	
	public CodeInfo getGender ();
	
	public CodeInfo getAddressType ();
	
	public String getBlockNumber ();
	
	public String getStreetName ();
	
	public String getFloorNumber ();
	
	public String getUnitNumber ();
	
	public String getBuildingName ();
	
	public String getPostalCode ();
	
	public CodeInfo getMaritalStatus ();
	
	public List<String> getMobileContact ();
	
	public List<String> getOfficeContact ();
	
	public String getHomeContact ();
	
	public String getOfficeEmail ();
	
	public String getPersonalEmail ();
	
	public CodeInfo getEduationLevel ();

	public CodeInfo getDepartment ();
	
	public Date getDateOfEnlistment () throws ParseException;
	
	public Date getDateOfAppointment () throws ParseException;
	
	public Date getDateOfRetirement () throws ParseException;
	
	public CodeInfo getDesignation ();
	
	public CodeInfo getRankOrGrade ();
	
	public CodeInfo getServiceType ();
	
	public CodeInfo getTenureOfService ();
	
	public CodeInfo getMedicalScheme ();
	
	public CodeInfo getDivisionStatus ();
	
	public CodeInfo getSchemeOfService ();
	
	public CodeInfo getEmploymentStatus ();
	
	public Date getLeavingServiceDate () throws ParseException;
	
	public Double getSalary () throws NumberFormatException;

	public List<ILeave> getLeaves ();
	
	public List<ISpouse> getSpouses ();
	
	public List<IChild> getChildren ();
	
	public Set<CodeInfo> convertToInternalCode ();
	
	public CodeInfo getPresentUnit ();
	
	public CodeInfo getPresentDesignation ();
	
	public Date getCurrentSchemeDate () throws ParseException;
}
