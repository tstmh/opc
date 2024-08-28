package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.dao.dac.DataFilter;
import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.membership.DiscrepancyRecord;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.membership.MembershipFees;
import com.stee.spfcore.model.membership.MembershipPaymentCheckRecord;
import com.stee.spfcore.model.membership.PaymentDataSource;
import com.stee.spfcore.model.membership.PaymentHistory;
import com.stee.spfcore.model.membership.ReconciliationReport;
import com.stee.spfcore.model.membership.StatusChangeRecord;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.service.membership.MembershipServiceException;
import com.stee.spfcore.utils.DateTimeUtil;
import com.stee.spfcore.vo.membership.MembershipCriteria;
import com.stee.spfcore.vo.membership.MembershipExtend;
import com.stee.spfcore.vo.membership.MembershipReport;
import com.stee.spfcore.vo.membership.PaymentRecordCriteria;

public class MembershipDAO {
    private static final Logger logger = Logger.getLogger( MembershipDAO.class.getName() );

    private static final String SEARCH_MEMBERSHIP_COMMON_SQL = "SELECT personnel.nric AS nric, personnel.name AS name, " + "personnel.employment.serviceType AS serviceType, personnel.employment.rankOrGrade AS rankOrGrade, "
            + "personnel.employment.organisationOrDepartment AS organisationOrDepartment, " + "personnel.employment.schemeOfService AS schemeOfService, " + "personnel.preferredContactMode AS preferredContactMode, "
            + "mem.membershipType AS membershipType, mem.membershipStatus AS membershipStatus, " + "mem.updatedBy AS membershipModifiedBy, " + "mem.dateOfCessation AS dateOfCessation, " + "mem.withdrawalStatus AS withdrawalStatus, "
            + "mem.isSPRAMember AS isSPRAMember, " + "mem.insurance.nominationStatus AS nominationStatus, mem.insurance.insuranceCreatedDate AS insuranceCreatedDate, "
            + "mem.insurance.insuranceUpdatedOn AS insuranceUpdatedDate, mem.insurance.submissionDate AS insuranceSubmissionDate, " + "mem.effectiveDate AS effectiveDate, mem.expiryDate AS expiryDate, "
            + "mem.hasInsuranceCoverage AS hasInsuranceCoverage, " + "mem.effectiveDateOfCoverage AS effectiveDateOfCoverage " + "FROM PersonalDetail AS personnel, Membership AS mem " + "where personnel.nric = mem.nric";

    /**
     * Get the membership by nric.
     * 
     * @param nric
     * @return the membership detail. Null if not found
     * @throws AccessDeniedException
     * @throws MembershipServiceException
     *             exception while retrieving the data.
     */
    public Membership getMembership( String nric ) throws AccessDeniedException {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        return ( Membership ) session.get( Membership.class, nric );
    }

    /**
     * Add new membership detail.
     * 
     * @param membership
     *            the detail to be added.
     * @throws AccessDeniedException
     * @throws MembershipServiceException
     *             exception while trying to add the detail.
     */
    public void addMembership( Membership membership, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        membership.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), membership.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + membership.getNric() );
        }

        session.save( membership );
        session.flush();
    }

    /**
     * Update membership detail
     * 
     * @param membership
     * @throws AccessDeniedException
     * @throws MembershipServiceException
     */
    public void updateMembership( Membership membership, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        membership.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), membership.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + membership.getNric() );
        }

        session.merge( membership );
        session.flush();
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalDetail > searchMembersByCritieria( MembershipCriteria membershipCriteria ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( "SELECT personnel FROM PersonalDetail AS personnel, Membership AS mem WHERE personnel.nric = mem.nric" );

        buildQueryStringByCriteria( queryStr, membershipCriteria );
        Query query = session.createQuery( queryStr.toString() );
        setCriteriaParameter( query, membershipCriteria );

        List< PersonalDetail > result = ( List< PersonalDetail > ) query.list();

        // Filter data based on what caller can see.
        result = DataFilter.filterPersonalDetails( SecurityInfo.createInstance(), result );

        return result;
    }

    @SuppressWarnings( "unchecked" )
    public List< MembershipReport > searchMemberReportByCritieria( MembershipCriteria membershipCriteria ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SEARCH_MEMBERSHIP_COMMON_SQL );

        buildQueryStringByCriteria( queryStr, membershipCriteria );
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("%s", queryStr));
        }
        Query query = session.createQuery( queryStr.toString() );
        setCriteriaParameter( query, membershipCriteria );
        query.setResultTransformer( Transformers.aliasToBean( MembershipReport.class ) );
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("%s", query));
        }
        List< MembershipReport > result = ( List< MembershipReport > ) query.list();

        // Filter data base on what caller can see.
        result = DataFilter.filterMembershipReports( SecurityInfo.createInstance(), result );

        return result;
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

    /**
     * 
     * @Title: searchPaymentRecordByAccrualMonth
     * @Description: search membership Payment Record By accrualMonth
     * @param @param membershipCriteria
     * @param @return
     * @return List<Membership>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< Membership > searchPaymentRecordByAccrualMonth( PaymentRecordCriteria paymentRecordCriteria ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( Membership.class );
        criteria.createAlias( "paymentRecords", "paymentRecords" );

        if ( null != paymentRecordCriteria.getAccrualMonth() && !"".equals( paymentRecordCriteria.getAccrualMonth() ) ) {
            criteria.add( Restrictions.eq( "paymentRecords.accrualMonth", paymentRecordCriteria.getAccrualMonth() ) );
        }

        if ( null != paymentRecordCriteria.getBalance() && !"".equals( paymentRecordCriteria.getBalance() ) ) {

            String balance = paymentRecordCriteria.getBalance();
            Double checkBal = Double.parseDouble( balance.substring( 1 ) );

            if ( "!0".equals( balance ) ) {
                criteria.add( Restrictions.ne( "paymentRecords.balance", checkBal ) );
            }
            else if ( "<0".equals( balance ) ) {
                criteria.add( Restrictions.lt( "paymentRecords.balance", checkBal ) );
            }
            else if ( ">0".equals( balance ) ) {
                criteria.add( Restrictions.gt( "paymentRecords.balance", checkBal ) );
            }
            else if ( "=0".equals( balance ) ) {
                criteria.add( Restrictions.eq( "paymentRecords.balance", checkBal ) );
            }
            else {
                return null;
            }
        }

        List< Membership > result = ( List< Membership > ) criteria.list();

        // Filter data based on what user can see
        result = DataFilter.filterMemberships( SecurityInfo.createInstance(), result );

        return result;
    }

    /**
     * 
     * @Title: searchMembershipNominee
     * @Description: search MembershipNominee by NRIC and MEMBER_NRIC
     * @param @param membership
     * @param @return
     * @return Membership
     * @throws
     */
    public Integer searchCountMembershipNominee( String memberNric, String nric ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( "SELECT count(1) FROM SPFCORE.INSURANCE_NOMINEE_DETAILS " + "WHERE NRIC = :nric " + "AND MEMBER_NRIC = :memberNric" );

        Query query = session.createSQLQuery( queryStr.toString() );
        query.setParameter( "nric", nric );
        query.setParameter( "memberNric", memberNric );

        return ( ( Integer ) query.uniqueResult() ).intValue();
    }

    /**
     * Update Membership Fees Record
     * 
     * @param membershipFees
     */
    public void updateMembershipFees( MembershipFees membershipFees, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.merge( membershipFees );

        session.flush();
    }

    /**
     * add new Membership Fees Record
     * 
     * @param membershipFees
     */
    public void addMembershipFees( MembershipFees membershipFees, String requester ) {
        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.save( membershipFees );

        session.flush();
    }

    @SuppressWarnings( "unchecked" )
    public List< MembershipFees > searchMembershipFees( String rankType, String rank, Date date ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select m from MembershipFees m where 1=1 " );
        if ( rankType != null ) {
            queryBuilder.append( "and m.rankType = :rankType " );
        }
        if ( rank != null ) {
            queryBuilder.append( "and m.rank = :rank " );
        }
        if ( date != null ) {
            queryBuilder.append( "and ( m.effectiveDate <= :date ) and ( m.obsoleteDate >= :date or m.obsoleteDate is null ) " );
        }
        Query query = session.createQuery( queryBuilder.toString() );
        if ( rankType != null ) {
            query.setParameter( "rankType", rankType );
        }
        if ( rank != null ) {
            query.setParameter( "rank", rank );
        }
        if ( date != null ) {
            query.setParameter( "date", date );
        }
        return ( List< MembershipFees > ) query.list();
    }

    /**
     * @return
     * 
     * @Title: searchMembershipExtendsByCriteria
     * @Description: Search Membership Extends By Criteria
     * @param @param currentDate
     * @return void
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< MembershipExtend > searchMembershipExtends( ActivationStatus membershipStatus, String[] leaveTypes, Date date ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( "select distinct p, m from PersonalDetail p, Membership m left join p.leaves l where p.nric = m.nric" );

        // Add Search Criteria
        if ( null != membershipStatus ) {
            queryStr.append( " AND m.membershipStatus = :membershipStatus" );
        }
        if ( null != leaveTypes && leaveTypes.length > 0) {
                queryStr.append( " AND l.leaveType in :leaveTypes" );
        }
        if ( null != date ) {
            queryStr.append( " AND l.startDate <= :currentDate" );
            queryStr.append( " AND l.endDate >= :currentDate" );
        }

        Query query = session.createQuery( queryStr.toString() );

        // Set Search Criteria
        if ( null != membershipStatus ) {
            query.setParameter( "membershipStatus", membershipStatus );
        }
        if ( null != leaveTypes && leaveTypes.length > 0) {
            query.setParameterList( "leaveTypes", leaveTypes );
        }
        if ( null != date ) {
            query.setParameter( "currentDate", DateTimeUtil.getDateDaysAfter( date, 0 ) );
        }

        List< MembershipExtend > membershipExtendList = new ArrayList<>();
        List< Object[] > resultList = query.list();
        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("Query result size: %s", resultList.size()));
        }
        for ( Object[] result : resultList ) {
            MembershipExtend membershipExtend = this.toMembershipExtend( result );
            if ( this.isOnFullMonthLeave( membershipExtend.getPersonalDetail(), leaveTypes, date ) ) {
                membershipExtendList.add( membershipExtend );
            }
        }
        logger.info( "Query result size (on full month leave): " + membershipExtendList.size() );

        // Filter data based on what caller can see.
        membershipExtendList = DataFilter.filterMembershipExtendList( SecurityInfo.createInstance(), membershipExtendList );

        logger.info( "Query result size (security filtered): " + membershipExtendList.size() );

        return membershipExtendList;
    }

    /**
     * 
     * @Title: searchMembersByCritieria2
     * @Description: Search Members By Criteria2
     * @param @param membershipCriteria
     * @param @param hasInsuranceCoverage
     * @param @return
     * @return List<PersonalDetail>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< PersonalDetail > searchMembersByCritieria2( MembershipCriteria membershipCriteria, Boolean hasInsuranceCoverage ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( "SELECT personnel FROM PersonalDetail AS personnel, Membership AS mem " + "WHERE personnel.nric = mem.nric" );

        buildQueryStringByCriteria( queryStr, membershipCriteria );

        queryStr.append( " AND mem.hasInsuranceCoverage = :hasInsuranceCoverage" );

        Query query = session.createQuery( queryStr.toString() );

        setCriteriaParameter( query, membershipCriteria );

        query.setParameter( "hasInsuranceCoverage", hasInsuranceCoverage );

        List< PersonalDetail > result = ( List< PersonalDetail > ) query.list();

        // Filter data base on what user can see.
        result = DataFilter.filterPersonalDetails( SecurityInfo.createInstance(), result );

        return result;
    }

    @SuppressWarnings( "unchecked" )
    public List< StatusChangeRecord > getStatusChangeRecords( String nric, int month, int year ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "from StatusChangeRecord rec where rec.nric = :nric and " + "month(rec.updatedOn) = :month and year(rec.updatedOn) = :year order by rec.updatedOn asc" );

        query.setParameter( "nric", nric );
        query.setParameter( "month", month );
        query.setParameter( "year", year );

        return ( List< StatusChangeRecord > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< StatusChangeRecord > getStatusChangeRecords( int month, int year ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "from StatusChangeRecord rec where " + "month(rec.updatedOn) = :month and year(rec.updatedOn) = :year order by rec.updatedOn asc" );

        query.setParameter( "month", month );
        query.setParameter( "year", year );

        return ( List< StatusChangeRecord > ) query.list();

    }

    public void addStatusChangeRecord( StatusChangeRecord addstatuschangerecord ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.save( addstatuschangerecord );
        session.flush();
    }

    public void updateStatusChangeRecord( StatusChangeRecord updatestatuschangerecord ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.update( updatestatuschangerecord );
        session.flush();
    }

    public void deleteStatusChangeRecord( long id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "delete StatusChangeRecord where id = :id" );
        query.setParameter( "id", id );

        int count = query.executeUpdate();
        session.flush();

        if (( count != 1 ) && ( logger.isLoggable( Level.WARNING ) )){
            logger.warning(String.format("Membership status change record doesn't exists: %s", id));
        }
    }

    @SuppressWarnings( "unchecked" )
    public DiscrepancyRecord getDiscrepancyRecord( String nric, Integer month, Integer year ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "from DiscrepancyRecord rec where " + "rec.nric = :nric and rec.month = :month and rec.year = :year" );

        query.setParameter( "nric", nric );
        query.setParameter( "month", month );
        query.setParameter( "year", year );

        List< DiscrepancyRecord > list = query.list();
        if ( list == null || list.isEmpty() ) {
            return null;
        }
        return list.get( 0 );
    }

    @SuppressWarnings( "unchecked" )
    public List< DiscrepancyRecord > getDiscrepancyRecords( Integer month, Integer year, boolean includeZeroBalance ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = null;

        if ( includeZeroBalance ) {
            query = session.createQuery( "from DiscrepancyRecord rec where " + "rec.month = :month and rec.year = :year" );

        }
        else {
            query = session.createQuery( "from DiscrepancyRecord rec where " + "rec.month = :month and rec.year = :year and balance != 0" );
        }

        query.setParameter( "month", month );
        query.setParameter( "year", year );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< DiscrepancyRecord > getDiscrepancyRecords( String nric, boolean includeZeroBalance ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = null;
        if ( includeZeroBalance ) {
            query = session.createQuery( "from DiscrepancyRecord rec where " + "rec.nric = :nric order by rec.year desc, rec.month desc" );
        }
        else {
            query = session.createQuery( "from DiscrepancyRecord rec where " + "rec.nric = :nric and balance != 0  order by rec.year desc, rec.month desc" );
        }
        query.setParameter( "nric", nric );

        return query.list();
    }

    public void saveDiscrepancyRecord( DiscrepancyRecord discrepancyrecord ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.merge( discrepancyrecord );
        session.flush();
    }

    public int deleteDiscrepancyRecords( String nric, Integer month, Integer year ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "delete DiscrepancyRecord where 1=1 " );
        if ( nric != null ) {
            queryString.append( "and nric = :nric " );
        }
        if ( month != null ) {
            queryString.append( "and month = :month " );
        }
        if ( year != null ) {
            queryString.append( "and year = :year " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( nric != null ) {
            query.setParameter( "nric", nric );
        }
        if ( month != null ) {
            query.setParameter( "month", month );
        }
        if ( year != null ) {
            query.setParameter( "year", year );
        }

        int count = query.executeUpdate();
        session.flush();

        return count;
    }

    public int deletePaymentHistories( Integer month, Integer year, PaymentDataSource source ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "delete PaymentHistory where sourceMonth = :month and sourceYear = :year and source = :source" );
        query.setParameter( "month", month );
        query.setParameter( "year", year );
        query.setParameter( "source", source );

        int count = query.executeUpdate();
        session.flush();

        return count;
    }

    @SuppressWarnings( "unchecked" )
    public List< DiscrepancyRecord > computeNonCurrentMonthDiscrepancyRecordsForReconciliationReport() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryString = new StringBuilder( "select " );
        queryString.append( "dr, ped.name, ped.employment.dateOfAppointment " );
        queryString.append( "from DiscrepancyRecord dr, PersonalDetail ped where " );
        queryString.append( "dr.nric = ped.nric and " );
        queryString.append( "( dr.year != YEAR( CURRENT_DATE() ) or dr.month != MONTH( CURRENT_DATE() ) ) and" );
        queryString.append( "dr.balance != 0" );
        Query query = session.createQuery( queryString.toString() );

        List< Object[] > results = query.list();
        return this.populateTransientFields( results );
    }

    @SuppressWarnings( "unchecked" )
    public ReconciliationReport getLatestReconciliationReport( int year, int month ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "select rr from ReconciliationReport rr where rr.year = :year and rr.month = :month order by rr.revisionTimestamp desc" );
        query.setMaxResults( 1 );
        query.setParameter( "year", year );
        query.setParameter( "month", month );
        List< ReconciliationReport > results = ( List< ReconciliationReport > ) query.list();
        ReconciliationReport result = null;
        if ( results != null && results.size() > 0 ) {
            result = results.get( 0 );
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public ReconciliationReport getReconciliationReport( int year, int month, Date revisionTimestamp ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "select rr from ReconciliationReport rr where rr.year = :year and rr.month = :month and rr.revisionTimestamp = :revisionTimestamp" );
        query.setMaxResults( 1 );
        query.setParameter( "year", year );
        query.setParameter( "month", month );
        query.setParameter( "revisionTimestamp", revisionTimestamp );
        List< ReconciliationReport > results = ( List< ReconciliationReport > ) query.list();
        ReconciliationReport result = null;
        if ( results != null && results.size() > 0 ) {
            result = results.get( 0 );
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public List< Date > getReconciliationReportRevisionTimestamps( int year, int month ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "select rr.revisionTimestamp from ReconciliationReport rr where rr.year = :year and rr.month = :month" );
        query.setParameter( "year", year );
        query.setParameter( "month", month );
        return query.list();
    }

    public void addReconciliationReport( ReconciliationReport report, String requester ) {
        report.setId( null );
        report.setUpdater( requester );
        report.setRevisionTimestamp( new Date() );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.save( report );
        session.flush();
    }

    public void addMembershipPaymentCheckRecord( MembershipPaymentCheckRecord checkRecord ) {
        checkRecord.setId( 0L );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.save( checkRecord );
    }

    @SuppressWarnings( "unchecked" )
    protected Map< String, String > getRankRankTypeMapping( Session session ) {
        Map< String, String > rankRankTypeMap = new HashMap<>();

        Query query = session.createQuery( "select distinct mf.rank, mf.rankType from MembershipFees mf" );
        List< Object[] > results = query.list();
        for ( Object[] result : results ) {
            rankRankTypeMap.put( result[ 0 ].toString(), result[ 1 ].toString() );
        }

        return rankRankTypeMap;
    }

    protected List< DiscrepancyRecord > populateTransientFields( List< Object[] > results ) {
        List< DiscrepancyRecord > discrepancyRecords = null;
        if ( results != null ) {
            discrepancyRecords = new ArrayList<>( results.size() );
            for ( Object[] result : results ) {
                DiscrepancyRecord discrepancyRecord = ( DiscrepancyRecord ) result[ 0 ];
                discrepancyRecord.setName( ( String ) result[ 1 ] );
                discrepancyRecord.setAppointmentDate( ( Date ) result[ 2 ] );
                List< PaymentHistory > paymentHistories = discrepancyRecord.getPaymentHistories();
                double amountCollected = 0d;
                double offset = 0d;
                if ( paymentHistories != null ) {
                    for ( PaymentHistory paymentHistory : paymentHistories ) {
                        switch ( paymentHistory.getSource() ) {
                            case MANUAL:
                                offset += ( paymentHistory.getAmount() == null ) ? 0d : paymentHistory.getAmount();
                                break;
                            case CPO:
                                amountCollected += ( paymentHistory.getAmount() == null ) ? 0d : paymentHistory.getAmount();
                                break;
                            case OTHERS:
                                amountCollected += ( paymentHistory.getAmount() == null ) ? 0d : paymentHistory.getAmount();
                                break;
                            default:
                                break;
                        }
                    }
                }
                discrepancyRecord.setAmountCollected( amountCollected );
                discrepancyRecord.setOffset( offset );
                discrepancyRecords.add( discrepancyRecord );
            }
        }
        return discrepancyRecords;
    }

    protected MembershipExtend toMembershipExtend( Object[] personalDetailMembership ) {
        MembershipExtend membershipExtend = new MembershipExtend();
        membershipExtend.setPersonalDetail( ( PersonalDetail ) personalDetailMembership[ 0 ] );
        membershipExtend.setMembership( ( Membership ) personalDetailMembership[ 1 ] );
        return membershipExtend;
    }

    protected boolean isOnFullMonthLeave( PersonalDetail personalDetail, String[] leaveTypes, Date currentDate ) {
        boolean isOnFullMonthLeave = false;
        if ( personalDetail != null && leaveTypes != null && currentDate != null) {
            isOnFullMonthLeave = true;
            List< Date > fullMonthDates = DateTimeUtil.getFullMonthDates( currentDate );
            for ( Date d : fullMonthDates ) {
                if ( !this.isOnLeave( personalDetail, leaveTypes, d ) ) {
                    isOnFullMonthLeave = false;
                    break;
                }
            }
        }
        return isOnFullMonthLeave;
    }

    protected boolean isOnLeave( PersonalDetail personalDetail, String[] leaveTypes, Date leaveDateWithoutTime ) {
        boolean isOnLeave = false;
        if ( personalDetail != null && leaveTypes != null && leaveDateWithoutTime != null) {
            List< Leave > leaves = personalDetail.getLeaves();
            if ( leaves != null ) {
                for ( Leave leave : leaves ) {
                    if ( leave.getLeaveType() != null && ArrayUtils.contains( leaveTypes, leave.getLeaveType() ) && leave.getStartDate() != null
                    && leave.getStartDate().getTime() <= leaveDateWithoutTime.getTime() && leave.getEndDate() != null && leave.getEndDate().getTime() >= leaveDateWithoutTime.getTime()) {
                        isOnLeave = true;
                        break;
                    }
                }
            }
        }
        return isOnLeave;
    }
}
