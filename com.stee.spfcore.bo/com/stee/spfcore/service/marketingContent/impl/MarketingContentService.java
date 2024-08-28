package com.stee.spfcore.service.marketingContent.impl;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.marketingContent.*;
import com.stee.spfcore.model.marketingContent.internal.*;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.IMarketingContentConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.marketingContent.MarketingContentException;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.marketingContent.BinaryAttachment;
import com.stee.spfcore.vo.marketingContent.ContentTemplateInfo;
import org.hibernate.HibernateException;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;

public class MarketingContentService extends AbstractMarketingContentService {

	private static final String EMAIL_MODULE = "MC_PREVIEW";
	
	private ECMUtil ecmUtil;
	private HtmlUtil htmlUtil;
	private HtmlUtil previewHtmlUtil;
	private EmailUtil emailUtil;
	private SmsUtil smsUtil;
	private TemplateHelper templateHelper;
	private String attachmentUrl;
	private String attachmentPreviewUrl;
	
	public MarketingContentService() {
		super ();
		
		try {
			ecmUtil = new ECMUtil();
		}
		catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Fail to construct ECMUtil.", e);
		}
		
		IMarketingContentConfig config = ServiceConfig.getInstance().getMarketingContentConfig();
		attachmentUrl = config.portalAttachmentURL()  + "?docId=";
		attachmentPreviewUrl = config.portalAttachmentPreviewURL()  + "?docId=";
		
		htmlUtil = new HtmlUtil(config.ecmContentURL(), attachmentUrl);
		previewHtmlUtil = new HtmlUtil(config.ecmContentURL(), attachmentPreviewUrl);
		emailUtil = new EmailUtil(config.ecmContentURL(), ecmUtil);
		smsUtil = new SmsUtil();
		templateHelper = new TemplateHelper();
	}
	
	
	@Override
	public void addMarketingContentSet(MarketingContentSet contentSet, String requester) throws MarketingContentException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			cleanUpHtml (contentSet);
			
			dao.addMarketingContentSet(contentSet, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add MarketingContentSet", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to add MarketingContentSet", e);
		}
	}

	private void cleanUpHtml (MarketingContentSet contentSet) {
		for (MarketingContent content : contentSet.getContents()) {
			String html = content.getHtmlContent();
			if (html != null) {
				content.setHtmlContent(htmlUtil.removeMceAttribute(html));
			}
		}
	}
	
	
	@Override
	public void updateMarketingContentSet(MarketingContentSet contentSet, String requester)
			throws MarketingContentException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			cleanUpHtml (contentSet);
			
			dao.updateMarketingContentSet(contentSet, requester);
			
			// If already published (has PublishingContentSetTask), need to create PublishingContent 
			// for new Content. 
			PublishingContentSetTask task = dao.getPublishingContentSetTask(contentSet.getId());
			if (task != null) {
				Set<String> emails = new HashSet<>();
				Set<String> phones = new HashSet<>();
				
				Date now = new Date ();
				
				// Find the list of content id for the new content set
				List<String> marketingContentIds = new ArrayList<>();
				for (MarketingContent content : contentSet.getContents()) {
					marketingContentIds.add(content.getId());
				}
				
				// Form the list of content id in PublishingContentSetTask
				// At the same time, remove the PublishingContent from 
				// PublishingContentSetTask if it is not in the new MarketingContentSet
				List<String> publishedContentIds = new ArrayList<>();
				
				ListIterator<PublishingContent> iterator = task.getContents().listIterator();
				while (iterator.hasNext()) {
					PublishingContent publishingContent = iterator.next();
					
					// If the publishingContent not in the new MarketingContentSet, remove it.
					if (!marketingContentIds.contains(publishingContent.getContentId())) {
						iterator.remove();
					}
					
					publishedContentIds.add(publishingContent.getContentId());
					
					emails.addAll(publishingContent.getEmails());
					phones.addAll(publishingContent.getPhones());
				}
				
				List<String> emailList = new ArrayList<>(emails);
				List<String> phoneList = new ArrayList<>(phones);
				
				// If the MarketingContent don't have corresponding PublishingContent,
				// and the publishing date has not reach yet, create it.
				for (MarketingContent content : contentSet.getContents()) {
					if ((!publishedContentIds.contains(content.getId())) && (now.before(content.getActualPublishDate()))){
						PublishingContent publishingContent = new PublishingContent(0, content.getId(), false, content.getActualPublishDate(),
																																				false, emailList, phoneList);
						task.getContents().add(publishingContent);
					}
				}
				
				dao.updatePublishingContentSetTask(task, requester);
			}
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update MarketingContentSet", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to update MarketingContentSet", e);
		}
		
	}

	@Override
	public void deleteMarketingContentSet(String id, String requester)
			throws MarketingContentException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			MarketingContentSet contentSet = dao.getMarketingContentSet(id);
			
			dao.deleteMarketingContentSet(contentSet, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete MarketingContentSet", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to delete MarketingContentSet", e);
		}
		
	}

	
	@Override
	public void publishContentSet(String contentSetId, List<String> emails, List<String> phones, String senderEmail, String senderPassword, String requester) 
													throws MarketingContentException {
		
		try {
			
			MarketingContentSet contentSet = dao.getMarketingContentSet(contentSetId);
			
			List<PublishingContent> contentStates = new ArrayList<>();
			
			for (MarketingContent content : contentSet.getContents()) {
				List<String> phoneList = null;
				if (content.isSmsContentAvailable()) {
					phoneList = phones;
				}
				else {
					phoneList = Collections.emptyList();
				}
				
				PublishingContent publishingContent = new PublishingContent(0, content.getId(), false, content.getActualPublishDate(), false, emails, phoneList);
				contentStates.add(publishingContent);
				
				logger.log(Level.INFO, "Save attachment " + content.getAttachments().size());
				// Save attachment
				for (Attachment attachment : content.getAttachments()) {
					BinaryFile binaryFile = ecmUtil.download(attachment.getDocId());
					binaryFile.setAttachment(true);
					binaryFile.setContentId(content.getId());
					dao.saveBinaryFile(binaryFile, requester);
				}
				
				logger.log(Level.INFO, "Save content, isTemplateBased? " + content.isTemplateBased());
				if (content.isTemplateBased()) {
					logger.log(Level.INFO, "saveTemplateContent ");
					saveTemplateContent (content, requester);
				}
				else {
					logger.log(Level.INFO, "saveECMContent ");
					saveECMContent (content, requester);
				}
			}
			
			PublishingContentSetTask task = new PublishingContentSetTask(0, contentSetId, false, contentStates, false, senderEmail, senderPassword);
			
			logger.log(Level.INFO, "Adding Publishing Content Set Task");
			dao.addPublishingContentSetTask (task, requester);
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete MarketingContentSet", e);
			throw new MarketingContentException ("Fail to delete MarketingContentSet", e);
		}
	}


	@Override
	public void cancelPublishContentSet(String contentSetId, String requester) throws MarketingContentException {
		
		try {
			
			PublishingContentSetTask task = dao.getPublishingContentSetTask(contentSetId);
			
			task.setCancelled(true);
			
			dao.updatePublishingContentSetTask(task, requester);
			
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to cancel PublishingContentSetTask", e);
			throw new MarketingContentException ("Fail to cancel PublishingContentSetTask", e);
		}
	}

	
	@Override
	public void appendContentRecipients(String contentId, List<String> emails, List<String> phones, String requester)
			throws MarketingContentException {
		
		try {
			PublishingContent content = dao.getPublishingContent(contentId);
			
			List<String> orgEmails = content.getEmails();
			
			for (String email : emails) {
				if (!orgEmails.contains(email)) {
					orgEmails.add(email);
				}
			}
			
			List<String> orgPhones = content.getPhones();
			for (String phone : phones) {
				if (!orgPhones.contains(phone)) {
					orgPhones.add(phone);
				}
			}
			
			dao.updatePublishingContent(content, requester);
			
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update PublishingContent", e);
			throw new MarketingContentException ("Fail to update PublishingContent", e);
		}
	}

	
	
	
	@Override
	public void removeContentRecipients(String contentId, List<String> emails, List<String> phones, String requester)
			throws MarketingContentException {
		
		try {
			PublishingContent content = dao.getPublishingContent(contentId);
			
			for (String email : emails) {
				content.getEmails().remove(email);
			}

			for (String phone : phones) {
				content.getPhones().remove(phone);
			}
			
			dao.updatePublishingContent(content, requester);
			
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to remove Publishing Content Recipients", e);
			throw new MarketingContentException ("Fail to remove Publishing Content Recipients", e);
		}
	}


	@Override
	public void processTask() throws MarketingContentException {
		
		transferContents ();
		
		sendNotifications ();
	}
	
	
	private void transferContents () throws MarketingContentException {
		
		List<PublishingContent> contents = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			contents = dao.getPendingTransferContents();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get publishing content pending transfer", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get publishing content pending transfer", e);
		}
		
		for (PublishingContent content: contents) {
			// Note this method should not throw exception
			// Fail to transfer one content should not
			// stop other content to be transferred.
			transferContent (content);
		}
	}
	
	
	private void transferContent (PublishingContent publishingContent) {
		
		try {			
			SessionFactoryUtil.beginTransaction();
			
			MarketingContent content = dao.getMarketingContent(publishingContent.getContentId());
			
			// Transfer attachment
			for (Attachment attachment : content.getAttachments()) {
				BinaryFile binaryFile = ecmUtil.download(attachment.getDocId());
				binaryFile.setAttachment(true);
				binaryFile.setContentId(content.getId());
				//messaging.send(new CreateBinaryFileMessage(binaryFile));
				dao.saveBinaryFile(binaryFile, "Intranet");
			}
			
			if (content.isTemplateBased()) {
				transferTemplateContent (content);
			}
			else {
				transferECMContent (content);
			}
				
			// Only considered transferred if all the above file is send to MQ.	
			publishingContent.setTransferred(true);
			dao.updatePublishingContent(publishingContent, "Intranet");
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (HibernateException | MarketingContentException e) {
			logger.log(Level.SEVERE, "Fail to transfer marketing content:" + Util.replaceNewLine( publishingContent.getContentId() ), e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	
	private void transferECMContent (MarketingContent content) throws MarketingContentException {
		
		// Process HTML content
		// Extract object id of images embedded in HTML and replace
		// the URL to portal side URL instead of ECM URL
		List<String> imageIds = new ArrayList<>();
				
		// Transfer the image
		for (String imageId : imageIds) {
			BinaryFile binaryFile = ecmUtil.download(imageId);
			binaryFile.setAttachment(false);
			binaryFile.setContentId(content.getId());

			dao.saveBinaryFile(binaryFile,"Intranet");
		}

		String processedHTML = htmlUtil.process(content.getHtmlContent(), imageIds, true);

		HtmlFile htmlFile = new HtmlFile(content.getId(), processedHTML);
		//messaging.send(new CreateHtmlFileMessage(htmlFile));
		dao.saveHtmlFile(htmlFile, "Intranet");

	}
	
	private void transferTemplateContent (MarketingContent content) throws MarketingContentException {
		
		ContentTemplate template = dao.getContentTemplate(content.getTemplateId());
		
		// Transfer template resource
		for (TemplateResource resource : template.getResources()) {
			BinaryFile binaryFile = new BinaryFile();
			binaryFile.setAttachment(false);
			binaryFile.setContentId(content.getId());
			binaryFile.setDocId(resource.getId());
			binaryFile.setName(resource.getFileName());
			binaryFile.setContentType(resource.getContentType());
			binaryFile.setContent(templateHelper.getResourceContent (resource.getFileName()));
			//messaging.send(new CreateBinaryFileMessage(binaryFile));
			dao.saveBinaryFile(binaryFile, "Intranet");
		}

		String processedHTML = templateHelper.processHTML(template.getFileName(), false, attachmentUrl, content.getTemplateHeader(),
				content.getTemplateTitle(), content.getTemplateBody());
		HtmlFile htmlFile = new HtmlFile(content.getId(), processedHTML);
		//messaging.send(new CreateHtmlFileMessage(htmlFile));
		dao.saveHtmlFile(htmlFile, "Intranet");
	}
	
	private void sendNotifications () throws MarketingContentException {
		
		List<PublishingContentSetTask> tasks = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			tasks = dao.getPendingEmailSendingContentSets();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get tasks with pending email sending", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get tasks with pending email sending", e);
		}
		
		for (PublishingContentSetTask task: tasks) {
			// KS here. The query already filter out cancelled task
			if (task.isCancelled()) {
				continue;
			}
			
			// Note this method should not throw exception
			// Fail to send one content should not
			// stop other content to be send.
			sendNotification (task);
		}
	}
	
	
	private void sendNotification (PublishingContentSetTask task) {
		
		Date now = new Date ();
		List<PublishingContent> toSendList = new ArrayList<>();
		
		for (PublishingContent content : task.getContents()) {
			// Will wait for next invocation to send email as
			// the content is not transfer to portal yet.
			if (content.isNotificationSent() || !content.isTransferred()) {
				continue;
			}
			
			if (now.after(content.getPublishDate())) {
				toSendList.add(content);
				
				task.setPublished(true);
				content.setNotificationSent(true);
			}
		}
		
		if (toSendList.isEmpty()) {
			return;
		}
		
		// Will only attempt to send email/sms once per content.
		// So as not to flood their email and SMS
		try {
			SessionFactoryUtil.beginTransaction();
			
			dao.updatePublishingContentSetTask(task, "Intranet");
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (HibernateException e) {
			logger.log(Level.SEVERE, "Fail to get update publishing content set task", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		
		for (PublishingContent content : toSendList) {
			MarketingContent marketingContent = null;
			try {
				SessionFactoryUtil.beginTransaction();
				marketingContent = dao.getMarketingContent(content.getContentId());
				SessionFactoryUtil.commitTransaction();
			}
			catch (HibernateException e) {
				logger.log(Level.SEVERE, "Fail to get marketing content:" + Util.replaceNewLine(content.getContentId()), e);
				SessionFactoryUtil.rollbackTransaction();
				continue;
			}
			
			if (marketingContent != null) {
				sendEmail (task, content, marketingContent);
				
				// SMS content may be empty
				if (marketingContent.isSmsContentAvailable()) {
					sendSMS (content, marketingContent);
				}
			}
		}
	}
	
	private void sendEmail (PublishingContentSetTask task, PublishingContent content, MarketingContent marketingContent) {
		try {
			SessionFactoryUtil.beginTransaction();
			
			List<String> sentList = emailUtil.sendEmail(content, marketingContent, task.getSenderEmail(), 
					task.getSenderEmailPassword());
			dao.addEmailLogs (marketingContent.getId(), sentList);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (HibernateException | MarketingContentException e) {
			logger.log(Level.SEVERE, "Fail to send email for content:" + Util.replaceNewLine(marketingContent.getId()), e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	private void sendSMS (PublishingContent content, MarketingContent marketingContent) {
		try {
			SessionFactoryUtil.beginTransaction();

			smsUtil.sendExtSMS(marketingContent.getSmsContent(), content.getPhones());
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (HibernateException e) {
			logger.log(Level.SEVERE, "Fail to send SMS for content:" + Util.replaceNewLine(marketingContent.getId()), e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	@Override
	public void addUserContentViewRecord(UserContentViewRecord userContantViewRecord) throws MarketingContentException {
		throw new UnsupportedOperationException ("Internet only operation.");
	}


	@Override
	public List<BinaryAttachment> getAttachments(String contentId) throws MarketingContentException {
		throw new UnsupportedOperationException ("Internet only operation.");
	}


	@Override
	public List<ContentTemplateInfo> getTemplateInfos () throws MarketingContentException {
		
		List<ContentTemplateInfo> result;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			result = dao.getTemplateInfos ();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Marketing Contents Templates", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get Marketing Contents Templates", e);
		}
		
		return result;
	}


	@Override
	public void sendEmailPreview (String contentId, String emailAddress) throws MarketingContentException {
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(EMAIL_MODULE);
				
		try {
			SessionFactoryUtil.beginTransaction();
			
			MarketingContent content = dao.getMarketingContent(contentId);
			String password = mailSenderConfig.senderPassword();
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					password = encipher.decrypt(password);
				} 
				catch (Exception e) {
					logger.log(Level.SEVERE, "Error while decrypting the configured system email password.", e);
				}
			}
			emailUtil.sendEmail(content, emailAddress, mailSenderConfig.senderAddress(), password);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (HibernateException | MarketingContentException e) {
			logger.log(Level.SEVERE, "Fail to send email preview:" + Util.replaceNewLine(contentId), e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}


	@Override
	public String getPreviewHtmlFile (String contentId) throws MarketingContentException {
		
		String htmlContent = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			MarketingContent content = dao.getMarketingContent(contentId);
			
			if (content.isTemplateBased()) {
				ContentTemplate template = dao.getContentTemplate(content.getTemplateId());
				
				htmlContent = templateHelper.processHTML(template.getFileName(), false, attachmentPreviewUrl, content.getTemplateHeader(), 
						content.getTemplateTitle(), content.getTemplateBody());
			}
			else {
				List<String> imageIds = new ArrayList<>();
				htmlContent = previewHtmlUtil.process(content.getHtmlContent(), imageIds, true);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (HibernateException e) {
			logger.log(Level.SEVERE, "Fail to send email preview:" + Util.replaceNewLine(contentId), e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return htmlContent;
	}


	@Override
	public BinaryFile getPreviewBinaryFile (String id) throws MarketingContentException {
		
		// Will assume to be template resource first 
		// (should be faster to access DB then REST call to ECM). 
		// If not found, then try retrieve from ECM.
		
		BinaryFile binaryFile = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
		
			TemplateResource resource = dao.getTemplateResource(id);
			
			if (resource != null) {
				binaryFile = new BinaryFile();
				binaryFile.setDocId(resource.getId());
				binaryFile.setName(resource.getFileName());
				binaryFile.setContentType(resource.getContentType());
				binaryFile.setContent(templateHelper.getResourceContent (resource.getFileName()));
			}
			else {
				binaryFile = ecmUtil.download(id);
			}
			
			SessionFactoryUtil.commitTransaction();
			
		}
		catch (HibernateException e) {
			logger.log(Level.INFO, "Fail to retrieve template resource:" + Util.replaceNewLine(id), e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return binaryFile;
	}
	
	private void saveECMContent (MarketingContent content, String requester) throws MarketingContentException {
		logger.log(Level.INFO, "saveECMContent >> " + content.getTemplateId());
	
		// Process HTML content
		// Extract object id of images embedded in HTML and replace
		// the URL to portal side URL instead of ECM URL
		List<String> imageIds = new ArrayList<>();
		String processedHTML = htmlUtil.process(content.getHtmlContent(), imageIds, true);
		
		logger.log(Level.INFO, "save image >> " + imageIds.size());
		// Save the image
		for (String imageId : imageIds) {
			BinaryFile binaryFile = ecmUtil.download(imageId);
			binaryFile.setAttachment(false);
			binaryFile.setContentId(content.getId());
			logger.log(Level.INFO, "save binary file >> " + binaryFile.getContentId());
			dao.saveBinaryFile(binaryFile, requester);
		}
				
		HtmlFile htmlFile = new HtmlFile(content.getId(), processedHTML);
		logger.log(Level.INFO, "save html file >> " + htmlFile.getContentId());
		dao.saveHtmlFile(htmlFile, requester);
	}
	
	private void saveTemplateContent (MarketingContent content, String requester) throws MarketingContentException {
		
		logger.log(Level.INFO, "saveTemplateContent >> " + content.getTemplateId());
		
		ContentTemplate template = dao.getContentTemplate(content.getTemplateId());
		
		// Process HTML
		String processedHTML = templateHelper.processHTML(template.getFileName(), false, attachmentUrl, content.getTemplateHeader(), 
				content.getTemplateTitle(), content.getTemplateBody());
		
		logger.log(Level.INFO, "save template resource >> " + template.getResources().size());
		
		// Save template resource
		for (TemplateResource resource : template.getResources()) {
			BinaryFile binaryFile = new BinaryFile();
			binaryFile.setAttachment(false);
			binaryFile.setContentId(content.getId());
			binaryFile.setDocId(resource.getId());
			binaryFile.setName(resource.getFileName());
			binaryFile.setContentType(resource.getContentType());
			binaryFile.setContent(templateHelper.getResourceContent (resource.getFileName()));
			
			logger.log(Level.INFO, "save binary file >> " + binaryFile.getDocId());
			dao.saveBinaryFile(binaryFile, requester);
		}
		
		HtmlFile htmlFile = new HtmlFile(content.getId(), processedHTML);
		logger.log(Level.INFO, "save html file >> " + htmlFile.getContentId());
		dao.saveHtmlFile(htmlFile, requester);
	}
	
}
