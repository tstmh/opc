package com.stee.spfcore.model;

public enum Module {

	COURSE ("WelfareCourse"),
	FAMILY_DAY ("FamilyDay"),
	FLU ("FluVaccination"),
	HIV_HEALTH_SCREENING ("HivHealthScreening"),
	SAG ("SAG");
	
	private String value;
	
	private Module (String value) {
		this.value = value;
	}

	@Override
	public String toString () {
		return value;
	}
	
	public static Module getModule (String value) {
		Module [] modules = Module.values();
		
		for (Module module : modules) {
			if (module.value.equals(value)) {
				return module;
			}
		}
		
		return null;
	}
	
}
