package com.stee.spfcore.dao.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.Id;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;

public class StringIdHibernateGenerator implements IdentifierGenerator, Configurable, PersistentIdentifierGenerator {

    private static final Logger logger = Logger.getLogger( StringIdHibernateGenerator.class.getName() );

    private static String DEFAULT_SCHEMA = "SPFCORE";

    private Dialect dialect;
    private String sequenceName;
    private String sequenceNameWithSchema;

    private String[] createSequenceSQL;
    private String[] dropSequenceSQL;
    private String nextValueSQL;

    public StringIdHibernateGenerator() {
        // This method is intentionally left empty
    }

    @Override
    public Serializable generate( SessionImplementor session, Object object ) throws HibernateException {

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
            id = generateId( session, dialect );
        }

        return id;
    }

    @Override
    public void configure( Type type, Properties params, Dialect dialect ) throws MappingException {
        this.dialect = dialect;

        String targetTable = params.getProperty( "target_table" );
        sequenceName = ( targetTable + "_seq" ).toUpperCase();
        sequenceNameWithSchema = DEFAULT_SCHEMA + "." + sequenceName;

        createSequenceSQL = dialect.getCreateSequenceStrings( sequenceNameWithSchema, 0, 1 );

        dropSequenceSQL = dialect.getDropSequenceStrings( sequenceNameWithSchema );

        nextValueSQL = dialect.getSequenceNextValString( sequenceNameWithSchema );
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

    private String generateId( SessionImplementor session, Dialect dialect ) {

        logger.info( String.format( "Executing SQL: %s", nextValueSQL ) );

        String id = null;

        try (Connection connection = session.connection();
             PreparedStatement statement = connection.prepareStatement(nextValueSQL);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                id = String.valueOf(rs.getInt(1));
            }

        }
        catch ( SQLException e ) {
            throw new HibernateException( "Fail to get sequence from " + sequenceNameWithSchema );
        }

        return String.format( "%s_%s", sequenceName, id );
    }

    @Override
    public Object generatorKey() {
        return sequenceNameWithSchema;
    }

    @Override
    public String[] sqlCreateStrings( Dialect arg0 ) throws HibernateException {
        return createSequenceSQL;
    }

    @Override
    public String[] sqlDropStrings( Dialect arg0 ) throws HibernateException {
        return dropSequenceSQL;
    }
}
