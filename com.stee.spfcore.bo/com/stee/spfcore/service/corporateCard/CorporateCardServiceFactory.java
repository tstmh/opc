package com.stee.spfcore.service.corporateCard;

import com.stee.spfcore.service.corporateCard.impl.CorporateCardService;
import com.stee.spfcore.utils.EnvironmentUtils;

public class CorporateCardServiceFactory {

    private CorporateCardServiceFactory(){}
    private static ICorporateCardService corporateCardService;

    public static synchronized ICorporateCardService getCorporateCardService() {

        if ( corporateCardService == null ) {
            corporateCardService = createCorporateCardService();
        }

        return corporateCardService;
    }

    private static ICorporateCardService createCorporateCardService() {
    	if ( EnvironmentUtils.isIntranet() ) {
            return new CorporateCardService();
        }

        return null;
    }
}
