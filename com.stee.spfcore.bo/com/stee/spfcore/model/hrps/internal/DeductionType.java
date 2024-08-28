package com.stee.spfcore.model.hrps.internal;

public enum DeductionType {
	
	CEASE ("Ceassation"),
	ONE_TIME_DEDUCT ("One Time Deduction"),
	RECURRING_DEDUCT ("Recurring Deduction"),
	NONE ("None");
	
	private String type;
	
	private DeductionType(String type) {
		this.type = type;
	}
	
	public static DeductionType get (String type) {
		DeductionType [] typeList = DeductionType.values();
		for (DeductionType deductionType : typeList) {
			if (deductionType.type.equals(type)) {
				return deductionType;
			}
		}
		return NONE;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
