package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IFileHandlingConfig.properties" })
public interface IFileHandlingConfig extends Config {
	
	@DefaultValue("dev")
  String env();
	
	@Key("feb.content.storage.service.hostname")
  @DefaultValue("localhost")
	String hostname ();
	
	@Key("feb.content.storage.service.port")
  @DefaultValue("10039")
	int port ();
	
	@Key("feb.content.storage.service.username")
  @DefaultValue("")
	String username ();
	
	@Key("feb.content.storage.service.password")
  @DefaultValue("")
	String password ();
	
	@Key("${env}.working.folder")
  @DefaultValue("c:\\temp")
  String workingFolder();

	@Key("${env}.ftp.folder")
  @DefaultValue("c:\\ftp")
  String ftpFolder();
	
	@Key("${env}.scheduled.second")
  @DefaultValue("*/10")
	String scheduledSecound ();
	
	@Key("${env}.scheduled.minute")
  @DefaultValue("*")
	String scheduledMinute ();
	
	@Key("${env}.scheduled.hour")
  @DefaultValue("*")
	String scheduledHour ();
	
	@Key("feb.delay.before.apply.second")
  @DefaultValue("5")
	int delayBeforeApply ();
}
