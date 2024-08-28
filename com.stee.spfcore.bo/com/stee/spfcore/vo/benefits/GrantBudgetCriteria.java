package com.stee.spfcore.vo.benefits;

import java.util.Date;

public class GrantBudgetCriteria {
	
	private String grantType;
	
	private String grantSubType;
	
	private Date effectiveDate;

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getGrantSubType() {
		return grantSubType;
	}

	public void setGrantSubType(String grantSubType) {
		this.grantSubType = grantSubType;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public String toString() {
		return "GrantBudgetCriteria [grantType=" + grantType
				+ ", grantSubType=" + grantSubType + ", effectiveDate="
				+ effectiveDate + ", getGrantType()=" + getGrantType()
				+ ", getGrantSubType()=" + getGrantSubType()
				+ ", getEffectiveDate()=" + getEffectiveDate()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
