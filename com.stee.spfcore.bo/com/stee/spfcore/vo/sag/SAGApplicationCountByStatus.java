package com.stee.spfcore.vo.sag;

public class SAGApplicationCountByStatus {

	public int pendingCount = 0;
	
	public int blacklistedCount = 0;
	
	public int amendedCount = 0;
	
	public int withdrawnCount = 0;
	
	public int rejectedCount = 0;
	
	public int supportedCount = 0;
	
	public int revertedCount = 0;
	
	public int successfulCount = 0;
	
	public int unsuccessfulCount = 0;
	
	public int recommendedCount = 0;
	
	public int notRecommendedCount = 0;
	
	public int verifiedCount = 0;
	
	public int routedToPOCount = 0;

	public SAGApplicationCountByStatus() {
		super();
	}

	public SAGApplicationCountByStatus( int pendingCount, int blacklistedCount,
			int amendedCount, int withdrawnCount, int rejectedCount,
			int supportedCount, int revertedCount, int successfulCount,
			int unsuccessfulCount, int recommendedCount,
			int notRecommendedCount, int verifiedCount, int routedToPOCount ) {
		super();
		this.pendingCount = pendingCount;
		this.blacklistedCount = blacklistedCount;
		this.amendedCount = amendedCount;
		this.withdrawnCount = withdrawnCount;
		this.rejectedCount = rejectedCount;
		this.supportedCount = supportedCount;
		this.revertedCount = revertedCount;
		this.successfulCount = successfulCount;
		this.unsuccessfulCount = unsuccessfulCount;
		this.recommendedCount = recommendedCount;
		this.notRecommendedCount = notRecommendedCount;
		this.verifiedCount = verifiedCount;
		this.routedToPOCount = routedToPOCount;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount( int pendingCount ) {
		this.pendingCount = pendingCount;
	}

	public int getBlacklistedCount() {
		return blacklistedCount;
	}

	public void setBlacklistedCount( int blacklistedCount ) {
		this.blacklistedCount = blacklistedCount;
	}

	public int getAmendedCount() {
		return amendedCount;
	}

	public void setAmendedCount( int amendedCount ) {
		this.amendedCount = amendedCount;
	}

	public int getWithdrawnCount() {
		return withdrawnCount;
	}

	public void setWithdrawnCount( int withdrawnCount ) {
		this.withdrawnCount = withdrawnCount;
	}

	public int getRejectedCount() {
		return rejectedCount;
	}

	public void setRejectedCount( int rejectedCount ) {
		this.rejectedCount = rejectedCount;
	}

	public int getSupportedCount() {
		return supportedCount;
	}

	public void setSupportedCount( int supportedCount ) {
		this.supportedCount = supportedCount;
	}

	public int getRevertedCount() {
		return revertedCount;
	}

	public void setRevertedCount( int revertedCount ) {
		this.revertedCount = revertedCount;
	}

	public int getSuccessfulCount() {
		return successfulCount;
	}

	public void setSuccessfulCount( int successfulCount ) {
		this.successfulCount = successfulCount;
	}

	public int getUnsuccessfulCount() {
		return unsuccessfulCount;
	}

	public void setUnsuccessfulCount( int unsuccessfulCount ) {
		this.unsuccessfulCount = unsuccessfulCount;
	}

	public int getRecommendedCount() {
		return recommendedCount;
	}

	public void setRecommendedCount( int recommendedCount ) {
		this.recommendedCount = recommendedCount;
	}

	public int getNotRecommendedCount() {
		return notRecommendedCount;
	}

	public void setNotRecommendedCount( int notRecommendedCount ) {
		this.notRecommendedCount = notRecommendedCount;
	}

	public int getVerifiedCount() {
		return verifiedCount;
	}

	public void setVerifiedCount( int verifiedCount ) {
		this.verifiedCount = verifiedCount;
	}

	public int getRoutedToPOCount() {
		return routedToPOCount;
	}

	public void setRoutedToPOCount( int routedToPOCount ) {
		this.routedToPOCount = routedToPOCount;
	}

	@Override
	public String toString() {
		return "SAGApplicationCountByStatus [pendingCount=" + pendingCount
				+ ", blacklistedCount=" + blacklistedCount + ", amendedCount="
				+ amendedCount + ", withdrawnCount=" + withdrawnCount
				+ ", rejectedCount=" + rejectedCount + ", supportedCount="
				+ supportedCount + ", revertedCount=" + revertedCount
				+ ", successfulCount=" + successfulCount
				+ ", unsuccessfulCount=" + unsuccessfulCount
				+ ", recommendedCount=" + recommendedCount
				+ ", notRecommendedCount=" + notRecommendedCount
				+ ", verifiedCount=" + verifiedCount + ", routedToPOCount="
				+ routedToPOCount + "]";
	}
	
}
