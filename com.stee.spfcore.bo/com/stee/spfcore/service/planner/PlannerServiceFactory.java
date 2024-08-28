package com.stee.spfcore.service.planner;

import com.stee.spfcore.service.planner.impl.PlannerService;

public class PlannerServiceFactory {

    private PlannerServiceFactory(){}
    private static IPlannerService plannerService;

    public static synchronized IPlannerService getPlannerService() {
        if ( plannerService == null ) {
            plannerService = createPlannerService();
        }
        return plannerService;
    }

    private static IPlannerService createPlannerService() {

    	return new PlannerService();

    }
}
