package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy( LoadType.FIRST )
@Sources( { "file:${spfcore.config}/IPlannerConfig.properties" } )
public interface IPlannerConfig extends Accessible {

    @Key( "planner.acceptedCountLimit" )
    @DefaultValue( "100" )
    int acceptedCountLimit();

    @Key( "planner.terminationDuration" )
    @DefaultValue( "120" )
    int terminationDuration();

    @Key( "planner.isExportEnabled" )
    @DefaultValue( "true" )
    boolean isExportEnabled();

    @Key( "planner.exportPath" )
    @DefaultValue( "c:\\spfcore\\planner\\output" )
    String exportPath();

    @Key( "planner.isDebugEnabled" )
    @DefaultValue( "true" )
    boolean isDebugEnabled();

    @Key( "planner.seed" )
    @DefaultValue( "0" )
    long seed();
}
