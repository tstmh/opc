package com.stee.spfcore.service.hr.impl.htvms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.IChild;
import com.stee.spfcore.service.hr.impl.input.ILeave;
import com.stee.spfcore.service.hr.impl.input.IRecord;
import com.stee.spfcore.service.hr.impl.input.ISpouse;
import com.stee.spfcore.service.hr.impl.util.CodeMappingUtil;

class Record implements IRecord {

	private static final String DEFAULT_ID_TYPE = "SC";
	private static final String MEDICAL_SCHEME = "MedicalScheme";
	private static final String TENURE_END_DATE = "TenureEndDate";
	private static final String EMPLOYMENT_STATUS = "EmploymentStatus";
	private static final String GROSS_MONTHLY_SALARY = "GrossMonthlySalary";
	private static final String SERVICE_TYPE = "ServiceType";
	private static final String EMAIL_PERSONAL = "EmailPersonal";
	private static final String EMAIL_OFFICE = "EmailOffice";
	private static final String ADDRESS_POSTAL = "AddressPostal";
	private static final String ADDRESS_BUILDING_NAME = "AddressBuildingName";
	private static final String ADDRESS_TYPE = "AddressType";
	private static final String ADDRESS_UNIT = "AddressUnit";
	private static final String ADDRESS_FLOOR = "AddressFloor";
	private static final String ADDRESS_STREET = "AddressStreet";
	private static final String ADDRESS_BLOCK = "AddressBlock";
	private static final String MARITAL_STATUS = "MaritalStatus";
	private static final String RACE = "Race";
	private static final String GENDER = "Gender";
	private static final String RANK = "Rank";
	private static final String DESIGNATION = "Designation";
	private static final String EDUCATION_LEVEL = "EducationLevel";
	private static final String HOME_CONTACT_NO = "HomeContactNo";
	private static final String CONTACT_O = "ContactO";
	private static final String CONTACT_HP = "ContactHP";
	private static final String ENLISTMENT_DATE = "EnlistmentDate";
	private static final String EXITED_DATE = "ExitedDate";
	private static final String APPOINTMENT_DATE = "AppointmentDate";
	private static final String BIRTH_DATE = "BirthDate";
	private static final String SCHEME = "Scheme";
	private static final String DEPARTMENT = "Department";
	private static final String NATIONALITY = "Nationality";
	private static final String ID_TYPE = "IDType";
	private static final String NAME = "Name";
	private static final String ID_NO = "IDNo";
	
	private SimpleDateFormat dateFormat;
	
	private String nric;
	private String name;
	private CodeInfo citizenship;
	private CodeInfo nationality;
	private CodeInfo department;
	private CodeInfo scheme;
	private String birthDate;
	private String appointmentDate;
	private String exitedDate;
	private String enlistmentDate;
	private List<String> mobilePhones;
	private List<String> officePhones;
	private String homePhone;
	private List<ILeave> leaves;
	private CodeInfo educationLevel;
	private CodeInfo designation;
	private CodeInfo rank;
	private CodeInfo gender;
	private CodeInfo race;
	private CodeInfo maritalStatus;
	private String addressBlock;
	private String addressStreet;
	private String addressFloor;
	private String addressUnit;
	private String addressBuildingName;
	private String addressPostal;
	private CodeInfo addressType;
	private String emailOffice;
	private String emailPersonal;
	private CodeInfo serviceType;
	private String grossMonthlySalary;
	private CodeInfo employmentStatus;
	private CodeInfo tenureEndDate;
	private CodeInfo medicalScheme;
	
	private CodeMappingUtil codeMappingUtil;
	
	public Record (Element recordElement, CodeMappingUtil codeMappingUtil) {
		
		dateFormat = new SimpleDateFormat("ddMMyyyy");
		dateFormat.setLenient(false);
		
		this.codeMappingUtil = codeMappingUtil;
		
		nric = recordElement.getChildTextTrim(ID_NO).toUpperCase();
		name = recordElement.getChildTextTrim(NAME);
		
		String idType = recordElement.getChildTextTrim(ID_TYPE);
		
		// Default to Singaporean
		if (idType == null || idType.isEmpty()) {
			citizenship = new CodeInfo(ID_TYPE, true, DEFAULT_ID_TYPE, "");
		}
		else {
			citizenship = new CodeInfo(ID_TYPE, true, idType, "");
		}

		nationality = new CodeInfo(NATIONALITY, true, recordElement.getChildTextTrim(NATIONALITY), "");
		department = new CodeInfo(DEPARTMENT, true, recordElement.getChildTextTrim(DEPARTMENT), "");
		scheme = new CodeInfo(SCHEME, true, recordElement.getChildTextTrim(SCHEME), "");
		birthDate = recordElement.getChildTextTrim (BIRTH_DATE);
		appointmentDate = recordElement.getChildTextTrim(APPOINTMENT_DATE);
		exitedDate = recordElement.getChildTextTrim(EXITED_DATE);
		enlistmentDate = recordElement.getChildTextTrim(ENLISTMENT_DATE);
		
		mobilePhones = new ArrayList<>();
		List<Element> hpElements = recordElement.getChildren(CONTACT_HP);
		for (Element hpElement : hpElements) {
			mobilePhones.add(hpElement.getTextTrim());
		}
		
		officePhones = new ArrayList<>();
		List<Element> officeElements = recordElement.getChildren(CONTACT_O);
		for (Element officeElememt : officeElements) {
			officePhones.add(officeElememt.getTextTrim());
		}
		
		homePhone = recordElement.getChildTextTrim(HOME_CONTACT_NO);
		
		// Seem like NTVMS input don't have leaves
		leaves = new ArrayList<>();
		
		educationLevel = new CodeInfo(EDUCATION_LEVEL, true, recordElement.getChildTextTrim(EDUCATION_LEVEL), "");
		designation = new CodeInfo(DESIGNATION, true, recordElement.getChildTextTrim(DESIGNATION), "");
		rank = new CodeInfo(RANK, true, recordElement.getChildTextTrim(RANK), "");
		gender = new CodeInfo(GENDER, true, recordElement.getChildTextTrim(GENDER), "");
		race = new CodeInfo(RACE, true, recordElement.getChildTextTrim(RACE), "");
		maritalStatus = new CodeInfo(MARITAL_STATUS, true, recordElement.getChildTextTrim(MARITAL_STATUS), "");
		addressBlock = recordElement.getChildTextTrim(ADDRESS_BLOCK);
		addressStreet = recordElement.getChildTextTrim(ADDRESS_STREET);
		addressFloor = recordElement.getChildTextTrim(ADDRESS_FLOOR);
		addressUnit = recordElement.getChildTextTrim(ADDRESS_UNIT);
		addressBuildingName = recordElement.getChildTextTrim(ADDRESS_BUILDING_NAME);
		addressPostal = recordElement.getChildTextTrim(ADDRESS_POSTAL);
		addressType = new CodeInfo(ADDRESS_TYPE, true, recordElement.getChildTextTrim(ADDRESS_TYPE), "");
		emailOffice = recordElement.getChildTextTrim(EMAIL_OFFICE);
		emailPersonal = recordElement.getChildTextTrim(EMAIL_PERSONAL);
		serviceType = new CodeInfo(SERVICE_TYPE, true, recordElement.getChildTextTrim(SERVICE_TYPE), "");
		grossMonthlySalary = recordElement.getChildTextTrim(GROSS_MONTHLY_SALARY);
		employmentStatus = new CodeInfo(EMPLOYMENT_STATUS, true, recordElement.getChildTextTrim(EMPLOYMENT_STATUS), "");
		tenureEndDate = new CodeInfo(TENURE_END_DATE, true, recordElement.getChildTextTrim(TENURE_END_DATE), "");
		medicalScheme = new CodeInfo(MEDICAL_SCHEME, true, recordElement.getChildTextTrim(MEDICAL_SCHEME), "");
		
	}
	
	@Override
	public String getNric() {
		return nric;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public CodeInfo getCitizenship() {
		return citizenship;
	}

	@Override
	public CodeInfo getNationality() {
		return nationality;
	}

	@Override
	public CodeInfo getDepartment() {
		return department;
	}

	@Override
	public CodeInfo getSchemeOfService() {
		return scheme;
	}

	@Override
	public Date getDateOfBirth() throws ParseException {
		if (birthDate != null && !birthDate.isEmpty()) {
			return parseDateString(birthDate);
		}
		return null;
	}

	@Override
	public Date getDateOfAppointment() throws ParseException {
		if (appointmentDate != null && !appointmentDate.isEmpty()) {
			return parseDateString(appointmentDate);
		}
		return null;
	}

	@Override
	public Date getLeavingServiceDate() throws ParseException {
		if (exitedDate != null && !exitedDate.isEmpty()) {
			return parseDateString(exitedDate);
		}
		
		return null;
	}

	@Override
	public Date getDateOfEnlistment() throws ParseException {
		if (enlistmentDate != null && !enlistmentDate.isEmpty()) {
			return parseDateString(enlistmentDate);
		}
		
		return null;
	}

	@Override
	public List<String> getMobileContact() {
		return mobilePhones;
	}

	@Override
	public List<String> getOfficeContact() {
		return officePhones;
	}

	@Override
	public String getHomeContact() {
		return homePhone;
	}
	
	@Override
	public List<ILeave> getLeaves() {
		return leaves;
	}

	@Override
	public CodeInfo getEduationLevel() {
		return educationLevel;
	}

	@Override
	public CodeInfo getDesignation() {
		return designation;
	}

	@Override
	public CodeInfo getRankOrGrade() {
		return rank;
	}

	@Override
	public CodeInfo getRace() {return race;	}
	
	@Override
	public CodeInfo getGender() {
		return gender;
	}

	@Override
	public CodeInfo getMaritalStatus() {
		return maritalStatus;
	}

	@Override
	public String getBlockNumber() {
		return addressBlock;
	}

	@Override
	public String getStreetName() {
		return addressStreet;
	}

	@Override
	public String getFloorNumber() {
		return addressFloor;
	}

	@Override
	public String getUnitNumber() {
		return addressUnit;
	}

	@Override
	public String getBuildingName() {
		return addressBuildingName;
	}

	@Override
	public String getPostalCode() {
		return addressPostal;
	}

	@Override
	public CodeInfo getAddressType() {
		return addressType;
	}

	@Override
	public String getOfficeEmail() {
		return emailOffice;
	}

	@Override
	public String getPersonalEmail() {
		return emailPersonal;
	}

	@Override
	public CodeInfo getServiceType() {
		return serviceType;
	}

	@Override
	public Double getSalary() throws NumberFormatException {
		if (grossMonthlySalary != null && !grossMonthlySalary.isEmpty()) {
			return Double.valueOf(grossMonthlySalary);
		}
		return null;
	}

	@Override
	public CodeInfo getEmploymentStatus() {
		return employmentStatus;
	}

	@Override
	public CodeInfo getTenureOfService() {
		return tenureEndDate;
	}

	@Override
	public CodeInfo getMedicalScheme() {
		return medicalScheme;
	}

	@Override
	public Date getDateOfRetirement() {
		return null;
	}
	
	@Override
	public CodeInfo getDivisionStatus() {
		return null;
	}

	@Override
	public List<ISpouse> getSpouses() {
		return null;
	}

	@Override
	public List<IChild> getChildren() {
		return null;
	}

	@Override
	public CodeInfo getPresentUnit () {
		return null;
	}

	@Override
	public CodeInfo getPresentDesignation () {
		return null;
	}

	@Override
	public Date getCurrentSchemeDate () throws ParseException {
		return null;
	}
	
	@Override
	public Set<CodeInfo> convertToInternalCode() {
		
		Set<CodeInfo> nonMatchingCodes = new HashSet<>();
		
		
		codeMappingUtil.convertToInternalCode (citizenship, CodeType.ID_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (nationality, CodeType.NATIONALITY, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (department, CodeType.UNIT_DEPARTMENT, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (scheme, CodeType.SCHEME_OF_SERVICE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (educationLevel, CodeType.EDUCATION, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (designation, CodeType.DESIGNATIONS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (rank, CodeType.RANK, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (gender, CodeType.GENDERS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (race, CodeType.RACE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (maritalStatus, CodeType.MARITAL_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (addressType, CodeType.X_ADDRESS_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (serviceType, CodeType.SERVICE_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (employmentStatus, CodeType.EMPLOYMENT_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (tenureEndDate, CodeType.TENURE_OF_SERVICE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (medicalScheme, CodeType.X_MEDICAL_SCHEME, nonMatchingCodes);
		
		for (int i = 0; i < leaves.size (); i++) {
			Leave leave = (Leave) leaves.get (i);
			codeMappingUtil.convertToInternalCode (leave.getLeaveType(), CodeType.ABSENCES, nonMatchingCodes);
		}
		
		return nonMatchingCodes;
	}

	private synchronized Date parseDateString (String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}

	
}
