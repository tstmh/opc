package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISearchConfig.properties" })

public interface ISearchConfig extends Config 
{
	@Key("search.service.hostname")
	@DefaultValue("localhost")
	String hostname ();
	
	@Key("search.service.localhost")
	@DefaultValue("localhost")
	String localhost ();

	@Key("search.service.port")
	@DefaultValue("10039")
	String port ();
	
	@Key("search.service.sslport")
	@DefaultValue("10041")
	String sslport ();
	
	@Key("search.service.anonscope")
	@DefaultValue("1509432829522")
	String anonscope ();
	
	@Key("search.service.scope")
	@DefaultValue("1509432926252")
	String scope ();
	
	@Key("search.service.username")
	@DefaultValue("wpsadmin")
	String username ();
	
	@Key("search.service.password")
	@DefaultValue("password")
	String password ();
}
