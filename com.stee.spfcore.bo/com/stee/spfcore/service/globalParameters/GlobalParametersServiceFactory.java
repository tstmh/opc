package com.stee.spfcore.service.globalParameters;

import com.stee.spfcore.service.globalParameters.impl.GlobalParametersService;
import com.stee.spfcore.utils.EnvironmentUtils;

public class GlobalParametersServiceFactory {

    private GlobalParametersServiceFactory(){}
    private static IGlobalParametersService globalParametersService;

    public static synchronized IGlobalParametersService getGlobalParametersService() {

        if ( globalParametersService == null ) {
            globalParametersService = createGlobalParametersService();
        }

        return globalParametersService;
    }

    private static IGlobalParametersService createGlobalParametersService() {

        if ( EnvironmentUtils.isIntranet() ) {
            return new GlobalParametersService();
        }

        return null;
    }
}
