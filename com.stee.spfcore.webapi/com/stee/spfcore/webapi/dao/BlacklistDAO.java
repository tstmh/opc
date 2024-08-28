package com.stee.spfcore.webapi.dao;

import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BlacklistDAO {
	
	private EntityManager entityManager;
	
	private static final String SELECT_ONLY_ENABLED = "(v.effectiveDate is not null and v.effectiveDate <= current_date()) and (v.obsoleteDate is null or v.obsoleteDate > current_date())";
	private static final Logger logger = LoggerFactory.getLogger(BlacklistDAO.class);
	
	@Autowired
	public BlacklistDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public boolean isBlacklisted ( String nric, String module ){
		logger.info("Is Blacklisted");
		
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder builder = new StringBuilder( "select count(v) from Blacklistee v " );
        builder.append( "where v.nric = :nric and v.module = :module and " );
        builder.append( SELECT_ONLY_ENABLED );
		
		Query<Number> query = session.createQuery( builder.toString() );
		
		query.setParameter( "nric", nric );
        query.setParameter( "module", module );

        long count = ( ( Number ) query.uniqueResult() ).longValue();

        return ( count > 0 );
	}
	
}
