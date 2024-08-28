package com.stee.spfcore.service.corporateCard.impl;

import com.stee.spfcore.service.configuration.IMailServerConfig;
import com.stee.spfcore.service.configuration.IMarketingContentConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.Util;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailUtil {

	private static final Logger logger = Logger.getLogger(EmailUtil.class.getName());
	
	private String hostname;
	private int port;
	private Properties properties;
	private int recipientsLimit;
	
	public EmailUtil () {
		
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
	
	public void sendBroadcastEmail (String subject, String body, final String senderEmail, final String senderPassword
			, List<String> emails) {
		logger.info("Send Broadcast Email !!");
		Session session = Session.getInstance(this.properties,
				new javax.mail.Authenticator() {
			@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(senderEmail, senderPassword);
					}
				});
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients( Message.RecipientType.TO, InternetAddress.parse( senderEmail ) );
			
			List<InternetAddress []> addressArrayList = buildAddresses (emails);
			
			message.setSubject( Util.replaceNewLine(subject) );
			message.setText(body, "UTF-8", "html");
			
			for (InternetAddress [] addressArray : addressArrayList) {
				message.setRecipients(Message.RecipientType.BCC, addressArray);
				Transport.send(message);
			}
		} catch (Exception e) {
			logger.severe(String.format("Fail to send email via SMTP:%s %s",this.hostname , e));
		}
		
	}
	
	private List<InternetAddress []> buildAddresses (List<String> emails) {
		
		List<InternetAddress []> addressArrayList = new ArrayList<>();
		
		List<InternetAddress> subList = new ArrayList<>();
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
			
			if (subList.size() >= recipientsLimit) {
				InternetAddress [] addressArray = subList.toArray(new InternetAddress [subList.size()]);
				addressArrayList.add(addressArray);
				
				subList = new ArrayList<>();
			}
		}
		
		if (!subList.isEmpty()) {
			InternetAddress [] addressArray = subList.toArray(new InternetAddress [subList.size()]);
			addressArrayList.add(addressArray);
		}
		
		return addressArrayList;
	}
}
