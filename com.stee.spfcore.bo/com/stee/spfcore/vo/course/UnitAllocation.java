package com.stee.spfcore.vo.course;

public class UnitAllocation {

	private String unitCodeId;
	
	private String unitName;
	
	private int totalOffice;
	
	private float allocated;

	public UnitAllocation() {
		super();
	}

	public UnitAllocation(String unitCodeId, String unitName, int totalOffice, float allocated) {
		super();
		this.unitCodeId = unitCodeId;
		this.unitName = unitName;
		this.totalOffice = totalOffice;
		this.allocated = allocated;
	}

	public String getUnitCodeId() {
		return unitCodeId;
	}

	public void setUnitCodeId(String unitCodeId) {
		this.unitCodeId = unitCodeId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getTotalOffice() {
		return totalOffice;
	}

	public void setTotalOffice(int totalOffice) {
		this.totalOffice = totalOffice;
	}

	public float getAllocated() {
		return allocated;
	}

	public void setAllocated(float allocated) {
		this.allocated = allocated;
	}
}
