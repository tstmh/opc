package com.stee.spfcore.vo.sag;

public class SAGApplicationsCountDetail {

	private int totalCount = 0;
	
	private int saaTotalCount = 0;
	
	private int saTotalCount = 0;
	
	private int sgTotalCount = 0;
	
	private SAGApplicationCountByStatus saaCountByStatus;
	
	private SAGApplicationCountByStatus saCountByStatus;
	
	private SAGApplicationCountByStatus sgCountByStatus;

	public SAGApplicationsCountDetail() {
		super();
	}

	public SAGApplicationsCountDetail( int totalCount, int saaTotalCount,
			int saTotalCount, int sgTotalCount,
			SAGApplicationCountByStatus saaCountMap,
			SAGApplicationCountByStatus saCountMap,
			SAGApplicationCountByStatus sgCountMap ) {
		super();
		this.totalCount = totalCount;
		this.saaTotalCount = saaTotalCount;
		this.saTotalCount = saTotalCount;
		this.sgTotalCount = sgTotalCount;
		this.saaCountByStatus = saaCountMap;
		this.saCountByStatus = saCountMap;
		this.sgCountByStatus = sgCountMap;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount( int totalCount ) {
		this.totalCount = totalCount;
	}

	public int getSaaTotalCount() {
		return saaTotalCount;
	}

	public void setSaaTotalCount( int saaTotalCount ) {
		this.saaTotalCount = saaTotalCount;
	}

	public int getSaTotalCount() {
		return saTotalCount;
	}

	public void setSaTotalCount( int saTotalCount ) {
		this.saTotalCount = saTotalCount;
	}

	public int getSgTotalCount() {
		return sgTotalCount;
	}

	public void setSgTotalCount( int sgTotalCount ) {
		this.sgTotalCount = sgTotalCount;
	}

	public SAGApplicationCountByStatus getSaaCountByStatus() {
		return saaCountByStatus;
	}

	public void setSaaCountByStatus( SAGApplicationCountByStatus saaCountByStatus ) {
		this.saaCountByStatus = saaCountByStatus;
	}

	public SAGApplicationCountByStatus getSaCountByStatus() {
		return saCountByStatus;
	}

	public void setSaCountByStatus( SAGApplicationCountByStatus saCountByStatus ) {
		this.saCountByStatus = saCountByStatus;
	}

	public SAGApplicationCountByStatus getSgCountByStatus() {
		return sgCountByStatus;
	}

	public void setSgCountByStatus( SAGApplicationCountByStatus sgCountByStatus ) {
		this.sgCountByStatus = sgCountByStatus;
	}

	@Override
	public String toString() {
		return "SAGApplicationsCountDetail [totalCount=" + totalCount
				+ ", saaTotalCount=" + saaTotalCount + ", saTotalCount="
				+ saTotalCount + ", sgTotalCount=" + sgTotalCount
				+ ", saaCountMap=" + saaCountByStatus + ", saCountMap=" + saCountByStatus
				+ ", sgCountMap=" + sgCountByStatus + "]";
	}
}
