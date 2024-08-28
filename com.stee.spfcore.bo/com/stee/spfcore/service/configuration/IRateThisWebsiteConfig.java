package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IRateThisWebsiteConfig.properties" })
public interface IRateThisWebsiteConfig extends Config {

	@Key("bpm.scheduled.second")
	@DefaultValue("0")
	String scheduledSecound();

	@Key("bpm.scheduled.minute")
	@DefaultValue("0")
	String scheduledMinute();

	@Key("bpm.scheduled.hour")
	@DefaultValue("8")
	String scheduledHour();

	@Key("email.to.addresses")
	@DefaultValue("spf_care_wel_benefits@spf.gov.sg")
	String emailToAddresses();

	@Key("email.user.address")
	@DefaultValue("spf_care_wel_benefits@spf.gov.sg")
	String emailUserAddress();

	@Key("email.user.password")
	@DefaultValue("")
	String emailUserPassword();

	@Key("email.rtw.responseEmail.subject.template.name")
	@DefaultValue("RTW-E001_Subject.vm")
	String rateThisWebsiteResponseEmailSubject();

	@Key("email.rtw.responseEmail.body.template.name")
	@DefaultValue("RTW-E001_Body.vm")
	String rateThisWebsiteResponseEmailBody();
}
