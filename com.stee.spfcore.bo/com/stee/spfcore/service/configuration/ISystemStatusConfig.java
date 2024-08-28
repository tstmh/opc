package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ISystemStatusConfig.properties" })
public interface ISystemStatusConfig extends Config {

	@Key("heartbeat.report.interval.in.seconds")
	@DefaultValue("60")
	int heartbeatReportIntervalInSeconds();

	@Key("heartbeat.grace.period.in.seconds")
	@DefaultValue("15")
	int heartbeatGracePeriodInSeconds();

	@Key("not.serviceable.report.interval.in.seconds")
	@DefaultValue("900")
	int notServiceableReportIntervalInSeconds();

	@DefaultValue("dev")
    String env();

	@Key("${env}.scheduled.second")
	@DefaultValue("15")
	String scheduledSecound();

	@Key("${env}.scheduled.minute")
	@DefaultValue("*")
	String scheduledMinute();

	@Key("${env}.scheduled.hour")
	@DefaultValue("*")
	String scheduledHour();

	@Key("email.to.addresses")
	@DefaultValue("core_tech@spf.gov.sg")
	String emailToAddresses();

	@Key("email.user.address")
	@DefaultValue("spfcore@spf.gov.sg")
	String emailUserAddress();

	@Key("email.user.password")
	@DefaultValue("")
	String emailUserPassword();

	@Key("email.system.servicable.subject.template.name")
	@DefaultValue("System_Serviable_Email_Subject.vm")
	String systemServicableEmailSubject();

	@Key("email.system.servicable.body.template.name")
	@DefaultValue("System_Servicable_Email_Body.vm")
	String systemServicableEmailBody();
}
