package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISSRSProxyConfig.properties" })
public interface ISSRSProxyConfig extends Config {
	
	@Key("ssrs.server.hostname")
  @DefaultValue("localhost")
  String hostname();
	
	@Key("ssrs.server.port")
  @DefaultValue("80")
  int port();
	
	@Key("ssrs.server.protocol")
  @DefaultValue("http")
  String protocol();
	
	@Key("ssrs.server.virtualDirectory")
  @DefaultValue("ReportServer")
  String virtualDirectory();
	
	@Key("ssrs.server.username")
  @DefaultValue("Administrator")
  String username();
	
	@Key("ssrs.server.password")
  @DefaultValue("P@ssw0rd")
  String password ();
	
	@Key("ssrs.server.domain")
  @DefaultValue("SPFUAT")
  String domain ();
	
}
