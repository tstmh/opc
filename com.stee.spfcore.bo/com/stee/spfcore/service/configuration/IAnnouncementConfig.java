package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IAnnouncementConfig.properties" })
public interface IAnnouncementConfig extends Config {
	
	@Key("user.announcement.life.time")
  @DefaultValue("90")
	int userAnnouncementLifeTime ();
	
}
