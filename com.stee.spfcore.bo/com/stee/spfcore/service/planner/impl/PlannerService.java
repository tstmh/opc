package com.stee.spfcore.service.planner.impl;

import java.util.logging.Logger;

import com.stee.spfcore.dao.PlannerDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.planner.PlannerConfig;
import com.stee.spfcore.service.planner.IPlannerService;
import com.stee.spfcore.service.planner.PlannerServiceException;

public class PlannerService implements IPlannerService {
    private static final Logger LOGGER = Logger.getLogger( PlannerService.class.getName() );

    private PlannerDAO plannerDao;

    public PlannerService() {
        this.plannerDao = new PlannerDAO();
    }

    public PlannerConfig getPlannerConfig() throws PlannerServiceException {
        LOGGER.info( "start BPMPlannerService.getPlannerConfig()" );

        PlannerConfig plannerConfig = null;
        try {
            SessionFactoryUtil.beginTransaction();

            plannerConfig = this.plannerDao.getPlannerConfig();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception exception ) {
            String msg = "failed BPMPlannerService.getPlannerConfig()";
            LOGGER.warning( msg );
            SessionFactoryUtil.rollbackTransaction();
            throw new PlannerServiceException( msg );
        }

        LOGGER.info( "end BPMPlannerService.getPlannerConfig()" );
        return plannerConfig;
    }
}
