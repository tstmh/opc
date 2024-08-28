package com.stee.spfcore.webapi.service.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IMailSenderConfig.properties" })
public interface IMailSenderConfig  extends Config {
	
	@DefaultValue("module")
  String module();
	
	@Key("${module}.sender.email.address")
  @DefaultValue("spfcore@spf.gov.sg")
  String senderAddress ();

	@Key("${module}.sender.email.password")
  @DefaultValue("password")
  String senderPassword();
}