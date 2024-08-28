package com.stee.spfcore.service.hrps;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.hrps.HRPSDetails;
import com.stee.spfcore.model.hrps.internal.BenefitsDetails;
import com.stee.spfcore.model.hrps.internal.BenefitsReport;
import com.stee.spfcore.model.hrps.internal.BenefitsSummary;
import com.stee.spfcore.model.hrps.internal.MembershipDeductionDetail;
import com.stee.spfcore.model.hrps.internal.PersonalNameRankUnit;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.benefits.BenefitsServiceException;
import com.stee.spfcore.vo.benefits.BenefitsReportWrapper;

public interface IHRPSService {
	
	public void processOutboundFile() throws HRPSServiceException;
	
	public void processOutboundPostFile() throws HRPSServiceException, BenefitsServiceException, AccessDeniedException, ParseException;
	
	public List<MembershipDeductionDetail> createMembershipDeductionDetailByCriteria() throws HRPSServiceException;
	
	public PersonalNameRankUnit getPersonalNameRankUnit(String nric) throws HRPSServiceException;

	public List<BenefitsSummary> getBereavementSummary(Date searchStartDate, Date searchEndDate) throws HRPSServiceException;
	
	public List<BenefitsSummary> getWeddingSummary(Date searchStartDate, Date searchEndDate) throws HRPSServiceException;

	public List<BenefitsSummary> getNewbornSummary(Date searchStartDate, Date searchEndDate) throws HRPSServiceException;
	
	public List<BenefitsDetails> getBereavementApplicationForHRP(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException;

	public void generateInboundFile(Date searchEndDate) throws HRPSServiceException;

	public void updateHRPSDetails(HRPSDetails hrpsDetails) throws HRPSServiceException;

	public HRPSDetails getHRPSDetailsByReference(String reference) throws HRPSServiceException;

	public void saveBenefitsDetailsForHRP(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException;

	public List<BenefitsReport> getBereavementReport(Date searchStartDate, Date searchEndDate, String applicationStatus) throws HRPSServiceException;

	public List<BenefitsReport> getWeddingReport(Date searchStartDate, Date searchEndDate, String applicationStatus) throws HRPSServiceException;

	public List<BenefitsReport> getNewbornReport(Date searchStartDate, Date searchEndDate, String applicationStatus) throws HRPSServiceException;

	public HRPSDetails getHRPSDetails(String id) throws HRPSServiceException;

	public void informFOForVerification(List<String> nrics, Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException;

	public void informADForEndorsement(List<String> nrics, Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException;
	
	public void informADForFinalizedApplication(List<String> nrics, Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException;
	
	public void updateVerifiedApplications (List<BenefitsReportWrapper> bereavementReports, List<BenefitsReportWrapper> weddingReports, List<BenefitsReportWrapper> newbornReports, String officerLevel, String officerName, String officerNric) throws HRPSServiceException;
	    	
	
}
