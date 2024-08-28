package com.stee.spfcore.webapi.model.marketing;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Category {
	
	PERSONNEL ("personnel", new Field [] {Field.NRIC, Field.NAME, Field.CITIZENSHIP, 
																				Field.DATE_OF_BIRTH, Field.EDUATION_LEVEL,
																				Field.GENDER, Field.MARITAL_STATUS,
																				Field.NATIONALITY, Field.RACE, Field.INTEREST}), 
																				
	EMPLOYMENT ("employment", new Field [] {Field.APPOINTMENT_DATE, Field.RETIREMENT_DATE, 
																					Field.LEAVING_SERVICE_DATE,	Field.RANK, Field.DEPARTMENT,
																					Field.SUBUNIT, Field.SERVICE_TYPE, Field.EMPLOYMENT_STATUS}), 
	
	CHILD ("child", new Field [] {Field.CHILD_COUNT, Field.DATE_OF_BIRTH}),
	
	SPOUSE ("spouse", new Field [] {Field.DATE_OF_MARRIAGE, Field.DATE_OF_BIRTH}), 
	
	MEMBERSHIP ("membership", new Field [] {Field.CESSATION_DATE, Field.EFFECTIVE_DATE, Field.EXPIRY_DATE,
																					Field.MEMBERSHIP_STATUS, Field.MEMBERSHIP_TYPE, Field.HAS_INSURANCE_COVERAGE}),
	
	
	COURSE ("course", new Field [] {Field.ATTENDED_COURSE_TITLE}),
	
	
	BLACKLIST ("blacklist", new Field [] {Field.BLACKLISTED_MODULE});
	
	
	private String value;
	private List<Field> fields;
	
	private Category (String value, Field [] fields) {
		this.value = value;
		this.fields = Arrays.asList(fields);
	}
	
	
	public static Category getCategory (String value) {
		
		Category [] categories = Category.values();
		
		for (Category category : categories) {
			if (category.value.equals(value)) {
				return category;
			}
		}
		
		return null;
	} 
	
	public String toString () {
		return value;
	}
	
	public boolean containsField (Field field) {
		return fields.contains(field);
	}
	
	public List<Field> getFields () {
		return Collections.unmodifiableList(fields);
	}

}

