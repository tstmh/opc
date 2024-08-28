package com.stee.spfcore.service.template;

import com.stee.spfcore.service.template.impl.TemplateService;
import com.stee.spfcore.utils.EnvironmentUtils;

public class TemplateServiceFactory {

    private TemplateServiceFactory(){}
    private static ITemplateService templateService;

    public static synchronized ITemplateService getTemplateService() {

        if ( templateService == null ) {
            templateService = createTemplateService();
        }

        return templateService;
    }

    private static ITemplateService createTemplateService() {

        if ( EnvironmentUtils.isIntranet() ) {
            return new TemplateService();
        }

        // Internet side will not be sending SMS or email.
        return null;
    }
}
