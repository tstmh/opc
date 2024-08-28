package com.stee.spfcore.model.hrps.internal;

public class BenefitsStatistic {
	
	private int spfCount;
	private int gcCount;
	private int pnsfCount;
	private int cnbCount;
	private int nonSpfCount;
	private int isdCount;
	private int mhqCount;
	
	
	public BenefitsStatistic() {
		super();
	}
	
	public BenefitsStatistic(int spfCount, int gcCount, int pnsfCount,
			int cnbCount, int nonSpfCount, int isdCount, int mhqCount) {
		super();
		this.spfCount = spfCount;
		this.gcCount = gcCount;
		this.pnsfCount = pnsfCount;
		this.cnbCount = cnbCount;
		this.nonSpfCount = nonSpfCount;
		this.isdCount = isdCount;
		this.mhqCount = mhqCount;
	}

	public int getSpfCount() {
		return spfCount;
	}

	public void setSpfCount(int spfCount) {
		this.spfCount = spfCount;
	}

	public int getGcCount() {
		return gcCount;
	}

	public void setGcCount(int gcCount) {
		this.gcCount = gcCount;
	}

	public int getPnsfCount() {
		return pnsfCount;
	}

	public void setPnsfCount(int pnsfCount) {
		this.pnsfCount = pnsfCount;
	}

	public int getCnbCount() {
		return cnbCount;
	}

	public void setCnbCount(int cnbCount) {
		this.cnbCount = cnbCount;
	}

	public int getNonSpfCount() {
		return nonSpfCount;
	}

	public void setNonSpfCount(int nonSpfCount) {
		this.nonSpfCount = nonSpfCount;
	}
	
	public int getIsdCount() {
		return isdCount;
	}

	public void setIsdCount(int isdCount) {
		this.isdCount = isdCount;
	}
	
	public int getMhqCount() {
		return mhqCount;
	}

	public void setMhqCount(int mhqCount) {
		this.mhqCount = mhqCount;
	}
}
