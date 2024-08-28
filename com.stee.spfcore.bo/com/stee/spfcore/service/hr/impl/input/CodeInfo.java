package com.stee.spfcore.service.hr.impl.input;

public class CodeInfo {

	private String tag;
	private boolean isCode;
	private String value;
	private String desc;

	public CodeInfo(String tag, boolean isCode, String value, String desc) {
		this.tag = tag;
		this.isCode = isCode;
		this.value = value;
		this.desc = desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTag() {
		return tag;
	}

	public boolean isCode() {
		return isCode;
	}

	public String getValue() {
		return value;
	}

	public String getDescription () {
		return desc;
	}

	public void setDescription (String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isCode ? 1231 : 1237);
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeInfo other = (CodeInfo) obj;
		if (isCode != other.isCode)
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
