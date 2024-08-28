package com.stee.spfcore.service.hrps.impl;

import com.stee.spfcore.model.hrps.HRPSConfig;
import com.stee.spfcore.model.hrps.internal.BenefitsStatistic;
import com.stee.spfcore.service.configuration.IMailServerConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.template.TemplateUtil;

import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailUtil {
	
	private static final Logger logger = Logger.getLogger(EmailUtil.class.getName());
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private static final String PROCESSED_EMAIL_SUBJECT = "HRPS-E001_Subject.vm";
	private static final String PROCESSED_EMAIL_BODY = "HRPS-E001_Body.vm";
	private static final String POST_PROCESSED_EMAIL_SUBJECT = "HRPS-E003_Subject.vm";
	private static final String POST_PROCESSED_EMAIL_BODY = "HRPS-E003_Body.vm";
	private static final String INTERNAL_ERROR_EMAIL_SUBJECT = "HRPS-E002_Subject.vm";
	private static final String INTERNAL_ERROR_EMAIL_BODY = "HRPS-E002_Subject.vm";
	private static final String FINANCE_EMAIL_SUBJECT = "FIN-003_Subject.vm";
	private static final String FINANCE_EMAIL_BODY = "FIN-003_Body.vm";
	private static final String FINANCE_EMAIL_AD_SUBJECT = "FIN-004_Subject.vm";
	private static final String FINANCE_EMAIL_AD_BODY = "FIN-004_Body.vm";
	private static final String FINANCE_EMAIL_FINALIZED_SUBJECT = "FIN-005_Subject.vm";
	private static final String FINANCE_EMAIL_FINALIZED_BODY = "FIN-005_Body.vm";
	
	private String hostname;
	private int port;
	private Properties properties;
	
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
	
	}
	
	public void sendInternalError(String file, Date processedDate, String reason, Exception exception, HRPSConfig config) {
	
		//Setup data for template
		Map<String, String> context = new HashMap<>();
		context.put("FileName", file);
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("Reason", reason);
		context.put("Exception", exception.getMessage());
		
		String subject = TemplateUtil.getInstance().format(INTERNAL_ERROR_EMAIL_SUBJECT, context);
		String body = TemplateUtil.getInstance ().format(INTERNAL_ERROR_EMAIL_BODY, context);
		
		sendEmail(subject, body, config.getSenderAddress(), config.getEmailPassword(), convertAddressesToList(config.getEmailToAddress()),null,null);
		
	}
	
	public void sendProcessedStatus(String file, Date processedDate, int successCount, int rejectCount, List<Record> rejRecord, HRPSConfig config) {
		
		//Setup data for template
		Map<String, Object> context = new HashMap<>();
		context.put("FileName",file);
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("SuccessfulCount",String.valueOf(successCount));
		context.put("RejectedCount",String.valueOf(rejectCount));
		context.put("rejectedList", rejRecord);
		
		String subject = TemplateUtil.getInstance().format(PROCESSED_EMAIL_SUBJECT, context);
		String body = TemplateUtil.getInstance ().format(PROCESSED_EMAIL_BODY, context);
		
		sendEmail(subject, body, config.getSenderAddress(), config.getEmailPassword(), convertAddressesToList(config.getEmailToAddress()), null, null);
		
	}
	
	public void sendPostProcessedStatus(String file, Date processedDate, int successCount, int rejectCount, List<PostRecord> rejRecord, HRPSConfig config) {
		
		//Setup data for template
		Map<String, Object> context = new HashMap<>();
		context.put("FileName",file);
		context.put("ProcessedDate", DATE_FORMAT.format(processedDate));
		context.put("SuccessfulCount",String.valueOf(successCount));
		context.put("RejectedCount",String.valueOf(rejectCount));
		context.put("rejectedList", rejRecord);
		
		String subject = TemplateUtil.getInstance().format(POST_PROCESSED_EMAIL_SUBJECT, context);
		String body = TemplateUtil.getInstance ().format(POST_PROCESSED_EMAIL_BODY, context);
		
		sendEmail(subject, body, config.getSenderAddress(), config.getEmailPassword(), convertAddressesToList(config.getEmailToAddress()), null, null);
		
	}
	
	public void sendReportToFO(BenefitsStatistic bgStatistic, BenefitsStatistic wgStatistic, BenefitsStatistic nbStatistic, List<String> reportUrls, List<String> emails,
			HRPSConfig config) {
		
		//Setup data for template
		Map<String, Object> context = new HashMap<>();
		context.put("bgStatistic", bgStatistic);
		context.put("wgStatistic", wgStatistic);
		context.put("nbStatistic", nbStatistic);
		
		for(String reportUrl : reportUrls) {
			if (reportUrl != null && logger.isLoggable( Level.INFO )) {
				logger.info(String.format("reportUrl %s", reportUrl));
			}
		}

		List<String> reportNames = Arrays.asList("Bereavement Report", "Wedding Report", "Newborn Report", "Summary Report");		
		String reportKey = null;
		String reportName = null;
		String link = null;
		
		for( int i=0; i<reportUrls.size(); i++ ) {		
				
			if (reportUrls.get(i) != null) {					
				reportKey = "reportUrl" + i;
				reportName = reportNames.get(i);
				if ( logger.isLoggable( Level.INFO ) ) {
					logger.info(String.format("reportKey %s reportUrl %s", reportKey, reportUrls.get(i)));
				}
				link = "<a href=\"" + reportUrls.get(i)+ "\">" + reportName + "</a>";
				context.put(reportKey, link);

			}
		}

		String subject = TemplateUtil.getInstance().format(FINANCE_EMAIL_SUBJECT, context);
		String body = TemplateUtil.getInstance ().format(FINANCE_EMAIL_BODY, context);
		
		sendEmail(subject, body, config.getSenderAddress(), config.getEmailPassword(), emails, reportUrls ,null);
	}
	
	public void sendReportToAD(BenefitsStatistic bgStatistic, BenefitsStatistic wgStatistic, BenefitsStatistic nbStatistic, List<String> reportUrls, List<String> emails,
			HRPSConfig config) {
		
		//Setup data for template
		Map<String, Object> context = new HashMap<>();
		context.put("bgStatistic", bgStatistic);
		context.put("wgStatistic", wgStatistic);
		context.put("nbStatistic", nbStatistic);
		
		for(String reportUrl : reportUrls) {
			if (reportUrl != null && logger.isLoggable( Level.INFO )) {
				logger.info(String.format("reportUrl %s", reportUrl));
			}
		}

		List<String> reportNames = Arrays.asList("Bereavement Report", "Wedding Report", "Newborn Report", "Summary Report");		
		String reportKey = null;
		String reportName = null;
		String link = null;
		
		for( int i=0; i<reportUrls.size(); i++ ) {		
				
			if (reportUrls.get(i) != null) {					
				reportKey = "reportUrl" + i;
				reportName = reportNames.get(i);
				if ( logger.isLoggable( Level.INFO ) ) {
					logger.info(String.format("reportKey %s reportUrl %s", reportKey, reportUrls.get(i)));
				}
				link = "<a href=\"" + reportUrls.get(i)+ "\">" + reportName + "</a>";
				context.put(reportKey, link);

			}
		}
		
		String subject = TemplateUtil.getInstance().format(FINANCE_EMAIL_AD_SUBJECT, context);
		String body = TemplateUtil.getInstance ().format(FINANCE_EMAIL_AD_BODY, context);
		
		sendEmail(subject, body, config.getSenderAddress(), config.getEmailPassword(), emails, reportUrls, null);
	}
	
	public void sendFinalizedReport(BenefitsStatistic bgStatistic, BenefitsStatistic wgStatistic, BenefitsStatistic nbStatistic, List<String> reportUrls, List<String> emails,
			HRPSConfig config) {
		
		//Setup data for template
		Map<String, Object> context = new HashMap<>();
		context.put("bgStatistic", bgStatistic);
		context.put("wgStatistic", wgStatistic);
		context.put("nbStatistic", nbStatistic);
			
		for(String reportUrl : reportUrls) {
			if (reportUrl != null && logger.isLoggable( Level.INFO )) {
				logger.info(String.format("reportUrl %s", reportUrl));
			}
		}
		
		List<String> reportNames = Arrays.asList("Bereavement Report", "Wedding Report", "Newborn Report", "Summary Report");		
		String reportKey = null;
		String reportName = null;
		String link = null;
		
		for( int i=0; i<reportUrls.size(); i++ ) {		
				
			if (reportUrls.get(i) != null) {					
				reportKey = "reportUrl" + i;
				reportName = reportNames.get(i);
				if ( logger.isLoggable( Level.INFO ) ) {
					logger.info(String.format("reportKey %s reportUrl %s", reportKey, reportUrls.get(i)));
				}
				link = "<a href=\"" + reportUrls.get(i)+ "\">" + reportName + "</a>";
				context.put(reportKey, link);

			}
		}

		
		String subject = TemplateUtil.getInstance().format(FINANCE_EMAIL_FINALIZED_SUBJECT, context);
		String body = TemplateUtil.getInstance ().format(FINANCE_EMAIL_FINALIZED_BODY, context);
		
		sendEmail(subject, body, config.getSenderAddress(), config.getEmailPassword(), emails, reportUrls, null);
	}

private void buildMessageBodyUrl (MimeMessage message, List<String> reportUrls, String body) throws MessagingException {
		
		MimeMultipart mailContent = new MimeMultipart("related");
		
		//Body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(body, "text/html");
		mailContent.addBodyPart(textPart);
		
		//Attachment
		if (reportUrls != null && !reportUrls.isEmpty()) {
			for(String reportUrl : reportUrls) {
				if (reportUrl != null) {
					MimeBodyPart bodyPart = new MimeBodyPart();
					bodyPart.setText(body);

					mailContent.addBodyPart(bodyPart);
				}
			}
		}
		message.setContent(mailContent);
	}
	
	private InternetAddress [] convertAddresses (List<String>emails) {
		List<InternetAddress> addresses = new ArrayList<>();
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
			addresses.add(address);
		}
		return addresses.toArray(new InternetAddress[addresses.size()]);
	}

	private List<String> convertAddressesToList(String addresses) {
		String[] splitted = addresses.split(",\\s*");
		return Arrays.asList(splitted);
	}

	
	private void sendEmail(String subject, String body, final String senderEmail, final String senderPassword
			, List<String> emails, List<String> reportUrls, List<String> ccEmails) {
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
			
			if (ccEmails != null) {
				InternetAddress [] ccAddresses = convertAddresses(ccEmails);
				message.setRecipients( Message.RecipientType.CC, ccAddresses);
			}
			
			if (emails != null) {
				InternetAddress [] addresses = convertAddresses(emails);
				message.setRecipients(Message.RecipientType.TO, addresses);
			}

			buildMessageBodyUrl(message, reportUrls, body);
			
			message.setSubject( Util.replaceNewLine(subject) );
			
			Transport.send(message);
		
		} catch (Exception e) {
			logger.severe(String.format("Fail to send email via SMTP: %s %s", this.hostname, e));
		}
		
	}
}
