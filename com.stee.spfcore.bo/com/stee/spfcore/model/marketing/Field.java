package com.stee.spfcore.model.marketing;

import java.util.Arrays;
import java.util.List;


public enum Field {

	// Personnel, Child, Spouse
	DATE_OF_BIRTH ("dateOfBirth", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	
	// Personnel
	NAME ("name", new RuleType [] {RuleType.STRING, RuleType.LIST}),
	NRIC ("nric", new RuleType [] {RuleType.STRING, RuleType.LIST}),
	CITIZENSHIP ("citizenship", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	EDUATION_LEVEL ("eduationLevel", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	GENDER ("gender", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	MARITAL_STATUS ("maritalStatus", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	NATIONALITY ("nationality", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	RACE ("race", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	INTEREST ("interest", new RuleType [] {RuleType.LIST}),
	
	// Employment
	APPOINTMENT_DATE ("dateOfAppointment", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	RETIREMENT_DATE ("dateOfRetirement", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	LEAVING_SERVICE_DATE ("leavingServiceDate", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	RANK ("rank", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	DEPARTMENT ("department", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	SUBUNIT ("subunit", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	SERVICE_TYPE ("serviceType", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	EMPLOYMENT_STATUS ("employmentStatus", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	DIVISION_STATUS ("divisionStatus", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	SCHEME_OF_SERVICE ("schemeOfService", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	DESIGNATION ("designation", new RuleType [] {RuleType.CODE, RuleType.LIST}),
	
	// Child
	CHILD_COUNT ("childCount", new RuleType [] {RuleType.NUMBER}),
	
	// Spouse
	DATE_OF_MARRIAGE ("dateOfMarriage", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	
	// Membership
	CESSATION_DATE ("dateOfCessation", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	EFFECTIVE_DATE ("effectiveDate", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	EXPIRY_DATE ("expiryDate", new RuleType [] {RuleType.DATE, RuleType.DURATION}),
	MEMBERSHIP_STATUS ("membershipStatus", new RuleType [] {RuleType.STRING, RuleType.LIST}),
	MEMBERSHIP_TYPE ("membershipType", new RuleType [] {RuleType.STRING, RuleType.LIST}),
	HAS_INSURANCE_COVERAGE ("hasInsuranceCoverage", new RuleType [] {RuleType.BOOLEAN}),
	
	
	// Course
	ATTENDED_COURSE_TITLE ("attendedCourseTitle", new RuleType [] {RuleType.LIST}),
	
	// Blacklist
	BLACKLISTED_MODULE ("blacklistedModule", new RuleType [] {RuleType.LIST})
	;
	
	private String value;
	private List<RuleType> ruleTypes;
	
	
	private Field (String value, RuleType [] ruleTypes) {
		this.value = value;
		this.ruleTypes = Arrays.asList(ruleTypes);
	}
	
	public static Field getField (String value) {
		
		Field [] fields = Field.values();
		
		for (Field field : fields) {
			if (field.value.equals(value)) {
				return field;
			}
		}
		
		return null;
	}
	
	public boolean isApplicable (RuleType ruleType) {
		return ruleTypes.contains(ruleType);
	}

	@Override
	public String toString () {
		return value;
	}
	
	
}
