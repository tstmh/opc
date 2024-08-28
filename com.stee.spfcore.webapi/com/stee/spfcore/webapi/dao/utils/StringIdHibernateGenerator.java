package com.stee.spfcore.webapi.dao.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

public class StringIdHibernateGenerator implements IdentifierGenerator, PersistentIdentifierGenerator {

    private static final Logger logger = Logger.getLogger( StringIdHibernateGenerator.class.getName() );

    private static String DEFAULT_SCHEMA = "SPFCORE";

    private Dialect dialect;
    private String sequenceName;
    private String sequenceNameWithSchema;
    private String sequenceCallSyntax;
    
//    private EntityManager entityManager;
    
    public StringIdHibernateGenerator() {
    }
//    @Lazy
//	@Autowired
//	public StringIdHibernateGenerator(EntityManager entityManager) {
//		this.entityManager = entityManager;
//	}

    @Override
    @Transactional
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
              
    	List< Field > fields = getAnnotatedFields( object );
    	
    	if ( fields.size() == 0 ) {
            throw new HibernateException( "No identify field in class:" + object.getClass() );
        }
        else if ( fields.size() > 1 ) {
            throw new HibernateException( "More than one identify field in class:" + object.getClass() );
        }
    	
    	String id = readFieldValue( object, fields.get( 0 ) );
        logger.info( "ID value: " + id );
    	
        // Only generate new id if the id null or empty.
        if ( id == null || id.trim().isEmpty() ) {
        	logger.info("sqName>>>>>>>"+sequenceName);
        	logger.info("sqCallSyntax>>>>>>>"+sequenceCallSyntax);
        	
        	
//        	Session session1 = entityManager.unwrap(Session.class);
//        	session1.flush();
//        	long seqValue = ((Number) session1.createNativeQuery(sequenceCallSyntax)
//					            .uniqueResult()
//					        ).longValue();

//        	long test = 0;
//        	Query query = Session.class.cast(session)
//            .createNativeQuery(sequenceCallSyntax);
//        	long res = (long) query.getSingleResult();
//        	logger.info("log>>>>>>>>>>>>>"+res);
        	
        
        	long seqValue = ((Number)
                    Session.class.cast(session)
                    .createNativeQuery(sequenceCallSyntax)
                    .uniqueResult()
                ).longValue();
        	
        	logger.log (Level.INFO, "generate: " + String.format( "%s_%s", sequenceName, seqValue));
        	
            id = String.format( "%s_%s", sequenceName, seqValue );
        }
 
        return id;
    }
    
    @Override
    public void configure( Type type, Properties params, ServiceRegistry serviceRegistry ) throws MappingException {
        
    	JdbcEnvironment jdbcEnvironment = serviceRegistry.getService(JdbcEnvironment.class);
    	this.dialect = jdbcEnvironment.getDialect();
    	
        String targetTable = params.getProperty( "target_table" );
        sequenceName = ( targetTable + "_seq" ).toUpperCase();
        sequenceNameWithSchema = DEFAULT_SCHEMA + "." + sequenceName;
        logger.info("name with schema>>>"+sequenceNameWithSchema);
        sequenceCallSyntax = dialect.getSequenceNextValString( sequenceNameWithSchema );
        
    }

    /**
     * Find all field that is annotated with id.
     * 
     * @param object
     * @return
     */
    private List< Field > getAnnotatedFields( Object object ) {

        final List< Field > declaredFields = getAllFieldsList( object.getClass() );
        final List< Field > annotatedFields = new ArrayList< Field >();

        for ( Field field : declaredFields ) {
            if ( field.getAnnotation( Id.class ) != null ) {
                annotatedFields.add( field );
            }
        }

        return annotatedFields;
    }

    public List< Field > getAllFieldsList( final Class< ? > cls ) {
        final List< Field > allFields = new ArrayList< Field >();
        Class< ? > currentClass = cls;
        while ( currentClass != null ) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for ( final Field field : declaredFields ) {
                allFields.add( field );
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    private String readFieldValue( Object object, Field field ) {
        try {
            return ( String ) FieldUtils.readField( field, object, true );
        }
        catch ( IllegalArgumentException | IllegalAccessException e ) {
            throw new HibernateException( "Unable to access id field in class:" + object.getClass() );
        }
    }
    
}
