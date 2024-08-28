package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;


@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISupportConfig.properties" })

public interface ISupportConfig extends Config {
	@DefaultValue("dev")
	  String env();
	
	@Key("rest.bpm.prefix")
	  @DefaultValue("http://")
		String prefix ();
	
	@Key("rest.bpm.hostname")
	  @DefaultValue("localhost")
		String hostname ();
	
	@Key("rest.bpm.port")
	  @DefaultValue("9080")
		String port ();
	
	@Key("rest.bpm.user")
	  @DefaultValue("bpmadmin")
		String user ();
	
	@Key("rest.bpm.password")
	  @DefaultValue("P@ssw0rd")
		String password ();
}
