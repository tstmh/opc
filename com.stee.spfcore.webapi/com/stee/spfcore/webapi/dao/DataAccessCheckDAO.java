package com.stee.spfcore.webapi.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.dao.dac.DepartmentAndSubUnit;
import com.stee.spfcore.webapi.utils.MappingUtil;

@Repository
public class DataAccessCheckDAO {
	
	private EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(DataAccessCheckDAO.class);

	
	@Autowired
	public DataAccessCheckDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    public DepartmentAndSubUnit getDepartmentAndSubUnit( String nric ) {
        logger.info("Get Department and Subunit");
		Session session = entityManager.unwrap(Session.class);
        
        Query query = session.createQuery( "SELECT c.organisationOrDepartment as department, c.subunit as subunit FROM Employment as c WHERE c.nric = :nric" );
        query.setParameter( "nric", nric );
        
        //List<DepartmentAndSubUnit> list = MappingUtil.getResultList(query, DepartmentAndSubUnit.class);
        query.setResultTransformer( Transformers.aliasToBean( DepartmentAndSubUnit.class ) );

        List result = query.list();
        if ( result.isEmpty() )
        {
            return null;
        }
        else
        {
            return ( DepartmentAndSubUnit ) result.get( 0 );
        }
    }
    
    
	
}
