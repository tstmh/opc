package com.stee.spfcore.service.marketingContent.impl;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.stee.spfcore.dao.MarketingContentDAO;
import com.stee.spfcore.model.marketingContent.Attachment;
import com.stee.spfcore.model.marketingContent.ContentTemplate;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.TemplateResource;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.marketingContent.internal.PublishingContent;
import com.stee.spfcore.service.configuration.IMailServerConfig;
import com.stee.spfcore.service.configuration.IMarketingContentConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.marketingContent.MarketingContentException;
import com.stee.spfcore.utils.Util;

public class EmailUtil {

	private static final Logger logger = Logger.getLogger(EmailUtil.class.getName());

	private String hostname;
	private int port;
	private Properties properties;
	private HtmlUtil htmlUtil;
	private ECMUtil ecmUtil;
	private int recipientsLimit;
	private TemplateHelper templateHelper;
	private MarketingContentDAO dao;

	public EmailUtil (String baseUrl, ECMUtil ecmUtil) {
		this.ecmUtil = ecmUtil;

		templateHelper = new TemplateHelper();

		htmlUtil = new HtmlUtil(baseUrl, "cid:");

		dao = new MarketingContentDAO();

		IMailServerConfig config = ServiceConfig.getInstance().getMailServerConfig();

		hostname = config.hostname();
		port = config.port();
		String protocol = config.protocol();

		this.properties = new Properties();
		this.properties.put("mail.smtp.host", this.hostname);
		this.properties.put("mail.smtp.port", this.port);

		if ("ssl".equals(protocol)) {
			this.properties.put("mail.smtp.auth", true);
			this.properties.put("mail.smtp.ssl.enable", true);
		}
		else if ("tls".equals(protocol)) {
			this.properties.put("mail.smtp.auth", true);
			this.properties.put("mail.smtp.starttls.enable", true);
		}

		Security.setProperty("ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
		Security.setProperty("ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl");

		IMarketingContentConfig marketingContentConfig = ServiceConfig.getInstance().getMarketingContentConfig();
		recipientsLimit = marketingContentConfig.maxRecipientsPerMessage();
	}


	public List<String> sendEmail (PublishingContent publishingContent, MarketingContent marketingContent,
								   final String senderEmail, final String senderPassword) throws MarketingContentException  {

		Session session = Session.getInstance(this.properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(senderEmail, senderPassword);
					}
				});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients( Message.RecipientType.TO, InternetAddress.parse( senderEmail ) );

			buildMessage (message, marketingContent);

			List<String> emails = publishingContent.getEmails();
			List<String> sentList = new ArrayList<String> ();
			List<InternetAddress []> addressArrayList = buildAddresses (emails, sentList);

			for (InternetAddress [] addressArray : addressArrayList) {
				//message.setRecipients(Message.RecipientType.TO, addressArray);
				//Instead of exposing all recipient email, use the BCC function to send to all recipient
				message.setRecipients(Message.RecipientType.BCC, addressArray);
				Transport.send(message);
			}

			return sentList;
		}
		catch (MessagingException exception) {
			logger.log (Level.SEVERE, "Fail to send email via SMTP: " + this.hostname + exception);
			throw new MarketingContentException ("Fail to send email via SMTP: " + this.hostname + exception);
		}
	}

	public void sendEmail (MarketingContent marketingContent, final String address, final String senderEmail, final String senderPassword)
			throws MarketingContentException {

		Session session = Session.getInstance(this.properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication () {
				return new PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));

			buildMessage(message, marketingContent);

			message.setRecipients(Message.RecipientType.TO, address);
			Transport.send(message);
		}
		catch (MessagingException exception) {
			logger.log(Level.SEVERE, "Fail to send email via SMTP: " + this.hostname + exception);
			throw new MarketingContentException("Fail to send email via SMTP: " + this.hostname + exception);
		}
	}


	private void buildMessage (MimeMessage message, MarketingContent marketingContent) throws MessagingException, MarketingContentException {

		MimeMultipart mailContent = new MimeMultipart("related");


		if (marketingContent.isTemplateBased()) {
			buildTemplateMessage (mailContent, marketingContent);
		}
		else {
			buildECMMessage (mailContent, marketingContent);
		}

		// Attachment
		if (marketingContent.getAttachments() != null) {
			for (Attachment attachment : marketingContent.getAttachments()) {
				BinaryFile binaryFile = ecmUtil.download(attachment.getDocId());

				MimeBodyPart bodyPart = new MimeBodyPart ();
				DataSource dataSource = new ByteArrayDataSource(binaryFile.getContent(), binaryFile.getContentType());
				bodyPart.setDataHandler(new DataHandler(dataSource));
				bodyPart.setFileName(binaryFile.getName());
				bodyPart.setDisposition(MimeBodyPart.ATTACHMENT);

				mailContent.addBodyPart(bodyPart);
			}
		}

		message.setSubject( Util.replaceNewLine( marketingContent.getTitle() ));
		message.setContent(mailContent);
	}

	private void buildECMMessage (MimeMultipart mailContent, MarketingContent marketingContent) throws MessagingException, MarketingContentException {

		// Process the email contents
		List<String> imageIds = new ArrayList<String>();

		String processedHTML = htmlUtil.process(marketingContent.getHtmlContent(), imageIds, false);

		// Body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent (processedHTML, "text/html");
		mailContent.addBodyPart(textPart);

		// Image attachment
		for (String imageId : imageIds) {
			MimeBodyPart bodyPart = new MimeBodyPart ();

			BinaryFile binaryFile = ecmUtil.download(imageId);
			DataSource dataSource = new ByteArrayDataSource(binaryFile.getContent(), binaryFile.getContentType());
			bodyPart.setDataHandler(new DataHandler(dataSource));
			bodyPart.setFileName(binaryFile.getName());
			bodyPart.setContentID("<" + imageId + ">");
			bodyPart.setDisposition(MimeBodyPart.INLINE);

			mailContent.addBodyPart(bodyPart);
		}
	}

	private void buildTemplateMessage (MimeMultipart mailContent, MarketingContent marketingContent)
			throws MessagingException, MarketingContentException {

		ContentTemplate template = dao.getContentTemplate(marketingContent.getTemplateId());

		// Process HTML
		String processedHTML = templateHelper.processHTML(template.getFileName(), true, "", marketingContent.getTemplateHeader(),
				marketingContent.getTemplateTitle(), marketingContent.getTemplateBody());

		// Body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent (processedHTML, "text/html");
		mailContent.addBodyPart(textPart);

		// Image attachment
		for (TemplateResource resource : template.getResources()) {
			MimeBodyPart bodyPart = new MimeBodyPart ();

			DataSource dataSource = new ByteArrayDataSource(templateHelper.getResourceContent(resource.getFileName()), resource.getContentType());
			bodyPart.setDataHandler(new DataHandler(dataSource));
			bodyPart.setFileName(resource.getFileName());
			bodyPart.setContentID("<" + resource.getId() + ">");
			bodyPart.setDisposition(MimeBodyPart.INLINE);

			mailContent.addBodyPart(bodyPart);
		}
	}

	private List<InternetAddress []> buildAddresses (List<String> emails, List<String> sentList) {

		List<InternetAddress []> addressArrayList = new ArrayList<InternetAddress []>();

		List<InternetAddress> subList = new ArrayList<InternetAddress>();
		for (String email : emails) {

			// Will skip the email
			InternetAddress address;
			try {
				address = new InternetAddress(email);
			}
			catch (AddressException e) {
				logger.log(Level.WARNING, "Error will parsing email address:" + Util.replaceNewLine( email ), e);
				continue;
			}
			subList.add(address);
			sentList.add(email);

			if (subList.size() >= recipientsLimit) {
				InternetAddress [] addressArray = subList.toArray(new InternetAddress [subList.size()]);
				addressArrayList.add(addressArray);

				subList = new ArrayList<InternetAddress>();
			}
		}

		if (!subList.isEmpty()) {
			InternetAddress [] addressArray = subList.toArray(new InternetAddress [subList.size()]);
			addressArrayList.add(addressArray);
		}

		return addressArrayList;
	}
}
