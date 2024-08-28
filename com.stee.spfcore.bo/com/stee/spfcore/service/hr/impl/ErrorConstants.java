package com.stee.spfcore.service.hr.impl;

public class ErrorConstants {
	
	public static final String INVALID_XML = "Invalid XML format";
	
	public static final String PROCESS_ID_NOT_DEFINE = "Process Id not define.";
	
	public static final String INVALID_RECORD_COUNT = "Invalid record count.";
	
	public static final String RECORD_COUNT_NOT_DEFINE = "Record count not define.";
	
	public static final String CODE_TABLE_TAG = "CODE TABLE";
	public static final String CODE_TABLE_ERROR_CODE = "N001";
	
	public static final String CODE_TABLE_ERROR_MSG = "Code '@Code@' not found for @Field@";
	
	// Name Field error
	public static final String NAME_TAG = "NAME";
	public static final String NAME_MISSING_CODE = "E001";
	public static final String NAME_MISSING_MSG = "Name field is blank, mandatory field missing";
	public static final String NAME_INVALID_CODE = "E002";
	public static final String NAME_INVALID_MSG = "Name field is invalid, field contains Special characters";
	public static final String NAME_LENGTH_INVALID_CODE = "E003";
	public static final String NAME_LENGTH_INVALID_MSG = "Name field is invalid, field length is not valid";
	
	//ID Type (Citizenship) field error
	public static final String ID_TYPE_TAG = "ID TYPE";
	public static final String ID_TYPE_MISSING_CODE = "E004";
	public static final String ID_TYPE_MISSING_MSG = "Type of ID is blank, mandatory field missing";
	
	// NRIC field error
	public static final String NRIC_TAG = "NRIC";
	public static final String NRIC_MISSING_CODE = "E007";
	public static final String NRIC_MISSING_MSG = "FIN/NRIC is blank, mandatory field missing";
	public static final String NRIC_LENGTH_INVALID_CODE = "E008";
	public static final String NRIC_LENGTH_INVALID_MSG = "FIN/NRIC field is invalid, field length is not valid";
	public static final String NRIC_INVALID_CODE = "E009";
	public static final String NRIC_INVALID_MSG = "FIN/NRIC  field is invalid, field contains Special characters";
	public static final String NRIC_INVALID_CHECKSUM_CODE = "E010";
	public static final String NRIC_INVALID_CHECKSUM_MSG = "FIN/NRIC  Failed on check sum algorithm validation, invalid ID";
	public static final String NRIC_INVALID_START_CHARACTER_CODE = "E011";
	public static final String NRIC_INVALID_START_CHARACTER_MSG = "NRIC not starting with the Valid Alphabets";
	
	public static final String DEPARTMENT_TAG = "DEPARTMENT";
	public static final String DEPARTMENT_MISSING_CODE = "E013";
	public static final String DEPARTMENT_MISSING_MSG = "Department field is blank, mandatory field missing";
	
	// Date of Birth error
	public static final String DATE_OF_BIRTH_TAG = "DATE OF BIRTH";
	public static final String DATE_OF_BIRTH_MISSING_CODE = "E019";
	public static final String DATE_OF_BIRTH_MISSING_MSG = "Date of birth field is blank, mandatory field missing";
	
	public static final String DATE_OF_BIRTH_ERROR_CODE = "E020";
	public static final String DATE_OF_BIRTH_ERROR_MSG = "Date of birth field is invalid.";
	
	// Appointment Date error
	public static final String DATE_OF_APPOINTMENT_TAG = "DATE OF APPOINTMENT";
	public static final String DATE_OF_APPOINTMENT_MISSING_CODE = "E022";
	public static final String DATE_OF_APPOINTMENT_MISSING_MSG = "Appointment date is blank, mandatory fields missing";
	public static final String DATE_OF_APPOINTMENT_ERROR_CODE = "E023";
	public static final String DATE_OF_APPOINTMENT_ERROR_MSG = "Appointment date field is invalid";
	public static final String DATE_OF_APPOINTMENT_FUTURE_ERROR_CODE = "E024";
	public static final String DATE_OF_APPOINTMENT_FUTURE_ERROR_MSG = "Appointment date field is invalid, field contains future date";
	
	public static final String LEAVING_SERVICE_DATE_TAG = "LEAVING SERVICE DATE";
	public static final String LEAVING_SERVICE_DATE_ERROR_CODE = "E025";
	public static final String LEAVING_SERVICE_DATE_ERROR_MSG = "Leaving service date is invalid.";

	public static final String DATE_OF_ENLISTMENT_TAG = "DATE OF ENLISTMENT";
	public static final String DATE_OF_ENLISTMENT_ERROR_CODE = "E026";
	public static final String DATE_OF_ENLISTMENT_ERROR_MSG = "Enlistment date field is invalid.";
	
	public static final String MOBILE_NUMBER_TAG = "MOBILE NUMBER";
	public static final String MOBILE_NUMBER_ERROR_CODE = "E029";
	public static final String MOBILE_NUMBER_ERROR_MSG = "Contact number (H/P) is invalid, invalid length";
	
	public static final String OFFICE_NUMBER_TAG = "OFFICE NUMBER";
	public static final String OFFICE_NUMBER_ERROR_CODE = "E031";
	public static final String OFFICE_NUMBER_ERROR_MSG = "Contact number (O) is invalid, invalid length";
	
	public static final String HOME_NUMBER_TAG = "HOME NUMBER";
	public static final String HOME_NUMBER_ERROR_CODE = "E033";
	public static final String HOME_NUMBER_ERROR_MSG = "Contact number (H) is invalid, invalid length";
	
	public static final String LEAVE_START_DATE_TAG = "LEAVE START DATE";
	public static final String LEAVE_START_DATE_ERROR_CODE = "E034";
	public static final String LEAVE_START_DATE_ERROR_MSG = "Leave start date field is invalid.";
	
	public static final String LEAVE_END_DATE_TAG = "LEAVE END DATE";
	public static final String LEAVE_END_DATE_ERROR_CODE = "E035";
	public static final String LEAVE_END_DATE_ERROR_MSG = "Leave End date field is invalid.";
	
	public static final String LEAVE_TYPE_TAG = "LEAVE TYPE";
	public static final String LEAVE_TYPE_MISSING_CODE = "E036";
	public static final String LEAVE_TYPE_MISSING_MSG = "Type of Leave field is blank, mandatory field missing";

	public static final String RETIREMENT_DATE_TAG = "RETIREMENT DATE";
	public static final String RETIREMENT_DATE_MISSING_CODE = "E037";
	public static final String RETIREMENT_DATE_MISSING_MSG = "ORD/Retirement/Contract due date field is blank, mandatory field missing";

	public static final String RETIREMENT_DATE_ERROR_CODE = "E038";
	public static final String RETIREMENT_DATE_ERROR_MSG = "ORD/Retirement/Contract due date field is invalid.";
	
	public static final String RETIREMENT_DATE_PAST_DATE_CODE = "E039";
	public static final String RETIREMENT_DATE_PAST_DATE_MSG = "ORD/Retirement/Contract due date field is invalid, field contains past date";
	
	public static final String RETIREMENT_DATE_BEFORE_APPOINTMENT_DATE_CODE = "E040";
	public static final String RETIREMENT_DATE_BEFORE_APPOINTMENT_DATE_MSG = "ORD/Retirement/Contract due date field is invalid, date is before appointment date";
	
	public static final String GENDER_TAG = "GENDER";
	public static final String GENDER_MISSING_CODE = "E043";
	public static final String GENDER_MISSING_MSG = "Gender field is blank, mandatory field missing";
	
	public static final String MARITAL_STATUS_TAG = "MARITAL STATUS";
	public static final String MARITAL_STATUS_MISSING_CODE = "E047";
	public static final String MARITAL_STATUS_MISSING_MSG = "Marital status field is blank, mandatory field missing";
	
	public static final String DATE_OF_MARRIAGE_TAG = "DATE OF MARRIAGE";
	public static final String DATE_OF_MARRIAGE_ERROR_CODE = "E048";
	public static final String DATE_OF_MARRIAGE_ERROR_MSG = "Date of marriage field is invalid.";
	
	public static final String SPOUSE_NAME_TAG = "SPOUSE NAME";
	public static final String SPOUSE_NAME_ERROR_CODE = "E049";
	public static final String SPOUSE_NAME_ERROR_MSG = "Spouse Name field is invalid, field size is not valid";
	
	public static final String SPOUSE_NAME_INVALID_CODE = "E050";
	public static final String SPOUSE_NAME_INVALID_MSG = "Spouse Name field is invalid, field contains invalid special characters";
	
	public static final String SPOUSE_ID_TAG = "SPOUSE ID";
	public static final String SPOUSE_ID_ERROR_CODE = "E051";
	public static final String SPOUSE_ID_ERROR_MSG = "Spouse ID is invalid, contains invalid field length";
	
	public static final String SPOUSE_ID_MISSING_CODE = "E052";
	public static final String SPOUSE_ID_MISSING_MSG = "Spouse ID field is blank, mandatory field missing";
	
	public static final String DATE_OF_MARRIAGE_MISSING_CODE = "E053";
	public static final String DATE_OF_MARRIAGE_MISSING_MSG = "Date of marriage field is blank, mandatory field missing";
	
	
	public static final String CHILD_NAME_TAG = "CHILD NAME";
	public static final String CHILD_NAME_LENGTH_INVALID_CODE = "E054";
	public static final String CHILD_NAME_LENGTH_INVALID_MSG = "Child Name field is invalid, field size is not valid";
	
	public static final String CHILD_NAME_INVALID_CODE = "E055";
	public static final String CHILD_NAME_INVALID_MSG = "Child Name field is invalid, field contains invalid special characters";
	
	public static final String CHILD_ID_TAG = "CHILD ID";
	public static final String CHILD_ID_ERROR_CODE = "E056";
	public static final String CHILD_ID_ERROR_MSG = "Child ID is not having valid length";
	
	public static final String CHILD_ID_MISSING_CODE = "E057";
	public static final String CHILD_ID_MISSING_MSG = "Child ID field is blank, mandatory field missing.";
	
	public static final String CHILD_DOB_TAG = "CHILD DOB";
	public static final String CHILD_DOB_MISSING_CODE = "E058";
	public static final String CHILD_DOB_MISSING_MSG = "Child DOB field is blank, mandatory field missing.";
	
	public static final String CHILD_DOB_ERROR_CODE = "E059";
	public static final String CHILD_DOB_ERROR_MSG = "Child DOB field is invalid.";
	
	
	public static final String ADDRESS_TYPE_TAG = "ADDRESS TYPE";
	public static final String ADDRESS_TYPE_MISSING_CODE = "E061";
	public static final String ADDRESS_TYPE_MISSING_MSG = "Address type field is missing, mandatory field missing";
	
	public static final String OFFICE_EMAIL_TAG = "OFFICE EMAIL";
	public static final String OFFICE_EMAIL_ERROR_CODE = "E062";
	public static final String OFFICE_EMAIL_ERROR_MSG = "Office email address is invalid, contains invalid field length";
	
	public static final String OFFICE_EMAIL_FORMAT_CODE = "E064";
	public static final String OFFICE_EMAIL_FORMAT_MSG = "Office email address contains invalid email format";
	
	public static final String PERSONAL_EMAIL_TAG = "PERSONAL EMAIL";
	public static final String PERSONAL_EMAIL_ERROR_CODE = "E065";
	public static final String PERSONAL_EMAIL_ERROR_MSG = "Personal email address is invalid, contains invalid field length";
	
	public static final String PERSONAL_EMAIL_FORMAT_CODE = "E067";
	public static final String PERSONAL_EMAIL_FORMAT_MSG = "Personal email address contains invalid email format";
	
	public static final String SERVICE_TYPE_TAG = "SERVICE TYPE";
	public static final String SERVICE_TYPE_ERROR_CODE = "E068";
	public static final String SERVICE_TYPE_ERROR_MSG = "Service Type field is blank, mandatory field missing";
	
	public static final String SALARY_TAG = "SALARY";
	public static final String SALARY_ERROR_CODE = "E070";
	public static final String SALARY_ERROR_MSG = "Gross Monthly is invalid.";
	
	public static final String EMPLOYMENT_STATUS_TAG = "EMPLOYMENT STATUS";
	public static final String EMPLOYMENT_STATUS_ERROR_CODE = "E071";
	public static final String EMPLOYMENT_STATUS_ERROR_MSG = "Employment Status field is blank, mandatory field missing";
	
	public static final String MEDICAL_SCHEME_TAG = "MEDICAL SCHEME";
	public static final String MEDICAL_SCHEME_MISSING_CODE = "E073";
	public static final String MEDICAL_SCHEME_MISSING_MSG = "Medical Scheme field is blank, mandatory field missing";
	
	public static final String RESIDENTIAL_BLOCK_TAG = "RESIDENTIAL_BLOCK";
	public static final String RESIDENTIAL_BLOCK_ERROR_CODE = "E075";
	public static final String RESIDENTIAL_BLOCK_ERROR_MSG = "Residential block is invalid, contains invalid field length";

	public static final String STREET_NAME_TAG = "STREET_NAME";
	public static final String STREET_NAME_ERROR_CODE = "E076";
	public static final String STREET_NAME_ERROR_MSG = "Street name is invalid, contains invalid field length";

	public static final String RESIDENTIAL_FLOOR_TAG = "RESIDENTIAL_FLOOR";
	public static final String RESIDENTIAL_FLOOR_NUMBER_ERROR_CODE = "E077";
	public static final String RESIDENTIAL_FLOOR_NUMBER_ERROR_MSG = "Residential floor number is invalid, contains invalid field length";

	public static final String RESIDENTIAL_UNIT_TAG = "RESIDENTIAL_UNIT";
	public static final String RESIDENTIAL_UNIT_NUMBER_ERROR_CODE = "E078";
	public static final String RESIDENTIAL_UNIT_NUMBER_ERROR_MSG = "Residential unit number is invalid, contains invalid field length";

	public static final String RESIDENTIAL_BUILDING_NAME_TAG = "RESIDENTIAL BUILDING NAME";
	public static final String RESIDENTIAL_BUILDING_NAME_ERROR_CODE = "E079";
	public static final String RESIDENTIAL_BUILDING_NAME_ERROR_MSG = "Residential building name is invalid, contains invalid field length";

	public static final String POSTAL_CODE_TAG = "POSTAL CODE";
	public static final String POSTAL_CODE_ERROR_CODE = "E080";
	public static final String POSTAL_CODE_ERROR_MSG = "Residential postal code is invalid, contains invalid field length";

	public static final String RESIDENTIAL_BLOCK_MISSING_CODE = "E081";
	public static final String RESIDENTIAL_BLOCK_MISSING_MSG = "Residential block field is blank, mandatory field missing";

	public static final String STREET_NAME_MISSING_CODE = "E082";
	public static final String STREET_NAME_MISSING_MSG = "Street name field is blank, mandatory field missing";

	public static final String POSTAL_CODE_MISSING_CODE = "E083";
	public static final String POSTAL_CODE_MISSING_MSG = "Residential postal code field is blank, mandatory field missing";

	public static final String RANK_TAG = "RANK";
	public static final String RANK_MISSING_CODE = "E084";
	public static final String RANK_MISSING_MSG = "Rank code field is blank, mandatory field missing";

	public static final String CHILD_NAME_MISSING_CODE = "E085";
	public static final String CHILD_NAME_MISSING_MSG = "Child name field is blank, mandatory field missing";

	public static final String SPOUSE_NAME_MISSING_CODE = "E086";
	public static final String SPOUSE_NAME_MISSING_MSG = "Spouse name field is blank, mandatory field missing";

	public static final String LEAVING_SERVICE_DATE_PAST_DATE_CODE = "E087";
	public static final String LEAVING_SERVICE_DATE_PAST_DATE_MSG = "Leaving service date is invalid, field contains past date";

	public static final String LEAVING_SERVICE_DATE_BEFORE_APPOINTMENT_DATE_CODE = "E088";
	public static final String LEAVING_SERVICE_DATE_BEFORE_APPOINTMENT_DATE_MSG = "Leaving service date is invalid, date is before appointment date";
	
	public static final String LEAVING_SERVICE_DATE_AFTER_RETIREMENT_DATE_CODE = "E089";
	public static final String LEAVING_SERVICE_DATE_AFTER_RETIREMENT_DATE_MSG = "Leaving service date is invalid, date is after ORD/Retirement/Contract due date";
	
	public static final String ACTIVE_RECORD_TAG = "ACTIVE RECORD";
	public static final String ACTIVE_RECORD_ERROR_CODE = "E100";
	public static final String ACTIVE_RECORD_ERROR_MSG = "Record with active employment status from other system already exists.";

	public static final String PRESENT_UNIT_TAG = "PRESENT UNIT";
	public static final String PRESENT_UNIT_MISSING_CODE = "E110";
	public static final String PRESENT_UNIT_MISSING_MSG = "PresentUnit field is blank, mandatory field missing";
	
	public static final String CURRENT_SCHEME_DATE_TAG = "CURRENT_SCHEME_DATE";
	public static final String CURRENT_SCHEME_DATE_ERROR_CODE = "E120";
	public static final String CURRENT_SCHEME_DATE_ERROR_MSG = "Current scheme date is invalid.";

	public static final String SPOUSE_ID_TYPE_MISSING_CODE = "E130";
	public static final String SPOUSE_ID_TYPE_MISSING_MSG = "ID Type of Spouse is blank, mandatory field missing";
	public static final String SPOUSE_ID_LENGTH_INVALID_CODE = "E131";
	public static final String SPOUSE_ID_LENGTH_INVALID_MSG = "FIN/NRIC field of Spouse is invalid, field length is not valid";
	public static final String SPOUSE_ID_INVALID_CODE = "E132";
	public static final String SPOUSE_ID_INVALID_MSG = "FIN/NRIC field of Spouse is invalid, field contains Special characters";
	public static final String SPOUSE_ID_INVALID_CHECKSUM_CODE = "E133";
	public static final String SPOUSE_ID_INVALID_CHECKSUM_MSG = "Spouse FIN/NRIC failed on check sum algorithm validation, invalid ID";
	public static final String SPOUSE_ID_INVALID_START_CHARACTER_CODE = "E134";
	public static final String SPOUSE_ID_INVALID_START_CHARACTER_MSG = "Spouse FIN/NRIC not starting with a valid Alphabets";
	public static final String SPOUSE_ID_DUPLICATE_CODE = "E135";
	public static final String SPOUSE_ID_DUPLICATE_MSG = "Duplicate Spouse FIN/NRIC.";
	
	public static final String MARRIAGE_CERTIFICATE_TAG = "MARRIAGE CERTIFICATE";
	public static final String MARRIAGE_CERTIFICATE_LENGTH_INVALID_CODE = "E136";
	public static final String MARRIAGE_CERTIFICATE_LENGTH_INVALID_MSG = "Marriage Certificate field is invalid, field length is not valid.";
	
	public static final String CHILD_ID_TYPE_TAG = "Child ID Type";
	public static final String CHILD_ID_TYPE_MISSING_CODE = "E140";
	public static final String CHILD_ID_TYPE_MISSING_MSG = "ID Type of Child is blank, mandatory field missing";
	public static final String CHILD_ID_LENGTH_INVALID_CODE = "E141";
	public static final String CHILD_ID_LENGTH_INVALID_MSG = "FIN/NRIC field of Child is invalid, field length is not valid";
	public static final String CHILD_ID_INVALID_CODE = "E142";
	public static final String CHILD_ID_INVALID_MSG = "FIN/NRIC field of Child is invalid, field contains Special characters";
	public static final String CHILD_ID_INVALID_CHECKSUM_CODE = "E143";
	public static final String CHILD_ID_INVALID_CHECKSUM_MSG = "Child FIN/NRIC failed on check sum algorithm validation, invalid ID";
	public static final String CHILD_ID_INVALID_START_CHARACTER_CODE = "E144";
	public static final String CHILD_ID_INVALID_START_CHARACTER_MSG = "Child FIN/NRIC not starting with a valid Alphabets";
	public static final String CHILD_ID_DUPLICATE_ERROR_CODE = "E145";
	public static final String CHILD_ID_DUPLICATE_ERROR_MSG = "Duplicate Child FIN/NRIC.";
	
	
	// Prevents instantiation
	private ErrorConstants () {}
	
}
