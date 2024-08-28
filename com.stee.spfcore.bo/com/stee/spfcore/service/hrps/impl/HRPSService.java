package com.stee.spfcore.service.hrps.impl;

import com.stee.spfcore.dao.*;
import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.benefits.ApprovalRecord;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.hrps.HRPSConfig;
import com.stee.spfcore.model.hrps.HRPSDetails;
import com.stee.spfcore.model.hrps.RecordStatus;
import com.stee.spfcore.model.hrps.internal.*;
import com.stee.spfcore.model.membership.MembershipType;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.benefits.BenefitsServiceException;
import com.stee.spfcore.service.hrps.HRPSServiceException;
import com.stee.spfcore.service.hrps.IHRPSService;
import com.stee.spfcore.service.report.IReportService;
import com.stee.spfcore.service.report.ReportServiceFactory;
import com.stee.spfcore.vo.benefits.BenefitsReportWrapper;
import com.stee.spfcore.vo.membership.MembershipCriteria;
import com.stee.spfcore.vo.membership.MembershipReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HRPSService implements IHRPSService{
	
	private static final Logger LOGGER = Logger.getLogger(HRPSService.class.getName());
	private static final List<String> FINANCE_REPORTS = Arrays.asList("Finance_Bereavement","Finance_Wedding","Finance_newborn","Finance_Summary");
	
	private MembershipDAO membershipDAO;
	private HRPSDAO hrpsDAO;
	private PersonnelDAO personnelDAO;
	private EmailUtil emailUtil;
	private IReportService reportService;
	private BenefitsDAO benefitsDAO;
	
	public HRPSService() {
		this.membershipDAO = new MembershipDAO();
		this.personnelDAO = new PersonnelDAO();
		this.hrpsDAO = new HRPSDAO();
		this.emailUtil = new EmailUtil();
		this.reportService = ReportServiceFactory.getInstance();
		this.benefitsDAO = new BenefitsDAO();
	}
	
	@Override
	public void generateInboundFile(Date searchEndDate) throws HRPSServiceException {
		LOGGER.log(Level.INFO, "[HRPS] Start Generating HRP File");
		
		HRPSConfig hrpsConfig = new HRPSConfig();
		List<HRPSDetails> hrpsDetails = new ArrayList<>();
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			hrpsConfig = hrpsDAO.getHRPSConfig();
			
			hrpsDetails = hrpsDAO.getHRPSDetailsByStatusAndDate(RecordStatus.PENDING, searchEndDate);
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "[HRPS] Fail to get HRPS Details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		InboundBuilder builder = new InboundBuilder(hrpsConfig, LOGGER, emailUtil);
		//Build Inbound File
		builder.generateInbound(hrpsDetails);
		
		LOGGER.log(Level.INFO, "[HRPS] Generating HRP File Ended");
	}
	
	@Override
	public void saveBenefitsDetailsForHRP(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {

		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.log(Level.INFO, "[HRPS] Saving Benefits Details for HRP");
			LOGGER.info(String.format("[HRPS] officer action: %s", officerAction)); //Endorsed
			LOGGER.info(String.format("[HRPS] search start date: %s", searchStartDate));
			LOGGER.info(String.format("[HRPS] search end date: %s", searchEndDate));
		}
		HRPSConfig hrpsConfig = new HRPSConfig();
		List<BenefitsDetails> bgBenefitsDetails = null;
		List<BenefitsDetails> wgBenefitsDetails = null;
		List<BenefitsDetails> nbBenefitsDetails = null;
		List<HRPSDetails> hrpsDetails = new ArrayList<>();
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			hrpsConfig = hrpsDAO.getHRPSConfig();
			
			bgBenefitsDetails = hrpsDAO.getBereavementApplicationForHRP(searchStartDate, searchEndDate, officerAction);
			for(int i=0; i<bgBenefitsDetails.size(); i++)
			{
				LOGGER.log(Level.INFO, "[HRPS] benefitsDetails reference number: "+bgBenefitsDetails.get(i).getReferenceNumber());
			}
			
			wgBenefitsDetails = hrpsDAO.getWeddingApplicationForHRP(searchStartDate, searchEndDate, officerAction);
			
			nbBenefitsDetails = hrpsDAO.getNewbornApplicationForHRP(searchStartDate, searchEndDate, officerAction);
			
			if (!bgBenefitsDetails.isEmpty()) {
				buildHRPSDetails(hrpsDetails, bgBenefitsDetails, hrpsConfig);
			}
			
			if (wgBenefitsDetails != null && !wgBenefitsDetails.isEmpty()) {
				buildHRPSDetails(hrpsDetails, wgBenefitsDetails, hrpsConfig);
			}
			
			if (nbBenefitsDetails != null && !nbBenefitsDetails.isEmpty()) {
				buildHRPSDetails(hrpsDetails, nbBenefitsDetails, hrpsConfig);
			}
			
			hrpsDAO.saveHRPSDetails(hrpsDetails, SessionFactoryUtil.getUser());
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "[HRPS] Fail to get Benefits Details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		LOGGER.log(Level.INFO, "[HRPS] End Saving Benefits Details for HRP");
	}
	
	private void buildHRPSDetails(List<HRPSDetails> hrpsDetails, List<BenefitsDetails> benefitsDetails, HRPSConfig hrpsConfig) {
		Date updatedDate = new Date();
		
		//Payment date is usually next month from processing month
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONDAY, 1);
		Date paymentDate = cal.getTime();
		
		for (BenefitsDetails detail : benefitsDetails) {
			HRPSDetails hrpsDetail = new HRPSDetails();
			hrpsDetail.setId(detail.getReferenceNumber());
			hrpsDetail.setNric(detail.getMemberNric());
			hrpsDetail.setRecordType(hrpsConfig.getInboundDetailRecordType());
			hrpsDetail.setWageCode(hrpsConfig.getInboundDetailWageType());
			hrpsDetail.setAmount(detail.getAmountToBePaid());
			//One Time Payment: Start Date same as End Date
			//Payment Date should on 12th of every month
			hrpsDetail.setStartDate(paymentDate);
			hrpsDetail.setEndDate(paymentDate);
			hrpsDetail.setStatus(RecordStatus.PENDING);
			hrpsDetail.setReference(detail.getReferenceNumber());
			hrpsDetail.setUpdatedDate(updatedDate);
			hrpsDetails.add(hrpsDetail);
		}
	}
	@Override
	public void processOutboundFile() throws HRPSServiceException {
		LOGGER.log(Level.INFO,"[HRPS] Processing Outbound File from HRPS");
		
		HRPSConfig hrpsConfig = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
		
			hrpsConfig = hrpsDAO.getHRPSConfig();
		
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "[HRPS] Fail to get HRPS Config", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		OutboundProcessor processor = new OutboundProcessor(hrpsConfig, LOGGER, emailUtil);
		//Process Outbound File
		processor.processOutboundFile();
		
		LOGGER.log(Level.INFO,"[HRPS] End Processing Outbound File from HRPS");
	}
	
	@Override
	public void processOutboundPostFile() throws HRPSServiceException, BenefitsServiceException, AccessDeniedException, ParseException {
		LOGGER.log(Level.INFO,"[HRPS] Processing Outbound Post File from HRPS");
		
		HRPSConfig hrpsConfig = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			hrpsConfig = hrpsDAO.getHRPSConfig();
		
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "[HRPS] Fail to get HRPS Config", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		OutboundProcessor processor = new OutboundProcessor(hrpsConfig, LOGGER, emailUtil);
		processor.processOutboundPostFile();
		
		LOGGER.log(Level.INFO,"[HRPS] End Processing Outbound Post File from HRPS");
	}
	
	@Override
	public HRPSDetails getHRPSDetails(String id) throws HRPSServiceException {
		HRPSDetails result = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			result = hrpsDAO.getHRPSDetails(id);
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get HRPS Details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	@Override
	public HRPSDetails getHRPSDetailsByReference(String reference) throws HRPSServiceException {
		HRPSDetails result = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			result = hrpsDAO.getHRPSDetailsByReference(reference);
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get HRPS Details by Reference", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new HRPSServiceException("Fail to get HRPS Details by Reference, e");
		}
		return result;
	}
	
	@Override
	public void updateHRPSDetails(HRPSDetails hrpsDetails) throws HRPSServiceException {
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();

			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			hrpsDAO.updateHRPSDetails(hrpsDetails, SessionFactoryUtil.getUser());
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to update HRPS Details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	@SuppressWarnings("unused")
	private List<MembershipDeductionDetail> createDeductionDetailByPeriod (Date searchStartDate, Date searchEndDate, List<String> serviceTypes) throws HRPSServiceException {
		
		List <MembershipDeductionDetail> membershipDeductionDetails = new ArrayList<>();
		
		List <MembershipReport> membershipReportActiveList = null;
		List <MembershipReport> membershipReportInactiveList = null;
		List <MembershipReport> membershipReportCessationList = null;
		
		MembershipCriteria membershipCriteriaActive = new MembershipCriteria();
		MembershipCriteria membershipCriteriaInactive = new MembershipCriteria();
		MembershipCriteria membershipCriteriaCessation = new MembershipCriteria();
		
		//New
		membershipCriteriaActive.setMembershipStatus(ActivationStatus.ACTIVE);
		membershipCriteriaActive.setMembershipType(MembershipType.ORDINARY);
		membershipCriteriaActive.setSearchEffStartDate(searchStartDate);
		membershipCriteriaActive.setSearchEffStartDate(searchEndDate);
		
		//Expired
		membershipCriteriaInactive.setMembershipStatus(ActivationStatus.INACTIVE);
		membershipCriteriaInactive.setMembershipType(MembershipType.NON_MEMBER);
		membershipCriteriaInactive.setSearchExpStartDate(searchStartDate);
		membershipCriteriaInactive.setSearchExpEndDate(searchEndDate);
		
		//Cessation
		membershipCriteriaCessation.setMembershipStatus(ActivationStatus.INACTIVE);
		membershipCriteriaCessation.setMembershipType(MembershipType.NON_MEMBER);
		membershipCriteriaCessation.setSearchCessationStartDate(searchStartDate);
		membershipCriteriaCessation.setSearchCessationEndDate(searchEndDate);
		
		CodeUtil codeUtil = new CodeUtil();
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			membershipReportActiveList = membershipDAO.searchMemberReportByCritieria(membershipCriteriaActive);
			
			membershipReportInactiveList = membershipDAO.searchMemberReportByCritieria(membershipCriteriaInactive);
			
			membershipReportCessationList = membershipDAO.searchMemberReportByCritieria(membershipCriteriaCessation);
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,"Fail to retreive membership report details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		if (membershipReportActiveList != null && !membershipReportActiveList.isEmpty()) {
			createDeductionDetails(membershipReportActiveList, DeductionType.RECURRING_DEDUCT, serviceTypes, membershipDeductionDetails, codeUtil);
		} 
		
		if (membershipReportInactiveList != null && !membershipReportInactiveList.isEmpty()) {
			createDeductionDetails(membershipReportInactiveList, DeductionType.CEASE, serviceTypes, membershipDeductionDetails, codeUtil);
		}
		
		if (membershipReportCessationList != null && !membershipReportCessationList.isEmpty()) {
			createDeductionDetails(membershipReportCessationList, DeductionType.CEASE, serviceTypes, membershipDeductionDetails, codeUtil);
		}
		LOGGER.log(Level.INFO, "Size of membershipDeductionDetails: " + membershipDeductionDetails.size());
		
		return membershipDeductionDetails;
	}
	
	private void createDeductionDetails (List<MembershipReport> membershipReportList, DeductionType type, 
			List<String> serviceTypes, List <MembershipDeductionDetail> membershipDeductionDetails, CodeUtil codeUtil) {
		
		Calendar cal = Calendar.getInstance();
		
		for (MembershipReport membershipReport : membershipReportList) {
			if (serviceTypes.contains(membershipReport.getServiceType())) {
				MembershipDeductionDetail membershipDeductionDetail = new MembershipDeductionDetail();
				membershipDeductionDetail.setNric(membershipReport.getNric());
				membershipDeductionDetail.setName(membershipReport.getName());
				membershipDeductionDetail.setRank(codeUtil.getDescription(CodeType.RANK, membershipReport.getRankOrGrade()));
				membershipDeductionDetail.setUnit(codeUtil.getDescription(CodeType.UNIT_DEPARTMENT, membershipReport.getOrganisationOrDepartment()));
				membershipDeductionDetail.setType(type);
				membershipDeductionDetail.setMonth(cal.get(Calendar.MONTH) + 1);
				membershipDeductionDetail.setYear(cal.get(Calendar.YEAR));
				
				membershipDeductionDetails.add(membershipDeductionDetail);
			}
		}
	}
	@Override
	public List<MembershipDeductionDetail> createMembershipDeductionDetailByCriteria() throws HRPSServiceException {
		MembershipCriteria membershipCriteriaInactive = new MembershipCriteria();
		List<MembershipDeductionDetail> membershipDeductionDetail = null;

		Calendar todayCalendar = Calendar.getInstance();
		// Set searchStartDate to the first day of the current month
		todayCalendar.set(Calendar.DAY_OF_MONTH, 1);
		Date searchStartDate = todayCalendar.getTime();

		// Set searchEndDate to the last day of the current month
		int lastDay = todayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		todayCalendar.set(Calendar.DAY_OF_MONTH, lastDay);
		Date searchEndDate = todayCalendar.getTime();

		membershipCriteriaInactive.setMembershipStatus(ActivationStatus.INACTIVE);
		membershipCriteriaInactive.setMembershipType(MembershipType.NON_MEMBER);
		membershipCriteriaInactive.setSearchExpStartDate(searchStartDate);
		membershipCriteriaInactive.setSearchExpEndDate(searchEndDate);
		
		List <String> serviceTypes = new ArrayList<>();
		serviceTypes.add("338");
		
		try {
			SessionFactoryUtil.beginTransaction();
		
			membershipDeductionDetail =  hrpsDAO.createMembershipDeductionDetailByCriteria(membershipCriteriaInactive, serviceTypes);
		
			SessionFactoryUtil.commitTransaction();
		
		} catch (Exception e){
			LOGGER.log(Level.SEVERE,"Fail to create membership deduction details", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return membershipDeductionDetail;
	}
	
	public PersonalNameRankUnit getPersonalNameRankUnit (String nric) throws HRPSServiceException {
		
		PersonalNameRankUnit personalNameRankUnit = null;
		
		try {
			
			SessionFactoryUtil.beginTransaction();
		
			personalNameRankUnit = hrpsDAO.getPersonalNameRankUnit(nric);
		
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to create personal name rank unit", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		return personalNameRankUnit;
	}
	
	@Override
	public List<BenefitsSummary> getBereavementSummary(Date searchStartDate, Date searchEndDate) throws HRPSServiceException {
		
		List<BenefitsSummary> benefitsSummary = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsSummary = hrpsDAO.getBereavementSummary(searchStartDate, searchEndDate);
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get bereavement summary", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsSummary;
	}
	
	@Override
	public List<BenefitsSummary> getWeddingSummary(Date searchStartDate, Date searchEndDate) throws HRPSServiceException {
		
		List<BenefitsSummary> benefitsSummary = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();

			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsSummary = hrpsDAO.getWeddingSummary(searchStartDate, searchEndDate);
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get wedding summary", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsSummary;
	}
	
	@Override
	public List<BenefitsSummary> getNewbornSummary(Date searchStartDate, Date searchEndDate) throws HRPSServiceException {
		
		List<BenefitsSummary> benefitsSummary = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsSummary = hrpsDAO.getNewbornSummary(searchStartDate, searchEndDate);
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get wedding summary", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsSummary;
	}
	
	public List<BenefitsDetails> getBereavementApplicationForHRP(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
		
		List<BenefitsDetails> benefitsDetails = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsDetails = hrpsDAO.getBereavementApplicationForHRP(searchStartDate, searchEndDate, officerAction);
			
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get bereavement application for hrp", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsDetails;
	}
	
	@Override
	public List<BenefitsReport> getBereavementReport(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
		
		List<BenefitsReport> benefitsReport = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();

			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsReport = hrpsDAO.getBereavementReport(searchStartDate, searchEndDate, officerAction);
			LOGGER.info("bereavement inside:"+benefitsReport);
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get bereavement report", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsReport; 
	}
	
	@Override
	public List<BenefitsReport> getWeddingReport(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
		
		List<BenefitsReport> benefitsReport = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsReport = hrpsDAO.getWeddingReport(searchStartDate, searchEndDate, officerAction);
			LOGGER.info("wedding inside:"+benefitsReport);
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get wedding report", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsReport; 
	}
	
	@Override
	public List<BenefitsReport> getNewbornReport(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
		
		List<BenefitsReport> benefitsReport = null;
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			
			benefitsReport = hrpsDAO.getNewbornReport(searchStartDate, searchEndDate, officerAction);
			LOGGER.info("newborn inside:"+benefitsReport);
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get Newborn report", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return benefitsReport; 
	}
	
	@Override
	public void informFOForVerification(List<String> nrics, Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
	
		List<String> emails = null;
		HRPSConfig config = null;
		BenefitsStatistic bgStatistic = new BenefitsStatistic();
		BenefitsStatistic wgStatistic = new BenefitsStatistic();
		BenefitsStatistic nbStatistic = new BenefitsStatistic();
		
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}

			config = hrpsDAO.getHRPSConfig();
			
			// Get office email of FO
			emails = personnelDAO.getOfficeEmailAddress(nrics);
			
			// Get Approved Bereavement Statistics
			bgStatistic = hrpsDAO.getBereavementStatistic(searchStartDate, searchEndDate, officerAction);
			LOGGER.info(String.format("BG SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", bgStatistic.getSpfCount(), bgStatistic.getGcCount(), bgStatistic.getPnsfCount(), bgStatistic.getCnbCount(),bgStatistic.getIsdCount(),bgStatistic.getMhqCount(),bgStatistic.getNonSpfCount()));
			
			// Get Wedding Statistics
			wgStatistic = hrpsDAO.getWeddingStatistic(searchStartDate, searchEndDate, officerAction);
			LOGGER.info(String.format("WG SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", wgStatistic.getSpfCount(), wgStatistic.getGcCount(), wgStatistic.getPnsfCount(), wgStatistic.getCnbCount(),wgStatistic.getIsdCount(),wgStatistic.getMhqCount(),wgStatistic.getNonSpfCount()));
			
			// Get Newborn Statistics
			nbStatistic = hrpsDAO.getNewbornStatistic(searchStartDate, searchEndDate, officerAction);
			LOGGER.info(String.format("NB SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", nbStatistic.getSpfCount(), nbStatistic.getGcCount(), nbStatistic.getPnsfCount(), nbStatistic.getCnbCount(),nbStatistic.getIsdCount(),nbStatistic.getMhqCount(),nbStatistic.getNonSpfCount()));
			
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to get benefits statistic data");
			SessionFactoryUtil.rollbackTransaction();
			throw new HRPSServiceException("Fail to get benefits statistic data", e);
		}

		List<String> reportUrls = getFinanceReportUrl(searchStartDate, searchEndDate, officerAction);
			

		emailUtil.sendReportToFO(bgStatistic, wgStatistic, nbStatistic, reportUrls, emails, config);
	}
	
	private List<String> getFinanceReportUrl(Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
		
		SimpleDateFormat reportDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> reportUrls = new ArrayList<>();
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("start date: %s", reportDateFormat.format(searchStartDate)));
			LOGGER.info(String.format("end date: %s", reportDateFormat.format(searchEndDate)));
		}
		//Prepare Report Parameters
		Map<String, String> parameters = new HashMap<>();
		parameters.put("SEARCH_START_DATE", reportDateFormat.format(searchStartDate));
		parameters.put("SEARCH_END_DATE", reportDateFormat.format(searchEndDate));
		parameters.put("OFFICER_ACTION", officerAction);
		
		//Download Report
		try {
			for (String report: FINANCE_REPORTS) {
				String reportUrl = reportService.getReportUrl(report, parameters);
				reportUrls.add(reportUrl);
			}
			
		} catch (Exception e) {
			//Do no throw
			LOGGER.log(Level.SEVERE, "Fail to get Report urls", e);
		}
		
		return reportUrls;
	}
	
	@Override
	public void informADForEndorsement (List<String> nrics, Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
		List<String> emails = null;
		HRPSConfig config = null;
		BenefitsStatistic bgStatistic = new BenefitsStatistic();
		BenefitsStatistic wgStatistic = new BenefitsStatistic();
		BenefitsStatistic nbStatistic = new BenefitsStatistic();
		
		try {
			LOGGER.log(Level.INFO,"informADForEndorsement is transaction active: "+SessionFactoryUtil.isTransactionActive());
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
			}
			else
			{
				SessionFactoryUtil.getCurrentSession().close();
				SessionFactoryUtil.getCurrentSession().beginTransaction();
			}
			config = hrpsDAO.getHRPSConfig();
			emails = personnelDAO.getOfficeEmailAddress(nrics);
			
			// Get Approved Bereavement Statistics
			bgStatistic = hrpsDAO.getBereavementStatistic(searchStartDate, searchEndDate, officerAction);
			LOGGER.info(String.format("BG SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", bgStatistic.getSpfCount(), bgStatistic.getGcCount(), bgStatistic.getPnsfCount(), bgStatistic.getCnbCount(),bgStatistic.getIsdCount(),bgStatistic.getMhqCount(),bgStatistic.getNonSpfCount()));
						
			// Get Wedding Statistics
			wgStatistic = hrpsDAO.getWeddingStatistic(searchStartDate, searchEndDate, officerAction);
			LOGGER.info(String.format("WG SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", wgStatistic.getSpfCount(), wgStatistic.getGcCount(), wgStatistic.getPnsfCount(), wgStatistic.getCnbCount(),wgStatistic.getIsdCount(),wgStatistic.getMhqCount(),wgStatistic.getNonSpfCount()));
						
			// Get Newborn Statistics
			nbStatistic = hrpsDAO.getNewbornStatistic(searchStartDate, searchEndDate, officerAction);
			LOGGER.info(String.format("NB SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", nbStatistic.getSpfCount(), nbStatistic.getGcCount(), nbStatistic.getPnsfCount(), nbStatistic.getCnbCount(),nbStatistic.getIsdCount(),nbStatistic.getMhqCount(),nbStatistic.getNonSpfCount()));
		     
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to retrieve office email");
			SessionFactoryUtil.rollbackTransaction();
			throw new HRPSServiceException("Fail to retrieve office email", e);
		}
		List<String> reportUrls = getFinanceReportUrl(searchStartDate, searchEndDate, officerAction);

		emailUtil.sendReportToAD(bgStatistic, wgStatistic, nbStatistic, reportUrls, emails, config);
	}

	
	 public void updateVerifiedApplications (List<BenefitsReportWrapper> bereavementReports, List<BenefitsReportWrapper> weddingReports, List<BenefitsReportWrapper> newbornReports, String officerLevel, String officerName, String officerNric) throws HRPSServiceException {
		 
		    try
		    {
		    	LOGGER.log(Level.INFO,"updateVerifiedApplications is transaction active: "+SessionFactoryUtil.isTransactionActive());
				if(!SessionFactoryUtil.isTransactionActive())
				{
					SessionFactoryUtil.beginTransaction();
				}
				else
				{
					SessionFactoryUtil.getCurrentSession().close();
					SessionFactoryUtil.getCurrentSession().beginTransaction();
				}
		 	BereavementGrant bereavementGrant;
		 	WeddingGift weddingGift;
		 	NewBornGift newbornGift;
		 	Date currentDate = new Date();
		 	for(int i=0; i<bereavementReports.size(); i++)
	    	{
	    		LOGGER.info(bereavementReports.get(i).getBenefitsReport().getReferenceNumber()+":"+bereavementReports.get(i).getBenefitsReport().getOfficerAction());
					bereavementGrant = benefitsDAO.getBereavementGrant(bereavementReports.get(i).getBenefitsReport().getReferenceNumber());
					if(bereavementReports.get(i).getBenefitsReport().getOfficerAction().equals("Verified")&& officerLevel.equals("FO"))
					{
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Verified");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(bereavementReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(bereavementReports.get(i).getBenefitsReport().getReferenceNumber());
						bereavementGrant.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveBereavementGrant(bereavementGrant, officerNric);
					}
					else if(bereavementReports.get(i).getBenefitsReport().getOfficerAction().equals("Rejected")&& officerLevel.equals("FO"))
					{
						bereavementGrant.setApplicationStatus(ApplicationStatus.REJECTED);
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Rejected");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(bereavementReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(bereavementReports.get(i).getBenefitsReport().getReferenceNumber());
						bereavementGrant.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveBereavementGrant(bereavementGrant, officerNric);
					}
					else if(bereavementReports.get(i).getBenefitsReport().getOfficerAction().equals("Endorsed")&& officerLevel.equals("AD"))
					{
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Endorsed");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(bereavementReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(bereavementReports.get(i).getBenefitsReport().getReferenceNumber());
						bereavementGrant.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveBereavementGrant(bereavementGrant, officerNric);
					}
					else
					{
						bereavementGrant.setApplicationStatus(ApplicationStatus.REJECTED);
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Rejected");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(bereavementReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(bereavementReports.get(i).getBenefitsReport().getReferenceNumber());
						bereavementGrant.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveBereavementGrant(bereavementGrant, officerNric);
					}
	    		
	    	}
		 	for(int i=0; i<weddingReports.size(); i++)
	    	{
	    		LOGGER.info(weddingReports.get(i).getBenefitsReport().getReferenceNumber()+":"+weddingReports.get(i).getBenefitsReport().getOfficerAction());
					weddingGift = benefitsDAO.getWeddingGift(weddingReports.get(i).getBenefitsReport().getReferenceNumber());
					if(weddingReports.get(i).getBenefitsReport().getOfficerAction().equals("Verified")&& officerLevel.equals("FO"))
					{
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Verified");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(weddingReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(weddingReports.get(i).getBenefitsReport().getReferenceNumber());
						weddingGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveWeddingGift(weddingGift, officerNric);
					}
					else if(weddingReports.get(i).getBenefitsReport().getOfficerAction().equals("Rejected")&& officerLevel.equals("FO"))
					{
						weddingGift.setApplicationStatus(ApplicationStatus.REJECTED);
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Rejected");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(weddingReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(weddingReports.get(i).getBenefitsReport().getReferenceNumber());
						weddingGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveWeddingGift(weddingGift, officerNric);
					}
					else if(weddingReports.get(i).getBenefitsReport().getOfficerAction().equals("Endorsed")&& officerLevel.equals("AD"))
					{
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Endorsed");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(weddingReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(weddingReports.get(i).getBenefitsReport().getReferenceNumber());
						weddingGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveWeddingGift(weddingGift, officerNric);
					}
					else
					{
						weddingGift.setApplicationStatus(ApplicationStatus.REJECTED);
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Rejected");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(weddingReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(weddingReports.get(i).getBenefitsReport().getReferenceNumber());
						weddingGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveWeddingGift(weddingGift, officerNric);
					}
	    	}
		 	
		 	for(int i=0; i<newbornReports.size(); i++)
	    	{
	    		LOGGER.info(newbornReports.get(i).getBenefitsReport().getReferenceNumber()+":"+newbornReports.get(i).getBenefitsReport().getOfficerAction());
	    			newbornGift= benefitsDAO.getNewBorn(newbornReports.get(i).getBenefitsReport().getReferenceNumber());
					if(newbornReports.get(i).getBenefitsReport().getOfficerAction().equals("Verified")&& officerLevel.equals("FO"))
					{
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Verified");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(newbornReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(newbornReports.get(i).getBenefitsReport().getReferenceNumber());
						newbornGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveNewBorn(newbornGift, officerNric);
					}
					else if(newbornReports.get(i).getBenefitsReport().getOfficerAction().equals("Rejected")&& officerLevel.equals("FO"))
					{
						newbornGift.setApplicationStatus(ApplicationStatus.REJECTED);
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Rejected");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(newbornReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(newbornReports.get(i).getBenefitsReport().getReferenceNumber());
						newbornGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveNewBorn(newbornGift, officerNric);
					}
					else if(newbornReports.get(i).getBenefitsReport().getOfficerAction().equals("Endorsed")&& officerLevel.equals("AD"))
					{
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Endorsed");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(newbornReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(newbornReports.get(i).getBenefitsReport().getReferenceNumber());
						newbornGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveNewBorn(newbornGift, officerNric);
					}
					else
					{
						newbornGift.setApplicationStatus(ApplicationStatus.REJECTED);
						ApprovalRecord approvalRecord = new ApprovalRecord();
						approvalRecord.setOfficerAction("Rejected");
						approvalRecord.setDateOfCompletion(currentDate);
						approvalRecord.setOfficerComments(newbornReports.get(i).getComments());
						approvalRecord.setOfficerLevel(officerLevel);
						approvalRecord.setOfficerName(officerName);
						approvalRecord.setOfficerNric(officerNric);
						approvalRecord.setReferenceNumber(newbornReports.get(i).getBenefitsReport().getReferenceNumber());
						newbornGift.getApprovalRecords().add(approvalRecord);
						benefitsDAO.saveNewBorn(newbornGift, officerNric);
					}	
	    	}
		 		try
		 		{
		 		SessionFactoryUtil.commitTransaction();
		 		}
		 		catch(Exception e)
		 		{
		 		LOGGER.log(Level.SEVERE, "error in commit transaction");
		 		}
		    }
		 	catch (Exception e) {
				LOGGER.log(Level.SEVERE, "error in HRPSService updateVerifiedApplications");
				SessionFactoryUtil.rollbackTransaction();
				throw new HRPSServiceException("error in HRPSService updateVerifiedApplications", e);
		 	}
	    	
		}
	 
	 @Override
		public void informADForFinalizedApplication (List<String> nrics, Date searchStartDate, Date searchEndDate, String officerAction) throws HRPSServiceException {
			List<String> emails = null;
			HRPSConfig config = null;
			BenefitsStatistic bgStatistic = new BenefitsStatistic();
			BenefitsStatistic wgStatistic = new BenefitsStatistic();
			BenefitsStatistic nbStatistic = new BenefitsStatistic();
			
			try {
				LOGGER.log(Level.INFO,"informADForFinalizedApplication is transaction active: "+SessionFactoryUtil.isTransactionActive());
				if(!SessionFactoryUtil.isTransactionActive())
				{
					SessionFactoryUtil.beginTransaction();

				}
				else
				{
					SessionFactoryUtil.getCurrentSession().close();
					SessionFactoryUtil.getCurrentSession().beginTransaction();
				}
				config = hrpsDAO.getHRPSConfig();
				// Get office email of AD
				emails = personnelDAO.getOfficeEmailAddress(nrics);
				
				// Get Approved Bereavement Statistics
				bgStatistic = hrpsDAO.getBereavementStatistic(searchStartDate, searchEndDate, officerAction);
				LOGGER.info(String.format("BG SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", bgStatistic.getSpfCount(), bgStatistic.getGcCount(), bgStatistic.getPnsfCount(), bgStatistic.getCnbCount(),bgStatistic.getIsdCount(),bgStatistic.getMhqCount(),bgStatistic.getNonSpfCount()));
							
				// Get Wedding Statistics
				wgStatistic = hrpsDAO.getWeddingStatistic(searchStartDate, searchEndDate, officerAction);
				LOGGER.info(String.format("WG SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", wgStatistic.getSpfCount(), wgStatistic.getGcCount(), wgStatistic.getPnsfCount(), wgStatistic.getCnbCount(),wgStatistic.getIsdCount(),wgStatistic.getMhqCount(),wgStatistic.getNonSpfCount()));
											
				// Get Newborn Statistics
				nbStatistic = hrpsDAO.getNewbornStatistic(searchStartDate, searchEndDate, officerAction);
				LOGGER.info(String.format("NB SPF Count=%s, GC Count=%s, PNSF Count=%s, CNB Count=%s, ISD Count=%s, MHQ Count=%s, NON-SPF Count=%s", nbStatistic.getSpfCount(), nbStatistic.getGcCount(), nbStatistic.getPnsfCount(), nbStatistic.getCnbCount(),nbStatistic.getIsdCount(),nbStatistic.getMhqCount(),nbStatistic.getNonSpfCount()));
							
				SessionFactoryUtil.commitTransaction();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Fail to retrieve office email");
				SessionFactoryUtil.rollbackTransaction();
				throw new HRPSServiceException("Fail to retrieve office email", e);
			}

			List<String> reportUrls = getFinanceReportUrl(searchStartDate, searchEndDate, officerAction);

			emailUtil.sendFinalizedReport(bgStatistic, wgStatistic, nbStatistic, reportUrls, emails, config);
		}



}
