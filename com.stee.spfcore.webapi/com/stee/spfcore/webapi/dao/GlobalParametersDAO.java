package com.stee.spfcore.webapi.dao;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.globalParameters.GlobalParameters;

@Repository
public class GlobalParametersDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(GlobalParametersDAO.class);
	
	@Autowired
	public GlobalParametersDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    public GlobalParameters getGlobalParametersByUnit( String unit, String subunit) {
    	Session session = entityManager.unwrap(Session.class);
    	String sqlStatement = "";
    	if(subunit == null || subunit == "" || unit.equals("PCWF"))
    	{
    		logger.info("do not require checks for subunit");
    		sqlStatement = "Select g from GlobalParameters g where g.corporateCardUnitOrPcwf = :unit";
    	}
    	else
    	{
    		logger.info("require checks for subunit");
    		sqlStatement = "Select g from GlobalParameters g where g.corporateCardUnitOrPcwf = :unit and g.corporateCardSubunit = :subunit";
    	}
    	Query query = session.createQuery( sqlStatement );
        query.setParameter( "unit", unit );
        if(subunit != null && subunit !="" && !unit.equals("PCWF"))
        {
        	query.setParameter("subunit", subunit);
        }
        
        List< ? > list = query.list();

        if ( !list.isEmpty() ) {
            return ( GlobalParameters ) list.get( 0 );
        }
        else {
            return null;
        }	
    }
		
}
