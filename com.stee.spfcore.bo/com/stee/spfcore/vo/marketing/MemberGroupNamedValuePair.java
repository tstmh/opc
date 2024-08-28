package com.stee.spfcore.vo.marketing;

public class MemberGroupNamedValuePair {
	private String name;
	private String value;
	boolean template;
	
	public MemberGroupNamedValuePair () {
		
	}
	
	public MemberGroupNamedValuePair (String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}
	
}
