package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISmsServerConfig.properties" })
public interface ISmsServerConfig extends Config {

	@DefaultValue("dev")
    String env();
	
    @Key("${env}.sms.hostname")
    @DefaultValue("127.0.0.1")
    String hostname();

    @Key("${env}.sms.port")
    @DefaultValue("443")
    int port();
    
    @Key("${env}.sms.protocol")
    @DefaultValue("ssl")
    String protocol();
    
    @Key("${env}.sms.truststore")
    @DefaultValue("")
    String trustStore();
    
    @Key("${env}.sms.truststore.password")
    @DefaultValue("")
    String trustStorePassword();
    
    
}
