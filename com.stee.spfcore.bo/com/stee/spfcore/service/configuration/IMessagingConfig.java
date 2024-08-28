package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IMessagingConfig.properties" })
public interface IMessagingConfig extends Config {

	@DefaultValue("dev")
    String env();

	@Key("${env}.mq.disabled")
    @DefaultValue("true")
    Boolean disabled();
	
    @Key("${env}.mq.hostname")
    @DefaultValue("127.0.0.1")
    String hostname();

    @Key("${env}.mq.port")
    @DefaultValue("1414")
    int port();

    @Key("${env}.mq.channel")
    @DefaultValue("mychannel")
    String channel();

    @Key("${env}.mq.queue.manager")
    @DefaultValue("mymgr")
    String manager();

    @Key("${env}.mq.remote.queue")
    @DefaultValue("localq")
    String queue();

    @Key("${env}.mq.ssl.enabled")
    @DefaultValue("false")
    Boolean sslEnabled();

    @Key("${env}.mq.ssl.cipher.suite")
    @DefaultValue("")
    String sslCipherSuite();

    @Key("${env}.mq.ssl.trust.store.path")
    @DefaultValue("")
    String sslTrustStorePath();

    @Key("${env}.mq.ssl.key.store.path")
    @DefaultValue("")
    String sslKeyStorePath();

    @Key("${env}.mq.ssl.password")
    @DefaultValue("")
    String sslPassword();

    @Key("${env}.mq.rf.header.mail.id")
    @DefaultValue("0")
    Integer mailId();

    @Key("${env}.mq.rf.header.request.number")
    @DefaultValue("0")
    Integer requestNumber();

    @Key("${env}.mq.rf.header.mail.number")
    @DefaultValue("0")
    Integer mailNumber();

    @Key("${env}.mq.rf.header.subject.id")
    @DefaultValue("0")
    Integer subjectId();

    @Key("${env}.mq.rf.header.source.system.id")
    @DefaultValue("0")
    Integer sourceSystemId();
    
    @Key("unhandled.msgs.log.enabled")
    @DefaultValue("false")
    Boolean unhandledMsgsLogEnabled();

    @Key("unhandled.msgs.log.path")
    @DefaultValue("")
    String unhandledMsgsLogPath();
}
