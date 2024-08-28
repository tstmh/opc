package com.stee.spfcore.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.stee.spfcore.model.AnnotatedModelList;
import com.stee.spfcore.service.configuration.IDAOSessionConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;

/**
 * Utility to access Hibernate Session Factory.
 */
public class SessionFactoryUtil {

    private static final Logger logger = Logger.getLogger( SessionFactoryUtil.class.getName() );

    /** The single instance of hibernate SessionFactory */
    private static org.hibernate.SessionFactory sessionFactory;

    private static ThreadLocal< String > currentUser = new ThreadLocal<>();

    public void unload() {
        currentUser.remove();
    }
    /**
     * disable constructor to guaranty a single instance
     */
    private SessionFactoryUtil() {
    }

    @SuppressWarnings( "deprecation" )
    private static void buildSessionFactory() {

        IDAOSessionConfig config = ServiceConfig.getInstance().getDaoSessionConfig();

        Configuration cfg = new Configuration();

        for (Class<?> clazz : AnnotatedModelList.MODEL_LIST) {
        	cfg.addAnnotatedClass(clazz);
        }

        cfg.setProperty( "hibernate.current_session_context_class", "thread" );
        cfg.setProperty("hibernate.jdbc.batch_size", "20");
        
        if (config.localTest()) {
        	String datasource = config.dataSource();
        	if (datasource != null && !datasource.trim().isEmpty()) {
        		cfg.setProperty( "hibernate.connection.datasource", config.dataSource() );
        		cfg.setProperty( Environment.JNDI_CLASS, config.jndiClass() );
          	cfg.setProperty( Environment.JNDI_URL, config.jndiURL() );
          	cfg.setProperty( Environment.JNDI_PREFIX + Context.SECURITY_PRINCIPAL, config.jndiUsername() );
          	cfg.setProperty( Environment.JNDI_PREFIX + Context.SECURITY_CREDENTIALS, config.jndiPassword() );
        	}
        	else {
        		cfg.setProperty("hibernate.connection.url", config.connectionUrl());
        		cfg.setProperty("hibernate.connection.driver_class", config.connectionDriverClass());
          	
        	}
        	
        	cfg.setProperty("hibernate.connection.username", config.connectionUsername());
        	/*SSAT Fix - Password management*/
        	String password = config.connectionPassword();

            String encryptionKey = EnvironmentUtils.getEncryptionKey();

            if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
                try {
                    Encipher encipher = new Encipher( encryptionKey );
                    password = encipher.decrypt( password );
                }
                catch ( Exception e ) {
                    logger.log( Level.SEVERE, "Error while decrypting the configured password.", e );
                }
            }
        	cfg.setProperty("hibernate.connection.password", password);
        	//end of SSAT Fix
        	cfg.setProperty("hibernate.dialect", config.hibernateDialect());
        }
        else {
        	if ( EnvironmentUtils.isInternet() ) {
            cfg.setProperty( "hibernate.connection.datasource", config.dataSource() );

            cfg.setProperty( "hibernate.dialect", "org.hibernate.dialect.DB2Dialect" );
        	}
        	else if ( EnvironmentUtils.isIntranet() ) {
            cfg.setProperty( "hibernate.connection.datasource", config.dataSource() );

            cfg.setProperty( "hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect" );
        	}
        	else {
            cfg.setProperty( "hibernate.connection.datasource", config.dataSource() );

            cfg.setProperty( "hibernate.dialect", "org.hibernate.dialect.DerbyDialect" );
        	}
        }
        
        if ( config.showSQL() ) {
            cfg.setProperty( "hibernate.show_sql", "true" );
        }

        
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry(); 
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
    }

    public static synchronized SessionFactory getInstance() {

        if ( sessionFactory == null ) {
            buildSessionFactory();
        }

        return sessionFactory;
    }

    /**
     * Returns a session from the session context. If there is no session in the
     * context it opens a session, stores it in the context and returns it. This
     * factory is intended to be used with a hibernate.cfg.xml including the
     * following property <property
     * name="current_session_context_class">thread</property> This would return
     * the current open session or if this does not exist, will create a new
     * session
     * 
     * @return the session
     */
    public static Session getCurrentSession() {
        Session session = null;
        try {
            session = getInstance().getCurrentSession();
        }
        catch ( Exception e ) {
            logger.log( Level.INFO, "No current session, try to open one." );
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

    public static boolean isTransactionActive () {
    	return getCurrentSession().getTransaction().isActive();
    }
    
    public static void rollbackTransaction() {
        try {
            getCurrentSession().getTransaction().rollback();
        }
        catch ( HibernateException e1 ) {
            logger.log( Level.INFO, "Error rolling back transaction", e1 );
        }
    }

    /**
     * closes the session factory
     */
    public static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

        sessionFactory = null;
    }

    /**
     * The username to be use for auditing.
     * Need to set the username before any DB operation to
     * ensure that the username will be store in the audit table.
     * 
     * @param username
     */
    public static void setUser( String username ) {
        currentUser.set( username );
    }

    public static String getUser() {
        return currentUser.get();
    }

}
