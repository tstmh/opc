package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IEnvConfig.properties" })
public interface IEnvConfig extends Config {

    @Key("${env}.allowInsuranceNomination")
    @DefaultValue("false")
    Boolean allowInsuranceNomination();
    
}
