package com.stee.spfcore.model.personnel;

public enum ContactLabel {

	HOME ("Home"), WORK ("Work"), MOBILE ("Mobile"), OTHERS ("Others");
	
	private String value;
	
	private ContactLabel (String label) {
		this.value = label;
	}
	
	public static ContactLabel get (String label) {
		if("Home".equals(label)){
			return HOME;
		} else if("Work".equals(label)){
			return WORK;
		} else if("Mobile".equals(label)){
			return MOBILE;
		} else {
			return OTHERS;
		}
	}

	@Override
	public String toString () {
		return value;
	}
	
}
