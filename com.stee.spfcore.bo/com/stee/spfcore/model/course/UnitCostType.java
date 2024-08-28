package com.stee.spfcore.model.course;

public enum UnitCostType {
	
	PER_PAX ("Per Pax"),
	PER_CLASS ("Per Class"),
	FREE ("Free");
	
	private String value;
	
	private UnitCostType (String value) {
		this.value = value;
	}

	@Override
	public String toString () {
		return value;
	}
	
	public static UnitCostType getUnitCostType (String value) {
		
		UnitCostType [] unitCostTypes = UnitCostType.values();
		
		for (UnitCostType type : unitCostTypes) {
			if (type.value.equals(value)) {
				return type;
			}
		}
		
		return null;
		
	}
	

}
