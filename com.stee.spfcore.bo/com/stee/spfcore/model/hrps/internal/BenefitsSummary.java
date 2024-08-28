package com.stee.spfcore.model.hrps.internal;

public class BenefitsSummary {
	
	private String serviceType;
	private String unit;
	private String subunit;
	private String deceasedRelation;
	private int count;
	private double amount;
	
	public BenefitsSummary () {
		super();
	}
	
	public BenefitsSummary(String serviceType, String unit, String subunit,
			String deceasedRelation, int count, double amount) {
		super();
		this.serviceType = serviceType;
		this.unit = unit;
		this.subunit = subunit;
		this.deceasedRelation = deceasedRelation;
		this.count = count;
		this.amount = amount;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSubunit() {
		return subunit;
	}

	public void setSubunit(String subunit) {
		this.subunit = subunit;
	}

	public String getDeceasedRelation() {
		return deceasedRelation;
	}

	public void setDeceasedRelation(String deceasedRelation) {
		this.deceasedRelation = deceasedRelation;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}	
	
}
