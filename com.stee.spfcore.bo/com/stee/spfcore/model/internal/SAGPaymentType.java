package com.stee.spfcore.model.internal;

public enum SAGPaymentType {

	GIRO ("GIRO"), 
	PAYNOW ("PAYNOW"),
	OTHERS("");
	
	private String value;

	private SAGPaymentType( String value ) {
		this.value = value;
	}
	
	public static SAGPaymentType getSAGAwardType( String value) {
		
		if("GIRO".equals( value )) {
			return GIRO;
		} else if("PAYNOW".equals( value )) {
			return PAYNOW;
		} else {
			return OTHERS;
		}
	}

	@Override
	public String toString() {
		return value;
	}
	
}
