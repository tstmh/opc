package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ITemplateConfig.properties" })
public interface ITemplateConfig extends Config {
	
	@Key("template.folder")
	@DefaultValue("c:\\template")
	String templateFolder ();
	
	@Key("resource.loader.datasource")
  @DefaultValue("jdbc/SPFCoreDS")
	String dataSource ();
	
	@Key("resource.loader.cache")
  @DefaultValue("false")
	boolean cacheTemplate ();
	
	@Key("resource.loader.modificationCheckInterval")
  @DefaultValue("60")
	long modificationCheckInterval ();
	
}
