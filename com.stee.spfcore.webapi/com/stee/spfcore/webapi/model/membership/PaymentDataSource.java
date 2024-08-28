package com.stee.spfcore.webapi.model.membership;


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

	public String toString() {
		return value;
	}

}
