package com.stee.spfcore.service.marketingContent;

import java.util.List;

import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.marketingContent.internal.HtmlFile;
import com.stee.spfcore.model.marketingContent.internal.UserContentViewRecord;
import com.stee.spfcore.vo.marketingContent.BinaryAttachment;
import com.stee.spfcore.vo.marketingContent.ContentTemplateInfo;

public interface IMarketingContentService {

	public void addMarketingContentSet (MarketingContentSet contentSet, String requester) throws MarketingContentException;
	
	public void updateMarketingContentSet (MarketingContentSet contentSet, String requester) throws MarketingContentException;
	
	public void deleteMarketingContentSet (String id, String requester) throws MarketingContentException;
	
	public MarketingContentSet getMarketingContentSet (String id) throws MarketingContentException;
	
	public MarketingContent getMarketingContent (String id) throws MarketingContentException;
	
	public List<MarketingContent> getMarketingContents (String module) throws MarketingContentException;
	
	public List<MarketingContent> getMarketingContents (String module, boolean templateBased) throws MarketingContentException;
	
	public String generateId () throws MarketingContentException;
	
	/**
	 * To be used by other services. Transaction must already be started before invoking this method 
	 * @param contentId
	 * @param emails
	 * @throws MarketingContentException
	 */
	public void publishContentSet (String contentSetId, List<String> emails, List<String> phones, String senderEmail, 
													String senderPassword, String requester) throws MarketingContentException;
	
	/**
	 * To be used by other services. Transaction must already be started before invoking this method 
	 * @param contentId
	 * @throws MarketingContentException
	 */
	public void cancelPublishContentSet (String contentSetId, String requester) throws MarketingContentException;
	
	/**
	 * To be used by other services. Transaction must already be started before invoking this method 
	 * @param contentId
	 * @throws MarketingContentException
	 */
	public void appendContentRecipients (String contentId, List<String> emails, List<String> phones, String requester) throws MarketingContentException;
	
	/**
	 * To be used by other services. Transaction must already be started before invoking this method 
	 * @param contentId
	 * @throws MarketingContentException
	 */
	public void removeContentRecipients (String contentId, List<String> emails, List<String> phones, String requester) throws MarketingContentException;
	
	
	/**
	 * Invoke by TaskProcessing Timer EJB 
	 * @throws MarketingContentException
	 */
	public void processTask () throws MarketingContentException;
	
	public BinaryFile getBinaryFile (String id) throws MarketingContentException;
	
	public BinaryFile getBinaryFile (String nric, String id) throws MarketingContentException;
	
	public HtmlFile getHtmlFile (String nric, String id) throws MarketingContentException;
	
	public void addUserContentViewRecord (UserContentViewRecord record) throws MarketingContentException;
	
	public List<BinaryAttachment> getAttachments (String contentId) throws MarketingContentException;
	
	public List<ContentTemplateInfo> getTemplateInfos () throws MarketingContentException;
	
	public void sendEmailPreview (String contentId, String emailAddress) throws MarketingContentException;
	
	public String getPreviewHtmlFile (String contentId) throws MarketingContentException;
	
	public BinaryFile getPreviewBinaryFile (String id) throws MarketingContentException;
}
