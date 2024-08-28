package com.stee.spfcore.model.membership;


public enum PaymentDataSource {

	CPO("CPO"), MANUAL("Manual"), OTHERS ("Others");

	private String value;

	private PaymentDataSource(String source) {
		this.value = source;
	}

	public static PaymentDataSource get(String source) {

		if ("CPO".equals(source)) {
			return CPO;
		} else if ("Manual".equals(source)) {
			return MANUAL;
		} else {
			return OTHERS;
		}
	}

	@Override
	public String toString() {
		return value;
	}

}
