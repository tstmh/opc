package com.stee.spfcore.webapi.model.personnel;

public enum ContactLabel {

	HOME ("Home"), WORK ("Work"), MOBILE ("Mobile"), OTHERS ("Others");
	
	private String value;
	
	private ContactLabel (String label) {
		this.value = label;
	}
	
	public static ContactLabel get (String label) {
		/*switch (label) {
			case "Home":
				return HOME;
			case "Work":
				return WORK;
			case "Mobile":
				return MOBILE;
			default:
				return OTHERS;
		}*/
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
	
	public String toString () {
		return value;
	}
	
}
