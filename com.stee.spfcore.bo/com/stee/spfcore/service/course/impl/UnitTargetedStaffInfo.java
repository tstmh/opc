package com.stee.spfcore.service.course.impl;

public class UnitTargetedStaffInfo implements Comparable<UnitTargetedStaffInfo> {
	
	private String unitCodeId;
	
	private int targetedCount;
	
	public UnitTargetedStaffInfo (String unitCodeId, int targetedCount) {
		super();
		this.unitCodeId = unitCodeId;
		this.targetedCount = targetedCount;
	}
	
	public UnitTargetedStaffInfo () {
		super();
	}
	
	public String getUnitCodeId () {
		return unitCodeId;
	}
	
	public void setUnitCodeId (String unitCodeId) {
		this.unitCodeId = unitCodeId;
	}
	
	public int getTargetedCount () {
		return targetedCount;
	}
	
	public void setTargetedCount (int targetedCount) {
		this.targetedCount = targetedCount;
	}

	@Override
	public int compareTo (UnitTargetedStaffInfo o) {
		return o.targetedCount - targetedCount;
	}
	
}
