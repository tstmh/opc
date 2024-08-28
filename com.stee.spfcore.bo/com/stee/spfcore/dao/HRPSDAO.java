package com.stee.spfcore.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import java.util.logging.*;

import com.stee.spfcore.dao.utils.SequenceUtil;
import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.hrps.HRPSConfig;
import com.stee.spfcore.model.hrps.HRPSDetails;
import com.stee.spfcore.model.hrps.RecordStatus;
import com.stee.spfcore.model.hrps.internal.BenefitsDetails;
import com.stee.spfcore.model.hrps.internal.BenefitsReport;
import com.stee.spfcore.model.hrps.internal.BenefitsStatistic;
import com.stee.spfcore.model.hrps.internal.BenefitsSummary;
import com.stee.spfcore.model.hrps.internal.MembershipDeductionDetail;
import com.stee.spfcore.model.hrps.internal.PersonalNameRankUnit;
import com.stee.spfcore.vo.membership.MembershipCriteria;

public class HRPSDAO {
	
	private static final Logger logger = Logger.getLogger(HRPSDAO.class.getName());
	private static final List<String> SPF_LIST = Arrays.asList("385","389","338");
	private static final String SPF = "SPF";
	private static final String GC = "GC";
	private static final String PNSF = "PNSF";
	private static final String CNB = "CNB";
	private static final String ISD = "ISD";
	private static final String MHQ = "MHQ";
	private static final String NON_SPF = "NON_SPF";
	
	public HRPSConfig getHRPSConfig() {
		logger.log(Level.INFO, "Get HRPS Config");
		HRPSConfig hrpsConfig = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append("select hrps from HRPSConfig hrps");
		
		Query query = session.createQuery(queryStr.toString());
		
		query.setMaxResults(1);
		hrpsConfig = (HRPSConfig)query.uniqueResult();
		if (hrpsConfig == null) {
			hrpsConfig = new HRPSConfig();
		}
		return hrpsConfig;
	}
	
	public String generateId() {
		return SequenceUtil.getInstance().getNextSequenceValue(HRPSDetails.class);
	}
	
	public HRPSDetails getHRPSDetails(String id) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		return (HRPSDetails) session.get(HRPSDetails.class, id);
	}
	
	public HRPSDetails getHRPSDetailsByReference(String reference) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append("select hrps FROM HRPSDetails hrps where hrps.reference = :reference");
		
		Query query = session.createQuery(queryStr.toString());
		
		query.setMaxResults(1);
		query.setParameter("reference", reference);
		
		return (HRPSDetails)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<HRPSDetails> getHRPSDetailsByStatusAndDate(RecordStatus status, Date date){
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append("select hrps FROM HRPSDetails hrps where 1=1 ");
		if (status != null) {
			queryStr.append("and hrps.status = :status ");
		}
		if (date != null) {
			queryStr.append("and hrps.updatedDate < :date");
		}
		
		Query query = session.createQuery(queryStr.toString());
		
		if (status != null) {
			query.setParameter("status", status);
		}
		if (date != null) {
			query.setParameter("date", date);
		}
		return (List<HRPSDetails>)query.list();
	}
	
	public void addHRPSDetails (HRPSDetails hrpsDetails, String requester) {
		logger.log(Level.INFO,"Add HRPS Details DAO");
		
		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		session.save(hrpsDetails);
		session.flush();
	}
	
	public void updateHRPSDetails (HRPSDetails hrpsDetails, String requester) {
		logger.log(Level.INFO,"Update HRPS Details DAO");
		
		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		session.merge(hrpsDetails);
		session.flush();
	}
	
	public void saveHRPSDetails (List<HRPSDetails> hrpsDetails, String requester) {
		 logger.log(Level.INFO,"Save HRPS Details List DAO");
		 
		 SessionFactoryUtil.setUser(requester);
		 Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		 
		 for (int i = 0; i < hrpsDetails.size(); i ++) {
			 HRPSDetails hrpsDetailObj = hrpsDetails.get(i);
			 session.saveOrUpdate(hrpsDetailObj);
			 
			 if ( i % 20 == 0) { //20, same as the JDBC batch size
	             //flush a batch of inserts and release memory:
				 session.flush();
				 session.clear();
			 }
		 }
		 session.flush();
	}
	
	public void deleteHRPSDetails (List<HRPSDetails> hrpsDetails, String requester) {
		logger.log(Level.INFO, "Delete HRPS Details List DAO");
		
		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		for (int i = 0; i < hrpsDetails.size(); i++) {
			HRPSDetails hrpsDetailObj = hrpsDetails.get(i);
			session.delete(hrpsDetailObj);
			
			if ( i % 20 == 0 ) {//20, same as the JDBC batch size
				//flush a batch of deletes and release memory
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MembershipDeductionDetail> getMembershipDeductionDetailsByMonth(int month, int year) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append("select mdd FROM MembershipDeductionDetails mdd where mdd.month = :month and mdd.year = :year");
		
		Query query = session.createQuery(queryStr.toString());
		
		query.setParameter("month", month);
		query.setParameter("year", year);
		
		return (List<MembershipDeductionDetail>)query.list();
		
	}
	
	public void saveMembershipDeductionDetails (List<MembershipDeductionDetail> details, String requester) {
		logger.log(Level.INFO,"Save Membership Deduction Details List DAO");
		 
		 SessionFactoryUtil.setUser(requester);
		 Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		 
		 for (int i = 0; i < details.size(); i++) {
			 MembershipDeductionDetail membershipDeductionObj = details.get(i);
			 session.saveOrUpdate(membershipDeductionObj);
			 
			 if ( i % 20 == 0) { //20, same as the JDBC batch size
	             //flush a batch of inserts and release memory:
				 session.flush();
				 session.clear();
			 }
		 }
		 session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<MembershipDeductionDetail> createMembershipDeductionDetailByCriteria(MembershipCriteria membershipCriteria, List<String> serviceTypes) {
		logger.log(Level.INFO,"Create Membership Deduction Details by Criteria");
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append("select personnel.nric as nric, personnel.name as name, "
				+ "(select c.description from Code c where c.id = personnel.employment.organisationOrDepartment and c.type = :unitCodeType) as unit, "
				+ "(select c.description from Code c where c.id = personnel.employment.rankOrGrade and c.type = :rankCodeType) as rank, "
				+ "(month(current_date())) as month, "
				+ "(year(current_date())) as year "
				+ "FROM PersonalDetail AS personnel, Membership AS mem " + "where personnel.nric = mem.nric");
		
		if (serviceTypes != null && !serviceTypes.isEmpty()) {
			queryStr.append(" AND personnel.employment.serviceType in :serviceTypes");
		}
		buildQueryStringByCriteria(queryStr, membershipCriteria);
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
		
        Query query = session.createQuery( queryStr.toString() );
        
        query.setParameter("unitCodeType", CodeType.UNIT_DEPARTMENT);
        query.setParameter("rankCodeType", CodeType.RANK);
        
        if (serviceTypes != null && !serviceTypes.isEmpty()) {
        	query.setParameterList("serviceTypes", serviceTypes);
        }
        
        setCriteriaParameter( query, membershipCriteria );
        
        query.setResultTransformer( Transformers.aliasToBean( MembershipDeductionDetail.class ) );
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", query));
		}

		return ( List< MembershipDeductionDetail > ) query.list();
	}
	
    private StringBuilder buildQueryStringByCriteria( StringBuilder queryStr, MembershipCriteria membershipCriteria ) {

        if ( membershipCriteria != null ) {
            if ( membershipCriteria.getName() != null && !membershipCriteria.getName().isEmpty() ) {
                queryStr.append( " AND personnel.name LIKE :name" );
            }

            if ( membershipCriteria.getNric() != null && !membershipCriteria.getNric().isEmpty() ) {
                queryStr.append( " AND personnel.nric LIKE :nric" );
            }

            if ( membershipCriteria.getOrgOrDeptCode() != null && !membershipCriteria.getOrgOrDeptCode().isEmpty() ) {
                queryStr.append( " AND personnel.employment.organisationOrDepartment = :orgOrDeptCode" );
            }

            if ( membershipCriteria.getSearchEffStartDate() != null && membershipCriteria.getSearchEffEndDate() != null ) {
                queryStr.append( " AND mem.effectiveDate BETWEEN :effStartDate AND :effEndDate" );
            }

            if ( membershipCriteria.getSearchExpStartDate() != null && membershipCriteria.getSearchExpEndDate() != null ) {
                if ( membershipCriteria.getSearchExpDateIncludeNull() != null && membershipCriteria.getSearchExpDateIncludeNull() ) {
                    queryStr.append( " AND ( ( mem.expiryDate BETWEEN :expStartDate AND :expEndDate ) OR ( mem.expiryDate IS NULL ) )" );
                }
                else {
                    queryStr.append( " AND mem.expiryDate BETWEEN :expStartDate AND :expEndDate" );
                }
            }
            else {
                if ( membershipCriteria.getSearchExpDateIncludeNull() != null && membershipCriteria.getSearchExpDateIncludeNull() ) {
                    queryStr.append( " AND mem.expiryDate IS NULL" );
                }
            }

            if ( membershipCriteria.getNominationStatus() != null && !ApplicationStatus.NONE.equals( membershipCriteria.getNominationStatus() ) ) {
                queryStr.append( " AND mem.insurance.nominationStatus = :nominationStatus" );
            }

            if ( membershipCriteria.getWithdrawalStatus() != null && !membershipCriteria.getWithdrawalStatus().equals( ApplicationStatus.NONE ) ) {
                queryStr.append( " AND mem.withdrawalStatus = :withdrawalStatus" );
            }

            if ( membershipCriteria.getMembershipStatus() != null && !membershipCriteria.getMembershipStatus().equals( ActivationStatus.NONE ) ) {
                queryStr.append( " AND mem.membershipStatus = :membershipStatus" );
            }


            if ( membershipCriteria.getSearchSubmissionStartDate() != null && membershipCriteria.getSearchSubmissionEndDate() != null ) {
                queryStr.append( " AND mem.insurance.submissionDate BETWEEN :submissionStartDate AND :submissionEndDate" );
            }
            // only submission end date
            if ( membershipCriteria.getSearchSubmissionStartDate() == null && membershipCriteria.getSearchSubmissionEndDate() != null ) {
                queryStr.append( " AND mem.insurance.submissionDate <= :submissionEndDate" );
            }

            if ( membershipCriteria.getSearchCessationStartDate() != null && membershipCriteria.getSearchCessationEndDate() != null ) {
                queryStr.append( " AND mem.dateOfCessation BETWEEN :cessationStartDate AND :cessationEndDate" );
            }
        }

        return queryStr;
    }

    private Query setCriteriaParameter( Query query, MembershipCriteria membershipCriteria ) {

        if ( membershipCriteria != null ) {
            if ( membershipCriteria.getName() != null && !membershipCriteria.getName().isEmpty() ) {
                query.setParameter( "name", "%" + membershipCriteria.getName() + "%" );
            }

            if ( membershipCriteria.getNric() != null && !membershipCriteria.getNric().isEmpty() ) {
                query.setParameter( "nric", "%" + membershipCriteria.getNric() + "%" );
            }

            if ( membershipCriteria.getOrgOrDeptCode() != null && !membershipCriteria.getOrgOrDeptCode().isEmpty() ) {
                query.setParameter( "orgOrDeptCode", membershipCriteria.getOrgOrDeptCode() );
            }

            if ( membershipCriteria.getSearchEffStartDate() != null && membershipCriteria.getSearchEffEndDate() != null ) {
                query.setParameter( "effStartDate", membershipCriteria.getSearchEffStartDate() );
                query.setParameter( "effEndDate", membershipCriteria.getSearchEffEndDate() );
            }

            if ( membershipCriteria.getSearchExpStartDate() != null && membershipCriteria.getSearchExpEndDate() != null ) {
                query.setParameter( "expStartDate", membershipCriteria.getSearchExpStartDate() );
                query.setParameter( "expEndDate", membershipCriteria.getSearchExpEndDate() );
            }

            if ( membershipCriteria.getNominationStatus() != null && !ApplicationStatus.NONE.equals( membershipCriteria.getNominationStatus() ) ) {
                query.setParameter( "nominationStatus", membershipCriteria.getNominationStatus() );
            }

            if ( membershipCriteria.getWithdrawalStatus() != null && !ApplicationStatus.NONE.equals( membershipCriteria.getWithdrawalStatus() ) ) {
                query.setParameter( "withdrawalStatus", membershipCriteria.getWithdrawalStatus() );
            }

            if ( membershipCriteria.getMembershipStatus() != null && !membershipCriteria.getMembershipStatus().equals( ActivationStatus.NONE ) ) {
                query.setParameter( "membershipStatus", membershipCriteria.getMembershipStatus() );
            }

            if ( membershipCriteria.getSearchSubmissionStartDate() != null && membershipCriteria.getSearchSubmissionEndDate() != null ) {
                query.setParameter( "submissionStartDate", membershipCriteria.getSearchSubmissionStartDate() );
                query.setParameter( "submissionEndDate", membershipCriteria.getSearchSubmissionEndDate() );
            }

            if ( membershipCriteria.getSearchSubmissionStartDate() == null && membershipCriteria.getSearchSubmissionEndDate() != null ) {
                query.setParameter( "submissionEndDate", membershipCriteria.getSearchSubmissionEndDate() );
            }

            if ( membershipCriteria.getSearchCessationStartDate() != null && membershipCriteria.getSearchCessationEndDate() != null ) {
                query.setParameter( "cessationStartDate", membershipCriteria.getSearchCessationStartDate() );
                query.setParameter( "cessationEndDate", membershipCriteria.getSearchCessationEndDate() );
            }
        }

        return query;
    }
	
    @SuppressWarnings("unchecked")
	public PersonalNameRankUnit getPersonalNameRankUnit (String nric) {
    	logger.log(Level.INFO,"Get Personnel Name Rank Unit");
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder queryStr = new StringBuilder();
		
		queryStr.append("select pd.nric as nric, pd.name as name, "
				+ "(select c.description from Code c where c.id = pd.employment.organisationOrDepartment and c.type = :unitCodeType) as unit, "
				+ "(select c.description from Code c where c.id = pd.employment.rankOrGrade and c.type = :rankCodeType) as rank "
				+ "FROM PersonalDetail pd WHERE pd.nric = :nric");
		
		Query query = session.createQuery( queryStr.toString() );
	    
	    query.setParameter("unitCodeType", CodeType.UNIT_DEPARTMENT);
	    query.setParameter("rankCodeType", CodeType.RANK);
	    query.setParameter("nric", nric);
	     
	    query.setMaxResults( 1 );
	    query.setResultTransformer( Transformers.aliasToBean( PersonalNameRankUnit.class ) );
	    
	    List< PersonalNameRankUnit > list = query.list();

        if ( !list.isEmpty() ) {
            return list.get( 0 );
        }

        return null;
    }
    /**
     * Get Bereavement Summary for AD.
     * @author 
     */
    @SuppressWarnings("unchecked")
	public List<BenefitsSummary> getBereavementSummary(Date searchStartDate, Date searchEndDate) {
    	logger.log(Level.INFO,"Get Bereavement Summary");
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select (select c.description from SPFCORE.CODES as c where CODE_TYPE = 'SERVICE_TYPE' and c.id =  ped.SERVICE_TYPE) as serviceType, ");
    	queryStr.append("(select c.description from SPFCORE.CODES as c where CODE_TYPE = 'UNIT_DEPARTMENT' and c.id = ped2.ORGANISATION_OR_DEPARTMENT) as unit, ");
    	queryStr.append("(select c.description from SPFCORE.CODES as c where CODE_TYPE = 'SUB_UNIT' and c.id = ped2.SUBUNIT) as subunit, ");
    	queryStr.append("bg.DECEASED_RELATION as deceasedRelation, count(*) as count, sum(bg.AMT_PAID) as amount " );
    	queryStr.append("from SPFCORE.BEREAVEMENT_GRANT_APPLICATION bg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on bg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("left join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped2 on bg.MEMBER_NRIC = ped2.NRIC and ped2.SERVICE_TYPE = '000' ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, BEREAVEMENT_REFERENCE_NUMBER, row_number() ");
    	queryStr.append("over (partition by BEREAVEMENT_REFERENCE_NUMBER order by DATE_OF_COMPLETION desc) as row_num " );
    	queryStr.append("from SPFCORE.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on bg.REFERENCE_NUMBER = aar.BEREAVEMENT_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = 'Approved' ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}
    	queryStr.append("group by ped.SERVICE_TYPE, ped2.ORGANISATION_OR_DEPARTMENT, ped2.SUBUNIT, bg.DECEASED_RELATION;");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
    	if (searchStartDate != null && searchEndDate != null) {
    		sqlQuery.setParameter("searchStartDate", searchStartDate);
    		sqlQuery.setParameter("searchEndDate", searchEndDate);
    	}
    	
    	sqlQuery.addScalar("serviceType",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("unit",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("subunit",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("count",StandardBasicTypes.INTEGER);
    	sqlQuery.addScalar("deceasedRelation",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("amount",StandardBasicTypes.DOUBLE);
    	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsSummary.class));
    	
    	return (List<BenefitsSummary>)sqlQuery.list();
    }
    /**
     * Get Wedding Summary for AD
     * @author 
     */
    @SuppressWarnings("unchecked")
	public List<BenefitsSummary> getWeddingSummary(Date searchStartDate, Date searchEndDate) {
    	logger.log(Level.INFO,"Get Wedding Summary");
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select (select c.description from SPFCORE.CODES as c where CODE_TYPE = 'SERVICE_TYPE' and c.id =  ped.SERVICE_TYPE) as serviceType, ");
    	queryStr.append("(select c.description from SPFCORE.CODES as c where CODE_TYPE = 'UNIT_DEPARTMENT' and c.id = ped2.ORGANISATION_OR_DEPARTMENT) as unit, ");
    	queryStr.append("(select c.description from SPFCORE.CODES as c where CODE_TYPE = 'SUB_UNIT' and c.id = ped2.SUBUNIT) as subunit, ");
    	queryStr.append("count(*) as count, sum(wg.AMT_PAID) as amount " );
    	queryStr.append("from SPFCORE.WEDDING_GIFT_APPLICATION wg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on wg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("left join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped2 on wg.MEMBER_NRIC = ped2.NRIC and ped2.SERVICE_TYPE = '000' ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, WEDDING_REFERENCE_NUMBER, row_number() ");
    	queryStr.append("over (partition by WEDDING_REFERENCE_NUMBER order by DATE_OF_COMPLETION desc) as row_num " );
    	queryStr.append("from SPFCORE.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on wg.REFERENCE_NUMBER = aar.WEDDING_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = 'Approved' ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}
    	queryStr.append("group by ped.SERVICE_TYPE, ped2.ORGANISATION_OR_DEPARTMENT, ped2.SUBUNIT;");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
    	if (searchStartDate != null && searchEndDate != null) {
    		sqlQuery.setParameter("searchStartDate", searchStartDate);
    		sqlQuery.setParameter("searchEndDate", searchEndDate);
    	}
    	
    	sqlQuery.addScalar("serviceType",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("unit",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("subunit",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("count",StandardBasicTypes.INTEGER);
    	sqlQuery.addScalar("amount",StandardBasicTypes.DOUBLE);
    	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsSummary.class));
    	
    	return (List<BenefitsSummary>)sqlQuery.list();
    	
    }
    /**
     * Get NewbornSummary for AD
     * @author 
     */
    @SuppressWarnings("unchecked")
	public List<BenefitsSummary> getNewbornSummary(Date searchStartDate, Date searchEndDate) {
    	logger.log(Level.INFO,"Get Wedding Summary");
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select (select c.description from SPFCORE.CODES as c where CODE_TYPE = 'SERVICE_TYPE' and c.id =  ped.SERVICE_TYPE) as serviceType, ");
    	queryStr.append("(select c.description from SPFCORE.CODES as c where CODE_TYPE = 'UNIT_DEPARTMENT' and c.id = ped2.ORGANISATION_OR_DEPARTMENT) as unit, ");
    	queryStr.append("(select c.description from SPFCORE.CODES as c where CODE_TYPE = 'SUB_UNIT' and c.id = ped2.SUBUNIT) as subunit, ");
    	queryStr.append("count(*) as count, sum(nb.AMT_PAID) as amount " );
    	queryStr.append("from SPFCORE.NEWBORN_GIFT_APPLICATION nb ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on nb.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("left join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped2 on nb.MEMBER_NRIC = ped2.NRIC and ped2.SERVICE_TYPE = '000' ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, NEW_BORN_REFERENCE_NUMBER, row_number() ");
    	queryStr.append("over (partition by NEW_BORN_REFERENCE_NUMBER order by DATE_OF_COMPLETION desc) as row_num " );
    	queryStr.append("from SPFCORE.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on nb.REFERENCE_NUMBER = aar.NEW_BORN_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = 'Approved' ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}
    	queryStr.append("group by ped.SERVICE_TYPE, ped2.ORGANISATION_OR_DEPARTMENT, ped2.SUBUNIT;");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
    	if (searchStartDate != null && searchEndDate != null) {
    		sqlQuery.setParameter("searchStartDate", searchStartDate);
    		sqlQuery.setParameter("searchEndDate", searchEndDate);
    	}
    	
    	sqlQuery.addScalar("serviceType",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("unit",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("subunit",StandardBasicTypes.STRING);
    	sqlQuery.addScalar("count",StandardBasicTypes.INTEGER);
    	sqlQuery.addScalar("amount",StandardBasicTypes.DOUBLE);
    	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsSummary.class));
    	
    	return (List<BenefitsSummary>)sqlQuery.list();
    	
    }
    /**
     * Get Approved Bereavement Application for HRP
     * Include only Police SR, Police JR, Police Civilian, PNSF
     * 20211216 Changed to allow all officers
     * @author 
     */
    @SuppressWarnings("unchecked")
	public List<BenefitsDetails> getBereavementApplicationForHRP(Date searchStartDate, Date searchEndDate, String officerAction) {
    	logger.log(Level.INFO,"Get Bereavement Application for HRP");
    	
    	Session session = SessionFactoryUtil.getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select bg.REFERENCE_NUMBER as referenceNumber, bg.MEMBER_NRIC as memberNric, ");
    	queryStr.append("bg.AMT_PAID as amountToBePaid, bg.SUBMITTED_BY as submittedBy ");
    	queryStr.append("from SPFCORE.BEREAVEMENT_GRANT_APPLICATION bg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped ");
    	queryStr.append("on bg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd ");
    	queryStr.append("on pd.NRIC = bg.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION,REFERENCE_NUMBER,BEREAVEMENT_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY BEREAVEMENT_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on bg.REFERENCE_NUMBER = aar.BEREAVEMENT_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}
    	queryStr.append("and ped.SERVICE_TYPE in :serviceType ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
    	addCommonParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	addCommonScalars(sqlQuery);
    	
    	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsDetails.class));
    	
    	return (List<BenefitsDetails>)sqlQuery.list();
    }
    
    /**
     * Get Approved Wedding Application for HRP
     * Include only Police SR, Police JR, Police Civilian, PNSF 
     * 20211216 Changed to allow all officers
     * @author 
     */
    @SuppressWarnings("unchecked")
	public List<BenefitsDetails> getWeddingApplicationForHRP(Date searchStartDate, Date searchEndDate, String officerAction) {
    	logger.log(Level.INFO,"Get Wedding Application for HRP");
    	
    	Session session = SessionFactoryUtil.getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select wg.REFERENCE_NUMBER as referenceNumber, wg.MEMBER_NRIC as memberNric, ");
    	queryStr.append("wg.AMT_PAID as amountToBePaid, wg.SUBMITTED_BY as submittedBy ");
    	queryStr.append("from SPFCORE.WEDDING_GIFT_APPLICATION wg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped ");
    	queryStr.append("on wg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd ");
    	queryStr.append("on pd.NRIC = wg.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION,REFERENCE_NUMBER,WEDDING_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY WEDDING_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on wg.REFERENCE_NUMBER = aar.WEDDING_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}
    	queryStr.append("and ped.SERVICE_TYPE in :serviceType ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
    	addCommonParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	 
    	addCommonScalars(sqlQuery);
    	
    	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsDetails.class));
    	
    	return (List<BenefitsDetails>)sqlQuery.list();
    }
    
    /**
     * Get Approved Newborn Application for HRP
     * Include only Police SR, Police JR, Police Civilian, PNSF
     * 20211216 Changed to allow all officers
     * @author 
     */
    @SuppressWarnings("unchecked")
	public List<BenefitsDetails> getNewbornApplicationForHRP(Date searchStartDate, Date searchEndDate, String officerAction) {
    	logger.log(Level.INFO,"Get Newborn Application for HRP");
    	
    	Session session = SessionFactoryUtil.getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select nb.REFERENCE_NUMBER as referenceNumber, nb.MEMBER_NRIC as memberNric, ");
    	queryStr.append("nb.AMT_PAID as amountToBePaid, nb.SUBMITTED_BY as submittedBy ");
    	queryStr.append("from SPFCORE.NEWBORN_GIFT_APPLICATION nb ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped ");
    	queryStr.append("on nb.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd ");
    	queryStr.append("on pd.NRIC = nb.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION,REFERENCE_NUMBER,NEW_BORN_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY NEW_BORN_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on nb.REFERENCE_NUMBER = aar.NEW_BORN_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}
    	queryStr.append("and ped.SERVICE_TYPE in :serviceType ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
    	addCommonParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	addCommonScalars(sqlQuery);
    	
    	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsDetails.class));
    	
    	return (List<BenefitsDetails>)sqlQuery.list();
    }
    
    private void addCommonScalars( SQLQuery sqlQuery ) {
    	
        sqlQuery.addScalar( "referenceNumber", StandardBasicTypes.STRING );
        sqlQuery.addScalar( "submittedBy", StandardBasicTypes.STRING );
        sqlQuery.addScalar( "memberNric", StandardBasicTypes.STRING );
        sqlQuery.addScalar( "amountToBePaid", StandardBasicTypes.DOUBLE );

    }
    
    private void addCommonParameters( SQLQuery sqlQuery , Date startDate, Date endDate, String officerAction) {
    	if (startDate != null && endDate != null) {
    		sqlQuery.setParameter("searchStartDate", startDate);
    		sqlQuery.setParameter("searchEndDate", endDate);
    	}
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("SPF_LIST %s", SPF_LIST));
		}
    	sqlQuery.setParameterList("serviceType", SPF_LIST);
    	sqlQuery.setParameter("officerAction", officerAction);

    }
    
    @SuppressWarnings("unchecked")
	public List<BenefitsReport> getBereavementReport(Date searchStartDate, Date searchEndDate, String officerAction) {
    	logger.log(Level.INFO, "Get BereavementReport");
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select bg.REFERENCE_NUMBER as referenceNumber, bg.SUBMITTED_BY as submittedBy, pd.NAME as memberName, pd.NRIC as memberNric, "); 
    	queryStr.append("(select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'SERVICE_TYPE' and c.ID = ped.SERVICE_TYPE) as serviceType, ");
    	queryStr.append("(select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'UNIT_DEPARTMENT' and c.id = ped.ORGANISATION_OR_DEPARTMENT) as unit, ");
    	queryStr.append("(CASE WHEN SUBUNIT is NULL or SUBUNIT = '' THEN '-' "); 
    	queryStr.append("ELSE (select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'SUB_UNIT' and c.id = ped.SUBUNIT) END) as subunit, ");
    	queryStr.append("'BEREAVEMENT' as typeOfBenefits, UPPER(bg.DECEASED_RELATION) as deceasedRelation, UPPER(bg.DECEASED_NAME) as name, ");
    	queryStr.append("bg.DECEASED_NRIC as nric, bg.DECEASE_DATE as date, bg.AMT_PAID as amount, aar.OFFICER_ACTION as officerAction ");
    	queryStr.append("from SPFCORE.BEREAVEMENT_GRANT_APPLICATION bg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on bg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd on pd.NRIC = bg.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, REFERENCE_NUMBER, BEREAVEMENT_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY BEREAVEMENT_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on bg.REFERENCE_NUMBER = aar.BEREAVEMENT_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (searchStartDate != null && searchEndDate != null) {
    		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
    	}

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
    	
     	if (searchStartDate != null && searchEndDate != null && officerAction != null) {
    		sqlQuery.setParameter("searchStartDate", searchStartDate);
    		sqlQuery.setParameter("searchEndDate", searchEndDate);
    		sqlQuery.setParameter("officerAction", officerAction);
    	}
     	
     	addBenefitsReportScalars(sqlQuery);
     	
     	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsReport.class));
     	return (List<BenefitsReport>)sqlQuery.list();
    }
    
    @SuppressWarnings("unchecked")
   	public List<BenefitsReport> getWeddingReport(Date searchStartDate, Date searchEndDate, String officerAction) {
       	logger.log(Level.INFO,"Get Wedding Report");
       	
       	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
       	
       	StringBuilder queryStr = new StringBuilder();
       	
       	queryStr.append("select wg.REFERENCE_NUMBER as referenceNumber, wg.SUBMITTED_BY as submittedBy, pd.NAME as memberName, pd.NRIC as memberNric, "); 
       	queryStr.append("(select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'SERVICE_TYPE' and c.ID = ped.SERVICE_TYPE) as serviceType, ");
       	queryStr.append("(select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'UNIT_DEPARTMENT' and c.id = ped.ORGANISATION_OR_DEPARTMENT) as unit, ");
       	queryStr.append("(CASE WHEN SUBUNIT is NULL or SUBUNIT = '' THEN '-' "); 
       	queryStr.append("ELSE (select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'SUB_UNIT' and c.id = ped.SUBUNIT) END) as subunit, ");
       	queryStr.append("'WEDDING' as typeOfBenefits, '-' as deceasedRelation, UPPER(wg.SPOUSE_NAME) as name, ");
       	queryStr.append("wg.SPOUSE_NRIC as nric, wg.DATE_OF_MARRIAGE as date, wg.AMT_PAID as amount, aar.OFFICER_ACTION as officerAction ");
       	queryStr.append("from SPFCORE.WEDDING_GIFT_APPLICATION wg ");
       	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on wg.MEMBER_NRIC = ped.NRIC ");
       	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd on pd.NRIC = wg.MEMBER_NRIC ");
       	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, REFERENCE_NUMBER, WEDDING_REFERENCE_NUMBER, ROW_NUMBER() ");
       	queryStr.append("OVER (PARTITION BY WEDDING_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
       	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
       	queryStr.append("on wg.REFERENCE_NUMBER = aar.WEDDING_REFERENCE_NUMBER ");
       	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
       	if (searchStartDate != null && searchEndDate != null) {
       		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
       	}

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
       	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
       	
        	if (searchStartDate != null && searchEndDate != null && officerAction != null) {
       		sqlQuery.setParameter("searchStartDate", searchStartDate);
       		sqlQuery.setParameter("searchEndDate", searchEndDate);
       		sqlQuery.setParameter("officerAction", officerAction);
       	}
        	
        	addBenefitsReportScalars(sqlQuery);
        	
        	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsReport.class));
			return (List<BenefitsReport>) sqlQuery.list();
       }
    
    @SuppressWarnings("unchecked")
   	public List<BenefitsReport> getNewbornReport(Date searchStartDate, Date searchEndDate, String officerAction) {
       	logger.log(Level.INFO, "Get NewbornReport");
       	
       	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
       	
       	StringBuilder queryStr = new StringBuilder();
       	
       	queryStr.append("select nb.REFERENCE_NUMBER as referenceNumber, nb.SUBMITTED_BY as submittedBy, pd.NAME as memberName, pd.NRIC as memberNric, "); 
       	queryStr.append("(select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'SERVICE_TYPE' and c.ID = ped.SERVICE_TYPE) as serviceType, ");
       	queryStr.append("(select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'UNIT_DEPARTMENT' and c.id = ped.ORGANISATION_OR_DEPARTMENT) as unit, ");
       	queryStr.append("(CASE WHEN SUBUNIT is NULL or SUBUNIT = '' THEN '-' "); 
       	queryStr.append("ELSE (select c.DESCRIPTION FROM SPFCORE.CODES c where CODE_TYPE = 'SUB_UNIT' and c.id = ped.SUBUNIT) END) as subunit, ");
       	queryStr.append("'NEWBORN' as typeOfBenefits, '-' as deceasedRelation, UPPER(nb.CHILD_NAME) as name, ");
       	queryStr.append("nb.BIRTH_CERTIFICATE_NO as nric, nb.CHILD_DATE_OF_BIRTH as date, nb.AMT_PAID as amount, aar.OFFICER_ACTION as officerAction ");
       	queryStr.append("from SPFCORE.NEWBORN_GIFT_APPLICATION nb ");
       	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on nb.MEMBER_NRIC = ped.NRIC ");
       	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd on pd.NRIC = nb.MEMBER_NRIC ");
       	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, REFERENCE_NUMBER, NEW_BORN_REFERENCE_NUMBER, ROW_NUMBER() ");
       	queryStr.append("OVER (PARTITION BY NEW_BORN_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
       	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
       	queryStr.append("on nb.REFERENCE_NUMBER = aar.NEW_BORN_REFERENCE_NUMBER ");
       	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
       	if (searchStartDate != null && searchEndDate != null) {
       		queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");
       	}

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
       	SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
       	
        	if (searchStartDate != null && searchEndDate != null && officerAction != null) {
       		sqlQuery.setParameter("searchStartDate", searchStartDate);
       		sqlQuery.setParameter("searchEndDate", searchEndDate);
       		sqlQuery.setParameter("officerAction", officerAction);
       	}
        	
        	addBenefitsReportScalars(sqlQuery);
        	
        	sqlQuery.setResultTransformer(Transformers.aliasToBean(BenefitsReport.class));
			return (List<BenefitsReport>)sqlQuery.list();
       }
    
    private void addBenefitsReportScalars(SQLQuery sqlQuery) {
      	sqlQuery.addScalar("referenceNumber", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("memberName", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("memberNric", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("serviceType", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("submittedBy", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("unit", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("subunit", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("typeOfBenefits", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("deceasedRelation", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("name", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("nric", StandardBasicTypes.STRING);
     	sqlQuery.addScalar("date", StandardBasicTypes.DATE);
     	sqlQuery.addScalar("amount", StandardBasicTypes.DOUBLE);
     	sqlQuery.addScalar("officerAction", StandardBasicTypes.STRING);
    }
   
    public BenefitsStatistic getBereavementStatistic(Date searchStartDate, Date searchEndDate, String officerAction) {
    	
    	BenefitsStatistic statistic = new BenefitsStatistic();
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	//Get SPF Officer count
    	SQLQuery sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(SPF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	statistic.setSpfCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get GC Officer count
    	sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(GC));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setGcCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get PNSF Officer count
    	sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(PNSF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setPnsfCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get CNB Officer count
    	sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(CNB));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setCnbCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(ISD));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setIsdCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(MHQ));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setMhqCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get NON SPF Officer count
    	sqlQuery = session.createSQLQuery(getBereavementStatisticQueryStr(NON_SPF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setNonSpfCount(((Number)sqlQuery.uniqueResult()).intValue());
   
    	return statistic;
    }
    
    private String getBereavementStatisticQueryStr(String type) {
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select count(*) from SPFCORE.BEREAVEMENT_GRANT_APPLICATION bg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on bg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd on pd.NRIC = bg.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, REFERENCE_NUMBER, BEREAVEMENT_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY BEREAVEMENT_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on bg.REFERENCE_NUMBER = aar.BEREAVEMENT_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (type.equals(SPF)) {
    		queryStr.append("and ped.SERVICE_TYPE in ('338','389','385') ");
    	} else if (type.equals(GC)) {
    		queryStr.append("and ped.SERVICE_TYPE = '412' ");
    	} else if (type.equals(PNSF)) {
    		queryStr.append("and ped.SERVICE_TYPE = '222' ");
    	} else if (type.equals(CNB)) {
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '01' ");
    	}
    		else if (type.equals(ISD)){
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '06' ");
    	}
    		else if (type.equals(MHQ)){
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '02' ");
    	}
    		else if (type.equals(NON_SPF)) {
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT not in ('01','02','06') ");
    	}
    	queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	return queryStr.toString();
    }
    
	public BenefitsStatistic getWeddingStatistic(Date searchStartDate, Date searchEndDate, String officerAction) {
    	
    	BenefitsStatistic statistic = new BenefitsStatistic();
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	//Get SPF Officer count
    	SQLQuery sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(SPF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	statistic.setSpfCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get GC Officer count
    	sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(GC));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setGcCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get PNSF Officer count
    	sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(PNSF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setPnsfCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get CNB Officer count
    	sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(CNB));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setCnbCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(ISD));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setIsdCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(MHQ));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setMhqCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get NON SPF Officer count
    	sqlQuery = session.createSQLQuery(getWeddingStatisticQueryStr(NON_SPF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setNonSpfCount(((Number)sqlQuery.uniqueResult()).intValue());
   
    	return statistic;
    }
    
    private String getWeddingStatisticQueryStr(String type) {
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select count(*) from SPFCORE.WEDDING_GIFT_APPLICATION wg ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on wg.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd on pd.NRIC = wg.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, REFERENCE_NUMBER, WEDDING_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY WEDDING_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on wg.REFERENCE_NUMBER = aar.WEDDING_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (type.equals(SPF)) {
    		queryStr.append("and ped.SERVICE_TYPE in ('338','389','385') ");
    	} else if (type.equals(GC)) {
    		queryStr.append("and ped.SERVICE_TYPE = '412' ");
    	} else if (type.equals(PNSF)) {
    		queryStr.append("and ped.SERVICE_TYPE = '222' ");
    	} else if (type.equals(CNB)) {
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '01' ");
    	} else if (type.equals(ISD)){
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '06' ");
    	}
    		else if (type.equals(MHQ)){
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '02' ");
    	}
    		else if (type.equals(NON_SPF)) {
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT not in ('01','02','06') ");
    	}
    	queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	return queryStr.toString();
    }
    
public BenefitsStatistic getNewbornStatistic(Date searchStartDate, Date searchEndDate, String officerAction) {
    	
    	BenefitsStatistic statistic = new BenefitsStatistic();
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	//Get SPF Officer count
    	SQLQuery sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(SPF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	statistic.setSpfCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get GC Officer count
    	sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(GC));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setGcCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get PNSF Officer count
    	sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(PNSF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setPnsfCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get CNB Officer count
    	sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(CNB));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setCnbCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(ISD));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setIsdCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(MHQ));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setMhqCount(((Number)sqlQuery.uniqueResult()).intValue());
    	
    	//Get NON SPF Officer count
    	sqlQuery = session.createSQLQuery(getNewbornStatisticQueryStr(NON_SPF));
    	addBenefitsStatisticParameters(sqlQuery, searchStartDate, searchEndDate, officerAction);
    	
    	statistic.setNonSpfCount(((Number)sqlQuery.uniqueResult()).intValue());
   
    	return statistic;
    }
    
    private String getNewbornStatisticQueryStr(String type) {
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("select count(*) from SPFCORE.NEWBORN_GIFT_APPLICATION nb ");
    	queryStr.append("inner join SPFCORE.PERSONAL_EMPLOYMENT_DETAILS ped on nb.MEMBER_NRIC = ped.NRIC ");
    	queryStr.append("inner join SPFCORE.PERSONAL_DETAILS pd on pd.NRIC = nb.MEMBER_NRIC ");
    	queryStr.append("inner join (select DATE_OF_COMPLETION, OFFICER_ACTION, REFERENCE_NUMBER, NEW_BORN_REFERENCE_NUMBER, ROW_NUMBER() ");
    	queryStr.append("OVER (PARTITION BY NEW_BORN_REFERENCE_NUMBER ORDER BY DATE_OF_COMPLETION DESC ) as row_num ");
    	queryStr.append("from spfcore.APPLICATION_APPROVAL_RECORD) aar ");
    	queryStr.append("on nb.REFERENCE_NUMBER = aar.NEW_BORN_REFERENCE_NUMBER ");
    	queryStr.append("where aar.row_num = 1 and aar.OFFICER_ACTION = :officerAction ");
    	if (type.equals(SPF)) {
    		queryStr.append("and ped.SERVICE_TYPE in ('338','389','385') ");
    	} else if (type.equals(GC)) {
    		queryStr.append("and ped.SERVICE_TYPE = '412' ");
    	} else if (type.equals(PNSF)) {
    		queryStr.append("and ped.SERVICE_TYPE = '222' ");
    	} else if (type.equals(CNB)) {
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '01' ");
    	} else if (type.equals(ISD)){
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '06' ");
    	}
    		else if (type.equals(MHQ)){
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT = '02' ");
    	}
    		else if (type.equals(NON_SPF)) {
    		queryStr.append("and (ped.SERVICE_TYPE = '000' or ped.SERVICE_TYPE = '111') and ped.SUBUNIT not in ('01','02','06') ");
    	}
    	queryStr.append("and aar.DATE_OF_COMPLETION between :searchStartDate and :searchEndDate ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("Query String: %s", queryStr));
		}
    	
    	return queryStr.toString();
    }
    
    private void addBenefitsStatisticParameters(SQLQuery sqlQuery, Date searchStartDate, Date searchEndDate, String officerAction) {
    	sqlQuery.setParameter("searchStartDate", searchStartDate);
    	sqlQuery.setParameter("searchEndDate", searchEndDate);
    	sqlQuery.setParameter("officerAction", officerAction);
    }
}
    
