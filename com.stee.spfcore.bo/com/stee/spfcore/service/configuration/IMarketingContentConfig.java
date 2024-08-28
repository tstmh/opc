package com.stee.spfcore.service.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.FIRST)
@Sources({ "file:${spfcore.config}/IMarketingContentConfig.properties" })
public interface IMarketingContentConfig extends Config {
	
	@Key("ecm.content.url")
  @DefaultValue("http://localhost:9080/fncmis/resources/ObjectStoreID/ContentStream/ContentID")
	String ecmContentURL (); 
	
	@Key("ecm.content.objectStoreID")
  @DefaultValue("{26BF139C-6122-40B1-8C49-C28E48D4028A}")
	String ecmObjectStoreID ();

	@Key("ecm.content.username")
  @DefaultValue("bpmadmin")
	String ecmUsername ();
	
	@Key("ecm.content.password")
  @DefaultValue("P@ssw0rd")
	String ecmPassword ();
	
	@Key("portal.attachment.url")
  @DefaultValue("MarketingAttachment")
	String portalAttachmentURL ();
	
	@Key("email.max.recipients.per.message")
  @DefaultValue("100")
	int maxRecipientsPerMessage ();
	
	@Key("portal.attachment.preview.url")
  @DefaultValue("MarketingAttachmentPreview")
	String portalAttachmentPreviewURL ();
	
	@Key("portal.content.preview.url")
  @DefaultValue("MarketingContentPreview")
	String portalContentPreviewURL ();
}
