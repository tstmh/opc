package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IBPMEnvConfig.properties" })
public interface IBPMEnvConfig extends Config {
	
	@DefaultValue("dev")
    String env();
	
    @Key("${env}.bpm.fuda.url")
    @DefaultValue("/BPMFUDA/")
    String bpmFudaUrl();

    @Key("${env}.bpm.datasource")
    @DefaultValue("jdbc/spfCoreDB")
    String dataSource();

    @Key("${env}.bpm.dateformat")
    @DefaultValue("dd/MM/yyyy")
    String dateFormat();
    
    @Key("${env}.bpm.filedownload.url")
    @DefaultValue("/BPMFUDA/DownloadFile?filePath=")
    String fileDownloadUrl();
    
    @Key("${env}.bpm.sms.host")
    @DefaultValue("sms.spfcore.gov.sg")
    String smsHost();
    
    @Key("${env}.bpm.smtp.email.auth")
    @DefaultValue("false")
    Boolean smtpEmailAuth();
    
    @Key("${env}.bpm.smtp.host")
    @DefaultValue("smtp.mail.spfcore.gov.sg")
    String smtpHost();
    
    @Key("${env}.bpm.reports.location")
    @DefaultValue("E:/SPF/Reports/")
    String reportsLocation();
    
    @Key("${env}.bpm.uniquedocid")
    @DefaultValue("UniqueDocId")
    String uniqueDocKey();
    
    @Key("${env}.bpm.uploadedfiles.location")
    @DefaultValue("E:/SPF/Uploaded_files/")
    String uploadedFilesLocation();
    
}
