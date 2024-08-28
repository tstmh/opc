package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IPortalUserConfig.properties" })
public interface IPortalUserConfig extends Config {

	
	@Key("generatedPassword.enabled")
	@DefaultValue("true")
	boolean useGeneratedPassword ();
	
	@Key("password.default")
	@DefaultValue("")
	String defaultPassword ();
	
	@Key("userGroup.default")
	@DefaultValue("spfcore")
	String defaultUserGroup ();
	
	@Key("userGroup.cto")
	@DefaultValue("SPFCORE_CTO")
	String ctoUserGroup ();
	
	@Key("puma.rest.hostname")
	@DefaultValue("localhost")
	String pumaRestHostName ();
	
	@Key("puma.rest.port")
	@DefaultValue("10039")
	int pumaRestPort ();
	
	@Key("puma.rest.username")
	@DefaultValue("")
	String pumaRestUsername ();
	
	@Key("puma.rest.password")
	@DefaultValue("")
	String pumaRestPassword ();
	
	
	
}
