package com.stee.spfcore.service.survey.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.SurveyDAO;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.survey.Survey;
import com.stee.spfcore.model.survey.SurveyBroadcast;
import com.stee.spfcore.model.survey.internal.SurveyBroadcastLog;
import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;

public class BroadcastHelper {

	private static Logger logger = Logger.getLogger(BroadcastHelper.class.getName());
	
	private SurveyDAO dao;
	private INotificationService notificationService;
	private String senderEmail;
	private String senderPassword;
	
	public BroadcastHelper () {
		dao = new SurveyDAO();
		notificationService = NotificationServiceFactory.getInstance();
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig("SURV");
		String password = mailSenderConfig.senderPassword();
		
		String encryptionKey = EnvironmentUtils.getEncryptionKey();
		
		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				password = encipher.decrypt(password);
			}
			catch (Exception e) {
				logger.info("Error while decrypting the configured password");
			}
		}
		senderEmail = mailSenderConfig.senderAddress();
		senderPassword = password;
	}
	
	
	public void broadcast (Survey survey) throws NotificationServiceException {
		
		Calendar cal = new GregorianCalendar();
		Date now = new Date ();
		
		for (SurveyBroadcast broadcast : survey.getBroadcasts()) {
			cal.setTime(survey.getPublishDate());
			cal.add(Calendar.DATE, broadcast.getOffset());
			
			if (now.after(cal.getTime())) {
				SurveyBroadcastLog log = dao.getSurveyBroadcastLog(broadcast.getId());
				if (log == null) {
					broadcast (survey, broadcast);
					
					log = new SurveyBroadcastLog(broadcast.getId(), survey.getId(), new Date ());
					dao.addSurveyBroadcastLog(log);
				}
			}
		}
	}
	
	private void broadcast (Survey survey, SurveyBroadcast broadcast) {
		
		List<PersonalDetail> personalDetails = dao.getPendingSurveyUsers(survey.getId());
		
		List<String> emails = retrieveEmail (personalDetails);
		
		if (emails.isEmpty()) {
			return;
		}
		
		BatchElectronicMail mail = new BatchElectronicMail(senderEmail, senderPassword, emails, broadcast.getSubject(), broadcast.getContent());
		
		try {
			notificationService.send(mail);
		}
		catch (NotificationServiceException e) {
			// Will only attempt to send broadcast once as not to flood email system due to one failure.
			logger.log (Level.SEVERE, "Fail to send survey broadcast:" + Util.replaceNewLine( broadcast.getId() ), e);
		}
	}
	
	private List<String> retrieveEmail (List<PersonalDetail> personalDetails) {
		
		List<String> emails = new ArrayList<>();
		for (PersonalDetail personalDetail : personalDetails) {
			String officeEmail = null;
			String personnalEmail = null;
			String preferredEmail = null;
			
			for (Email email : personalDetail.getEmailContacts()) {
				if (email.isPrefer()) {
					preferredEmail = email.getAddress();
				}
				else if (email.getLabel() == ContactLabel.WORK) {
					officeEmail = email.getAddress();
				}
				else if (email.getLabel() == ContactLabel.MOBILE) {
					personnalEmail = email.getAddress();
				}
			}
			
			if (preferredEmail != null) {
				emails.add(preferredEmail);
			}
			else if (officeEmail != null) {
				emails.add(officeEmail);
			}
			else if (personnalEmail != null) {
				emails.add(personnalEmail);
			}
		}
		
		return emails;
	}
	
}
