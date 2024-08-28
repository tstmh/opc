package com.stee.spfcore.webapi.service.config;

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
	
//	@Key("${module}.recipient.to.addresses")
//  @DefaultValue("")
//	List<String> toRecipientAddresses ();
//	
//	@Key("${module}.recipient.cc.addresses")
//  @DefaultValue("")
//	List<String> ccRecipientAddresses ();
}

