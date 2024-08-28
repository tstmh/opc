package com.stee.spfcore.vo.benefits;

import com.stee.spfcore.model.hrps.internal.BenefitsReport;
public class BenefitsReportWrapper {

	private String Comments;
	
	private BenefitsReport BenefitsReport;
	
	private boolean ApproveTick;
	
	public String getComments() {
		return Comments;
	}

	public void setComments( String Comments ) {
		this.Comments = Comments;
	}
	
	public BenefitsReport getBenefitsReport() {
		return BenefitsReport;
	}

	public void setBenefitsReport( BenefitsReport BenefitsReport ) {
		this.BenefitsReport = BenefitsReport;
	}
	
	public boolean getApproveTick() {
		return ApproveTick;
	}

	public void setApproveTick( boolean ApproveTick ) {
		this.ApproveTick = ApproveTick;
	}
}
