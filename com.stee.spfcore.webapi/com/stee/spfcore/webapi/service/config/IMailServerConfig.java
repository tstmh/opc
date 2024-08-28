package com.stee.spfcore.webapi.service.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IMailServerConfig.properties" })
public interface IMailServerConfig extends Config {

	@DefaultValue("dev")
    String env();
	
    @Key("${env}.smtp.hostname")
    @DefaultValue("127.0.0.1")
    String hostname();

    @Key("${env}.smtp.port")
    @DefaultValue("25")
    int port();

    @Key("${env}.smtp.protocol")
    @DefaultValue("ssl")
    String protocol();
    
    @Key("${env}.smtp.system.email.address")
    @DefaultValue("spfcore@spf.gov.sg")
    String systemEmailAddress();
    
    
    @Key("${env}.smtp.system.email.password")
    @DefaultValue("password")
    String systemEmailPassword();
    
    @Key("${env}.max.recipients.per.message")
    @DefaultValue("100")
  	int maxRecipientsPerMessage ();
    
}
