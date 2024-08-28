package com.stee.spfcore.service.configuration;

import java.util.List;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IWelfareCourseConfig.properties" })
public interface IWelfareCourseConfig extends Config {

	@Key("vacancy.allocation.task.scheduled.second")
  @DefaultValue("0")
	String vacancyAllocationTaskScheduledSecound ();
	
	@Key("vacancy.allocation.task.scheduled.minute")
  @DefaultValue("0")
	String vacancyAllocationTaskScheduledMinute ();
	
	@Key("vacancy.allocation.task.scheduled.hour")
  @DefaultValue("8")
	String vacancyAllocationTaskScheduledHour ();
	
	@Key ("vacancy.allocation.broadcast.systemTaskOver.days")
	@DefaultValue ("5")
	int daysBeforeSystemTakeover ();
	
	@Key ("vacancy.allocation.broadcast.reminder.days")
	@DefaultValue ("3")
	int daysBeforeReminder ();
	
	/**
	 * 385 - POLICE SR (HQ) PERSONNEL
	 * 389 - POLICE JR (HQ) PERSONNEL
	 * 412 - GURKHA CONTINGENT PERSONNEL
	 * 111 - NON-SPF UNIFORMED PERSONNEL
	 */
	@DefaultValue("385, 389, 412, 111")
	@Key("uniformed.officer.serviceType.codes")
	List<String> uniformedOfficerServiceTypeCodes ();
	
	
	/**
	 * 338 - POLICE CIVILIAN (HQ) PERSONNEL
	 * 000 - NON-SPF CIVILIAN PERSONNEL
	 */
	@DefaultValue("338, 000")
	@Key("civilian.officer.serviceType.codes")
	List<String> civilianOfficerServiceTypeCodes ();
	
	
	@DefaultValue("222")
	@Key("pnsf.officer.serviceType.codes")
	List<String> pnsfOfficerServiceTypeCode ();
	
	
	
}
