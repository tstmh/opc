package com.stee.spfcore.webapi.model.marketing;

public enum DurationUnit {

	DAYS ("days"), MONTHS ("months"), YEARS ("years");
	
	private String value;
	
	private DurationUnit (String value) {
		this.value = value;
	}
	
	public static DurationUnit getDurationUnit (String value) {
		DurationUnit [] units = DurationUnit.values();
		for (DurationUnit unit : units) {
			if (unit.value.equals(value)) {
				return unit;
			}
		}
		
		return null;
	}
	
	public String toString () {
		return value;
	}
}

