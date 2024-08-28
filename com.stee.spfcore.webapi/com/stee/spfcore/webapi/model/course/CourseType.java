package com.stee.spfcore.webapi.model.course;


public enum CourseType {

	COMPULSORY ("Compulsory"),
	NON_COMPULSORY ("Non-compulsory");
	
	private String value;
	
	private CourseType (String value) {
		this.value = value;
	}
	
	public String toString () {
		return value;
	}
	
	public static CourseType getCourseType (String value) {
		CourseType [] courseTypes = CourseType.values();
		
		for (CourseType type : courseTypes) {
			if (type.value.equals(value)) {
				return type;
			}
		}
		
		return null;
	}
	
}
