package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISAGConfig.properties" })
public interface ISAGConfig extends Accessible {

	@Key("sag.pcwfSpocEmail")
	@DefaultValue("shen_delun@spf.gov.sg")
	String pcwfSpocEmail();
	
	@Key("sag.spcsSpocEmail")
	@DefaultValue("Lee_Lee_Kiang@spf.gov.sg")
	String spcsSpocEmail();
	
	@Key("feb.scheduled.second")
	@DefaultValue("0")
	String scheduledSecound();

	@Key("feb.scheduled.minute")
	@DefaultValue("1")
	String scheduledMinute();

	@Key("feb.scheduled.hour")
	@DefaultValue("0")
	String scheduledHour();
}
