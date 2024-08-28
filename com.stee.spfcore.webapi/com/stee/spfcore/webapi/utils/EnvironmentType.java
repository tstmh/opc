package com.stee.spfcore.webapi.utils;

public enum EnvironmentType {

	INTERNET("internet"),
	INTRANET("intranet"),
	JUNIT_TEST("junit");
	
	private String type;
	
	private EnvironmentType (String type) {
		this.type = type;
	}
	
	public static EnvironmentType get (String type) {
		
		EnvironmentType [] typeList = EnvironmentType.values();
		for (EnvironmentType environmentType : typeList) {
			if (environmentType.type.equals(type)) {
				return environmentType;
			}
		}
		return JUNIT_TEST;
	}
	
	public String toString () {
		return this.type;
	}
}
