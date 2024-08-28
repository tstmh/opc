package com.stee.spfcore.webapi.model.course;

public enum CourseStatus {

	DRAFT ("Draft"),
	ACTIVATED ("Activated"),
	CANCELLED ("Cancelled");
	
	private String value;
	
	private CourseStatus (String value) {
		this.value = value;
	}
	
	public String toString () {
		return this.value;
	}
	
	
	public static CourseStatus getCourseStatus (String value) {
		
		CourseStatus [] courseStatusList = CourseStatus.values();
		for (CourseStatus courseStatus : courseStatusList) {
			if (courseStatus.value.equals(value)) {
				return courseStatus;
			}
		}
		return null;
	}
	
}
