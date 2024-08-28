package com.stee.spfcore.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.planner.PlannerConfig;
import com.stee.spfcore.security.AccessDeniedException;

public class PlannerDAO {
    public PlannerConfig getPlannerConfig() throws AccessDeniedException {
        PlannerConfig plannerConfig = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select pcfg from PlannerConfig pcfg" );
        Query query = session.createQuery( queryBuilder.toString() );
        query.setMaxResults( 1 );
        plannerConfig = ( PlannerConfig ) query.uniqueResult();
        if ( plannerConfig == null ) {
            plannerConfig = new PlannerConfig();
            session.save( plannerConfig );
        }

        return plannerConfig;
    }
}
