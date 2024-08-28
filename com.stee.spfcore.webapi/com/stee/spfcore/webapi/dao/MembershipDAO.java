package com.stee.spfcore.webapi.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.model.membership.DiscrepancyRecord;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.membership.MembershipPaymentCheckRecord;
import com.stee.spfcore.webapi.model.membership.PaymentDataSource;

@Repository
public class MembershipDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(MembershipDAO.class);
	
	public MembershipDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Membership getMembership(String nric) {
		logger.info("Get Membership");
		Session session = entityManager.unwrap(Session.class);
		
		return (Membership) session.get(Membership.class, nric);
	}
	
	public void updateMembership(Membership membership) {
		logger.info("Update Membership");
		Session session = entityManager.unwrap(Session.class);
		
		membership.preSave();

	    session.merge( membership );
	    session.flush();
	}
	
	public void addMembership(Membership membership) {
		logger.info("Add Membership");
		Session session = entityManager.unwrap(Session.class);
		
		membership.preSave();

	    session.save( membership );
	    session.flush();
	}
	
	public void saveDiscrepancyRecord(DiscrepancyRecord discrepancyRecord) {
		logger.info("Save Discrepency Record");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( discrepancyRecord );
	    session.flush();
	}
	
	public int deleteDiscrepancyRecord(String nric, Integer month, Integer year) {
		logger.info("Delete Discrepency Record");
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("delete DiscrepencyRecord where 1=1 ");
		
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
	
	public int deletePaymentHistories(Integer month, Integer year, PaymentDataSource source) {
		logger.info("Delete Payment Histories");
		Session session = entityManager.unwrap(Session.class);
		
		Query query = session.createQuery( "delete PaymentHistory where sourceMonth = :month and sourceYear = :year and source = :source" );
        query.setParameter( "month", month );
        query.setParameter( "year", year );
        query.setParameter( "source", source );

        int count = query.executeUpdate();
        session.flush();

        return count;
	}
	
	public void addMembershipPaymentCheckRecord( MembershipPaymentCheckRecord checkRecord ) {
		logger.info("Add Membership Payment Check Record");
        checkRecord.setId( 0L );
        Session session = entityManager.unwrap(Session.class);
        session.save( checkRecord );
    }
	
}
