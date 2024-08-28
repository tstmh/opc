package com.stee.spfcore.webapi.dao;

import java.util.List;

import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.code.Code;
import com.stee.spfcore.webapi.model.code.CodeType;

@Repository
public class CodeDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(CodeDAO.class);
	
	@Autowired
	public CodeDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Code getCode( CodeType type, String id ) {
		logger.info("Get Code");
		Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "SELECT c FROM Code as c WHERE c.type = :codeType and c.id = :codeId" );
        query.setParameter( "codeType", type );
        query.setParameter( "codeId", id );
        query.setMaxResults( 1 );

        List< ? > list = query.list();
        if ( !list.isEmpty() ) {
            return ( Code ) list.get( 0 );
        }
        else {
            return null;
        }
    }
	
	public void addCode( Code code ) {
		logger.info("Add Code");
		Session session = entityManager.unwrap(Session.class);

        session.save( code );

        session.flush();
    }

	public void updateCode( Code code ) {
		logger.info("Update Code");
		Session session = entityManager.unwrap(Session.class);

        session.merge( code );

        session.flush();
    }

	@SuppressWarnings("unchecked")
	public List < Code > getCodes(CodeType type) {
		Session session = entityManager.unwrap(Session.class);
		logger.info("Get Codes");

        Criteria criteria = session.createCriteria( Code.class );

        criteria.add( Restrictions.eq( "type", type ) );
        criteria.add( Restrictions.eq( "enabled", true ) );
        criteria.addOrder( Order.asc( "order" ) );

        return ( List< Code > ) criteria.list();
		
	}
	
}
