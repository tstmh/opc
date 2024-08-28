package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IHRInterfaceConfig.properties" })
public interface IHRInterfaceConfig extends Config {

	@DefaultValue("hrhub")
  String system ();
	
	@Key("${system}.scheduled.second")
  @DefaultValue("*/10")
	String scheduledSecound ();
	
	@Key("${system}.scheduled.minute")
  @DefaultValue("*")
	String scheduledMinute ();
	
	@Key("${system}.scheduled.hour")
  @DefaultValue("*")
	String scheduledHour ();
	
	@Key("${system}.log.folder")
  @DefaultValue("c:\\HRInterface\\log")
	String logFolder ();
	
	@Key("${system}.inbound.folder")
  @DefaultValue("c:\\HRInterface\\inbound")
	String inboundFolder ();
	
	@Key("${system}.working.folder")
  @DefaultValue("c:\\HRInterface\\working")
	String workingFolder ();
	
	@Key("${system}.outbound.folder")
  @DefaultValue("c:\\HRInterface\\outbound")
	String outboundFolder ();
	
	@Key("${system}.inbound.archive.folder")
  @DefaultValue("c:\\HRInterface\\archive\\inbound")
	String inboundArchiveFolder ();
	
	@Key("${system}.outbound.archive.folder")
  @DefaultValue("c:\\HRInterface\\archive\\outbound")
	String outboundArchiveFolder ();

	@Key("${system}.inbound.file")
  @DefaultValue("INPUT.XML")
	String inboundFile ();
	
	@Key("${system}.outbound.file")
  @DefaultValue("OUTPUT.XML")
	String outboundFile ();
	
	@Key("email.status.body.template.name")
  @DefaultValue("HRInterface_Status_Email_Body.vm")
	String statusEmailBody ();
	
	@Key("email.status.subject.template.name")
  @DefaultValue("HRInterface_Status_Email_Subject.vm")
	String statusEmailSubject ();
	
	@Key("email.code.body.template.name")
  @DefaultValue("HRInterface_Code_Email_Body.vm")
	String codeEmailBody ();
	
	@Key("email.code.subject.template.name")
  @DefaultValue("HRInterface_Code_Email_Subject.vm")
	String codeEmailSubject ();
	
	@Key("email.file.missing.body.template.name")
  @DefaultValue("HRInterface_File_Missing_Email_Body.vm")
	String fileMissingEmailBody ();
	
	@Key("email.file.missing.subject.template.name")
  @DefaultValue("HRInterface_File_Missing_Email_Subject.vm")
	String fileMissingEmailSubject ();
	
	@Key("email.servicetype.change.body.template.name")
  @DefaultValue("HRInterface_ServiceType_Change_Email_Body.vm")
	String serviceTypeChangeEmailBody ();
	
	@Key("email.servicetype.change.subject.template.name")
  @DefaultValue("HRInterface_ServiceType_Change_Email_Subject.vm")
	String serviceTypeChangeEmailSubject ();
	
	@Key("email.apply.membership.body.template.name")
  @DefaultValue("HRInterface_Apply_Membership_Email_Body.vm")
	String applyMembershipEmailBody ();
	
	@Key("email.apply.membership.subject.template.name")
  @DefaultValue("HRInterface_Apply_Membership_Email_Subject.vm")
	String applyMembershipEmailSubject ();
	
	@Key("email.to.addresses")
  @DefaultValue("core_tech@spf.gov.sg")
	String emailToAddresses ();
	
	@Key("email.user.address")
  @DefaultValue("spfcore@spf.gov.sg")
	String emailUserAddress ();
	
	@Key("email.user.password")
  @DefaultValue("")
	String emailUserPassword ();
	
	
}
