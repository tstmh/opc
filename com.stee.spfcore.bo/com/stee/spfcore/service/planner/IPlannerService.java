package com.stee.spfcore.service.planner;

import com.stee.spfcore.model.planner.PlannerConfig;

public interface IPlannerService {
    public PlannerConfig getPlannerConfig() throws PlannerServiceException;
}
