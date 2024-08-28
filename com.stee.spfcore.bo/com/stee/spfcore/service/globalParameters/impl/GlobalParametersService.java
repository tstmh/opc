package com.stee.spfcore.service.globalParameters.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.GlobalParametersDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.globalParameters.GlobalParameters;
import com.stee.spfcore.service.globalParameters.GlobalParametersException;
import com.stee.spfcore.service.globalParameters.IGlobalParametersService;

public class GlobalParametersService implements IGlobalParametersService {

    private static final Logger LOGGER = Logger.getLogger( GlobalParametersService.class.getName() );

    private GlobalParametersDAO dao;

    public GlobalParametersService() {
        dao = new GlobalParametersDAO();
    }

    @Override
    public GlobalParameters getGlobalParameters() throws GlobalParametersException {
        GlobalParameters result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getGlobalParameters();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getGlobalParameters() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public void addOrUpdateGlobalParameters( GlobalParameters globalParameters ) throws GlobalParametersException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addOrUpdateGlobalParameters( globalParameters );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "addOrUpdateGlobalParameters() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

	@Override
	public GlobalParameters getGlobalParametersByUnit( String unit, String subunit) throws GlobalParametersException {
		GlobalParameters result = null;
		try {
			SessionFactoryUtil.beginTransaction();
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("Unit: %s", unit));
                LOGGER.info(String.format("Subunit: %s", subunit));
            }
			result = dao.getGlobalParametersByUnit(unit, subunit);
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log( Level.WARNING, "Fail to get GlobalParameters by Unit", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	public String getNextSequenceId () throws GlobalParametersException {
		String id = null;
		
		try {
		SessionFactoryUtil.beginTransaction();
		
		id = dao.generateId();
		
		SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING , "Fail to get next sequence.");
			SessionFactoryUtil.rollbackTransaction();
			throw new GlobalParametersException("Fail to get next sequence.",e);
		}
		
		return id;
	}
    
}
