package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.dao.dac.DataFilter;
import com.stee.spfcore.model.personnel.ChangeRecord;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.model.personnel.HRChangeRecord;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.model.personnel.LeaveType;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.utils.DateTimeUtil;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.personnel.PersonalDepartmentWorkEmail;
import com.stee.spfcore.vo.personnel.PersonalOnContinuousLeaveStatus;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;
import com.stee.spfcore.vo.personnel.PersonnelCriteria;
import com.stee.spfcore.vo.personnel.SearchPersonnelResult;
import com.stee.spfcore.vo.personnel.SearchPersonnelResults;
import com.stee.spfcore.vo.personnel.PersonalNricName;

public class PersonnelDAO {

    private static final Logger logger = Logger.getLogger( PersonnelDAO.class.getName() );

    public PersonalDetail getPersonal( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        return ( PersonalDetail ) session.get( PersonalDetail.class, nric, new LockOptions( LockMode.PESSIMISTIC_READ ) );
    }

    public Employment getEmployment( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        return ( Employment ) session.get( Employment.class, nric );
    }

    public void addPersonal( PersonalDetail personal, String requester ) throws AccessDeniedException {
        logger.info( "start PersonnelDAO.addPersonal()" );
        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        personal.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), personal ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + personal.getNric() );
        }

        session.save( personal );
        session.flush();
        logger.info( "end PersonnelDAO.addPersonal()" );
    }

    public void updatePersonal( PersonalDetail personal, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        personal.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), personal ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + personal.getNric() );
        }

        session.get( PersonalDetail.class, personal.getNric(), new LockOptions( LockMode.PESSIMISTIC_WRITE ) );

        session.merge( personal );
        session.flush();
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalDetail > searchPersonnel( String name, String nric, String orgOrDeptCode ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( PersonalDetail.class );

        if ( name != null && !name.isEmpty() ) {
            criteria.add( Restrictions.like( "name", name ) );
        }

        if ( nric != null && !nric.isEmpty() ) {
            criteria.add( Restrictions.like( "nric", nric ) );
        }

        if ( orgOrDeptCode != null && !orgOrDeptCode.isEmpty() ) {
            criteria.createAlias( "employment", "emp" );
            criteria.add( Restrictions.eq( "emp.organisationOrDepartment", orgOrDeptCode ) );
        }

        List< PersonalDetail > result = ( List< PersonalDetail > ) criteria.list();

        // Filter data based on what caller can see. 
        result = DataFilter.filterPersonalDetails( SecurityInfo.createInstance(), result );

        return result;
    }

    @SuppressWarnings( "unchecked" )
    public SearchPersonnelResults searchPersonnel( String name, String nric, String department, String uwoDepartment, String uwoSubunit, int maxResults ) {
        SearchPersonnelResults results = new SearchPersonnelResults();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select new com.stee.spfcore.vo.personnel.SearchPersonnelResult( p.nric, p.name, p.employment.organisationOrDepartment, p.employment.subunit ) from PersonalDetail p where 1=1 " );
        if ( name != null ) {
            queryString.append( "and p.name like :name " );
        }
        if ( nric != null ) {
            queryString.append( "and p.nric like :nric " );
        }
        if ( department != null ) {
            queryString.append( "and p.employment.organisationOrDepartment = :department " );
        }
        if ( uwoDepartment != null ) {
            queryString.append( "and p.employment.organisationOrDepartment = :uwoDepartment " );
        }
        if ( uwoSubunit != null ) {
            queryString.append( "and p.employment.subunit = :uwoSubunit " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( name != null ) {
            query.setParameter( "name", "%" + name + "%" );
        }
        if ( nric != null ) {
            query.setParameter( "nric", "%" + nric + "%" );
        }
        if ( department != null ) {
            query.setParameter( "department", department );
        }
        if ( uwoDepartment != null ) {
            query.setParameter( "uwoDepartment", uwoDepartment );
        }
        if ( uwoSubunit != null ) {
            query.setParameter( "uwoSubunit", uwoSubunit );
        }
        List< SearchPersonnelResult > resultList = null;
        String message = "Unable to find any record(s). Please refine your search criteria(s).";
        if ( maxResults > 0 ) {
            query.setMaxResults( maxResults );
            resultList = ( List< SearchPersonnelResult > ) query.list();
            if ( resultList != null ) {
                if ( resultList.size() >= maxResults ) {
                    message = String.format( "Too many results. Only first %s results are displayed below. Please refine search criteria.", maxResults );
                }
                else if ( resultList.size() > 0 ) {
                    message = String.format( "Search returned %s result(s).", resultList.size() );
                }
            }
        }
        else {
            resultList = ( List< SearchPersonnelResult > ) query.list();
            if ( resultList != null && resultList.size() > 0) {
                message = String.format( "Search returned %s result(s).", resultList.size() );
            }
        }
        results.setResultList( resultList );
        results.setMessage( message );
        return results;
    }

    public ChangeRecord getChangeRecord( String nric ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( ChangeRecord ) session.get( ChangeRecord.class, nric );
    }

    public void addChangeRecord( ChangeRecord addchangerecord ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        addchangerecord.preSave();

        session.save( addchangerecord );
        session.flush();
    }

    public void updateChangeRecord( ChangeRecord updatechangerecord ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        updatechangerecord.preSave();

        session.merge( updatechangerecord );
        session.flush();
    }

    public void saveHRChangeRecord( HRChangeRecord savehrchangerecord ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( savehrchangerecord );

        session.flush();
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getHRUpdatedPersonnel( Date date ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "SELECT nric FROM HRChangeRecord where changeDate = :changeDate" );
        query.setDate( "changeDate", date );

        return query.list();
    }

    /**
     * @throws AccessDeniedException
     * 
     * @Title: searchPersonalLeavesByCriteria
     * @Description: Search Personal Leaves By Criteria
     * @param @param personnelCriteria
     * @param @return
     * @return List<Leave>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< Leave > searchPersonalLeavesByCriteria( PersonnelCriteria personnelCriteria ) throws AccessDeniedException {

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), personnelCriteria.getQueryNRIC() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + personnelCriteria.getQueryNRIC() );
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( "SELECT leave FROM PersonalDetail AS personnal left join personnal.leaves leave " + "WHERE personnal.nric = :nric and (leave.startDate <= :startDate and leave.endDate >= :endDate)" );

        Query query = session.createQuery( queryStr.toString() );

        // Set Search Criteria
        query.setParameter( "nric", personnelCriteria.getQueryNRIC() );
        query.setParameter( "startDate", personnelCriteria.getQueryEndDate() );
        query.setParameter( "endDate", personnelCriteria.getQueryStartDate() );

        // Execute Query
        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Object[] > getActivePersonnelForHivScreeningDueOfficerList( List< String > departments, List< String > serviceTypes ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select p.nric, p.name, p.dateOfBirth, p.employment.organisationOrDepartment, p.employment.serviceType " );
        queryString.append( "from PersonalDetail p " );
        queryString.append( "where p.employment.employmentStatus = '3' " );
        if ( departments != null && !departments.isEmpty() ) {
            queryString.append( "and p.employment.organisationOrDepartment in :depts " );
        }
        if ( serviceTypes != null && !serviceTypes.isEmpty() ) {
            queryString.append( "and p.employment.serviceType in :serviceTypes " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( departments != null && !departments.isEmpty() ) {
            query.setParameterList( "depts", departments );
        }
        if ( serviceTypes != null && !serviceTypes.isEmpty() ) {
            query.setParameterList( "serviceTypes", serviceTypes );
        }
        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Integer > getActivePersonnelCountsGroupByDepartment( List< String > departments, List< String > serviceTypes ) {
        List< Integer > counts = new ArrayList<>();
        Map< String, Integer > tempCountMap = new HashMap<>();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select p.employment.organisationOrDepartment, count( p ) " );
        queryString.append( "from PersonalDetail p " );
        queryString.append( "where p.employment.employmentStatus = '3' " );
        if ( departments != null && !departments.isEmpty() ) {
                queryString.append( "and p.employment.organisationOrDepartment in :depts " );
        }
        if ( serviceTypes != null && !serviceTypes.isEmpty() ) {
                queryString.append( "and p.employment.serviceType in :serviceTypes " );
        }
        queryString.append( "group by p.employment.organisationOrDepartment" );
        Query query = session.createQuery( queryString.toString() );
        if ( departments != null && !departments.isEmpty() ) {
            query.setParameterList( "depts", departments );
        }
        if ( serviceTypes != null && !serviceTypes.isEmpty() ) {
            query.setParameterList( "serviceTypes", serviceTypes );
        }
        List< Object[] > results = query.list();
        if ( results != null ) {
            for ( Object[] result : results ) {
                String dept = null;
                Integer count = null;
                if ( result[ 0 ] instanceof String ) {
                    dept = ( String ) result[ 0 ];
                }
                if ( result[ 1 ] != null ) {
                    if ( result[ 1 ] instanceof Long ) {
                        count = ( ( Long ) result[ 1 ] ).intValue();
                    }
                    else if ( result[ 1 ] instanceof Integer ) {
                        count = ( Integer ) result[ 1 ];
                    }
                }
                if ( dept != null && count != null) {
                    tempCountMap.put( dept, count );
                    logger.info( String.format( "dept=%s, count=%s", Util.replaceNewLine( dept ), count ) );
                }
            }
        }
        if ( departments != null ) {
            for ( String department : departments ) {
                Integer count = tempCountMap.get( department );
                if ( count == null ) {
                    count = 0;
                }
                counts.add( count );
                logger.info( String.format( "dept=%s, count=%s", Util.replaceNewLine( department ), count ) );
            }
        }
        return counts;
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalPreferredContacts > getActivePcwfMemberPreferredContacts() {
        List< PersonalPreferredContacts > personalPreferredContacts = new ArrayList<>();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "SELECT p FROM PersonalDetail p, Membership m WHERE p.nric = m.nric and p.employment.employmentStatus='3' and m.membershipStatus='ACTIVE'" );
        Query query = session.createQuery( queryString.toString() );
        List< PersonalDetail > results = ( List< PersonalDetail > ) query.list();
        if ( results != null ) {
            for ( PersonalDetail result : results ) {
                personalPreferredContacts.add( new PersonalPreferredContacts( result ) );
            }
        }
        logger.info( String.format( "preferred contacts count=%s", personalPreferredContacts.size() ) );
        return personalPreferredContacts;
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalPreferredContacts > getActivePersonnelPreferredContacts( List< String > departments, List< String > serviceTypes, List< String > nrics ) {
        List< PersonalPreferredContacts > personalPreferredContacts = new ArrayList<>();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "SELECT p FROM PersonalDetail p WHERE p.employment.employmentStatus='3' " );
        if ( departments != null && !departments.isEmpty()) {
            queryString.append( "and p.employment.organisationOrDepartment in :depts " );
        }
        if ( serviceTypes != null && !serviceTypes.isEmpty()) {
            queryString.append( "and p.employment.serviceType in :serviceTypes " );
        }
        if ( nrics != null && !nrics.isEmpty()) {
            queryString.append( "and p.nric in :nrics " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( departments != null && !departments.isEmpty()) {
            query.setParameterList( "depts", departments );
        }
        if ( serviceTypes != null && !serviceTypes.isEmpty()) {
            query.setParameterList( "serviceTypes", serviceTypes );
        }
        if (nrics != null && !nrics.isEmpty()) {
            query.setParameterList("nrics", nrics);
        }
        List< PersonalDetail > results = ( List< PersonalDetail > ) query.list();
        if ( results != null ) {
            for ( PersonalDetail result : results ) {
                personalPreferredContacts.add( new PersonalPreferredContacts( result ) );
            }
        }
        logger.info( String.format( "preferred contacts count=%s", personalPreferredContacts.size() ) );
        return personalPreferredContacts;
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalPreferredContacts > getPersonnelPreferredContacts( List< String > nrics ) {
        List< PersonalPreferredContacts > personalPreferredContacts = new ArrayList<>();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "SELECT p FROM PersonalDetail p WHERE 1=1 " );
        if ( nrics != null && !nrics.isEmpty() ) {
            queryString.append( "and p.nric in :nrics " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( nrics != null && !nrics.isEmpty() ) {
            query.setParameterList( "nrics", nrics );
        }
        List< PersonalDetail > results = ( List< PersonalDetail > ) query.list();
        if ( results != null ) {
            for ( PersonalDetail result : results ) {
                personalPreferredContacts.add( new PersonalPreferredContacts( result ) );
            }
        }
        logger.info( String.format( "preferred contacts count=%s", personalPreferredContacts.size() ) );
        return personalPreferredContacts;
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalDepartmentWorkEmail > getPersonnelOfficeEmails( List< String > nrics ) {
        List< PersonalDepartmentWorkEmail > deptWorkEmails = new ArrayList<>();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "SELECT p FROM PersonalDetail p WHERE 1=1 " );
        if ( nrics != null && !nrics.isEmpty() ) {
            queryString.append( "and p.nric in :nrics " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( nrics != null && !nrics.isEmpty() ) {
            query.setParameterList( "nrics", nrics );
        }
        List< PersonalDetail > results = ( List< PersonalDetail > ) query.list();
        if ( results != null ) {
            for ( PersonalDetail result : results ) {
                deptWorkEmails.add( new PersonalDepartmentWorkEmail( result ) );
            }
        }
        logger.info( String.format( "dept work emails count=%s", deptWorkEmails.size() ) );
        return deptWorkEmails;
    }

    public String getPersonalName( String nric ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "SELECT p.name FROM PersonalDetail p where p.nric = :nric" );
        query.setParameter( "nric", nric );

        return ( String ) query.uniqueResult();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< PersonalNricName > getPersonalName( List<String> nrics ) {

        List< PersonalNricName > personnelNricName = new ArrayList<>();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "SELECT p FROM PersonalDetail p where p.nric in :nrics" );
        query.setParameterList( "nrics", nrics );

        List< PersonalDetail > results = ( List< PersonalDetail > ) query.list();
        if ( results != null ) {
            for ( PersonalDetail result : results ) {
                personnelNricName.add( new PersonalNricName( result.getNric(), result.getName() ) );
            }
        }
        logger.info( String.format( "personnelNricName count=%s", personnelNricName.size() ) );
        return personnelNricName;
    }

    @SuppressWarnings( "unchecked" )
    public List< PersonalOnContinuousLeaveStatus > getOnContinuousLeaveStatuses( List< String > nrics, Date startDate, int numberOfDays ) {
        List< PersonalOnContinuousLeaveStatus > onContinuousLeaveStatuses = new ArrayList<>();

        List< Date > leaveDates = new ArrayList<>();
        if ( startDate != null ) {
            for ( int i = 0; i < numberOfDays; i++ ) {
                leaveDates.add( DateTimeUtil.getDateDaysAfter( startDate, i ) );
            }
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select p from PersonalDetail p where 1=1 " );
        if ( nrics != null ) {
            queryString.append( "and p.nric in :nrics " );
        }
        Query query = session.createQuery( queryString.toString() );
        if ( nrics != null ) {
            query.setParameterList( "nrics", nrics );
        }

        List< PersonalDetail > personalDetails = ( List< PersonalDetail > ) query.list();
        if ( personalDetails != null ) {
            for ( PersonalDetail personalDetail : personalDetails ) {
                String nric = personalDetail.getNric();
                boolean onContinuousLeaveStatus = this.isOnLeaveAllDates( personalDetail.getLeaves(), leaveDates );
                onContinuousLeaveStatuses.add( new PersonalOnContinuousLeaveStatus( nric, onContinuousLeaveStatus ) );
            }
        }

        return onContinuousLeaveStatuses;
    }

    public boolean isOnFullMonthNoPayLeave( String nric, Date date ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        PersonalDetail detail = ( PersonalDetail ) session.get( PersonalDetail.class, nric );
        return this.isOnFullMonthLeave( detail, LeaveType.NO_PAY_LEAVE_CODE_IDS, date );
    }

    private boolean isOnLeaveAllDates( List< Leave > leaves, List< Date > dates ) {
        boolean isOnLeaveAllDates = false;
        if ( dates != null && !dates.isEmpty()) {
            isOnLeaveAllDates = true;
            for ( Date date : dates ) {
                if ( !this.isOnLeave( leaves, date ) ) {
                    isOnLeaveAllDates = false;
                    break;
                }
            }
        }
        return isOnLeaveAllDates;
    }

    private boolean isOnLeave( List< Leave > leaves, Date date ) {
        boolean isOnLeave = false;
        if ( leaves != null ) {
            date = DateTimeUtil.getDateDaysAfter( date, 0 );
            if ( date != null ) {
                for ( Leave leave : leaves ) {
                    Date startDate = DateTimeUtil.getDateDaysAfter( leave.getStartDate(), 0 );
                    Date endDate = DateTimeUtil.getDateDaysAfter( leave.getEndDate(), 0 );
                    if ( startDate != null && endDate != null && startDate.getTime() <= date.getTime() && date.getTime() <= endDate.getTime()) {
                        isOnLeave = true;
                        break;
                    }
                }
            }
        }
        return isOnLeave;
    }

    private boolean isOnFullMonthLeave( PersonalDetail personalDetail, String[] leaveTypes, Date currentDate ) {
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

    private boolean isOnLeave( PersonalDetail personalDetail, String[] leaveTypes, Date leaveDateWithoutTime ) {
        boolean isOnLeave = false;
        if ( personalDetail != null && leaveTypes != null && leaveDateWithoutTime != null) {
            List< Leave > leaves = personalDetail.getLeaves();
            if ( leaves != null ) {
                for ( Leave leave : leaves ) {
                    if ( leave.getLeaveType() != null &&  ArrayUtils.contains( leaveTypes, leave.getLeaveType() ) && leave.getStartDate() != null
                    && leave.getStartDate().getTime() <= leaveDateWithoutTime.getTime() && leave.getEndDate() != null && leave.getEndDate().getTime() >= leaveDateWithoutTime.getTime()) {
                        isOnLeave = true;
                        break;
                    }
                }
            }
        }
        return isOnLeave;
    }

    public void saveExtraEmploymentInfo( ExtraEmploymentInfo info ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( info );

        session.flush();
    }

    public ExtraEmploymentInfo getExtraEmploymentInfo( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        return ( ExtraEmploymentInfo ) session.get( ExtraEmploymentInfo.class, nric );
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getOfficeEmailAddress( List< String > nrics ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select email.address from PersonalDetail as personal " );
        builder.append( "inner join personal.emailContacts as email where " );
        builder.append( "personal.nric in (:nrics) and email.label = :emailType" );

        Query query = session.createQuery( builder.toString() );
        query.setParameterList( "nrics", nrics );
        query.setParameter( "emailType", ContactLabel.WORK );

        return query.list();
    }
}
