package com.stee.spfcore.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.stee.spfcore.service.configuration.IDAOSessionConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.EnvironmentUtils;

public class BpmsUserSessionFactoryUtil {
    private static final Logger LOGGER = Logger.getLogger( BpmsUserSessionFactoryUtil.class.getName() );

    private static SessionFactory sessionFactory;

    private BpmsUserSessionFactoryUtil() {

    }
    private static void buildSessionFactory() {
        if ( !EnvironmentUtils.isIntranet() ) {
            sessionFactory = null;
            return;
        }

        IDAOSessionConfig config = ServiceConfig.getInstance().getDaoSessionConfig();

        Configuration cfg = new Configuration();
        cfg.setProperty( "hibernate.connection.datasource", config.bpmsuserDatasource() );
        cfg.setProperty( "hibernate.current_session_context_class", "thread" );
        cfg.setProperty( "hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect" );

        ServiceRegistryBuilder serviceRegBuilder = new ServiceRegistryBuilder();
        serviceRegBuilder.applySettings( cfg.getProperties() );
        ServiceRegistry serviceReg = serviceRegBuilder.buildServiceRegistry();
        sessionFactory = cfg.buildSessionFactory( serviceReg );
    }

    public static synchronized SessionFactory getInstance() {
        if ( sessionFactory == null ) {
            buildSessionFactory();
        }

        return sessionFactory;
    }

    public static Session getCurrentSession() {
        Session session = null;
        try {
            session = getInstance().getCurrentSession();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.INFO, "No current session, try to open one." );
            session = getInstance().openSession();
        }

        return session;
    }

    public static Session beginTransaction() {
        Session session = getCurrentSession();
        session.beginTransaction();
        return session;
    }

    public static void commitTransaction() {
        getCurrentSession().getTransaction().commit();

    }

    public static void rollbackTransaction() {
        try {
            getCurrentSession().getTransaction().rollback();
        }
        catch ( HibernateException e1 ) {
            LOGGER.log( Level.INFO, "Error rolling back transaction", e1 );
        }
    }
}
