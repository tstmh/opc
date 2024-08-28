package com.stee.spfcore.service.hr.impl.hrhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.service.hr.impl.ErrorConstants;
import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.DataException;
import com.stee.spfcore.service.hr.impl.input.IChild;
import com.stee.spfcore.service.hr.impl.input.ILeave;
import com.stee.spfcore.service.hr.impl.input.IRecord;
import com.stee.spfcore.service.hr.impl.input.ISpouse;
import com.stee.spfcore.service.hr.impl.util.CodeMappingUtil;

class Record implements IRecord {
	
	private static final String SPOUSE_DETAILS = "SpouseDetails";
	private static final String PRESENT_DESIGNATION_DESCRIPTION = "PresentDesignationDescription";
	private static final String PRESENT_UNIT_DESCRIPTION = "PresentUnitDescription";
	private static final String TENURE_OF_SERVICE_DESCRIPTION = "TenureOfServiceDescription";
	private static final String EMPLOYMENT_STATUS_DESCRIPTION = "EmploymentStatusDescription";
	private static final String MARITAL_STATUS_DESCRIPTION = "MaritalStatusDescription";
	private static final String RANK_DESCRIPTION = "RankDescription";
	private static final String OWNER_DESIGNATION_DESCRIPTION = "OwnerDesignationDescription";
	private static final String SCHEME_OF_SERVICE_DESCRIPTION = "SchemeOfServiceDescription";
	private static final String DIVISION_STATUS_DESCRIPTION = "DivisionStatusDescription";
	private static final String OWNER_UNIT_DESCRIPTION = "OwnerUnitDescription";
	private static final String SERVICE_TYPE_DESCRIPTION = "ServiceTypeDescription";
	private static final String NATIONALITY_DESCRIPTION = "NationalityDescription";
	private static final String RACE_DESCRIPTION = "RaceDescription";
	private static final String TYPE_OF_IC_DESCRIPTION = "TypeOfICDescription";
	private static final String CURRENT_SCHEME_DATE = "CurrentSchemeDate";
	private static final String PRESENT_DESIGNATION = "PresentDesignation";
	private static final String PRESENT_UNIT = "PresentUnit";
	private static final String CHILD_DETAILS = "ChildDetails";
	private static final String LEAVE_DETAILS = "LeaveDetails";
	private static final String MEDICAL_SCHEME = "MedicalScheme";
	private static final String TENURE_OF_SERVICE_CODE = "TenureOfServiceCode";
	private static final String EMPLOYMENT_STATUS_CODE = "EmploymentStatusCode";
	private static final String GROSS_MONTHLY_SALARY = "GrossMonthlySalary";
	private static final String OFFICE_EMAIL_ADDRESS = "OfficeEmailAddress";
	private static final String ADDRESS_TYPE = "AddressType";
	private static final String ADDRESS_POSTAL_CODE = "AddressPostalCode";
	private static final String ADDRESS_UNIT_NO = "AddressUnitNo";
	private static final String ADDRESS_FLOOR_NO = "AddressFloorNo";
	private static final String ADDRESS_STREET_NAME = "AddressStreetName";
	private static final String ADDRESS_BLOCK_NO = "AddressBlockNo";
	private static final String MARITAL_STATUS_CODE = "MaritalStatusCode";
	private static final String RANK_CODE = "RankCode";
	private static final String HOME_CONTACT_NO = "HomeContactNo";
	private static final String OFFICE_CONTACT_NO = "OfficeContactNo";
	private static final String HP_CONTACT_NO = "HPContactNo";
	private static final String DATE_OF_RETIREMENT = "DateOfRetirement";
	private static final String DATE_OF_LEAVING_SERVICE = "DateOfLeavingService";
	private static final String DATE_OF_APPOINTMENT = "DateOfAppointment";
	private static final String OWNER_DESIGNATION = "OwnerDesignation";
	private static final String SCHEME_OF_SERVICE_CODE = "SchemeOfServiceCode";
	private static final String DIVISION_STATUS_CODE = "DivisionStatusCode";
	private static final String OWNER_UNIT = "OwnerUnit";
	private static final String SERVICE_TYPE_CODE = "ServiceTypeCode";
	private static final String NATIONALITY_CODE = "NationalityCode";
	private static final String RACE_CODE = "RaceCode";
	private static final String HIGHEST_EDUCATION_LEVEL = "HighestEducationLevel";
	private static final String DATE_OF_BIRTH = "DateOfBirth";
	private static final String GENDER = "Gender";
	private static final String TYPE_OF_IC_CODE = "TypeOfICCode";
	private static final String NAME = "Name";
	private static final String NRIC = "NRIC";
	
	private SimpleDateFormat dateFormat;
	
	private String nric;
	private String name;
	private CodeInfo citizenship;
	private CodeInfo gender;
	private String dateOfBirth;
	private CodeInfo education;
	private CodeInfo race;
	private CodeInfo nationality;
	private CodeInfo serviceType;
	private CodeInfo department;
	private CodeInfo divisionStatus;
	private CodeInfo schemeOfService;
	private CodeInfo designation;
	private String appointmentDate;
	private String leavingDate;
	private String retirementDate;
	private List<String> mobilePhones;
	private List<String> officePhones;
	private String homePhone;
	private CodeInfo rankCode;
	private CodeInfo maritalStatus;
	private String blockNumber;
	private String streetName;
	private String floorNumber;
	private String unitNumber;
	private String postalCode;
	private CodeInfo addressType;
	private String officeAddress;
	private String salary;
	private CodeInfo employmentStatus;
	private CodeInfo tenureOfService;
	private CodeInfo medicalScheme;
	private List<ILeave> leaves;
	private List<ISpouse> spouses;
	private List<IChild> children;
	
	private CodeInfo presentUnit;
	private CodeInfo presentDesignation;
	private String currentSchemeDate;
	
	
	private CodeMappingUtil codeMappingUtil;
	
	public Record (Element recordElement, CodeMappingUtil codeMappingUtil) throws DataException {
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		
		this.codeMappingUtil = codeMappingUtil;
		
		try {
			extract (recordElement);
		}
		catch (Exception e) {
			throw new DataException(ErrorConstants.INVALID_XML, e);
		}
	}

	private void extract (Element recordElement) {
		nric = recordElement.getChildTextTrim(NRIC).toUpperCase();
		name = recordElement.getChildTextTrim(NAME);

		citizenship = new CodeInfo (TYPE_OF_IC_CODE, true, recordElement.getChildTextTrim(TYPE_OF_IC_CODE), 
				recordElement.getChildTextTrim(TYPE_OF_IC_DESCRIPTION));

		gender = new CodeInfo (GENDER, true, recordElement.getChildTextTrim(GENDER), "");
		
		dateOfBirth = recordElement.getChildTextTrim(DATE_OF_BIRTH);
		
		education = new CodeInfo (HIGHEST_EDUCATION_LEVEL, true, recordElement.getChildTextTrim(HIGHEST_EDUCATION_LEVEL), "");
		
		race = new CodeInfo (RACE_CODE, true, recordElement.getChildTextTrim(RACE_CODE), 
				recordElement.getChildTextTrim(RACE_DESCRIPTION));
		
		nationality = new CodeInfo (NATIONALITY_CODE, true, recordElement.getChildTextTrim(NATIONALITY_CODE), 
				recordElement.getChildTextTrim(NATIONALITY_DESCRIPTION));
		
		serviceType = new CodeInfo (SERVICE_TYPE_CODE, true, recordElement.getChildTextTrim(SERVICE_TYPE_CODE), 
				recordElement.getChildTextTrim(SERVICE_TYPE_DESCRIPTION));
		
		department = new CodeInfo (OWNER_UNIT, true, recordElement.getChildTextTrim(OWNER_UNIT), 
				recordElement.getChildTextTrim(OWNER_UNIT_DESCRIPTION));
		
		divisionStatus = new CodeInfo (DIVISION_STATUS_CODE, true, recordElement.getChildTextTrim(DIVISION_STATUS_CODE), 
				recordElement.getChildTextTrim(DIVISION_STATUS_DESCRIPTION));
		
		schemeOfService = new CodeInfo (SCHEME_OF_SERVICE_CODE, true, recordElement.getChildTextTrim(SCHEME_OF_SERVICE_CODE), 
				recordElement.getChildTextTrim(SCHEME_OF_SERVICE_DESCRIPTION));
		
		designation = new CodeInfo (OWNER_DESIGNATION, true, recordElement.getChildTextTrim(OWNER_DESIGNATION), 
				recordElement.getChildTextTrim(OWNER_DESIGNATION_DESCRIPTION));
		
		appointmentDate = recordElement.getChildTextTrim(DATE_OF_APPOINTMENT);
		leavingDate = recordElement.getChildTextTrim(DATE_OF_LEAVING_SERVICE);
		retirementDate = recordElement.getChildTextTrim(DATE_OF_RETIREMENT);
		
		mobilePhones = new ArrayList<>();
		List<Element> hpElements = recordElement.getChildren(HP_CONTACT_NO);
		for (Element hpElement : hpElements) {
			mobilePhones.add(hpElement.getTextTrim());
		}
		
		officePhones = new ArrayList<>();
		List<Element> officeElements = recordElement.getChildren(OFFICE_CONTACT_NO);
		for (Element officeElememt : officeElements) {
			officePhones.add(officeElememt.getTextTrim());
		}
		
		homePhone = recordElement.getChildTextTrim(HOME_CONTACT_NO);
		
		rankCode = new CodeInfo (RANK_CODE, true, recordElement.getChildTextTrim(RANK_CODE), recordElement.getChildTextTrim(RANK_DESCRIPTION));
		
		maritalStatus = new CodeInfo (MARITAL_STATUS_CODE, true, recordElement.getChildTextTrim(MARITAL_STATUS_CODE), 
				recordElement.getChildTextTrim(MARITAL_STATUS_DESCRIPTION));
		
		blockNumber = recordElement.getChildTextTrim(ADDRESS_BLOCK_NO);
		streetName = recordElement.getChildTextTrim(ADDRESS_STREET_NAME);
		floorNumber = recordElement.getChildTextTrim(ADDRESS_FLOOR_NO);
		unitNumber = recordElement.getChildTextTrim(ADDRESS_UNIT_NO);
		postalCode = recordElement.getChildTextTrim(ADDRESS_POSTAL_CODE);
		addressType = new CodeInfo (ADDRESS_TYPE, true, recordElement.getChildTextTrim(ADDRESS_TYPE), "");
		
		officeAddress = recordElement.getChildTextTrim(OFFICE_EMAIL_ADDRESS);
		
		salary = recordElement.getChildTextTrim(GROSS_MONTHLY_SALARY);
		
		employmentStatus = new CodeInfo (EMPLOYMENT_STATUS_CODE, true, recordElement.getChildTextTrim(EMPLOYMENT_STATUS_CODE),
				recordElement.getChildTextTrim(EMPLOYMENT_STATUS_DESCRIPTION));
		
		tenureOfService = new CodeInfo (TENURE_OF_SERVICE_CODE, true, recordElement.getChildTextTrim(TENURE_OF_SERVICE_CODE),
				recordElement.getChildTextTrim(TENURE_OF_SERVICE_DESCRIPTION));
		
		medicalScheme = new CodeInfo (MEDICAL_SCHEME, true, recordElement.getChildTextTrim(MEDICAL_SCHEME), "");
		
		leaves = new ArrayList<>();
		List<Element> leaveElements = recordElement.getChildren(LEAVE_DETAILS);
		for (Element leaveElement : leaveElements) {
			// HRHUB may send empty element. Strange XML format.
			if (!leaveElement.getChildren().isEmpty()) {
				leaves.add(new Leave(leaveElement));
			}
		}		
		
		spouses = new ArrayList<>();
		List<Element> spouseElements = recordElement.getChildren(SPOUSE_DETAILS);
		for (Element spouseElement : spouseElements) {
			// HRHUB may send empty element. Strange XML format.
			if (!spouseElement.getChildren().isEmpty()) {
				spouses.add (new Spouse(spouseElement));
			}
		}
		
		children = new ArrayList<>();
		List<Element> childElements = recordElement.getChildren(CHILD_DETAILS);
		for (Element childElement : childElements) {
			// HRHUB may send empty element. Strange XML format.
			if (!childElement.getChildren().isEmpty()) {
				children.add (new Child(childElement));
			}
		}		
		
		presentUnit = new CodeInfo (PRESENT_UNIT, true, recordElement.getChildTextTrim(PRESENT_UNIT), 
				recordElement.getChildTextTrim(PRESENT_UNIT_DESCRIPTION));
		
		presentDesignation = new CodeInfo (PRESENT_DESIGNATION , true, recordElement.getChildTextTrim(PRESENT_DESIGNATION), 
				recordElement.getChildTextTrim(PRESENT_DESIGNATION_DESCRIPTION));
		
		currentSchemeDate = recordElement.getChildTextTrim(CURRENT_SCHEME_DATE);
		
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
	public CodeInfo getGender() {
		return gender;
	}
	
	@Override
	public Date getDateOfBirth() throws ParseException {
		if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
			return parseDateString (dateOfBirth);
		}
		return null;
	}

	@Override
	public CodeInfo getEduationLevel() {
		return education;
	}
	
	@Override
	public CodeInfo getRace() {
		return race;
	}

	@Override
	public CodeInfo getNationality() {
		return nationality;
	}

	@Override
	public CodeInfo getServiceType() {
		return serviceType;
	}

	@Override
	public CodeInfo getDepartment() {
		return department;
	}

	@Override
	public CodeInfo getDivisionStatus() {
		return divisionStatus;
	}
	
	@Override
	public CodeInfo getSchemeOfService() {
		return schemeOfService;
	}

	@Override
	public CodeInfo getDesignation() {
		return designation;
	}

	@Override
	public Date getDateOfAppointment() throws ParseException {
		
		if (appointmentDate != null && !appointmentDate.isEmpty()) {
			return parseDateString (appointmentDate);
		}
		
		return null;
	}

	@Override
	public Date getLeavingServiceDate() throws ParseException {
		if (leavingDate != null && !leavingDate.isEmpty()) {
			return parseDateString (leavingDate);
		}
		return null;
	}

	@Override
	public Date getDateOfEnlistment() {
		return null;
	}
	
	@Override
	public Date getDateOfRetirement() throws ParseException {
		
		if (retirementDate != null && !retirementDate.isEmpty()) {
			return parseDateString (retirementDate);
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
	public CodeInfo getRankOrGrade() {
		return rankCode;
	}

	@Override
	public CodeInfo getMaritalStatus() {
		return maritalStatus;
	}
	
	@Override
	public String getBlockNumber() {
		return blockNumber;
	}

	@Override
	public String getStreetName() {
		return streetName;
	}

	@Override
	public String getFloorNumber() {
		return floorNumber;
	}

	@Override
	public String getUnitNumber() {
		return unitNumber;
	}

	@Override
	public String getBuildingName() {
		return null;
	}

	@Override
	public String getPostalCode() {
		return postalCode;
	}

	@Override
	public CodeInfo getAddressType() {
		return addressType;
	}

	@Override
	public String getOfficeEmail() {
		return officeAddress;
	}

	@Override
	public String getPersonalEmail() {
		return null;
	}

	@Override
	public Double getSalary() throws NumberFormatException {
		if (salary != null && !salary.isEmpty()) {
			return Double.valueOf(salary);
		}
		
		return null;
	}
	
	@Override
	public CodeInfo getEmploymentStatus() {
		return employmentStatus;
	}

	@Override
	public CodeInfo getTenureOfService() {
		return tenureOfService;
	}
	
	@Override
	public CodeInfo getMedicalScheme() {
		return medicalScheme;
	}
		
	@Override
	public List<ILeave> getLeaves() {
		return leaves;
	}

	@Override
	public List<ISpouse> getSpouses() {
		return spouses;
	}

	@Override
	public List<IChild> getChildren() {
		return children;
	}

	
	@Override
	public CodeInfo getPresentUnit () {
		return presentUnit;
	}

	@Override
	public CodeInfo getPresentDesignation () {
		return presentDesignation;
	}

	@Override
	public Date getCurrentSchemeDate () throws ParseException {
		if (currentSchemeDate != null && !currentSchemeDate.isEmpty()) {
			return parseDateString (currentSchemeDate);
		}
		
		return null;
	}
	
	
	
	@Override
	public Set<CodeInfo> convertToInternalCode() {
		
		Set<CodeInfo> nonMatchingCodes = new HashSet<>();
		
		codeMappingUtil.convertToInternalCode (citizenship, CodeType.ID_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (gender, CodeType.GENDERS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (education, CodeType.EDUCATION, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (race, CodeType.RACE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (nationality, CodeType.NATIONALITY, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (serviceType, CodeType.SERVICE_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (department, CodeType.UNIT_DEPARTMENT, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (divisionStatus, CodeType.DIVISION_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (schemeOfService, CodeType.SCHEME_OF_SERVICE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (designation, CodeType.DESIGNATIONS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (rankCode, CodeType.RANK, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (maritalStatus, CodeType.MARITAL_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (addressType, CodeType.X_ADDRESS_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (employmentStatus, CodeType.EMPLOYMENT_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (tenureOfService, CodeType.TENURE_OF_SERVICE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (medicalScheme, CodeType.X_MEDICAL_SCHEME, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (presentUnit, CodeType.UNIT_DEPARTMENT, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (presentDesignation, CodeType.DESIGNATIONS, nonMatchingCodes);
		
		for (int i = 0; i < leaves.size (); i++) {
			Leave leave = (Leave) leaves.get(i);
			codeMappingUtil.convertToInternalCode (leave.getLeaveType(), CodeType.ABSENCES, nonMatchingCodes);
		}
		
		for (int i = 0; i < spouses.size(); i++) {
			Spouse spouse = (Spouse) spouses.get(i);
			codeMappingUtil.convertToInternalCode(spouse.getIdType(), CodeType.ID_TYPE, nonMatchingCodes);
		}
		
		for (int i = 0; i < children.size(); i++) {
			Child child = (Child) children.get(i);
			codeMappingUtil.convertToInternalCode(child.getIdType(), CodeType.ID_TYPE, nonMatchingCodes);
		}
		
		return nonMatchingCodes;
	}

	
	private synchronized Date parseDateString (String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}
	
}
