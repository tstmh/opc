package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IStudyAwardConfig.properties" })
public interface IStudyAwardConfig extends Accessible {

	@Key("sag.scheduled.second")
	  @DefaultValue("*/10")
		String scheduledSecond ();
		
		@Key("sag.scheduled.minute")
	  @DefaultValue("*")
		String scheduledMinute ();
		
		@Key("sag.scheduled.hour")
	  @DefaultValue("*")
		String scheduledHour ();
	
}