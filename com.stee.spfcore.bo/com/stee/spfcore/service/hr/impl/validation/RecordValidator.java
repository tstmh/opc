package com.stee.spfcore.service.hr.impl.validation;

import static com.stee.spfcore.service.hr.impl.ErrorConstants.*;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

import com.stee.spfcore.service.configuration.ICodeConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.hr.impl.JobType;
import com.stee.spfcore.service.hr.impl.RecordError;
import com.stee.spfcore.service.hr.impl.input.CodeInfo;
import com.stee.spfcore.service.hr.impl.input.IChild;
import com.stee.spfcore.service.hr.impl.input.ILeave;
import com.stee.spfcore.service.hr.impl.input.IRecord;
import com.stee.spfcore.service.hr.impl.input.ISpouse;
import com.stee.spfcore.utils.DateUtils;

public class RecordValidator {

	private RecordValidator(){}
	private static final Pattern NAME_ALLOWED_CHARACTER_PATTERN = Pattern.compile("[a-zA-Z0-9\\s@ / , . ( ) ' * # -]+",Pattern.MULTILINE);
	private static final Pattern NRIC_ALLOWED_CHARACTER_PATTERN = Pattern.compile("[a-zA-Z0-9\\s]+",Pattern.MULTILINE);
	
	
	public static List<RecordError> validate (JobType jobType, IRecord record) {
		
		List<RecordError> errors = new ArrayList<>();
		
		validateNRIC (record, errors);
		
		validateName (record, errors);
		
		validateCitizenship (record, errors);
		
		validateDateOfBirth (record, errors);
		
		validateGender (record, errors);
		
		//Non-Mandatory for VMS3.0
		if (jobType != JobType.HTVMS) {
			validateMaritalStatus (record, errors);
		}
		
		validateEmploymentStatus (record, errors);
		
		validateDepartment (record, errors);
		
		validateServiceType (record, errors);
		
		validateDateOfAppointment (record, errors);
		
		validateDateOfEnlistment (record, errors);
		
		validateLeavingServiceDate (record, errors);
		
		// Only NSPAM and HRHUB. HTVMS don't have such field.
		if (jobType != JobType.HTVMS) {
			validateDateOfRetirement (record, errors);
		}
		
		validateSalary (record, errors);
		
		validateAddress (jobType, record, errors);
		
		validateMobileContact (record, errors);
		
		validateOfficeContact (record, errors);
		
		validateHomeContact (record, errors);
		
		validateOfficeEmail (record, errors);
		
		validatePersonalEmail (record, errors);
		
		validateLeave (record, errors);
		
		validateSpouses (jobType, record, errors);
		
		validateChildren (jobType, record, errors);
		
		validateMedicalScheme (record, errors);
		
		validateRank (record, errors);
		
		// Only HRHub has presentUnit and currentSchemeDate
		if (jobType == JobType.HRHUB) {
			validatePresentUnit (record, errors);
			validateCurrentSchemeDate (record, errors);
		}
		
		return errors;
	}
	
	
	private static void validateName (IRecord record, List<RecordError> errors) {
		
		String name = record.getName();
		
		// Name is mandatory
		if (name == null || name.trim().isEmpty()) {
			errors.add(new RecordError(record.getNric(), NAME_MISSING_CODE, NAME_MISSING_MSG, NAME_TAG));
		}
		// Name should not contains invalid characters
		else if (!NAME_ALLOWED_CHARACTER_PATTERN.matcher(name).matches()) {
			errors.add(new RecordError(record.getNric(), NAME_INVALID_CODE, NAME_INVALID_MSG, NAME_TAG));
		}
		// Name field length min 1, max 255
		else if (name.length() < 1 || name.length() > 255) {
			errors.add(new RecordError(record.getNric(), NAME_LENGTH_INVALID_CODE, NAME_LENGTH_INVALID_MSG, NAME_TAG));
		}
	}
	
	
	private static void validateNRIC (IRecord record, List<RecordError> errors) {
		String nric = record.getNric().toUpperCase(Locale.ENGLISH);
		
		// NRIC is mandatory
		if (nric == null || nric.trim().isEmpty()) {
			// I know it doesn't make sense, but that is what specified in interface spec.
			errors.add(new RecordError (null, NRIC_MISSING_CODE, NRIC_MISSING_MSG, NRIC_TAG));
		}
		// NRIC must be 9 character long
		else if (nric.length() != 9) {
			errors.add(new RecordError (record.getNric(), NRIC_LENGTH_INVALID_CODE, NRIC_LENGTH_INVALID_MSG, NRIC_TAG));
		}
		else if (!NRIC_ALLOWED_CHARACTER_PATTERN.matcher(nric).matches()) {
			errors.add(new RecordError (record.getNric(), NRIC_INVALID_CODE, NRIC_INVALID_MSG, NRIC_TAG));
		}
		else if (!(nric.startsWith("T") || nric.startsWith("S") || nric.startsWith("F") || nric.startsWith("G") || nric.startsWith("M"))) {
			errors.add(new RecordError (record.getNric(), NRIC_INVALID_START_CHARACTER_CODE, NRIC_INVALID_START_CHARACTER_MSG, NRIC_TAG));
		}
		else if (!NricNumberCheck.isValid(nric)) {
			errors.add(new RecordError (record.getNric(), NRIC_INVALID_CHECKSUM_CODE, NRIC_INVALID_CHECKSUM_MSG, NRIC_TAG));
		}
		
	}
	
	private static void validateCitizenship (IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = record.getCitizenship();
		String idType = codeInfo.getValue();
		
		// Citizenship (ID Type) is mandatory. 
		if (idType == null  || idType.isEmpty()) {
			errors.add(new RecordError (record.getNric(), ID_TYPE_MISSING_CODE, ID_TYPE_MISSING_MSG, ID_TYPE_TAG ));
		}
	}
	
	private static void validateGender (IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = record.getGender();
		String gender = codeInfo.getValue();
		
		// Gender is mandatory. 
		if (gender == null  || gender.isEmpty()) {
			errors.add(new RecordError (record.getNric(), GENDER_MISSING_CODE, GENDER_MISSING_MSG, GENDER_TAG));
		}
	}
	
	private static void validateMaritalStatus (IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = record.getMaritalStatus();
		String status = codeInfo.getValue();
		
		// Marital status is mandatory. 
		if (status == null  || status.isEmpty()) {
			errors.add(new RecordError (record.getNric(), MARITAL_STATUS_MISSING_CODE, MARITAL_STATUS_MISSING_MSG, MARITAL_STATUS_TAG ));
		}
	}
	
	private static void validateDepartment (IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = record.getDepartment();
		String department = codeInfo.getValue();
		
		// Department is mandatory. 
		if (department == null  || department.isEmpty()) {
			errors.add(new RecordError (record.getNric(), DEPARTMENT_MISSING_CODE, DEPARTMENT_MISSING_MSG, DEPARTMENT_TAG));
		}
	}
	
	private static void validateEmploymentStatus (IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = record.getEmploymentStatus();
		String status = codeInfo.getValue();
		
		// Employment status must be mandatory, else processing will fail. 
		if (status == null  || status.isEmpty()) {
			errors.add(new RecordError (record.getNric(), EMPLOYMENT_STATUS_ERROR_CODE, EMPLOYMENT_STATUS_ERROR_MSG, EMPLOYMENT_STATUS_TAG));
		}
	}
	
	private static void validateServiceType (IRecord record, List<RecordError> errors) {
		
		CodeInfo codeInfo = record.getServiceType();
		String serviceType = codeInfo.getValue();
		
		// Service Type must be mandatory, else membership processing will fail. 
		if (serviceType == null  || serviceType.isEmpty()) {
			errors.add(new RecordError (record.getNric(), SERVICE_TYPE_ERROR_CODE, SERVICE_TYPE_ERROR_MSG, SERVICE_TYPE_TAG));
		}
	}
	
	
	private static void validateDateOfAppointment (IRecord record, List<RecordError> errors) {
		
		Date today = new Date ();
		
		Date appointmentDate = null;
		
		try {
			appointmentDate = record.getDateOfAppointment();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), DATE_OF_APPOINTMENT_ERROR_CODE, DATE_OF_APPOINTMENT_ERROR_MSG, DATE_OF_APPOINTMENT_TAG));
			return;
		}
		
		if (appointmentDate == null) {
			errors.add(new RecordError (record.getNric(), DATE_OF_APPOINTMENT_MISSING_CODE, DATE_OF_APPOINTMENT_MISSING_MSG, DATE_OF_APPOINTMENT_TAG));
		}
		// Check if future date. The date is after today, ignore time.
		else if (DateUtils.isAfterDay(appointmentDate, today)) {
			errors.add(new RecordError (record.getNric(), DATE_OF_APPOINTMENT_FUTURE_ERROR_CODE, DATE_OF_APPOINTMENT_FUTURE_ERROR_MSG, DATE_OF_APPOINTMENT_TAG));
		}
	}
	
	private static void validateDateOfRetirement (IRecord record, List<RecordError> errors) {
		
		Date dateOfRetirement = null;
			
		try {
			dateOfRetirement = record.getDateOfRetirement();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), RETIREMENT_DATE_ERROR_CODE, RETIREMENT_DATE_ERROR_MSG, RETIREMENT_DATE_TAG));
			return;
		}
			
		if (dateOfRetirement == null) {
			errors.add(new RecordError (record.getNric(), RETIREMENT_DATE_MISSING_CODE, RETIREMENT_DATE_MISSING_MSG, RETIREMENT_DATE_TAG));
		}
	}
	
	private static void validateDateOfBirth (IRecord record, List<RecordError> errors) {
		Date dob = null;
		try {
			dob = record.getDateOfBirth();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), DATE_OF_BIRTH_ERROR_CODE, DATE_OF_BIRTH_ERROR_MSG, DATE_OF_BIRTH_TAG));
			return;
		}
		
		if (dob == null) {
			errors.add(new RecordError (record.getNric(), DATE_OF_BIRTH_MISSING_CODE, DATE_OF_BIRTH_MISSING_MSG, DATE_OF_BIRTH_TAG));
		}
	}
	
	private static void validateDateOfEnlistment (IRecord record, List<RecordError> errors) {
		try {
			record.getDateOfEnlistment();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), DATE_OF_ENLISTMENT_ERROR_CODE, DATE_OF_ENLISTMENT_ERROR_MSG, DATE_OF_ENLISTMENT_TAG));
		}
	}
	
	private static void validateLeavingServiceDate (IRecord record, List<RecordError> errors) {
		try {
			record.getLeavingServiceDate();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), LEAVING_SERVICE_DATE_ERROR_CODE, LEAVING_SERVICE_DATE_ERROR_MSG, LEAVING_SERVICE_DATE_TAG));
		}
	}
	
	private static void validateSalary (IRecord record, List<RecordError> errors) {
		
		try {
			record.getSalary();
		} 
		catch (NumberFormatException e) {
			errors.add(new RecordError (record.getNric(), SALARY_ERROR_CODE, SALARY_ERROR_MSG, SALARY_TAG));
			
		}
		
	}
	
	private static void validateAddress (JobType jobType, IRecord record, List<RecordError> errors) {
		
		CodeInfo codeInfo = record.getAddressType();
		String addressType = codeInfo.getValue();
		
		// Address type is mandatory for HRHUB and NSPAM
		if ((jobType != JobType.HTVMS) && (addressType == null  || addressType.isEmpty())){
			errors.add(new RecordError (record.getNric(), ADDRESS_TYPE_MISSING_CODE, ADDRESS_TYPE_MISSING_MSG, ADDRESS_TYPE_TAG));
		}
		
		String blockNumber = record.getBlockNumber();
		if (blockNumber != null && blockNumber.length() > 10) {
			errors.add(new RecordError (record.getNric(), RESIDENTIAL_BLOCK_ERROR_CODE, RESIDENTIAL_BLOCK_ERROR_MSG, RESIDENTIAL_BLOCK_TAG));
		}
		
		String streetName = record.getStreetName();
		
		//Street Name is Mandatory for HRHUB and NSPAM
		if (jobType != JobType.HTVMS) {
			if (streetName == null || streetName.isEmpty()) {
				errors.add(new RecordError (record.getNric(), STREET_NAME_MISSING_CODE, STREET_NAME_MISSING_MSG, STREET_NAME_TAG));
			}
			else if (streetName != null && streetName.length() > 32) {
				errors.add(new RecordError (record.getNric(), STREET_NAME_ERROR_CODE, STREET_NAME_ERROR_MSG, STREET_NAME_TAG));
			}
		} else {
			if (streetName != null && streetName.length() > 32) {
				errors.add(new RecordError (record.getNric(), STREET_NAME_ERROR_CODE, STREET_NAME_ERROR_MSG, STREET_NAME_TAG));
			}
		}
		
		String floorNumber = record.getFloorNumber();
		if (floorNumber != null && floorNumber.length() > 3) {
			errors.add(new RecordError (record.getNric(), RESIDENTIAL_FLOOR_NUMBER_ERROR_CODE, RESIDENTIAL_FLOOR_NUMBER_ERROR_MSG, RESIDENTIAL_FLOOR_TAG));
		}
		
		String unitNum = record.getUnitNumber ();
		if (unitNum != null && unitNum.length() > 5) {
			errors.add(new RecordError (record.getNric(), RESIDENTIAL_UNIT_NUMBER_ERROR_CODE, RESIDENTIAL_UNIT_NUMBER_ERROR_MSG, RESIDENTIAL_UNIT_TAG));
		}
		
		String buildingName = record.getBuildingName();
		if (buildingName != null && buildingName.length() > 32) {
			errors.add(new RecordError (record.getNric(), RESIDENTIAL_BUILDING_NAME_ERROR_CODE, RESIDENTIAL_BUILDING_NAME_ERROR_MSG, RESIDENTIAL_BUILDING_NAME_TAG));
		}
		
		String postalCode = record.getPostalCode();
		if (postalCode != null && postalCode.length() > 6) {
			errors.add(new RecordError (record.getNric(), POSTAL_CODE_ERROR_CODE, POSTAL_CODE_ERROR_MSG, POSTAL_CODE_TAG));
		}
	}
	
	private static void validateMobileContact (IRecord validateMobileContactRecord, List<RecordError> errors) {
		
		List<String> mobileContacts = validateMobileContactRecord.getMobileContact();
		for (String contact : mobileContacts) {
			if (contact != null && contact.length() > 16) {
				errors.add(new RecordError (validateMobileContactRecord.getNric(), MOBILE_NUMBER_ERROR_CODE, MOBILE_NUMBER_ERROR_MSG, MOBILE_NUMBER_TAG));
			}
		}
	}
	
	private static void validateOfficeContact (IRecord record, List<RecordError> errors) {
		
		List<String> contacts = record.getOfficeContact();
		for (String contact : contacts) {
			if (contact != null && contact.length() > 16) {
				errors.add(new RecordError (record.getNric(), OFFICE_NUMBER_ERROR_CODE, OFFICE_NUMBER_ERROR_MSG, OFFICE_NUMBER_TAG));
			}
		}
	}

	private static void validateHomeContact (IRecord record, List<RecordError> errors) {

		String contact = record.getHomeContact();
		if (contact != null && contact.length() > 16) {
			errors.add(new RecordError (record.getNric(), HOME_NUMBER_ERROR_CODE, HOME_NUMBER_ERROR_MSG, HOME_NUMBER_TAG));
		}
	}
	
	private static void validateOfficeEmail (IRecord record, List<RecordError> errors) {
		
		String email = record.getOfficeEmail();
		if (email != null && !email.trim().isEmpty()) {
			if (email.length() > 256) {
				errors.add(new RecordError (record.getNric(), OFFICE_EMAIL_ERROR_CODE, OFFICE_EMAIL_ERROR_MSG, OFFICE_EMAIL_TAG));
			}
			else if (!EmailValidator.validateEmail(email)) {
				errors.add(new RecordError (record.getNric(), OFFICE_EMAIL_FORMAT_CODE, OFFICE_EMAIL_FORMAT_MSG, OFFICE_EMAIL_TAG));
			}
		}
	}
	
	private static void validatePersonalEmail (IRecord record, List<RecordError> errors) {
		
		String email = record.getPersonalEmail();
		if (email != null && !email.trim().isEmpty()) {
			if (email.length() > 256) {
				errors.add(new RecordError (record.getNric(), PERSONAL_EMAIL_ERROR_CODE, PERSONAL_EMAIL_ERROR_MSG, PERSONAL_EMAIL_TAG));
			}
			else if (!EmailValidator.validateEmail(email)) {
				errors.add(new RecordError (record.getNric(), PERSONAL_EMAIL_FORMAT_CODE, PERSONAL_EMAIL_FORMAT_MSG, PERSONAL_EMAIL_TAG));
			}
		}
	}
	
	private static void validateLeave (IRecord record, List<RecordError> errors) {
		
		if (record.getLeaves() == null) {
			return;
		}
		
		for (ILeave leave : record.getLeaves()) {
			try {
				leave.getStartDate();
			} 
			catch (ParseException e) {
				errors.add(new RecordError (record.getNric(), LEAVE_START_DATE_ERROR_CODE, LEAVE_START_DATE_ERROR_MSG, LEAVE_START_DATE_TAG));
			}
			
			try {
				leave.getEndDate();
			} 
			catch (ParseException e) {
				errors.add(new RecordError (record.getNric(), LEAVE_END_DATE_ERROR_CODE, LEAVE_END_DATE_ERROR_MSG, LEAVE_END_DATE_TAG));
			}
			
			CodeInfo codeInfo = leave.getLeaveType();
			String type = codeInfo.getValue();
			if (type == null || type.isEmpty()) {
				errors.add(new RecordError (record.getNric(), LEAVE_TYPE_MISSING_CODE, LEAVE_TYPE_MISSING_MSG, LEAVE_TYPE_TAG));
			}
			
		}
	}
	
	private static void validateSpouses (JobType jobType, IRecord record, List<RecordError> errors) {
		
		if (record.getSpouses() == null) {
			return;
		}
		
		List<String> spouseIds = new ArrayList<>();
		
		for (ISpouse spouse : record.getSpouses()) {
			
			validateSpouseDateOfMarriage (spouse, record, errors);
				
			String name = spouse.getName();
			if (name != null && !name.trim().isEmpty()) {
				if (name.length() > 100) {
					errors.add(new RecordError (record.getNric(), SPOUSE_NAME_ERROR_CODE, SPOUSE_NAME_ERROR_MSG, SPOUSE_NAME_TAG));
				}
				else if (!NAME_ALLOWED_CHARACTER_PATTERN.matcher(name).matches()) {
					errors.add(new RecordError (record.getNric(), SPOUSE_NAME_INVALID_CODE, SPOUSE_NAME_INVALID_MSG, SPOUSE_NAME_TAG));
				}
			}
			
			String id = spouse.getId();
			
			if (id != null && !id.isEmpty()){
				if (id.length() > 50) {
					errors.add(new RecordError (record.getNric(), SPOUSE_ID_ERROR_CODE, SPOUSE_ID_ERROR_MSG, SPOUSE_ID_TAG));
				}
				
				if (spouseIds.contains(id.toUpperCase())) {
					errors.add(new RecordError (record.getNric(), SPOUSE_ID_DUPLICATE_CODE, SPOUSE_ID_DUPLICATE_MSG, SPOUSE_ID_TAG));
				}
				else {
					spouseIds.add(id.toUpperCase());
				}
				
				// Only HRHUB has ID Type Info.
				if (jobType == JobType.HRHUB) {
				
					CodeInfo codeInfo = spouse.getIdType();
					String idType = codeInfo.getValue();
				
					if (idType != null && !idType.isEmpty()) {
						validateSpouseNRIC (spouse, record, errors);
					}
				}
				
			}
			
			String certificateNumber = spouse.getCertificateNumber();
			if (certificateNumber != null && certificateNumber.length() > 50) {
				errors.add(new RecordError (record.getNric(), MARRIAGE_CERTIFICATE_LENGTH_INVALID_CODE, MARRIAGE_CERTIFICATE_LENGTH_INVALID_MSG, MARRIAGE_CERTIFICATE_TAG));
			}
			
		}
	}
	
	private static void validateSpouseDateOfMarriage (ISpouse spouse, IRecord record, List<RecordError> errors) {

        try {
            spouse.getDateOfMarriage();
        }
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), DATE_OF_MARRIAGE_ERROR_CODE, DATE_OF_MARRIAGE_ERROR_MSG, DATE_OF_MARRIAGE_TAG));
		}
	}
	
	
	private static boolean isHrhubNricOrFinIdType (String idType) {
		ICodeConfig codeConfig = ServiceConfig.getInstance().getCodeConfig();
		
		return codeConfig.hrhubNricFinType().contains(idType);
	}
	
	
	private static void validateSpouseNRIC (ISpouse spouse, IRecord record, List<RecordError> errors) {
		// Get code type for Spouse
		CodeInfo codeInfo = spouse.getIdType();
		String idType = codeInfo.getValue();
		
		if (Objects.equals(idType, "NR") || Objects.equals(idType, "FI")){
			String nric = spouse.getId().toUpperCase(Locale.ENGLISH);
			
			// NRIC must be 9 character long
			if (nric.length() != 9) {
				errors.add(new RecordError (record.getNric(), SPOUSE_ID_LENGTH_INVALID_CODE, SPOUSE_ID_LENGTH_INVALID_MSG, SPOUSE_ID_TAG));
			}
			else if (!NRIC_ALLOWED_CHARACTER_PATTERN.matcher(nric).matches()) {
				errors.add(new RecordError (record.getNric(), SPOUSE_ID_INVALID_CODE, SPOUSE_ID_INVALID_MSG, SPOUSE_ID_TAG));
			}
			else if (!(nric.startsWith("T") || nric.startsWith("S") || nric.startsWith("F") || nric.startsWith("G") || nric.startsWith("M"))) {
				errors.add(new RecordError (record.getNric(), SPOUSE_ID_INVALID_START_CHARACTER_CODE, SPOUSE_ID_INVALID_START_CHARACTER_MSG, SPOUSE_ID_TAG));
			}
			else if (!NricNumberCheck.isValid(nric)) {
				errors.add(new RecordError (record.getNric(), SPOUSE_ID_INVALID_CHECKSUM_CODE, SPOUSE_ID_INVALID_CHECKSUM_MSG, SPOUSE_ID_TAG));
			}
		}
		
	}
	
	private static void validateChildren (JobType jobType, IRecord record, List<RecordError> errors) {
		
		if (record.getChildren() == null) {
			return;
		}
		
		List<String> childIds = new ArrayList<>();
		
		for (IChild child : record.getChildren()) {
			
			validateChildDateOfBirth (child, record, errors);
			
			String name = child.getName();
			if (name != null && !name.trim().isEmpty()) {
				if (name.length() > 100) {
					errors.add(new RecordError (record.getNric(), CHILD_NAME_LENGTH_INVALID_CODE, CHILD_NAME_LENGTH_INVALID_MSG, CHILD_NAME_TAG));
				}
				else if (!NAME_ALLOWED_CHARACTER_PATTERN.matcher(name).matches()) {
					errors.add(new RecordError (record.getNric(), CHILD_NAME_INVALID_CODE, CHILD_NAME_INVALID_MSG, CHILD_NAME_TAG));
				}
			}

			String id = child.getId();
			if (id != null && !id.isEmpty()){
				if (id.length() > 50) {
					errors.add(new RecordError (record.getNric(), CHILD_ID_ERROR_CODE, CHILD_ID_ERROR_MSG, CHILD_ID_TAG));
				}
				// Check to ensure no duplicate child ids
				if (childIds.contains(id.toUpperCase())) {
					errors.add(new RecordError (record.getNric(), CHILD_ID_DUPLICATE_ERROR_CODE, CHILD_ID_DUPLICATE_ERROR_MSG, CHILD_ID_TAG));
				}
				else {
					childIds.add(id.toUpperCase());
				}
				
				// Only HRHUB has ID Type Info.
				if (jobType == JobType.HRHUB) {
						
					CodeInfo codeInfo = child.getIdType();
					String idType = codeInfo.getValue();
						
					if (idType != null  && !idType.isEmpty()) {
						validateChildNRIC (child, record, errors);
					}
				}
			}
			
		}
	}
	
	
	private static void validateChildDateOfBirth (IChild child, IRecord record, List<RecordError> errors) {

        try {
            child.getDateOfBirth();
        }
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), CHILD_DOB_ERROR_CODE, CHILD_DOB_ERROR_MSG, CHILD_DOB_TAG));
		}
	}
	
	private static void validateChildNRIC (IChild child, IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = child.getIdType();
		String idType = codeInfo.getValue();
		
		if (Objects.equals(idType, "NR") || Objects.equals(idType, "FI")){
			String nric = child.getId().toUpperCase(Locale.ENGLISH);
			
			// NRIC must be 9 character long
			if (nric.length() != 9) {
				errors.add(new RecordError (record.getNric(), CHILD_ID_LENGTH_INVALID_CODE, CHILD_ID_LENGTH_INVALID_MSG, CHILD_ID_TAG));
			}
			else if (!NRIC_ALLOWED_CHARACTER_PATTERN.matcher(nric).matches()) {
				errors.add(new RecordError (record.getNric(), CHILD_ID_INVALID_CODE, CHILD_ID_INVALID_MSG, CHILD_ID_TAG));
			}
			else if (!(nric.startsWith("T") || nric.startsWith("S") || nric.startsWith("F") || nric.startsWith("G") || nric.startsWith("M"))) {
				errors.add(new RecordError (record.getNric(), CHILD_ID_INVALID_START_CHARACTER_CODE, CHILD_ID_INVALID_START_CHARACTER_MSG, CHILD_ID_TAG));
			}
			else if (!NricNumberCheck.isValid(nric)) {
				errors.add(new RecordError (record.getNric(), CHILD_ID_INVALID_CHECKSUM_CODE, CHILD_ID_INVALID_CHECKSUM_MSG, CHILD_ID_TAG));
			}
		}
		
	}
	
	private static void validateMedicalScheme (IRecord record, List<RecordError> errors) {
		
		CodeInfo codeInfo = record.getMedicalScheme();
		String medicalScheme = codeInfo.getValue();
		
		// Medical Scheme is mandatory. 
		if (medicalScheme == null  || medicalScheme.isEmpty()) {
			errors.add(new RecordError (record.getNric(), MEDICAL_SCHEME_MISSING_CODE, MEDICAL_SCHEME_MISSING_MSG, MEDICAL_SCHEME_TAG));
		}
	}
	
	private static void validateRank (IRecord record, List<RecordError> errors) {
		
		CodeInfo codeInfo = record.getRankOrGrade();
		String rank = codeInfo.getValue();
		
		// Rank is mandatory. 
		if (rank == null  || rank.isEmpty()) {
			errors.add(new RecordError (record.getNric(), RANK_MISSING_CODE, RANK_MISSING_MSG, RANK_TAG));
		}
	}
	
	
	public static List<RecordError> validateDates (JobType jobType, IRecord record) {
		
		List<RecordError> errors = new ArrayList<>();
		
		Date today = new Date ();	
		
		Date appointmentDate = null;
		
		try {
			appointmentDate = record.getDateOfAppointment();
		} 
		catch (ParseException e) {
			// This should not happen as already check in earlier validation.
			errors.add(new RecordError (record.getNric(), DATE_OF_APPOINTMENT_ERROR_CODE, DATE_OF_APPOINTMENT_ERROR_MSG, DATE_OF_APPOINTMENT_TAG));
		}
		
		Date dateOfRetirement = null;
		
		// Retirement date checks
		if (jobType != JobType.HTVMS) {
			
			try {
				dateOfRetirement = record.getDateOfRetirement();
			} 
			catch (ParseException e) {
				// This should not happen as already check in earlier validation.
				errors.add(new RecordError (record.getNric(), RETIREMENT_DATE_ERROR_CODE, RETIREMENT_DATE_ERROR_MSG, RETIREMENT_DATE_TAG));
			}
			
			// Date of retirement cannot be contains past date. 
			if (DateUtils.isBeforeDay(dateOfRetirement, today)) {
				errors.add(new RecordError (record.getNric(), RETIREMENT_DATE_PAST_DATE_CODE, RETIREMENT_DATE_PAST_DATE_MSG, RETIREMENT_DATE_TAG));
			}
			
			// Date of retirement cannot be before appointment date
			if (DateUtils.isBeforeDay(dateOfRetirement, appointmentDate)) {
				errors.add(new RecordError (record.getNric(), RETIREMENT_DATE_BEFORE_APPOINTMENT_DATE_CODE, RETIREMENT_DATE_BEFORE_APPOINTMENT_DATE_MSG, RETIREMENT_DATE_TAG));
			}
		}
		
		// Leaving service date checks
		Date leavingServDate = null;
		
		try {
			leavingServDate = record.getLeavingServiceDate();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), LEAVING_SERVICE_DATE_ERROR_CODE, LEAVING_SERVICE_DATE_ERROR_MSG, LEAVING_SERVICE_DATE_TAG));
		}
		
		// If not null, then must NOT be past date, before appointment date, and after retirement date
		if (leavingServDate != null) {
			if (DateUtils.isBeforeDay(leavingServDate, today)) {
				errors.add(new RecordError (record.getNric(), LEAVING_SERVICE_DATE_PAST_DATE_CODE, LEAVING_SERVICE_DATE_PAST_DATE_MSG, LEAVING_SERVICE_DATE_TAG));
			}
			
			if (DateUtils.isBeforeDay(leavingServDate, appointmentDate)) {
				errors.add(new RecordError (record.getNric(), LEAVING_SERVICE_DATE_BEFORE_APPOINTMENT_DATE_CODE, LEAVING_SERVICE_DATE_BEFORE_APPOINTMENT_DATE_MSG, LEAVING_SERVICE_DATE_TAG));
			}
			
			if ((jobType != JobType.HTVMS) &&  (DateUtils.isAfterDay (leavingServDate, dateOfRetirement))){
				errors.add(new RecordError (record.getNric(), LEAVING_SERVICE_DATE_AFTER_RETIREMENT_DATE_CODE, LEAVING_SERVICE_DATE_AFTER_RETIREMENT_DATE_MSG, LEAVING_SERVICE_DATE_TAG));
			}
		}
		
		return errors;
	}
	
	
	private static void validatePresentUnit (IRecord record, List<RecordError> errors) {
		CodeInfo codeInfo = record.getPresentUnit();
		String presentUnit = codeInfo.getValue();
		
		// PresentUnit is mandatory. 
		if (presentUnit == null  || presentUnit.isEmpty()) {
			errors.add(new RecordError (record.getNric(), PRESENT_UNIT_MISSING_CODE, PRESENT_UNIT_MISSING_MSG, PRESENT_UNIT_TAG));
		}
	}
	
	private static void validateCurrentSchemeDate (IRecord record, List<RecordError> errors) {
		try {
			record.getCurrentSchemeDate();
		} 
		catch (ParseException e) {
			errors.add(new RecordError (record.getNric(), CURRENT_SCHEME_DATE_ERROR_CODE, CURRENT_SCHEME_DATE_ERROR_MSG, CURRENT_SCHEME_DATE_TAG));
		}
	}
}
