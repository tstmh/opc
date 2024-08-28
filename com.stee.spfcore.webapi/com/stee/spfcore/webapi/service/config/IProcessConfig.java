package com.stee.spfcore.webapi.service.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IProcessConfig.properties" })
public interface IProcessConfig extends Config {

    @Key("bpm.server.hostname")
    @DefaultValue("127.0.0.1")
    String hostname();

    @Key("bpm.server.port")
    @DefaultValue("9080")
    int port();

    @Key("bpm.server.username")
    @DefaultValue("administrator")
    String username();

    @Key("bpm.server.password")
    @DefaultValue("P@ssw0rd")
    String password();
    
    @DefaultValue("test")
    String process();

    @Key("${process}.bpdId")
    @DefaultValue("")
    String bpdId();

    @Key("${process}.branchId")
    @DefaultValue("")
    String branchId();
    
    @Key("${process}.snapshotId")
    @DefaultValue("")
    String snapshotId();
}
