package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy( LoadType.FIRST )
@Sources( { "file:${spfcore.config}/IDAOSessionConfig.properties" } )
public interface IDAOSessionConfig extends Config {

    @Key( "hibernate.connection.datasource" )
    @DefaultValue( "jdbc/SPFCoreDS" )
    String dataSource();

    @Key( "hibernate.show_sql" )
    @DefaultValue( "false" )
    boolean showSQL();

    @Key( "hibernate.connection.url" )
    @DefaultValue( "" )
    String connectionUrl();

    @Key( "hibernate.connection.username" )
    @DefaultValue( "" )
    String connectionUsername();

    @Key( "hibernate.connection.password" )
    @DefaultValue( "" )
    String connectionPassword();

    @Key( "hibernate.connection.driver_class" )
    @DefaultValue( "" )
    String connectionDriverClass();

    @Key( "hibernate.dialect" )
    @DefaultValue( "" )
    String hibernateDialect();

    @Key( "local.test" )
    @DefaultValue( "false" )
    boolean localTest();

    @Key( "hibernate.jndi.class" )
    @DefaultValue( "" )
    String jndiClass();

    @Key( "hibernate.jndi.url" )
    @DefaultValue( "" )
    String jndiURL();

    @Key( "hibernate.jndi.java.naming.security.principal" )
    @DefaultValue( "" )
    String jndiUsername();

    @Key( "hibernate.jndi.java.naming.security.credentials" )
    @DefaultValue( "" )
    String jndiPassword();

    @Key( "bpmsuser.datasource" )
    @DefaultValue( "jdbc/TeamWorksDB" )
    String bpmsuserDatasource();

    @Key( "bpmsuser.schema" )
    @DefaultValue( "bpmsuser" )
    String bpmsuserSchema();
}
