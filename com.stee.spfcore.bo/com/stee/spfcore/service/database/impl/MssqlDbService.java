package com.stee.spfcore.service.database.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.MssqlDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.service.database.DatabaseException;
import com.stee.spfcore.service.database.IDatabaseService;
import com.stee.spfcore.vo.database.DbColumnDetail;

public class MssqlDbService implements IDatabaseService {
    private static final Logger LOGGER = Logger.getLogger( MssqlDbService.class.getName() );
    private MssqlDAO dao;

    public MssqlDbService() {
        this.dao = new MssqlDAO();
    }

    public List< DbColumnDetail > getSpfcoreColumnDetails( String tableName ) throws DatabaseException {
        List< DbColumnDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            List< String > schemaNames = new ArrayList<>();
            List< String > tableNames = new ArrayList<>();
            schemaNames.add( "SPFCORE" );
            tableNames.add( tableName );

            results = dao.getColumnDetails( schemaNames, tableNames, null );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getSpfcoreColumnDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;

    }
}
