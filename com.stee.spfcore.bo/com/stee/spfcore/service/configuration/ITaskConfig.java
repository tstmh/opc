package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy( LoadType.FIRST )
@Sources( { "file:${spfcore.config}/ITaskConfig.properties" } )
public interface ITaskConfig extends Config {

    @DefaultValue( "dev" )
    String env();

    @Key( "${env}.scheduled.second" )
    @DefaultValue( "*/10" )
    String scheduledSecound();

    @Key( "${env}.scheduled.minute" )
    @DefaultValue( "*" )
    String scheduledMinute();

    @Key( "${env}.scheduled.hour" )
    @DefaultValue( "*" )
    String scheduledHour();

    @Key( "${env}.announcementTask.enabled" )
    @DefaultValue( "true" )
    Boolean announcementTaskEnabled();

    @Key( "${env}.marketingContentTask.enabled" )
    @DefaultValue( "true" )
    Boolean marketingContentTaskEnabled();

    @Key( "${env}.surveyTask.enabled" )
    @DefaultValue( "true" )
    Boolean surveyTaskEnabled();

    @Key( "${env}.courseTask.enabled" )
    @DefaultValue( "true" )
    Boolean courseTaskEnabled();

    @Key( "${env}.genericEventTask.enabled" )
    @DefaultValue( "true" )
    Boolean genericEventTaskEnabled();
}
