package com.stee.spfcore.vo.course;

import java.util.List;

public class SlotVacancyAllocation {

	private String slotId;
	
	private int maxClassSize;
	
	private int minClassSize;
	
	private int totalOffice;
	
	private List<UnitAllocation> unitAllocations;

	public SlotVacancyAllocation() {
		super();
	}

	public SlotVacancyAllocation(String slotId, int maxClassSize, int minClassSize, int totalOffice,
			List<UnitAllocation> unitAllocations) {
		super();
		this.slotId = slotId;
		this.maxClassSize = maxClassSize;
		this.minClassSize = minClassSize;
		this.totalOffice = totalOffice;
		this.unitAllocations = unitAllocations;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public int getMaxClassSize() {
		return maxClassSize;
	}

	public void setMaxClassSize(int maxClassSize) {
		this.maxClassSize = maxClassSize;
	}

	public int getMinClassSize() {
		return minClassSize;
	}

	public void setMinClassSize(int minClassSize) {
		this.minClassSize = minClassSize;
	}

	public int getTotalOffice() {
		return totalOffice;
	}

	public void setTotalOffice(int totalOffice) {
		this.totalOffice = totalOffice;
	}

	public List<UnitAllocation> getUnitAllocations() {
		return unitAllocations;
	}

	public void setUnitAllocations(List<UnitAllocation> unitAllocations) {
		this.unitAllocations = unitAllocations;
	}
}
