package com.stee.spfcore.webapi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.ApplicationStatus;
import com.stee.spfcore.webapi.model.benefits.BereavementGrant;
import com.stee.spfcore.webapi.model.benefits.HealthCareProvider;
import com.stee.spfcore.webapi.model.benefits.NewBornGift;
import com.stee.spfcore.webapi.model.benefits.WeddingGift;
import com.stee.spfcore.webapi.model.benefits.WeightMgmtSubsidy;

@Repository
public class BenefitsDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(BenefitsDAO.class);
	
	public BenefitsDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public BereavementGrant getBereavementGrant(String referenceNumber) {
		logger.info("Get Bereavement Grant");
		Session session = entityManager.unwrap(Session.class);
		
		BereavementGrant bereavementGrant = ( BereavementGrant ) session.get( BereavementGrant.class, referenceNumber );

        return bereavementGrant;
	}
	
	@SuppressWarnings( "unchecked" )
	public List< BereavementGrant > searchBereavementGrant( String nric ) {
		logger.info("Search Bereavement Grant");
		Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "SELECT c FROM BereavementGrant as c WHERE c.nric = :nric" );
        query.setParameter( "nric", nric );

        return ( List< BereavementGrant > ) query.list();
    }
	
	public void saveBereavementGrant( BereavementGrant bereavementGrant ) {
		logger.info("Save Bereavement Grant");
		Session session = entityManager.unwrap(Session.class);

        bereavementGrant.preSave();

        session.saveOrUpdate( bereavementGrant );
        session.flush();
    }
	
	public NewBornGift getNewBorn( String referenceNumber ) {
		logger.info("Get New Born");
		Session session = entityManager.unwrap(Session.class);

        NewBornGift gift = ( NewBornGift ) session.get( NewBornGift.class, referenceNumber );

        return gift;
    }
	
	@SuppressWarnings( "unchecked" )
	public List< NewBornGift > searchNewBorn( String nric ) {
		logger.info("Search New Born");
		Session session = entityManager.unwrap(Session.class);

		Query query = session.createQuery( "SELECT c FROM NewBornGift as c WHERE c.memberNric = :nric" );
        query.setParameter( "nric", nric );

        return ( List< NewBornGift > ) query.list();
    }
	
	public void saveNewBorn( NewBornGift newBornGift ) {
		logger.info("Save New Born");
		Session session = entityManager.unwrap(Session.class);

		newBornGift.preSave();

        session.saveOrUpdate( newBornGift );
        session.flush();
    }
	
	public WeddingGift getWeddingGift( String referenceNumber ) {
		logger.info("Get Wedding Gift");
		Session session = entityManager.unwrap(Session.class);

		WeddingGift gift = ( WeddingGift ) session.get( WeddingGift.class, referenceNumber );
        return gift;
    }
	
	@SuppressWarnings( "unchecked" )
	public List< WeddingGift > searchWeddingGift( String nric ) {
		logger.info("Search Wedding Gift");
		Session session = entityManager.unwrap(Session.class);

		Query query = session.createQuery( "SELECT c FROM WeddingGift as c WHERE c.memberNric = :nric" );
        query.setParameter( "nric", nric );

        return ( List< WeddingGift > ) query.list();
    }
	
	public void saveWeddingGift( WeddingGift weddingGift ) {
		logger.info("Save Wedding Gift");
		Session session = entityManager.unwrap(Session.class);

		weddingGift.preSave();

        session.saveOrUpdate( weddingGift );
        session.flush();
    }
	
	@SuppressWarnings( "unchecked" )
    public List< String > getBereavementGrantRelationships() {
		logger.info("Get Bereavement Grant Relationships");
		Session session = entityManager.unwrap(Session.class);
		
        StringBuffer queryString = new StringBuffer();
        queryString.append( "select budget.grantSubType from GrantBudget budget " );
        queryString.append( "where budget.grantType = 'Bereavement' " );
        queryString.append( "and budget.grantSubType != 'Annual' " );
        queryString.append( "and budget.effectiveDate <= current_date() " );
        queryString.append( "and budget.obsoleteDate > current_date() " );
        
        Query query = session.createQuery( queryString.toString() );
        return ( List< String > ) query.list();
    }
	
	public WeightMgmtSubsidy getWeightMgmtSubsidy( String referenceNumber ) {
		logger.info("Get Weight Management Subsidy");
		Session session = entityManager.unwrap(Session.class);
        WeightMgmtSubsidy result = ( WeightMgmtSubsidy ) session.get( WeightMgmtSubsidy.class, referenceNumber );
        
        return result;
    }
	

	
	@SuppressWarnings("unchecked")
	public List<WeightMgmtSubsidy> searchWeightMgmtSubsidyByNric( String nric, Calendar today) {
		logger.info("Search Weight Management Subsidy");
		Session session = entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(WeightMgmtSubsidy.class);

        criteria.add( Restrictions.eq( "nric", nric ) );

        // Get First Day Of Year
        Date firstDayOfYear = getFirstDayOfYear( today );
        // Get Last Day Of Year
        Date lastDayOfYear = getLastDayOfYear( today );
        criteria.add( Restrictions.between( "dateOfApplication", firstDayOfYear, lastDayOfYear ) );

        return ( List< WeightMgmtSubsidy > ) criteria.list();
 
    }	
	
	public HealthCareProvider getHealthCareProviderByServiceProvider( String serviceProvider ) {
		logger.info("Get Healthcare Provider By Service Provider");
		Session session = entityManager.unwrap(Session.class);
        return ( HealthCareProvider ) session.get( HealthCareProvider.class, serviceProvider );
    }
	
	public List< HealthCareProvider > getHealthCareProviderList() {
		logger.info("Get Healthcare Provider List");
		Session session = entityManager.unwrap(Session.class);
		
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<HealthCareProvider> cr = cb.createQuery(HealthCareProvider.class);
		Root<HealthCareProvider> root = cr.from(HealthCareProvider.class);
		cr.select(root);

		Query<HealthCareProvider> query = session.createQuery(cr);
		List<HealthCareProvider> results = query.getResultList();

        return results;
    }
	
    public Long searchCountBereavementGrant( String deceasedIdType, String deceasedNric, String deathCertificateNumber, String referenceNumber ) {
    	logger.info("Search Count Bereavement Grant");
        StringBuffer queryStr = new StringBuffer();
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
        logger.info("Query String: " + queryStr.toString());
        Session session = entityManager.unwrap(Session.class);
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
        logger.info("Bereavement Count: " + query.uniqueResult().toString());
        return ( ( Long ) query.uniqueResult() ).longValue();
    }

	@SuppressWarnings("unchecked")
	public List<BereavementGrant> searchBereavementGrantForLoginID(String nric) {
		logger.info("Search Bereavement Grant");
		Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery( "SELECT c FROM BereavementGrant as c WHERE c.submittedBy = :nric" );
        query.setParameter( "nric", nric );

        return ( List< BereavementGrant > ) query.list();
	}
    @SuppressWarnings("unchecked")
    public List<BereavementGrant> searchBereavementGrantForLoginID(String nric, Date date) {
        logger.info("Search Bereavement Grant For Login ID - 1 Year");
        Session session = entityManager.unwrap(Session.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        Date oneYearBefore = calendar.getTime();
        Query query = session.createQuery( "SELECT c FROM BereavementGrant as c WHERE c.submittedBy = :nric and c.dateOfSubmission >= :oneYearBefore and c.dateOfSubmission <= :date");
        query.setParameter("nric", nric);
        query.setParameter("oneYearBefore", oneYearBefore);
        query.setParameter("date", date);
        logger.info(query.getQueryString());

        return ( List< BereavementGrant > ) query.list();
    }

	@SuppressWarnings("unchecked")
	public List<WeddingGift> searchWeddingGiftForLoginID(String nric) {
		logger.info("Search Wedding Gift For Login ID");
		Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery( "SELECT c FROM WeddingGift as c WHERE c.submittedBy = :nric" );
        query.setParameter( "nric", nric );
        
        return ( List< WeddingGift > ) query.list();
	}

    @SuppressWarnings("unchecked")
    public List<WeddingGift> searchWeddingGiftForLoginID(String nric, Date date) {
        logger.info("Search Wedding Gift For Login ID - 1 Year");
        Session session = entityManager.unwrap(Session.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        Date oneYearBefore = calendar.getTime();
        Query query = session.createQuery( "SELECT c FROM WeddingGift as c WHERE c.submittedBy = :nric and c.dateOfSubmission >= :oneYearBefore and c.dateOfSubmission <= :date");
        query.setParameter("nric", nric);
        query.setParameter("oneYearBefore", oneYearBefore);
        query.setParameter("date", date);
        logger.info(query.getQueryString());

        return (List<WeddingGift>) query.getResultList();
    }

	@SuppressWarnings("unchecked")
	public List<NewBornGift> searchNewBornForLoginID(String nric) {
		logger.info("Search Newborn For Login ID");
		Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery( "SELECT c FROM NewBornGift as c WHERE c.submittedBy = :nric" );
        query.setParameter( "nric", nric );

        return ( List< NewBornGift > ) query.list();
	}

    @SuppressWarnings("unchecked")
    public List<NewBornGift> searchNewBornForLoginID(String nric, Date date) {
        logger.info("Search NewBorn For Login ID - 1 Year");
        Session session = entityManager.unwrap(Session.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        Date oneYearBefore = calendar.getTime();
        Query query = session.createQuery( "SELECT c FROM NewBornGift as c WHERE c.submittedBy = :nric and c.dateOfSubmission >= :oneYearBefore and c.dateOfSubmission <= :date");
        query.setParameter("nric", nric);
        query.setParameter("oneYearBefore", oneYearBefore);
        query.setParameter("date", date);
        logger.info(query.getQueryString());

        return (List<NewBornGift>) query.getResultList();
    }
	
	private Date getFirstDayOfYear( Calendar today ) {
		logger.info("Get First Day of Year");
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

        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
	
	private Date getLastDayOfYear( Calendar today ) {
		logger.info("Get Last Day of Year");
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

        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

	public void saveWeightManagementSubsidy(WeightMgmtSubsidy weightMgmtSubsidy) {
		logger.info("Save Weight Management Subsidy");
		Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate( weightMgmtSubsidy );

        session.flush();
		
	}
	
}
