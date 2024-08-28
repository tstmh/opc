package com.stee.spfcore.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.dao.utils.SequenceUtil;
import com.stee.spfcore.model.globalParameters.GlobalParameters;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;

public class GlobalParametersDAO {
	
	private static final Logger LOGGER = Logger.getLogger("GlobalParametersDAO");
	
    public GlobalParameters getGlobalParameters() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        GlobalParameters currentGlobalParameters = ( GlobalParameters ) session.get( GlobalParameters.class, 0 );
        if ( currentGlobalParameters == null ) {
            currentGlobalParameters = new GlobalParameters();
            session.save( currentGlobalParameters );
            session.flush();
        }
        return currentGlobalParameters;
    }

    public void addOrUpdateGlobalParameters( GlobalParameters globalParameters ) {
    	LOGGER.info("addOrUpdateGlobalParameters");
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate(globalParameters);
        session.flush();
    }
    
    public GlobalParameters getGlobalParametersByUnit( String unit, String subunit) {
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	String sqlStatement = "";
    	if(subunit == null || subunit.equals("") || unit.equals("PCWF"))
    	{
    		LOGGER.info("do not require checks for subunit");
    		sqlStatement = "Select g from GlobalParameters g where g.corporateCardUnitOrPcwf = :unit";
    	}
    	else
    	{
    		LOGGER.info("require checks for subunit");
    		sqlStatement = "Select g from GlobalParameters g where g.corporateCardUnitOrPcwf = :unit and g.corporateCardSubunit = :subunit";
    	}
    	Query query = session.createQuery( sqlStatement );
        query.setParameter( "unit", unit );
        if(subunit != null && !subunit.equals("") && !unit.equals("PCWF"))
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
    
    @SuppressWarnings("unchecked")
	public List<GlobalParameters> getGlobalParametersList() {
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	Query query = session.createQuery( "Select g from GlobalParameters g" );
        
        List< ? > list = query.list();
        
        if ( !list.isEmpty() ) {
            return (List<GlobalParameters>) list;
        }
        else {
            return null;
        }
        
    }
    
    public String generateId() {
        return SequenceUtil.getInstance().getNextSequenceValue( GlobalParameters.class );
    }
}
