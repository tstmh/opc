package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.stee.spfcore.model.report.SsrsReport;
import com.stee.spfcore.model.report.SsrsReportEnvironment;

public class ReportDAO {
    @SuppressWarnings( "unchecked" )
    public SsrsReportEnvironment getReportEnvironment( Boolean environmentEnabled, String environmentName ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select reportEnv from SsrsReportEnvironment reportEnv where 1=1 " );
        if ( environmentEnabled != null ) {
            queryBuilder.append( "and reportEnv.enabled = :environmentEnabled " );
        }
        if ( environmentName != null ) {
            queryBuilder.append( "and reportEnv.environmentName = :environmentName " );
        }
        Query query = session.createQuery( queryBuilder.toString() );
        query.setMaxResults( 1 );
        if ( environmentEnabled != null ) {
            query.setParameter( "environmentEnabled", environmentEnabled );
        }
        if ( environmentName != null ) {
            query.setParameter( "environmentName", environmentName );
        }
        List< SsrsReportEnvironment > results = ( List< SsrsReportEnvironment > ) query.list();
        SsrsReportEnvironment result = null;
        if ( results != null && results.size() > 0) {
            result = results.get( 0 );
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public SsrsReport getReport( Boolean environmentEnabled, String environmentName, Boolean reportEnabled, Boolean isAdhoc, String rootDir, String reportName ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "select report from SsrsReport report where 1=1 " );
        if ( environmentEnabled != null ) {
            queryBuilder.append( "and report.environment.enabled = :environmentEnabled " );
        }
        if ( environmentName != null ) {
            queryBuilder.append( "and report.environment.environmentName = :environmentName " );
        }
        if ( reportEnabled != null ) {
            queryBuilder.append( "and report.enabled = :reportEnabled " );
        }
        if ( isAdhoc != null ) {
            queryBuilder.append( "and report.isAdhoc = :isAdhoc " );
        }
        if ( rootDir != null ) {
            queryBuilder.append( "and report.rootDir = :rootDir " );
        }
        if ( reportName != null ) {
            queryBuilder.append( "and report.reportName = :reportName " );
        }
        Query query = session.createQuery( queryBuilder.toString() );
        query.setMaxResults( 1 );
        if ( environmentEnabled != null ) {
            query.setParameter( "environmentEnabled", environmentEnabled );
        }
        if ( environmentName != null ) {
            query.setParameter( "environmentName", environmentName );
        }
        if ( reportEnabled != null ) {
            query.setParameter( "reportEnabled", reportEnabled );
        }
        if ( isAdhoc != null ) {
            query.setParameter( "isAdhoc", isAdhoc );
        }
        if ( rootDir != null ) {
            query.setParameter( "rootDir", rootDir );
        }
        if ( reportName != null ) {
            query.setParameter( "reportName", reportName );
        }
        List< SsrsReport > results = ( List< SsrsReport > ) query.list();
        SsrsReport result = null;
        if ( results != null && results.size() > 0 ) {
            result = results.get( 0 );
        }
        return result;
    }
    
    
    public Object [] getReportInfo (String reportName) {
    	
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
      StringBuilder queryBuilder = new StringBuilder();
      
      queryBuilder.append("SELECT R.ROOT_DIR, R.REPORT_NAME ");

      queryBuilder.append("FROM SPFCORE.SSRS_REPORT R INNER JOIN SPFCORE.SSRS_REPORT_ENVIRONMENT RE ON ");

      queryBuilder.append("RE.ID = R.REPORT_ENVIRONMENT_ID WHERE RE.ENABLED = 1	AND R.REPORT_NAME = :reportName");
      
      SQLQuery sqlQuery = session.createSQLQuery(queryBuilder.toString());
      sqlQuery.setParameter( "reportName", reportName );
      
      return (Object[]) sqlQuery.uniqueResult();
    }
}
