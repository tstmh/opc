package com.stee.spfcore.dao;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.TypeLocatorImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TypeResolver;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.dao.dac.DataFilter;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.benefits.BenefitsProcess;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.GrantBudget;
import com.stee.spfcore.model.benefits.HealthCareProvider;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.benefits.WeightMgmtSubsidy;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.service.benefits.BenefitsServiceException;
import com.stee.spfcore.vo.benefits.BenefitsCriteria;
import com.stee.spfcore.vo.benefits.BereavementGrantDetails;
import com.stee.spfcore.vo.benefits.DashboardStatus;
import com.stee.spfcore.vo.benefits.GrantBudgetCriteria;
import com.stee.spfcore.vo.benefits.NewBornGiftDetails;
import com.stee.spfcore.vo.benefits.WeddingGiftDetails;

public class BenefitsDAO {

    private static final Logger logger = Logger.getLogger( BenefitsDAO.class.getName() );

    private static final String NEW_BORN_GRANT_TYPE = "NewBorn";
    private static final String NEW_BORN_GRANT_SUB_TYPE = "NewBorn";

    private static final String WEDDING_GRANT_TYPE = "Wedding";
    private static final String WEDDING_GRANT_SUB_TYPE = "Wedding";

    private static final String BEREAVEMENT_GRANT_TYPE = "Bereavement";

    private static final String SQL_NEW_BORN_BY_APPROVALS = "SELECT DISTINCT new_born_app.REFERENCE_NUMBER AS referenceNumber, " + "new_born_app.APPLICATION_STATUS AS applicationStatus, new_born_app.SUBMISSION_DATE AS dateOfSubmission, most_recent_app_record.DATE_OF_COMPLETION AS dateOfTaskCompletion, "
            + "new_born_app.CHILD_NAME AS childName, new_born_app.CHILD_ID_TYPE AS childIdType, new_born_app.BIRTH_CERTIFICATE_NO AS birthCertificateNo, " + "new_born_app.CHILD_DATE_OF_BIRTH AS childDateOfBirth, " + "new_born_app.SUBMITTED_BY AS submittedBy, "
            + "personal.NRIC AS memberNric, personal.NAME AS memberName, " + "budget.GRANT_AMOUNT AS amountToBePaid FROM " + "SPFCORE.NEWBORN_GIFT_APPLICATION AS new_born_app JOIN (" + "SELECT * FROM ("
            + "	 SELECT app.*, row_number() OVER (" + "PARTITION BY app.NEW_BORN_REFERENCE_NUMBER " + "ORDER BY app.DATE_OF_COMPLETION DESC " + ") AS row_num " + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records "
            + "WHERE ordered_app_records.row_num = 1" + ") AS most_recent_app_record " + "ON (new_born_app.REFERENCE_NUMBER = most_recent_app_record.NEW_BORN_REFERENCE_NUMBER ";

    private static final String SQL_NEW_BORN_BY_APPROVALS_SUFFIX = ")" + " LEFT JOIN SPFCORE.APPLICATION_GRANT_BUDGET AS budget" + " ON (new_born_app.CHILD_DATE_OF_BIRTH BETWEEN budget.EFFECTIVE_DATE AND budget.OBSOLETE_DATE"
            + " AND budget.GRANT_TYPE = :grantType AND budget.GRANT_SUB_TYPE = :grantSubType)" + " , SPFCORE.PERSONAL_DETAILS AS personal" + " WHERE new_born_app.MEMBER_NRIC = personal.NRIC" + " ORDER BY new_born_app.REFERENCE_NUMBER";

    private static final String SQL_NEW_BORN_TOTAL_AMOUNT_PAID = "SELECT SUM(new_born_app.AMT_PAID) AS totalNewBornGiftGrantPaid FROM " + "SPFCORE.NEWBORN_GIFT_APPLICATION AS new_born_app JOIN (" + "SELECT * FROM ("
            + "	 SELECT app.*, row_number() OVER (" + "PARTITION BY app.NEW_BORN_REFERENCE_NUMBER " + "ORDER BY app.DATE_OF_COMPLETION DESC " + ") AS row_num " + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records "
            + "WHERE ordered_app_records.row_num = 1" + ") AS most_recent_app_record " + "ON (new_born_app.REFERENCE_NUMBER = most_recent_app_record.NEW_BORN_REFERENCE_NUMBER ";

    private static final String SQL_WEDDING_BY_APPROVALS = "SELECT DISTINCT wedding.REFERENCE_NUMBER AS referenceNumber, " + "wedding.APPLICATION_STATUS AS applicationStatus, wedding.SUBMISSION_DATE AS dateOfSubmission, most_recent_app_record.DATE_OF_COMPLETION AS dateOfTaskCompletion,"
            + "wedding.SPOUSE_NAME AS spouseName, wedding.DATE_OF_MARRIAGE AS dateOfMarriage, " + "wedding.CERTIFICATE_NO AS certificateNo, " + "wedding.SUBMITTED_BY AS submittedBy, "
            + "personal.NRIC AS memberNric, personal.NAME AS memberName, " + "budget.GRANT_AMOUNT AS amountToBePaid FROM " + "SPFCORE.WEDDING_GIFT_APPLICATION AS wedding JOIN (" + "SELECT * FROM (" + "	 SELECT app.*, row_number() OVER ("
            + "PARTITION BY app.WEDDING_REFERENCE_NUMBER " + "ORDER BY app.DATE_OF_COMPLETION DESC " + ") as row_num " + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records " + "WHERE ordered_app_records.row_num = 1"
            + ") AS most_recent_app_record " + "ON (wedding.REFERENCE_NUMBER = most_recent_app_record.WEDDING_REFERENCE_NUMBER ";

    private static final String SQL_WEDDING_BY_APPROVALS_SUFFIX = ")" + " LEFT JOIN SPFCORE.APPLICATION_GRANT_BUDGET as budget" + " ON (wedding.DATE_OF_MARRIAGE BETWEEN budget.EFFECTIVE_DATE AND budget.OBSOLETE_DATE"
            + " AND budget.GRANT_TYPE = :grantType AND budget.GRANT_SUB_TYPE = :grantSubType)" + " , SPFCORE.PERSONAL_DETAILS AS personal" + " where wedding.MEMBER_NRIC = personal.NRIC" + " ORDER BY wedding.REFERENCE_NUMBER";

    private static final String SQL_WEDDING_TOTAL_AMOUNT_PAID = "SELECT SUM(wedding.AMT_PAID) AS totalWeddingGiftGrantPaid FROM " + "SPFCORE.WEDDING_GIFT_APPLICATION AS wedding JOIN (" + "SELECT * FROM ("
            + "	 SELECT app.*, row_number() OVER (" + "PARTITION BY app.WEDDING_REFERENCE_NUMBER " + "ORDER BY app.DATE_OF_COMPLETION DESC " + ") as row_num " + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records "
            + "WHERE ordered_app_records.row_num = 1" + ") AS most_recent_app_record " + "ON (wedding.REFERENCE_NUMBER = most_recent_app_record.WEDDING_REFERENCE_NUMBER ";

    private static final String SQL_BEREAVEMENT_BY_APPROVALS = "SELECT DISTINCT brv.REFERENCE_NUMBER AS referenceNumber, " + "brv.APPLICATION_STATUS AS applicationStatus, brv.SUBMISSION_DATE AS dateOfSubmission, most_recent_app_record.DATE_OF_COMPLETION AS dateOfTaskCompletion, "
            + "brv.DECEASED_NAME AS nameOfDeceased, brv.DECEASE_DATE AS deceaseDate, " + "brv.DEATH_CERTIFICATE_NO AS deathCertificateNo, " + "brv.SUBMITTED_BY AS submittedBy, brv.DECEASED_RELATION AS relationship, "
            + "personal.NRIC AS memberNric, personal.NAME AS memberName, " + "budget.GRANT_AMOUNT AS amountToBePaid FROM " + "SPFCORE.BEREAVEMENT_GRANT_APPLICATION AS brv JOIN (" + "SELECT * FROM (" + "	 SELECT app.*, row_number() OVER ("
            + "PARTITION BY app.BEREAVEMENT_REFERENCE_NUMBER " + "ORDER BY app.DATE_OF_COMPLETION DESC " + ") as row_num " + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records "
            + "WHERE ordered_app_records.row_num = 1" + ") AS most_recent_app_record " + "ON (brv.REFERENCE_NUMBER = most_recent_app_record.BEREAVEMENT_REFERENCE_NUMBER ";

    private static final String SQL_BEREAVMENT_BY_APPROVALS_SUFFIX = ")" + " LEFT JOIN SPFCORE.APPLICATION_GRANT_BUDGET as budget" + " ON (brv.DECEASE_DATE BETWEEN budget.EFFECTIVE_DATE AND budget.OBSOLETE_DATE"
            + " AND budget.GRANT_TYPE = :grantType AND budget.GRANT_SUB_TYPE = brv.DECEASED_RELATION)" + " , SPFCORE.PERSONAL_DETAILS AS personal" + " where brv.MEMBER_NRIC = personal.NRIC" + " ORDER BY brv.REFERENCE_NUMBER";

    private static final String SQL_BEREAVEMENT_TOTAL_AMOUNT_PAID = "SELECT SUM(brv.AMT_PAID) AS totalBereavementGrantPaid" + " FROM SPFCORE.BEREAVEMENT_GRANT_APPLICATION AS brv " + "JOIN (SELECT * FROM ("
            + "SELECT app.*, row_number() OVER (" + "PARTITION BY app.BEREAVEMENT_REFERENCE_NUMBER	" + "ORDER BY app.DATE_OF_COMPLETION DESC" + ") as row_num FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records	"
            + "WHERE ordered_app_records.row_num = 1" + ") AS most_recent_app_record ON " + "(brv.REFERENCE_NUMBER = most_recent_app_record.BEREAVEMENT_REFERENCE_NUMBER ";

    private static final String SQL_NEWBORN_GIFT_COMMON = "SELECT c.REFERENCE_NUMBER as referenceNumber," + "c.MEMBER_NRIC as memberNric," + "p.NAME as memberName," + "c.APPLICATION_STATUS as applicationStatus,"
            + "c.SUBMISSION_DATE as dateOfSubmission," + "c.SUBMITTED_BY as submittedBy," + "c.AMT_PAID as amountToBePaid," + "c.PAYMENT_DATE as paymentDate " + "FROM SPFCORE.NEWBORN_GIFT_APPLICATION as c "
            + "JOIN SPFCORE.PERSONAL_DETAILS p " + "ON p.NRIC = c.MEMBER_NRIC " + "WHERE 1=1 ";

    private static final String SQL_BEREAVEMENT_GRANT_COMMON = "SELECT c.REFERENCE_NUMBER as referenceNumber," + "c.MEMBER_NRIC as memberNric," + "p.NAME as memberName," + "c.APPLICATION_STATUS as applicationStatus,"
            + "c.SUBMISSION_DATE as dateOfSubmission," + "c.SUBMITTED_BY as submittedBy," + "c.AMT_PAID as amountToBePaid," + "c.PAYMENT_DATE as paymentDate " + "FROM SPFCORE.BEREAVEMENT_GRANT_APPLICATION as c "
            + "JOIN SPFCORE.PERSONAL_DETAILS p " + "ON p.NRIC = c.MEMBER_NRIC " + "WHERE 1=1 ";

    private static final String SQL_WEDDING_GIFT_COMMON = "SELECT c.REFERENCE_NUMBER as referenceNumber," + "c.MEMBER_NRIC as memberNric," + "p.NAME as memberName," + "c.APPLICATION_STATUS as applicationStatus,"
            + "c.SUBMISSION_DATE as dateOfSubmission," + "c.SUBMITTED_BY as submittedBy," + "c.AMT_PAID as amountToBePaid," + "c.PAYMENT_DATE as paymentDate " + "FROM SPFCORE.WEDDING_GIFT_APPLICATION as c "
            + "JOIN SPFCORE.PERSONAL_DETAILS p " + "ON p.NRIC = c.MEMBER_NRIC " + "WHERE 1=1 ";

    @SuppressWarnings( "unchecked" )
    public List< WeddingGift > searchWeddingGift( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Query query = session.createQuery( "SELECT c FROM WeddingGift as c WHERE c.memberNric = :nric" );
        query.setParameter( "nric", nric );

        return ( List< WeddingGift > ) query.list();
    }

    @SuppressWarnings("unchecked")
	public List< WeddingGift > searchWeddingGiftForLoginID( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Query query = session.createQuery( "SELECT c FROM WeddingGift as c WHERE c.submittedBy = :nric" );
        query.setParameter( "nric", nric );
        
        return ( List< WeddingGift > ) query.list();
    }
    /**
     * Get the wedding gift application by reference number
     * 
     * @param referenceNumber
     * @return WeddingGift or null
     * @throws AccessDeniedException
     * @throws BenefitsServiceException
     */
    public WeddingGift getWeddingGift( String referenceNumber ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        WeddingGift gift = ( WeddingGift ) session.get( WeddingGift.class, referenceNumber );

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), gift.getMemberNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + gift.getMemberNric() );
        }

        return gift;
    }

    /**
     * Search new born application by nric.
     * 
     * @param referenceNumber
     * @return NewBorn or null if detail not found.
     * @throws AccessDeniedException
     * @throws BenefitsServiceException
     *             Exception while retrieving new born application.
     */
    @SuppressWarnings( "unchecked" )
    public List< NewBornGift > searchNewBorn( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Query query = session.createQuery( "SELECT c FROM NewBornGift as c WHERE c.memberNric = :nric" );
        query.setParameter( "nric", nric );

        return ( List< NewBornGift > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< NewBornGift > searchNewBornForLoginID( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Query query = session.createQuery( "SELECT c FROM NewBornGift as c WHERE c.submittedBy = :nric" );
        query.setParameter( "nric", nric );

        return ( List< NewBornGift > ) query.list();
    }
    /**
     * Get the new born application by reference number.
     * 
     * @param referenceNumber
     * @return NewBorn or null if detail not found.
     * @throws AccessDeniedException
     * @throws BenefitsServiceException
     *             Exception while retrieving new born application.
     */
    public NewBornGift getNewBorn( String referenceNumber ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        NewBornGift gift = ( NewBornGift ) session.get( NewBornGift.class, referenceNumber );

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), gift.getMemberNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + gift.getMemberNric() );
        }

        return gift;
    }

    /**
     * Search the bereavement grant application by nric.
     * 
     * @param nric
     * @return
     * @throws AccessDeniedException
     * @throws BenefitsServiceException
     */
    @SuppressWarnings( "unchecked" )
    public List< BereavementGrant > searchBereavementGrant( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Query query = session.createQuery( "SELECT c FROM BereavementGrant as c WHERE c.nric = :nric" );
        query.setParameter( "nric", nric );

        return ( List< BereavementGrant > ) query.list();
    }
    
    @SuppressWarnings( "unchecked" )
    public List< BereavementGrant > searchBereavementGrantForLoginID( String nric ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Query query = session.createQuery( "SELECT c FROM BereavementGrant as c WHERE c.submittedBy = :nric" );
        query.setParameter( "nric", nric );

        return ( List< BereavementGrant > ) query.list();
    }
    /**
     * Get the bereavement grant application by reference number.
     * 
     * @param nric
     * @return BereavementGrant or null if detail not found.
     * @throws AccessDeniedException
     * @throws BenefitsServiceException
     *             Exception while retrieving bereavement grant application.
     */
    public BereavementGrant getBereavementGrant( String referenceNumber ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        BereavementGrant bereavementGrant = ( BereavementGrant ) session.get( BereavementGrant.class, referenceNumber );

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), bereavementGrant.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + bereavementGrant.getNric() );
        }

        return bereavementGrant;
    }

    /**
     * Search New Born Gift Applications List by Latest Approval Records
     * criteria.
     * 
     * @param benefitsCriteria
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List< NewBornGiftDetails > searchNewBornByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) {
        StringBuilder queryStr = new StringBuilder();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        queryStr.append( SQL_NEW_BORN_BY_APPROVALS );
        buildQueryStringByCriteria( queryStr, benefitsCriteria );
        queryStr.append( SQL_NEW_BORN_BY_APPROVALS_SUFFIX );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setCriteriaParameter( sqlQuery, benefitsCriteria );
        sqlQuery.setParameter( "grantType", NEW_BORN_GRANT_TYPE );
        sqlQuery.setParameter( "grantSubType", NEW_BORN_GRANT_SUB_TYPE );
        addCommonScalars( sqlQuery );
        sqlQuery.addScalar( "childName" );
        sqlQuery.addScalar( "childDateOfBirth", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "birthCertificateNo" );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( NewBornGiftDetails.class ) );

        List< NewBornGiftDetails > newBornList = ( List< NewBornGiftDetails > ) sqlQuery.list();

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Search New Born Gift Applications by Approvals Criteria --- result Size ---- " + newBornList.size() );
        }

        newBornList = DataFilter.filterNewBornGiftDetails( SecurityInfo.createInstance(), newBornList );

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Filtered Search New Born Gift Applications by Approvals Criteria --- result Size ---- " + newBornList.size() );
        }

        return newBornList;
    }

    /**
     * Search Wedding Gift Applications List by Latest Approval Records
     * criteria.
     * 
     * @param benefitsCriteria
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List< WeddingGiftDetails > searchWeddingByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) {
        StringBuilder queryStr = new StringBuilder();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        queryStr.append( SQL_WEDDING_BY_APPROVALS );
        buildQueryStringByCriteria( queryStr, benefitsCriteria );
        queryStr.append( SQL_WEDDING_BY_APPROVALS_SUFFIX );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setCriteriaParameter( sqlQuery, benefitsCriteria );
        sqlQuery.setParameter( "grantType", WEDDING_GRANT_TYPE );
        sqlQuery.setParameter( "grantSubType", WEDDING_GRANT_SUB_TYPE );
        addCommonScalars( sqlQuery );
        sqlQuery.addScalar( "spouseName" );
        sqlQuery.addScalar( "dateOfMarriage", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "certificateNo" );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( WeddingGiftDetails.class ) );

        List< WeddingGiftDetails > weddingGiftList = ( List< WeddingGiftDetails > ) sqlQuery.list();

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Search Wedding Gift Applications by Approvals Criteria --- result Size ---- " + weddingGiftList.size() );
        }

        weddingGiftList = DataFilter.filterWeddingGiftDetails( SecurityInfo.createInstance(), weddingGiftList );

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Filtered Search Wedding Gift Applications by Approvals Criteria --- result Size ---- " + weddingGiftList.size() );
        }

        return weddingGiftList;
    }

    /**
     * Search Bereavement Grant Applications List by Latest Approval Records
     * criteria.
     * 
     * @param benefitsCriteria
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List< BereavementGrantDetails > searchBereavementGrantByApprovalsCriteria( BenefitsCriteria benefitsCriteria ) {
        StringBuilder queryStr = new StringBuilder();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        queryStr.append( SQL_BEREAVEMENT_BY_APPROVALS );
        buildQueryStringByCriteria( queryStr, benefitsCriteria );
        queryStr.append( SQL_BEREAVMENT_BY_APPROVALS_SUFFIX );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setCriteriaParameter( sqlQuery, benefitsCriteria );
        // Set Grant Type field value for Search.
        sqlQuery.setParameter( "grantType", BEREAVEMENT_GRANT_TYPE );
        addCommonScalars( sqlQuery );
        sqlQuery.addScalar( "nameOfDeceased" );
        sqlQuery.addScalar( "deceaseDate", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "deathCertificateNo" );
        sqlQuery.addScalar( "relationship" );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( BereavementGrantDetails.class ) );

        List< BereavementGrantDetails > bereavementGrantList = ( List< BereavementGrantDetails > ) sqlQuery.list();

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Search Bereavement Grant Applications by Approvals Criteria --- result Size ---- " + bereavementGrantList.size() );
        }

        bereavementGrantList = DataFilter.filterBereavementGrantDetails( SecurityInfo.createInstance(), bereavementGrantList );

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Filtered Search Bereavement Grant Applications by Approvals Criteria --- result Size ---- " + bereavementGrantList.size() );
        }

        return bereavementGrantList;
    }

	public List <DashboardStatus> getDashBoardStatus() {
    	logger.info("Get Dashboard Status !!!");
    	List <DashboardStatus> dashBoardStatusList = new ArrayList<>();
    	
    	for (int i = 0; i < 16; i++) {
    		DashboardStatus dashBoardStatus = new DashboardStatus();
    		dashBoardStatus.setBereavementCount(0);
    		dashBoardStatus.setNewbornCount(0);
    		dashBoardStatus.setWeddingCount(0);
    		List <BereavementGrantDetails> bereavementGrantList;
    		List <NewBornGiftDetails> newbornGiftList;
    		List <WeddingGiftDetails> weddingGiftList;
    		Calendar todayDate = Calendar.getInstance();
    		String officerAction = "";
    		String officerLevel = "";
    		Calendar searchStartDate = Calendar.getInstance();
    		Calendar searchEndDate = Calendar.getInstance();
    		BenefitsCriteria benefitsCriteria;

            switch (i) {
                case 0:
                    // Unprocessed
                    officerAction = "Submitted";
                    officerLevel = "Applicant";
                    logger.info("[Unprocessed] Searching for all unprocessed applications");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Unprocessed");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 1:
                    // Within 0-7 days
                    officerAction = "Submitted";
                    officerLevel = "Applicant";
                    logger.info("[Unprocessed] Searching for all unprocessed (Within 0-7 days) applications");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- Within 0-7 days");
                    for (BereavementGrantDetails bereavementGrantDetails : bereavementGrantList) {
                        int diffInDays = this.getDiffInDays(todayDate, bereavementGrantDetails.getDateOfTaskCompletion());
                        if (diffInDays < 8) {
                            dashBoardStatus.setBereavementCount(dashBoardStatus.getBereavementCount() + 1);
                        }
                    }
                    for (NewBornGiftDetails newBornGiftDetails : newbornGiftList) {
                        int diffInDays = this.getDiffInDays(todayDate, newBornGiftDetails.getDateOfTaskCompletion());
                        if (diffInDays < 8) {
                            dashBoardStatus.setNewbornCount(dashBoardStatus.getNewbornCount() + 1);
                        }
                    }
                    for (WeddingGiftDetails weddingGiftDetails : weddingGiftList) {
                        int diffInDays = this.getDiffInDays(todayDate, weddingGiftDetails.getDateOfTaskCompletion());
                        if (diffInDays < 8) {
                            dashBoardStatus.setWeddingCount(dashBoardStatus.getWeddingCount() + 1);
                        }
                    }
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 2:
                    // Within 8-14 days
                    officerAction = "Submitted";
                    officerLevel = "Applicant";
                    logger.info("[Unprocessed] Searching for all unprocessed (Within 8-14 days) applications");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- Within 8-14 days");
                    for (BereavementGrantDetails bereavementGrantDetails : bereavementGrantList) {
                        int diffInDays = this.getDiffInDays(todayDate, bereavementGrantDetails.getDateOfTaskCompletion());
                        if (diffInDays > 7 && diffInDays < 15) {
                            dashBoardStatus.setBereavementCount(dashBoardStatus.getBereavementCount() + 1);
                        }
                    }
                    for (NewBornGiftDetails newBornGiftDetails : newbornGiftList) {
                        int diffInDays = this.getDiffInDays(todayDate, newBornGiftDetails.getDateOfTaskCompletion());
                        if (diffInDays > 7 && diffInDays < 15) {
                            dashBoardStatus.setNewbornCount(dashBoardStatus.getNewbornCount() + 1);
                        }
                    }
                    for (WeddingGiftDetails weddingGiftDetails : weddingGiftList) {
                        int diffInDays = this.getDiffInDays(todayDate, weddingGiftDetails.getDateOfTaskCompletion());
                        if (diffInDays > 7 && diffInDays < 15) {
                            dashBoardStatus.setWeddingCount(dashBoardStatus.getWeddingCount() + 1);
                        }
                    }
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 3:
                    // Within 15-21 days
                    officerAction = "Submitted";
                    officerLevel = "Applicant";
                    logger.info("[Unprocessed] Searching for all unprocessed (Within 15-21 days) applications");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- Within 15-21 days");
                    for (int k = 0; k < bereavementGrantList.size(); k++) {
                        int diffInDays = this.getDiffInDays(todayDate, bereavementGrantList.get(k).getDateOfTaskCompletion());
                        if (diffInDays > 15 && diffInDays < 22) {
                            dashBoardStatus.setBereavementCount(dashBoardStatus.getBereavementCount() + 1);
                        }
                    }
                    for (int k = 0; k < newbornGiftList.size(); k++) {
                        int diffInDays = this.getDiffInDays(todayDate, newbornGiftList.get(k).getDateOfTaskCompletion());
                        if (diffInDays > 15 && diffInDays < 22) {
                            dashBoardStatus.setNewbornCount(dashBoardStatus.getNewbornCount() + 1);
                        }
                    }
                    for (int k = 0; k < weddingGiftList.size(); k++) {
                        int diffInDays = this.getDiffInDays(todayDate, weddingGiftList.get(k).getDateOfTaskCompletion());
                        if (diffInDays > 15 && diffInDays < 22) {
                            dashBoardStatus.setWeddingCount(dashBoardStatus.getWeddingCount() + 1);
                        }
                    }
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 4:
                    // More than 21 days
                    officerAction = "Submitted";
                    officerLevel = "Applicant";
                    logger.info("[Unprocessed] Searching for all unprocessed (More than 21 days) applications");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- More than 21 days");
                    for (BereavementGrantDetails bereavementGrantDetails : bereavementGrantList) {
                        int diffInDays = this.getDiffInDays(todayDate, bereavementGrantDetails.getDateOfTaskCompletion());
                        if (diffInDays > 22) {
                            dashBoardStatus.setBereavementCount(dashBoardStatus.getBereavementCount() + 1);
                        }
                    }
                    for (NewBornGiftDetails newBornGiftDetails : newbornGiftList) {
                        int diffInDays = this.getDiffInDays(todayDate, newBornGiftDetails.getDateOfTaskCompletion());
                        if (diffInDays > 22) {
                            dashBoardStatus.setNewbornCount(dashBoardStatus.getNewbornCount() + 1);
                        }
                    }
                    for (WeddingGiftDetails weddingGiftDetails : weddingGiftList) {
                        int diffInDays = this.getDiffInDays(todayDate, weddingGiftDetails.getDateOfTaskCompletion());
                        if (diffInDays > 22) {
                            dashBoardStatus.setWeddingCount(dashBoardStatus.getWeddingCount() + 1);
                        }
                    }
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 5:
                    //Supported
                    officerAction = "Supported";
                    officerLevel = null;
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH));
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    logger.info("[Supported] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Supported");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 6:
                    //Manual
                    officerAction = "Supported";
                    officerLevel = "PO";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH));
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    logger.info("[Manual] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- Manual");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 7:
                    //Batch Upload
                    officerAction = "Supported";
                    officerLevel = "-";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH));
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    logger.info("[Batch Upload] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- Batch Upload");
                    for (BereavementGrantDetails bereavementGrantDetails : bereavementGrantList) {
                        if (!("SYSTEM").equals(bereavementGrantDetails.getSubmittedBy().toUpperCase(Locale.ENGLISH))) {
                            dashBoardStatus.setBereavementCount(dashBoardStatus.getBereavementCount() + 1);
                        }
                    }
                    for (WeddingGiftDetails weddingGiftDetails : weddingGiftList) {
                        if (!("SYSTEM").equals(weddingGiftDetails.getSubmittedBy().toUpperCase(Locale.ENGLISH))) {
                            dashBoardStatus.setWeddingCount(dashBoardStatus.getWeddingCount() + 1);
                        }
                    }
                    for (NewBornGiftDetails newBornGiftDetails : newbornGiftList) {
                        if (!("SYSTEM").equals(newBornGiftDetails.getSubmittedBy().toUpperCase(Locale.ENGLISH))) {
                            dashBoardStatus.setNewbornCount(dashBoardStatus.getNewbornCount() + 1);
                        }
                    }
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 8:
                    //HR Interface
                    officerAction = "Supported";
                    officerLevel = "-";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH));
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    logger.info("[HR Interface] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("- HR Interface");
                    for (BereavementGrantDetails bereavementGrantDetails : bereavementGrantList) {
                        if (("SYSTEM").equals(bereavementGrantDetails.getSubmittedBy().toUpperCase(Locale.ENGLISH))) {
                            dashBoardStatus.setBereavementCount(dashBoardStatus.getBereavementCount() + 1);
                        }
                    }
                    for (WeddingGiftDetails weddingGiftDetails : weddingGiftList) {
                        if (("SYSTEM").equals(weddingGiftDetails.getSubmittedBy().toUpperCase(Locale.ENGLISH))) {
                            dashBoardStatus.setWeddingCount(dashBoardStatus.getWeddingCount() + 1);
                        }
                    }
                    for (NewBornGiftDetails newBornGiftDetails : newbornGiftList) {
                        if (("SYSTEM").equals(newBornGiftDetails.getSubmittedBy().toUpperCase(Locale.ENGLISH))) {
                            dashBoardStatus.setNewbornCount(dashBoardStatus.getNewbornCount() + 1);
                        }
                    }
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 9:
                    //Route back by PO to applicant
                    officerAction = "Routed to Applicant";
                    officerLevel = "PO";
                    searchStartDate = Calendar.getInstance();
                    searchStartDate.set(Calendar.DATE, todayDate.get(Calendar.DATE) - 7);
                    searchEndDate = Calendar.getInstance();
                    logger.info("[Route Back to Applicant From PO] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Route Back to Applicant From PO");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 10:
                    //Resubmitted to PO
                    officerAction = "Re-Submitted to Processing Officer";
                    officerLevel = "Applicant";
                    logger.info("[Resubmitted to PO] Searching for all resubmitted applications");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Resubmitted to PO");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 11:
                    //Route Back to Processing Officer
                    officerAction = "Routed to Processing Officer";
                    officerLevel = "AD";
                    logger.info("[Route Back to PO From Welfare AD] Searching for all routed back applications from AD to PO");
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, null, null);
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Route Back to PO From Welfare AD");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 12:
                    //Approve by Welfare AD
                    officerAction = "Approved";
                    officerLevel = "AD";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH) - 1);
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    searchEndDate.set(Calendar.MONTH, searchEndDate.get(Calendar.MONTH) - 1);
                    searchEndDate.set(Calendar.DATE, searchEndDate.getActualMaximum(Calendar.DATE));
                    searchEndDate.set(Calendar.HOUR_OF_DAY, 23);
                    searchEndDate.set(Calendar.MINUTE, 59);
                    searchEndDate.set(Calendar.SECOND, 59);
                    logger.info("[Approved by Welfare AD] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Approved by Welfare AD");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 13:
                    //Rejected by Processing Officer
                    officerAction = "Rejected";
                    officerLevel = "PO";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH));
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    logger.info("[Rejected by PO] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Rejected by PO");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 14:
                    //Rejcted by AD
                    officerAction = "Rejected";
                    officerLevel = "AD";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.MONTH, searchStartDate.get(Calendar.MONTH) - 1);
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    searchEndDate.set(Calendar.MONTH, searchEndDate.get(Calendar.MONTH) - 1);
                    searchEndDate.set(Calendar.DATE, searchEndDate.getActualMaximum(Calendar.DATE));
                    searchEndDate.set(Calendar.HOUR_OF_DAY, 23);
                    searchEndDate.set(Calendar.MINUTE, 59);
                    searchEndDate.set(Calendar.SECOND, 59);
                    logger.info("[Rejected by AD]Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Rejcted by Welfare AD");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                case 15:
                    //Purged by System
                    officerAction = "Rejected";
                    officerLevel = "System";
                    searchStartDate.set(Calendar.DATE, 1);
                    searchStartDate.set(Calendar.HOUR_OF_DAY, 0);
                    searchStartDate.set(Calendar.MINUTE, 0);
                    searchStartDate.set(Calendar.SECOND, 0);
                    searchEndDate = todayDate;
                    logger.info("[Purge by System] Searching for applications between " + searchStartDate.getTime() + " to " + searchEndDate.getTime());
                    benefitsCriteria = getBenefitsCriteria(officerAction, officerLevel, searchStartDate.getTime(), searchEndDate.getTime());
                    bereavementGrantList = searchBereavementGrantByApprovalsCriteria(benefitsCriteria);
                    weddingGiftList = searchWeddingByApprovalsCriteria(benefitsCriteria);
                    newbornGiftList = searchNewBornByApprovalsCriteria(benefitsCriteria);
                    dashBoardStatus.setStatus("Purge by System");
                    dashBoardStatus.setBereavementCount(bereavementGrantList.size());
                    dashBoardStatus.setWeddingCount(weddingGiftList.size());
                    dashBoardStatus.setNewbornCount(newbornGiftList.size());
                    dashBoardStatusList.add(dashBoardStatus);
                    break;
                default:
                    if ( logger.isLoggable( Level.INFO ) ) {
                        logger.info(String.format("Outside of range: %s", i));
                    }
            }
    		
    	}
    	logger.info("End retrieving dashboard count!!!");
    	return dashBoardStatusList;
    }
    
    private BenefitsCriteria getBenefitsCriteria (String officerAction,
    		String officerLevel, Date searchStartDate, Date searchEndDate) {
    	BenefitsCriteria benefitsCriteria = new BenefitsCriteria();
    	benefitsCriteria.setOfficerLevel(officerLevel);
    	benefitsCriteria.setOfficerAction(officerAction);
    	benefitsCriteria.setSearchStartDate(searchStartDate);
    	benefitsCriteria.setSearchEndDate(searchEndDate);
    	
    	return benefitsCriteria;
    }
    

    /**
     * Build SQL query String based on criteria parameters.
     *
     * @param queryStr
     * @param benefitsCriteria
     * @return
     */
    private StringBuilder buildQueryStringByCriteria(StringBuilder queryStr, BenefitsCriteria benefitsCriteria ) {

        if ( benefitsCriteria != null ) {
            // Append Search by Officer Action Clause.
            if ( benefitsCriteria.getOfficerAction() != null && !benefitsCriteria.getOfficerAction().isEmpty() ) {
                queryStr.append( " AND most_recent_app_record.OFFICER_ACTION IN (:officerActionParamList)" );
            }

            // Append Search by Officer Level clause.
            if ( benefitsCriteria.getOfficerLevel() != null && !benefitsCriteria.getOfficerLevel().isEmpty() ) {
                queryStr.append( " AND most_recent_app_record.OFFICER_LEVEL IN (:officerLevelParamList)" );
            }

            // Append Search by Completion Date range clause.
            if ( benefitsCriteria.getSearchStartDate() != null && benefitsCriteria.getSearchEndDate() != null ) {
                queryStr.append( " AND most_recent_app_record.DATE_OF_COMPLETION BETWEEN " + ":searchCompletionStartDate AND :searchCompletionEndDate" );
            }

        }

        return queryStr;
    }

    /**
     * Set Query Parameter Values based on criteria parameters
     * 
     * @param query
     * @param benefitsCriteria
     * @return
     */
    private Query setCriteriaParameter( Query query, BenefitsCriteria benefitsCriteria ) {
        if ( benefitsCriteria != null ) {
            // Set Officer Action Search Param List
            if ( benefitsCriteria.getOfficerAction() != null && !benefitsCriteria.getOfficerAction().isEmpty() ) {
                List< String > officerActionParamList = new ArrayList<>();
                if ( benefitsCriteria.getOfficerAction().contains( "," ) ) {
                    officerActionParamList = Arrays.asList( benefitsCriteria.getOfficerAction().split( "," ) );
                }
                else {
                    officerActionParamList.add( benefitsCriteria.getOfficerAction() );
                }
                query.setParameterList( "officerActionParamList", officerActionParamList );
            }

            // Set Officer Level Search Param List.
            if ( benefitsCriteria.getOfficerLevel() != null && !benefitsCriteria.getOfficerLevel().isEmpty() ) {
                List< String > officerLevelParamList = new ArrayList<>();
                if ( benefitsCriteria.getOfficerLevel().contains( "," ) ) {
                    officerLevelParamList = Arrays.asList( benefitsCriteria.getOfficerLevel().split( "," ) );
                }
                else {
                    officerLevelParamList.add( benefitsCriteria.getOfficerLevel() );
                }
                query.setParameterList( "officerLevelParamList", officerLevelParamList );
            }

            // Set Completion Date Search Params
            if ( benefitsCriteria.getSearchStartDate() != null && benefitsCriteria.getSearchEndDate() != null ) {
                query.setParameter( "searchCompletionStartDate", benefitsCriteria.getSearchStartDate() );
                query.setParameter( "searchCompletionEndDate", benefitsCriteria.getSearchEndDate() );
            }
        }
        return query;
    }

    private Query addCommonScalars( SQLQuery sqlQuery ) {
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "dateOfSubmission", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "dateOfTaskCompletion", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "submittedBy" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );
        sqlQuery.addScalar( "amountToBePaid", StandardBasicTypes.DOUBLE );

        Properties params = new Properties();
        params.put( "enumClass", "com.stee.spfcore.model.ApplicationStatus" );
        params.put( "type", "12" );

        sqlQuery.addScalar( "applicationStatus", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, params ) );
        return sqlQuery;
    }

    /**
     * Get Grant budget by Id
     * 
     * @param id
     * @return
     */
    public GrantBudget getGrantBudget( long id ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( GrantBudget ) session.get( GrantBudget.class, id );
    }

    /**
     * search for Grant Budget Details by Criteria
     * 
     * @param budgetCriteria
     * @return
     */
    public List< GrantBudget > searchGrantBudgetByCriteria( GrantBudgetCriteria budgetCriteria ) {
        StringBuilder queryStr = new StringBuilder();
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        queryStr.append( "SELECT budget FROM GrantBudget AS budget " );
        buildBudgetQueryStringByCriteria( queryStr, budgetCriteria );

        Query query = session.createQuery( queryStr.toString() );
        setCriteriaParameters( query, budgetCriteria );

        @SuppressWarnings( "unchecked" )
        List< GrantBudget > grantBudgetList = ( List< GrantBudget > ) query.list();

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.log( Level.INFO, "Search Grant Budget Records by Type --- result Size ---- " + grantBudgetList.size() );
        }
        return grantBudgetList;
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getBereavementGrantRelationships() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select budget.grantSubType from GrantBudget budget " );
        queryString.append( "where budget.grantType = 'Bereavement' " );
        queryString.append( "and budget.grantSubType != 'Annual' " );
        queryString.append( "and budget.effectiveDate <= current_date() " );
        queryString.append( "and budget.obsoleteDate > current_date() " );
        Query query = session.createQuery( queryString.toString() );
        return query.list();
    }

    /**
     * build query string to fetch list of Grant budget by Criteria parameters.
     * 
     * @param queryStr
     * @param budgetCriteria
     * @return
     */
    private StringBuilder buildBudgetQueryStringByCriteria( StringBuilder queryStr, GrantBudgetCriteria budgetCriteria ) {
        if ( budgetCriteria != null ) {
            boolean isWhereClauseAdded = false;
            if ( budgetCriteria.getGrantType() != null && !budgetCriteria.getGrantType().isEmpty() ) {
                queryStr.append( "WHERE budget.grantType = :grantType" );
                isWhereClauseAdded = true;
            }

            if ( budgetCriteria.getEffectiveDate() != null ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND " );
                }
                else {
                    queryStr.append( " WHERE " );
                    isWhereClauseAdded = true;
                }
                queryStr.append( ":effectiveDate BETWEEN budget.effectiveDate AND budget.obsoleteDate" );
            }

            if ( budgetCriteria.getGrantSubType() != null && !budgetCriteria.getGrantSubType().isEmpty() ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND " );
                }
                else {
                    queryStr.append( " WHERE " );
                }
                queryStr.append( "budget.grantSubType = :grantSubType" );
            }
        }
        return queryStr;
    }

    /**
     * Set Criteria parameters in query to fetch Grant Budget by Criteria.
     * 
     * @param query
     * @param budgetCriteria
     * @return
     */
    private Query setCriteriaParameters( Query query, GrantBudgetCriteria budgetCriteria ) {
        if ( budgetCriteria != null ) {
            if ( budgetCriteria.getGrantType() != null && !budgetCriteria.getGrantType().isEmpty() ) {
                query.setParameter( "grantType", budgetCriteria.getGrantType() );
            }

            if ( budgetCriteria.getEffectiveDate() != null ) {
                query.setParameter( "effectiveDate", budgetCriteria.getEffectiveDate() );
            }

            if ( budgetCriteria.getGrantSubType() != null && !budgetCriteria.getGrantSubType().isEmpty() ) {
                query.setParameter( "grantSubType", budgetCriteria.getGrantSubType() );
            }
        }
        return query;
    }

    /**
     * add new Grant Budget Record
     * 
     * @param grantBudget
     */
    public void addGrantBudget( GrantBudget grantBudget, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        grantBudget.preSave();
        session.save( grantBudget );

        session.flush();
    }

    /**
     * Update Grant Budget Record
     * 
     * @param grantBudget
     */
    public void updateGrantBudget( GrantBudget grantBudget, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        grantBudget.preSave();
        session.merge( grantBudget );

        session.flush();
    }

    /**
     * Save Bereavement Grant Application
     * 
     * @param bereavementGrant
     * @throws AccessDeniedException
     */
    public void saveBereavementGrant( BereavementGrant bereavementGrant, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        bereavementGrant.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), bereavementGrant.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + bereavementGrant.getNric() );
        }

        session.saveOrUpdate( bereavementGrant );

        session.flush();
    }

    /**
     * Save New Born Gift Application
     * 
     * @param newBornGift
     * @throws AccessDeniedException
     */
    public void saveNewBorn( NewBornGift newBornGift, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        newBornGift.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), newBornGift.getMemberNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + newBornGift.getMemberNric() );
        }

        session.saveOrUpdate( newBornGift );

        session.flush();
    }

    /**
     * Save Wedding Gift Application
     * 
     * @param weddingGift
     * @throws AccessDeniedException
     */
    public void saveWeddingGift( WeddingGift weddingGift, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        weddingGift.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), weddingGift.getMemberNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + weddingGift.getMemberNric() );
        }

        session.saveOrUpdate( weddingGift );

        session.flush();
    }

    /**
     * Get Total Grant Amount Paid for the Type of Benefits Application
     * 
     * @param benefitsCriteria
     * @param queryStr
     * @return
     */
    private Double getTotalAmountPaidForGrant( BenefitsCriteria benefitsCriteria, StringBuilder queryStr ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        buildQueryStringByCriteria( queryStr, benefitsCriteria );
        queryStr.append( ")" );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setCriteriaParameter( sqlQuery, benefitsCriteria );

        Double result = ( Double ) sqlQuery.uniqueResult();

        if ( result != null ) {
            if ( logger.isLoggable( Level.INFO ) ) {
                logger.info(String.format("query Result --- %s", result));
            }
            return result;
        }

        // return 0 by default.
        return 0.0d;
    }

    /**
     * Get Total Bereavement Grant Amount Paid by Criteria.
     * 
     * @param benefitsCriteria
     * @return
     */
    public Double getTotalBereavementGrantPaidByCriteria( BenefitsCriteria benefitsCriteria ) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SQL_BEREAVEMENT_TOTAL_AMOUNT_PAID );

        return getTotalAmountPaidForGrant( benefitsCriteria, queryStr );
    }

    /**
     * Get Total New Born Gift Grant Amount Paid by Criteria.
     * 
     * @param benefitsCriteria
     * @return
     */
    public Double getTotalNewBornGiftGrantPaidByCriteria( BenefitsCriteria benefitsCriteria ) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SQL_NEW_BORN_TOTAL_AMOUNT_PAID );

        return getTotalAmountPaidForGrant( benefitsCriteria, queryStr );
    }

    /**
     * Get Total Wedding Gift Grant Amount Paid by Criteria.
     * 
     * @param benefitsCriteria
     * @return
     */

    public Double getTotalWeddingGiftGrantPaidByCriteria( BenefitsCriteria benefitsCriteria ) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SQL_WEDDING_TOTAL_AMOUNT_PAID );

        return getTotalAmountPaidForGrant( benefitsCriteria, queryStr );
    }
    /**
     * Search count of bereavement grant application (Not in DRAFT Status and not in REJECTED Status) by
     * deceasedNric and deathCertificateNumber and not equal to the referenceNumber(if Present).
     * 
     * @param deceasedIdType
     * @param deceasedNric
     * @param deathCertificateNumber
     * @param referenceNumber
     * @return
     */
    public Long searchCountBereavementGrant( String deceasedIdType, String deceasedNric, String deathCertificateNumber, String referenceNumber ) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append( "SELECT count(c) FROM BereavementGrant as c WHERE " + "c.applicationStatus != '" + ApplicationStatus.DRAFT.name() + "' and c.applicationStatus != '" + ApplicationStatus.REJECTED.name() + "'" );

        // compare deceasedNric and deathCertificateNumber in lowerCase
        if ( null != deceasedIdType ) {
            queryStr.append( " and lower(c.deceasedIdType) = :deceasedIdType" );
        }
        if ( null != deceasedNric ) {
            queryStr.append( " and lower(c.deceasedNric) = :deceasedNric" );
        }
        if ( null != deathCertificateNumber ) {
            queryStr.append( " and lower(c.deathCertificateNumber) = :deathCertificateNumber" );
        }
        if ( null != referenceNumber && !"".equals( referenceNumber ) ) {
            queryStr.append( " and c.referenceNumber != :referenceNumber" );
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( queryStr.toString() );

        if ( null != deceasedIdType ) {
            query.setParameter( "deceasedIdType", deceasedIdType.toLowerCase() );
        }
        if ( null != deceasedNric ) {
            query.setParameter( "deceasedNric", deceasedNric.toLowerCase() );
        }
        if ( null != deathCertificateNumber ) {
            query.setParameter( "deathCertificateNumber", deathCertificateNumber.toLowerCase() );
        }
        if ( null != referenceNumber && !"".equals( referenceNumber ) ) {
            query.setParameter( "referenceNumber", referenceNumber );
        }

        return ( ( Long ) query.uniqueResult() ).longValue();
    }

    /**
     * 
     * @Title: searchBereavementGrant
     * @Description: Search the bereavement grant application by
     *               benefitsCriteria.
     * @param @param benefitsCriteria
     * @param @return
     * @return List<CommonBenefitsDetails>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< BereavementGrantDetails > searchBereavementGrant( BenefitsCriteria benefitsCriteria ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SQL_BEREAVEMENT_GRANT_COMMON );

        buildBenefitsGrantByCriteria( queryStr, benefitsCriteria );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setBenefitsGrantCriteriaParameter( sqlQuery, benefitsCriteria );
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );

        Properties params = new Properties();
        params.put( "enumClass", "com.stee.spfcore.model.ApplicationStatus" );
        params.put( "type", "12" );
        sqlQuery.addScalar( "applicationStatus", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, params ) );

        
        sqlQuery.addScalar( "dateOfSubmission", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "submittedBy" );
        sqlQuery.addScalar( "amountToBePaid", StandardBasicTypes.DOUBLE );
        sqlQuery.addScalar( "paymentDate", StandardBasicTypes.DATE );

        sqlQuery.setResultTransformer( Transformers.aliasToBean( BereavementGrantDetails.class ) );

        List< BereavementGrantDetails > result = ( List< BereavementGrantDetails > ) sqlQuery.list();

        // Filter the result based on what caller can see.
        result = DataFilter.filterBereavementGrantDetails( SecurityInfo.createInstance(), result );

        return result;
    }

    /**
     * 
     * @Title: setBenefitsGrantCriteriaParameter
     * @Description: Set BenefitsGrant Parameter Values based on criteria
     *               parameters
     * @param @param sqlQuery
     * @param @param benefitsCriteria
     * @return void
     * @throws
     */
    private void setBenefitsGrantCriteriaParameter( SQLQuery sqlQuery, BenefitsCriteria benefitsCriteria ) {

        if ( benefitsCriteria != null ) {

            // Append Nric Criteria
            if ( null != benefitsCriteria.getNric() ) {
                sqlQuery.setParameter( "nric", "%" + benefitsCriteria.getNric() + "%" );

            }

            // Append referenceNumber Criteria
            if ( null != benefitsCriteria.getReferenceNumber() ) {
                sqlQuery.setParameter( "referenceNumber", "%" + benefitsCriteria.getReferenceNumber() + "%" );
            }

            // Append applicantName Criteria
            if ( null != benefitsCriteria.getApplicantName() ) {
                sqlQuery.setParameter( "applicantName", "%" + benefitsCriteria.getApplicantName() + "%" );
            }

        }
    }

    /**
     * 
     * @Title: buildBenefitsGrantByCriteria
     * @Description: Build SQL BenefitsGrant based on criteria parameters.
     * @param @param queryStr
     * @param @param benefitsCriteria
     * @return void
     * @throws
     */
    private void buildBenefitsGrantByCriteria( StringBuilder queryStr, BenefitsCriteria benefitsCriteria ) {

        if ( benefitsCriteria != null ) {

            // Append Search by nric
            if ( null != benefitsCriteria.getNric() ) {
                queryStr.append( " AND c.MEMBER_NRIC LIKE :nric" );
            }

            if ( null != benefitsCriteria.getReferenceNumber() ) {
                queryStr.append( " AND c.REFERENCE_NUMBER LIKE :referenceNumber" );
            }

            if ( null != benefitsCriteria.getApplicantName() ) {
                queryStr.append( " AND p.NAME LIKE :applicantName" );
            }

        }

    }

    /**
     * 
     * @Title: searchNewBorn
     * @Description: Search New Born Gift application by benefitsCriteria.
     * @param @param benefitsCriteria
     * @param @return
     * @return List<NewBornGiftDetails>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< NewBornGiftDetails > searchNewBorn( BenefitsCriteria benefitsCriteria ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SQL_NEWBORN_GIFT_COMMON );

        buildBenefitsGrantByCriteria( queryStr, benefitsCriteria );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setBenefitsGrantCriteriaParameter( sqlQuery, benefitsCriteria );
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );

        Properties params = new Properties();
        params.put( "enumClass", "com.stee.spfcore.model.ApplicationStatus" );
        params.put( "type", "12" );
        sqlQuery.addScalar( "applicationStatus", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, params ) );
        sqlQuery.addScalar( "dateOfSubmission", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "submittedBy" );
        sqlQuery.addScalar( "amountToBePaid", StandardBasicTypes.DOUBLE );
        sqlQuery.addScalar( "paymentDate", StandardBasicTypes.DATE );

        sqlQuery.setResultTransformer( Transformers.aliasToBean( NewBornGiftDetails.class ) );

        List< NewBornGiftDetails > result = ( List< NewBornGiftDetails > ) sqlQuery.list();

        // Filter the result based on what user can see.
        result = DataFilter.filterNewBornGiftDetails( SecurityInfo.createInstance(), result );

        return result;
    }

    /**
     * 
     * @Title: searchWeddingGift
     * @Description: Search the Wedding Gift application by benefitsCriteria.
     * @param @param benefitsCriteria
     * @param @return
     * @return List<WeddingGiftDetails>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< WeddingGiftDetails > searchWeddingGift( BenefitsCriteria benefitsCriteria ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append( SQL_WEDDING_GIFT_COMMON );

        buildBenefitsGrantByCriteria( queryStr, benefitsCriteria );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        setBenefitsGrantCriteriaParameter( sqlQuery, benefitsCriteria );
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );

        Properties params = new Properties();
        params.put( "enumClass", "com.stee.spfcore.model.ApplicationStatus" );
        params.put( "type", "12" );
        sqlQuery.addScalar( "applicationStatus", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, params ) );
        sqlQuery.addScalar( "dateOfSubmission", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "submittedBy" );
        sqlQuery.addScalar( "amountToBePaid", StandardBasicTypes.DOUBLE );
        sqlQuery.addScalar( "paymentDate", StandardBasicTypes.DATE );

        sqlQuery.setResultTransformer( Transformers.aliasToBean( WeddingGiftDetails.class ) );

        List< WeddingGiftDetails > result = ( List< WeddingGiftDetails > ) sqlQuery.list();

        // Filter the result based on what caller can see
        result = DataFilter.filterWeddingGiftDetails( SecurityInfo.createInstance(), result );

        return result;
    }

    // Phrase 2
    /**
     * @throws AccessDeniedException
     * 
     * @Title: getWeightMgmtSubsidy
     * @Description: get Weight Mgmt Subsidy By referenceNumber
     * @param @param referenceNumber
     * @param @return
     * @return WeightMgmtSubsidy
     * @throws
     */
    public WeightMgmtSubsidy getWeightMgmtSubsidy( String referenceNumber ) throws AccessDeniedException {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        WeightMgmtSubsidy result = ( WeightMgmtSubsidy ) session.get( WeightMgmtSubsidy.class, referenceNumber );

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), result.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + result.getNric() );
        }

        return result;
    }

    /**
     * Search the bereavement grant application by nric.
     * 
     * @param nric
     * @return
     * @throws AccessDeniedException
     * @throws WeightMgmtSubsidy
     */
    @SuppressWarnings( "unchecked" )
    public List< WeightMgmtSubsidy > searchWeightMgmtSubsidy( String nric ) throws AccessDeniedException {

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( WeightMgmtSubsidy.class );
        criteria.add( Restrictions.eq( "nric", nric ) );

        return ( List< WeightMgmtSubsidy > ) criteria.list();
    }

    /**
     * @throws AccessDeniedException
     * 
     * @Title: searchWeightMgmtSubsidyByNricAndYear
     * @Description: search Weight Mgmt Subsidy By Nric And Year
     * @param @param nric
     * @param @param year
     * @param @return
     * @return List<WeightMgmtSubsidy>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< WeightMgmtSubsidy > searchWeightMgmtSubsidyByNric( String nric, Calendar today ) throws AccessDeniedException {

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Criteria criteria = session.createCriteria( WeightMgmtSubsidy.class );

        criteria.add( Restrictions.eq( "nric", nric ) );

        // Get Last Day Of Year
        Date firstDayOfYear = getFirstDayOfYear( today );
        // Get First Day Of Year
        Date lastDayOfYear = getLastDayOfYear( today );
        criteria.add( Restrictions.between( "dateOfApplication", firstDayOfYear, lastDayOfYear ) );

        return ( List< WeightMgmtSubsidy > ) criteria.list();
    }

    /**
     * 
     * @Title: getLastDayOfYear
     * @Description:
     *               if today's month > 3, then return the next year of March 31
     *               else, return the year of March 31.
     *               For example:
     *               If today is 10/24/2016, Then return March 31 2017
     *               If today is 03/24/2016, Then return March 31 2016
     * @param @param year
     * @param @return
     * @return Date
     * @throws
     */
    private Date getLastDayOfYear( Calendar today ) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        if ( today.get( Calendar.MONTH ) > 2 ) {
            calendar.set( Calendar.YEAR, today.get( Calendar.YEAR ) + 1 );
            calendar.set( Calendar.MONTH, 2 );
            calendar.set( Calendar.DATE, 31 );
        }
        else {
            calendar.set( Calendar.YEAR, today.get( Calendar.YEAR ) );
            calendar.set( Calendar.MONTH, 2 );
            calendar.set( Calendar.DATE, 31 );
        }

        return calendar.getTime();
    }

    /**
     * 
     * @Title: getFirstDayOfYear
     * @Description:
     *               if today's month < 4, then return the last year of April 01
     *               else, return the year of April 01.
     *               For example:
     *               If today is 10/24/2016, Then return April 01 2016
     *               If today is 03/24/2016, Then return April 01 2015
     * @param @param year
     * @param @return
     * @return Date
     * @throws
     */
    private Date getFirstDayOfYear( Calendar today ) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        if ( today.get( Calendar.MONTH ) < 3 ) {
            calendar.set( Calendar.YEAR, today.get( Calendar.YEAR ) - 1 );
            calendar.set( Calendar.MONTH, 3 );
            calendar.set( Calendar.DATE, 1 );
        }
        else {
            calendar.set( Calendar.YEAR, today.get( Calendar.YEAR ) );
            calendar.set( Calendar.MONTH, 3 );
            calendar.set( Calendar.DATE, 1 );
        }

        return calendar.getTime();
    }

    /**
     * @throws AccessDeniedException
     * 
     * @Title: saveWeightMgmtSubsidy
     * @Description: Save Weight Mgmt Subsidy
     * @param @param weightMgmtSubsidy
     * @return void
     * @throws
     */
    public void saveWeightMgmtSubsidy( WeightMgmtSubsidy weightMgmtSubsidy, String requester ) throws AccessDeniedException {

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), weightMgmtSubsidy.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + weightMgmtSubsidy.getNric() );
        }

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.saveOrUpdate( weightMgmtSubsidy );

        session.flush();
    }

    /**
     * 
     * @Title: GetHealthCareProviderList
     * @Description: Get HealthCare Provider List
     * @param @return
     * @return List<HealthCareProvider>
     * @throws
     */
    @SuppressWarnings( "unchecked" )
    public List< HealthCareProvider > getHealthCareProviderList() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Criteria criteria = session.createCriteria( HealthCareProvider.class );

        return ( List< HealthCareProvider > ) criteria.list();
    }

    /**
     * 
     * @Title: getHealthCareProviderByServiceProvider
     * @Description: Get HealthCare Provider By ServiceProvider
     * @param @param serviceProvider
     * @param @return
     * @return HealthCareProvider
     * @throws
     */
    public HealthCareProvider getHealthCareProviderByServiceProvider( String serviceProvider ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        return ( HealthCareProvider ) session.get( HealthCareProvider.class, serviceProvider );
    }
    
    public int getDiffInDays(Calendar todayDate, Date submissionDate) {
    	
    	Calendar dateOfSubmission = Calendar.getInstance();
    	dateOfSubmission.clear();
    	dateOfSubmission.setTime(submissionDate);
        return (int)( (todayDate.getTimeInMillis() - dateOfSubmission.getTimeInMillis()) / (1000 * 60 * 60 * 24) );
    }
    
    public void saveBenefitsProcess( BenefitsProcess benefitsProcess ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( benefitsProcess );

        session.flush();
    }
    
    public List<BenefitsProcess> retrieveBenefitsProcess(Integer bpmProcessId) throws AccessDeniedException {

    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "SELECT c FROM BenefitsProcess as c WHERE c.bpmProcessId = :bpmProcessId" );
        query.setParameter( "bpmProcessId", bpmProcessId );
        
        return ( List< BenefitsProcess > ) query.list();
    }
    
    public List<BenefitsProcess> hasActiveBenefitsProcess(String creationPeriod, String status) throws AccessDeniedException {

    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "SELECT c FROM BenefitsProcess as c WHERE c.creationPeriod = :creationPeriod and c.status = :status" );
        query.setParameter( "creationPeriod", creationPeriod);
        query.setParameter( "status", status );
        return ( List< BenefitsProcess > ) query.list();
    }
    
    public void saveBereavementGrantList( List<BereavementGrant> bereavementGrantList, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        for(int i=0; i<bereavementGrantList.size(); i++)
        {
        	bereavementGrantList.get(i).preSave();
        	logger.info("after preSave");

        	if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), bereavementGrantList.get(i).getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + bereavementGrantList.get(i).getNric() );
        	}
        	 logger.info("before saveOrUpdate");
        	 session.saveOrUpdate(bereavementGrantList.get(i));
        	 logger.info("after saveOrUpdate");
        	 if ( i % 20 == 0) { //20, same as the JDBC batch size
	             //flush a batch of inserts and release memory:
				 session.flush();
				 session.clear();
			 }
        	 logger.info("before clear session");
        }
        session.flush();
    }
    
    public List<BenefitsProcess> hasUpdatedBenefitsProcess(String creationPeriod, String status, Date startDate, Date endDate) throws AccessDeniedException {

    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "SELECT c FROM BenefitsProcess as c WHERE c.creationPeriod = :creationPeriod and c.status = :status and c.updatedDate between :startDate and :endDate" );
        query.setParameter( "creationPeriod", creationPeriod);
        query.setParameter( "status", status );
        query.setParameter( "startDate", startDate );
        query.setParameter( "endDate", endDate );
        return ( List< BenefitsProcess > ) query.list();
    }
    

}
