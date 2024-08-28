package com.stee.spfcore.service.database;

import com.stee.spfcore.service.database.impl.MssqlDbService;
import com.stee.spfcore.utils.EnvironmentUtils;

public class DatabaseServiceFactory {

    private DatabaseServiceFactory(){}
    private static IDatabaseService databaseService;

    public static synchronized IDatabaseService getDatabaseService() {

        if ( databaseService == null ) {
            databaseService = createDatabaseService();
        }

        return databaseService;
    }

    private static IDatabaseService createDatabaseService() {
        if ( EnvironmentUtils.isIntranet() ) {
            return new MssqlDbService();
        }

        return null;
    }
}
