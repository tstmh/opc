package com.stee.spfcore.vo.sag;

public class SAGOnBehalfDetailForEmail {
	
	private Integer totalCount = 0;
	
	private Integer successfulSaaCount = 0;
	
	private Integer successfulSaCount = 0;
	
	private Integer successfulSgCount = 0;
	
	private Integer unsuccessfulSaaCount = 0;
	
	private Integer unsuccessfulSaCount = 0;
	
	private Integer unsuccessfulSgCount = 0;

	public SAGOnBehalfDetailForEmail() {
		super();
	}

	public SAGOnBehalfDetailForEmail( Integer totalCount,
			Integer successfulSaaCount, Integer successfulSaCount,
			Integer successfulSgCount, Integer unsuccessfulSaaCount,
			Integer unsuccessfulSaCount, Integer unsuccessfulSgCount ) {
		super();
		this.totalCount = totalCount;
		this.successfulSaaCount = successfulSaaCount;
		this.successfulSaCount = successfulSaCount;
		this.successfulSgCount = successfulSgCount;
		this.unsuccessfulSaaCount = unsuccessfulSaaCount;
		this.unsuccessfulSaCount = unsuccessfulSaCount;
		this.unsuccessfulSgCount = unsuccessfulSgCount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount( Integer totalCount ) {
		this.totalCount = totalCount;
	}

	public Integer getSuccessfulSaaCount() {
		return successfulSaaCount;
	}

	public void setSuccessfulSaaCount( Integer successfulSaaCount ) {
		this.successfulSaaCount = successfulSaaCount;
	}

	public Integer getSuccessfulSaCount() {
		return successfulSaCount;
	}

	public void setSuccessfulSaCount( Integer successfulSaCount ) {
		this.successfulSaCount = successfulSaCount;
	}

	public Integer getSuccessfulSgCount() {
		return successfulSgCount;
	}

	public void setSuccessfulSgCount( Integer successfulSgCount ) {
		this.successfulSgCount = successfulSgCount;
	}

	public Integer getUnsuccessfulSaaCount() {
		return unsuccessfulSaaCount;
	}

	public void setUnsuccessfulSaaCount( Integer unsuccessfulSaaCount ) {
		this.unsuccessfulSaaCount = unsuccessfulSaaCount;
	}

	public Integer getUnsuccessfulSaCount() {
		return unsuccessfulSaCount;
	}

	public void setUnsuccessfulSaCount( Integer unsuccessfulSaCount ) {
		this.unsuccessfulSaCount = unsuccessfulSaCount;
	}

	public Integer getUnsuccessfulSgCount() {
		return unsuccessfulSgCount;
	}

	public void setUnsuccessfulSgCount( Integer unsuccessfulSgCount ) {
		this.unsuccessfulSgCount = unsuccessfulSgCount;
	}

	@Override
	public String toString() {
		return "SAGOnBehalfEmailContentDetail [totalCount=" + totalCount
				+ ", successfulSaaCount=" + successfulSaaCount
				+ ", successfulSaCount=" + successfulSaCount
				+ ", successfulSgCount=" + successfulSgCount
				+ ", unsuccessfulSaaCount=" + unsuccessfulSaaCount
				+ ", unsuccessfulSaCount=" + unsuccessfulSaCount
				+ ", unsuccessfulSgCount=" + unsuccessfulSgCount + "]";
	}
}
