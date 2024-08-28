package com.stee.spfcore.service.configuration;

import java.util.List;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;


@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IMailRecipientConfig.properties" })
public interface IMailRecipientConfig extends Config {
	
	@DefaultValue("module")
  String module();
	
	@Key("${module}.recipient.to.groups")
  @DefaultValue("")
	List<String> toRecipientGroups ();
	
	@Key("${module}.recipient.cc.groups")
  @DefaultValue("")
	List<String> ccRecipientGroups ();

}

