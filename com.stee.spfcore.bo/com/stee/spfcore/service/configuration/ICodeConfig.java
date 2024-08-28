package com.stee.spfcore.service.configuration;

import java.util.List;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;


@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/ICodeConfig.properties" })
public interface ICodeConfig extends Config {

	@Key("code.serviceType.nonSPFUniformedPersonnel")
  @DefaultValue("111")
	String nonSPFUniServType ();
	
	@Key("code.serviceType.nonSPFCivilianPersonnel")
  @DefaultValue("000")
	String nonSPFCivServType ();
	
	@Key("code.employmentStatus.active")
  @DefaultValue("3")
	String activeEmploymentStatus ();
	
	@Key("code.serviceType.policeCivilianHQPersonnel")
  @DefaultValue("338")
	String policeCivHQServType ();
	
	@Key("code.serviceType.pnsmen")
  @DefaultValue("PNSMEN")
	String pnsmen ();
	
	@Key("code.idType.hrhub.nricOrFin")
  @DefaultValue("01,02,F1,F2,F5,ZA,ZB")
	List<String> hrhubNricFinType ();
	
}
