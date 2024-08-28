package com.stee.spfcore.service.hr.impl.nspam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

	private static final String MARITAL_STATUS_DESC = "MARITAL_STATUS_DESC";
	private static final String RACE_DESC = "RACE_DESC";
	private static final String RANK_DESC = "RANK_DESC";
	private static final String DEPARTMENT_DESC = "DEPARTMENT_DESC";
	private static final String NATIONALITY_DESC = "NATIONALITY_DESC";
	private static final String MEDICAL_SCHEME = "MEDICAL_SCHEME";
	private static final String TENURE_OF_SERVICE = "TENURE_OF_SERVICE";
	private static final String EMPLOYMENT_STATUS = "EMPLOYMENT_STATUS";
	private static final String GROSS_MONTHLY_SALARY = "GROSS_MONTHLY_SALARY";
	private static final String SERVICE_TYPE = "SERVICE_TYPE";
	private static final String PERSONAL_EMAIL = "PERSONAL_EMAIL";
	private static final String RESIDENTIAL_POSTAL = "RESIDENTIAL_POSTAL";
	private static final String RESIDENTIAL_BLDG_NAME = "RESIDENTIAL_BLDG_NAME";
	private static final String RESIDENTIAL_UNIT = "RESIDENTIAL_UNIT";
	private static final String RESIDENTIAL_FLOOR = "RESIDENTIAL_FLOOR";
	private static final String RESIDENTIAL_STREET = "RESIDENTIAL_STREET";
	private static final String RESIDENTIAL_BLOCK = "RESIDENTIAL_BLOCK";
	private static final String ADDRESS_TYPE = "ADDRESS_TYPE";
	private static final String CHILD = "CHILD";
	private static final String CHILDREN = "CHILDREN";
	private static final String RANK_CODE = "RANK_CODE";
	private static final String GENDER = "GENDER";
	private static final String RACE_CODE = "RACE_CODE";
	private static final String MARITAL_STATUS_CODE = "MARITAL_STATUS_CODE";
	private static final String SPOUSE = "SPOUSE";
	private static final String SPOUSES = "SPOUSES";
	private static final String DESIGNATION_DESC = "DESIGNATION_DESC";
	private static final String DUE_DATE = "DUE_DATE";
	private static final String HIGHEST_EDUCATION_LVL = "HIGHEST_EDUCATION_LVL";
	private static final String LEAVE = "LEAVE";
	private static final String LEAVES = "LEAVES";
	private static final String HOME_NO = "HOME_NO";
	private static final String OFFICE_NO = "OFFICE_NO";
	private static final String HANDPHONE_NO = "HANDPHONE_NO";
	private static final String ENLISTMENT_DATE = "ENLISTMENT_DATE";
	private static final String LEAVING_SERVICE_DATE = "LEAVING_SERVICE_DATE";
	private static final String APPOINTMENT_DATE = "APPOINTMENT_DATE";
	private static final String DOB = "DOB";
	private static final String SCHEME_OF_SERVICE = "SCHEME_OF_SERVICE";
	private static final String UID = "UID";
	private static final String DEPARTMENT_CODE = "DEPARTMENT_CODE";
	private static final String NATIONALITY_CODE = "NATIONALITY_CODE";
	private static final String UID_TYPE = "UID_TYPE";
	private static final String NAME = "NAME";
	
	private SimpleDateFormat dateFormat;
	
	private String name;
	private CodeInfo idType;
	private String id;
	private CodeInfo nationality;
	private CodeInfo department;
	private CodeInfo schemeOfService;
	private String dob;
	private String appointmentDate;
	private String leavingDate;
	private String enlistmentDate;
	private List<String> mobilePhones;
	private List<String> officePhones;
	private String homePhone;
	private List<ILeave> leaves;
	private CodeInfo education;
	private String dueDate;
	private CodeInfo designation;
	private CodeInfo rank;
	private CodeInfo gender;
	private CodeInfo race;
	private CodeInfo maritalStatus;
	private List<ISpouse> spouses;
	private List<IChild> children;
	private String addressBlock;
	private String street;
	private String floor;
	private String unit;
	private String buildingName;
	private String postal;
	private CodeInfo addressType;
	private String officeEmail;
	private String personalEmail;
	private CodeInfo serviceType;
	private String salary;
	private CodeInfo employmentStatus;
	private CodeInfo tenureOfService;
	private CodeInfo medicalScheme;
	
	private CodeMappingUtil codeMappingUtil;
	
	public Record (Element recordElement, CodeMappingUtil codeMappingUtil) {
		
		dateFormat = new SimpleDateFormat ("ddMMyyyy");
		dateFormat.setLenient(false);
		
		this.codeMappingUtil = codeMappingUtil;
		
		name = recordElement.getChildTextTrim(NAME);
		idType = new CodeInfo(UID_TYPE, true, recordElement.getChildTextTrim(UID_TYPE), "");
		id = recordElement.getChildTextTrim(UID).toUpperCase();
		nationality = new CodeInfo(NATIONALITY_CODE, true, recordElement.getChildTextTrim(NATIONALITY_CODE), 
				recordElement.getChildTextTrim(NATIONALITY_DESC));
		department = new CodeInfo(DEPARTMENT_CODE, true, recordElement.getChildTextTrim(DEPARTMENT_CODE), 
				recordElement.getChildTextTrim(DEPARTMENT_DESC));
		schemeOfService = new CodeInfo(SCHEME_OF_SERVICE, true, recordElement.getChildTextTrim(SCHEME_OF_SERVICE), "");
		dob = recordElement.getChildTextTrim(DOB);
		appointmentDate = recordElement.getChildTextTrim(APPOINTMENT_DATE);
		leavingDate = recordElement.getChildTextTrim(LEAVING_SERVICE_DATE);
		enlistmentDate = recordElement.getChildTextTrim(ENLISTMENT_DATE);
		
		mobilePhones = new ArrayList<>();
		String handPhone = recordElement.getChildTextTrim(HANDPHONE_NO);
		if (handPhone != null && !handPhone.isEmpty()) {
			mobilePhones.add(handPhone);
		}
		
		officePhones = new ArrayList<>();
		String officePhone = recordElement.getChildTextTrim(OFFICE_NO);
		if (officePhone != null && !officePhone.isEmpty()) {
			officePhones.add(officePhone);
		}
		
		homePhone = recordElement.getChildTextTrim(HOME_NO);
		
		leaves = new ArrayList<>();
		List<Element> leaveElements = recordElement.getChild(LEAVES).getChildren(LEAVE);
		for (Element leaveElement : leaveElements) {
			leaves.add(new Leave (leaveElement));
		}
		
		education = new CodeInfo(HIGHEST_EDUCATION_LVL, true, recordElement.getChildTextTrim(HIGHEST_EDUCATION_LVL), "");
		dueDate = recordElement.getChildTextTrim(DUE_DATE);
		designation = new CodeInfo(DESIGNATION_DESC, false, recordElement.getChildTextTrim(DESIGNATION_DESC), "");
		rank = new CodeInfo(RANK_CODE, true, recordElement.getChildTextTrim(RANK_CODE), recordElement.getChildTextTrim(RANK_DESC));
		gender = new CodeInfo(GENDER, true, recordElement.getChildTextTrim(GENDER), "");
		race = new CodeInfo(RACE_CODE, true, recordElement.getChildTextTrim(RACE_CODE), recordElement.getChildTextTrim(RACE_DESC));
		maritalStatus = new CodeInfo(MARITAL_STATUS_CODE, true, recordElement.getChildTextTrim(MARITAL_STATUS_CODE), 
				recordElement.getChildTextTrim(MARITAL_STATUS_DESC));
		
		spouses = new ArrayList<>();
		List<Element> spouseElements = recordElement.getChild(SPOUSES).getChildren(SPOUSE);
		for (Element spouseElement : spouseElements) {
			spouses.add(new Spouse (spouseElement));
		}
		
		children = new ArrayList<>();
		List<Element> childElements = recordElement.getChild(CHILDREN).getChildren(CHILD);
		for (Element childElement : childElements) {
			children.add(new Child (childElement));
		}
		
		addressBlock = recordElement.getChildTextTrim(RESIDENTIAL_BLOCK);
		street = recordElement.getChildTextTrim(RESIDENTIAL_STREET);
		floor = recordElement.getChildTextTrim(RESIDENTIAL_FLOOR);
		unit = recordElement.getChildTextTrim(RESIDENTIAL_UNIT);
		buildingName = recordElement.getChildTextTrim(RESIDENTIAL_BLDG_NAME);
		postal = recordElement.getChildTextTrim(RESIDENTIAL_POSTAL);
		addressType = new CodeInfo(ADDRESS_TYPE, true, recordElement.getChildTextTrim(ADDRESS_TYPE), "");
		
		// For email, only PERSONAL_EMAIL will contain value, if end with @SPF.gov.sg, 
		// should consider as office email, else as personal email
		String email = recordElement.getChildTextTrim(PERSONAL_EMAIL);
		if (email != null && email.toLowerCase(Locale.ENGLISH).endsWith("@spf.gov.sg")) {
			officeEmail = email;
		}
		else {
			personalEmail = email;
		}
		serviceType = new CodeInfo(SERVICE_TYPE, true, recordElement.getChildTextTrim(SERVICE_TYPE), "");
		salary = recordElement.getChildTextTrim(GROSS_MONTHLY_SALARY);
		employmentStatus = new CodeInfo(EMPLOYMENT_STATUS, true, recordElement.getChildTextTrim(EMPLOYMENT_STATUS), "");
		tenureOfService = new CodeInfo(TENURE_OF_SERVICE, true, recordElement.getChildTextTrim(TENURE_OF_SERVICE), "");
		medicalScheme = new CodeInfo(MEDICAL_SCHEME, true, recordElement.getChildTextTrim(MEDICAL_SCHEME), "");
	}
	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public CodeInfo getCitizenship() {
		return idType;
	}

	@Override
	public String getNric() {
		return id;
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
	public CodeInfo getDivisionStatus() {
		return null;
	}

	@Override
	public CodeInfo getSchemeOfService() {
		return schemeOfService;
	}

	@Override
	public Date getDateOfBirth() throws ParseException {
		if (dob != null && !dob.isEmpty()) {
			return parseDateString(dob);
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
		if (leavingDate != null && !leavingDate.isEmpty()) {
			return parseDateString(leavingDate);
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
		return education;
	}

	@Override
	public Date getDateOfRetirement() throws ParseException {
		
		if (dueDate != null && !dueDate.isEmpty()) {
			return parseDateString(dueDate);
		}
		return null;
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
	public CodeInfo getGender() {
		return gender;
	}

	@Override
	public CodeInfo getRace() {
		return race;
	}

	@Override
	public CodeInfo getMaritalStatus() {
		return maritalStatus;
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
	public String getBlockNumber() {
		return addressBlock;
	}

	@Override
	public String getStreetName() {
		return street;
	}
	
	@Override
	public String getFloorNumber() {
		return floor;
	}
	
	@Override
	public String getUnitNumber() {
		return unit;
	}

	
	@Override
	public String getBuildingName() {
		return buildingName;
	}

	@Override
	public String getPostalCode() {
		return postal;
	}

	@Override
	public CodeInfo getAddressType() {
		return addressType;
	}

	@Override
	public String getOfficeEmail() {
		return officeEmail;
	}

	@Override
	public String getPersonalEmail() {
		return personalEmail;
	}

	@Override
	public CodeInfo getServiceType() {
		return serviceType;
	}

	@Override
	public Double getSalary() {
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
		
		codeMappingUtil.convertToInternalCode (idType, CodeType.ID_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (nationality, CodeType.NATIONALITY, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (department, CodeType.UNIT_DEPARTMENT, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (schemeOfService, CodeType.SCHEME_OF_SERVICE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (education, CodeType.EDUCATION, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (designation, CodeType.DESIGNATIONS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (rank, CodeType.RANK, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (gender, CodeType.GENDERS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (race, CodeType.RACE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (maritalStatus, CodeType.MARITAL_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (addressType, CodeType.X_ADDRESS_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (serviceType, CodeType.SERVICE_TYPE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (employmentStatus, CodeType.EMPLOYMENT_STATUS, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (tenureOfService, CodeType.TENURE_OF_SERVICE, nonMatchingCodes);
		codeMappingUtil.convertToInternalCode (medicalScheme, CodeType.X_MEDICAL_SCHEME, nonMatchingCodes);
		
		for (int i = 0; i < leaves.size(); i++) {
			Leave leave = (Leave) leaves.get(i);
			codeMappingUtil.convertToInternalCode (leave.getLeaveType(), CodeType.ABSENCES, nonMatchingCodes);
		}
		
		return nonMatchingCodes;
	}
	
	private synchronized Date parseDateString (String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}

}
