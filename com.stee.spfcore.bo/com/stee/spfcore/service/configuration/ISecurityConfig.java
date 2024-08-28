package com.stee.spfcore.service.configuration;

import java.util.List;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISecurityConfig.properties" })
public interface ISecurityConfig extends Config {

	@Key("system.users")
	@DefaultValue("bpmadmin,wpsadmin")
	List<String> systemUsers ();
	
	@Key("welfare.groups")
	@DefaultValue("Welfare*,Staff Well-Being Head,Staff Well-Being Processing Officer,Assistant Staff Well-being Processing Officer")
	List<String> welfareGroups ();
	
	@Key("spf.upo.groups")
	@DefaultValue("SPF Unit Processing Officer")
	List<String> spfUPOGroups ();
	
	@Key("non.spf.upo.groups")
	@DefaultValue("Non-SPF Unit Processing Officer")
	List<String> nonSpfUPOGroups ();
	
	@Key("spf.lg.groups")
	@DefaultValue("SPFCORE_LG Officers")
	List<String> spfLGGroups ();
	
	
}
