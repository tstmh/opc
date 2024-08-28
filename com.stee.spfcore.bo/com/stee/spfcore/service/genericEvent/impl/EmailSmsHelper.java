package com.stee.spfcore.service.genericEvent.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;

import com.stee.spfcore.dao.AnnouncementDAO;
import com.stee.spfcore.dao.GenericEventDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.genericEvent.GEApplicationStatus;
import com.stee.spfcore.model.genericEvent.GETicketingChoice;
import com.stee.spfcore.model.genericEvent.GETicketingOption;
import com.stee.spfcore.model.genericEvent.GETicketingSection;
import com.stee.spfcore.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.model.genericEvent.GenericEventDepartment;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;
import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.service.announcement.impl.AnnouncementUtil;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.genericEvent.GenericEventServiceException;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.template.TemplateUtil;

import static com.ibm.java.diagnostics.utils.Context.logger;

public class EmailSmsHelper {

	private static final String GEM_Module = "GEM";

	private static final Logger logger = Logger.getLogger(EmailSmsHelper.class.getName());
	
	private static final String SUCCESS_EMAIL_SUBJECT = "GEM-E001_Subject.vm";
	private static final String SUCCESS_EMAIL_BODY = "GEM-E001_Body.vm";
	private static final String SUCCESS_SMS = "GEM-S001.vm";
	private static final String UNSUCCESS_EMAIL_SUBJECT = "GEM-E002_Subject.vm";
	private static final String UNSUCCESS_EMAIL_BODY = "GEM-E002_Body.vm";
	private static final String UNSUCCESS_SMS = "GEM-S002.vm";
	private static final String CANCEL_EMAIL_SUBJECT = "GEM-E003_Subject.vm";
	private static final String CANCEL_EMAIL_BODY = "GEM-E003_Body.vm";
	private static final String CANCEL_SMS = "GEM-S003.vm";
	private static final String POSTPONE_EMAIL_SUBJECT = "GEM-E004_Subject.vm";
	private static final String POSTPONE_EMAIL_BODY = "GEM-E004_Body.vm";
	private static final String POSTPONE_SMS = "GEM-S004.vm";
	
	
	private GenericEventDAO dao;
	private PersonnelDAO personnelDAO;
	private INotificationService notificationService;
	private AnnouncementDAO announcementDAO;
	private AnnouncementUtil announcementUtil;
	
	
	public EmailSmsHelper () {
		dao = new GenericEventDAO();
		personnelDAO = new PersonnelDAO();
		notificationService = NotificationServiceFactory.getInstance();
		announcementDAO = new AnnouncementDAO();
		announcementUtil = new AnnouncementUtil();
	}
	
	
	public void notifyApplicationResult (String eventId) throws GenericEventServiceException {
		
		GenericEventDetail eventDetail = null;
		List<GenericEventApplication> successList = null;
		List<GenericEventApplication> unsuccessList = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			eventDetail = dao.getGenericEventDetail(eventId);
			
			successList = dao.getGenericEventApplications(eventId, GEApplicationStatus.SUCCESSFUL);
			
			unsuccessList = dao.getGenericEventApplications(eventId, GEApplicationStatus.UNSUCCESSFUL);
					
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to notify application result", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		if (eventDetail == null) {
			logger.warning("Generic Event not found:" + Util.replaceNewLine(eventId));
		}
		
		if (successList != null && !successList.isEmpty()) {
			logger.info("inside notify success");
			notifySuccess (eventDetail, successList);
		}
		else {
			logger.info("No success application to notify for generic event: " + Util.replaceNewLine(eventId));
		}
		
		if (unsuccessList != null && !unsuccessList.isEmpty()) {
			logger.info("inside notify unsuccess");
			notifyUnsuccess (eventDetail, unsuccessList);
		}
		else {
			logger.info("No unsuccessful application to notify for generic event: " + Util.replaceNewLine(eventId));
		}
	}
	
	
	private void notifySuccess (GenericEventDetail eventDetail, List<GenericEventApplication> applications) {
		logger.info("inside notify success emailSmsHelper");
		for (GenericEventApplication application : applications) {
			notifySuccess (eventDetail, application);
		}
	}
	
	private void notifyUnsuccess (GenericEventDetail eventDetail, List<GenericEventApplication> applications) {
		
		for (GenericEventApplication application : applications) {
			notifyUnsuccess (eventDetail, application);
		}
	}
	
	
	private void notifySuccess (GenericEventDetail eventDetail, GenericEventApplication application) {
		
		if (logger.isLoggable(Level.INFO)) {
			logger.finest("Inform application success " + Util.replaceNewLine(application.getNric()));
		}
		
		PersonalDetail personalDetail = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			personalDetail = personnelDAO.getPersonal(application.getNric());
			
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to notify application result for applicant:" + Util.replaceNewLine(application.getNric()), e);
			SessionFactoryUtil.rollbackTransaction();
			return;
		}
			
		if (personalDetail == null) {
			logger.severe ("Fail to notify application result. Personnel detail for applicant not found:" + Util.replaceNewLine(application.getNric()));
			return;
		}
		
		if (personalDetail.getPreferredContactMode() == null || personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
			String email = retrieveEmailContact (personalDetail);
			if (email != null && !email.isEmpty()) {
				logger.info("going to send email");
				emailSuccess (eventDetail, application, email);
			}
			else {
				logger.warning("Email not found for " + Util.replaceNewLine(application.getNric()));
			}
		}
		else if (personalDetail.getPreferredContactMode() == ContactMode.SMS) {
			String phone = retrievePhoneContact (personalDetail);
			if (phone != null && !phone.isEmpty()) {
				smsSuccess (eventDetail, phone);
			}
			else {
				logger.warning("Phone number not found for " + Util.replaceNewLine(application.getNric()));
			}
		}
		else {
			logger.warning("Preferred contact mode not set for " + Util.replaceNewLine(application.getNric()));
		}
	}
	
	private void notifyUnsuccess (GenericEventDetail eventDetail, GenericEventApplication application) {
		
		if (logger.isLoggable(Level.INFO)) {
			logger.finest("Inform application unsuccess " + Util.replaceNewLine(application.getNric()));
		}
		
		PersonalDetail personalDetail = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			personalDetail = personnelDAO.getPersonal(application.getNric());
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to notify application result for applicant:" + Util.replaceNewLine(application.getNric()), e);
			SessionFactoryUtil.rollbackTransaction();
			return;
		}
			
		if (personalDetail == null) {
			logger.severe ("Fail to notify application result. Personnel detail for applicant not found:" + Util.replaceNewLine(application.getNric()));
			return;
		}
		
		if (personalDetail.getPreferredContactMode() == null || personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
			String email = retrieveEmailContact (personalDetail);
			if (email != null && !email.isEmpty()) {
				emailUnsuccess (eventDetail, email);
			}
			else {
				logger.warning("Email not found for " + Util.replaceNewLine(application.getNric()));
			}
		}
		else if (personalDetail.getPreferredContactMode() == ContactMode.SMS) {
			String phone = retrievePhoneContact (personalDetail);
			if (phone != null && !phone.isEmpty()) {
				smsUnsuccess (eventDetail, phone);
			}
			else {
				logger.warning("Phone number not found for " + Util.replaceNewLine(application.getNric()));
			}
		}
		else {
			logger.warning("Preferred contact mode not set for " + Util.replaceNewLine(application.getNric()));
		}
	}
	
	private String retrieveEmailContact (PersonalDetail personalDetail) {
		
		String prefferred = null;
		String office = null;
		String personal = null;
		
		for (Email email : personalDetail.getEmailContacts()) {
			if (email.isPrefer()) {
				prefferred = email.getAddress();
			}
			else if (email.getLabel() == ContactLabel.WORK) {
				office = email.getAddress();
			}
			else if (email.getLabel() == ContactLabel.MOBILE) {
				personal = email.getAddress();
			}
		}
		
		if (prefferred != null) {
			return prefferred;
		}
		else if (office != null) {
			return office;
		}
		else {
			return personal;
		}
	}
	
	private String retrievePhoneContact (PersonalDetail personalDetail) {
		
		for (Phone phone : personalDetail.getPhoneContacts()) {
			if (phone.isPrefer()) {
				return phone.getNumber();
			}
		}
		
		return null;
	}
	
	
	private void emailSuccess (GenericEventDetail eventDetail, GenericEventApplication application, String email) {
		
		Map<String, Object> context = new HashMap<>();
		boolean isUnitEvent = isUnitEvent(eventDetail);
		context.put("title", eventDetail.getTitle());
		context.put("hasTicketing", eventDetail.isHasTicketing());
		context.put("isUnitEvent", isUnitEvent);
		String startDate = "";
		if(eventDetail.getEventStartDate()==null)
		{
			startDate ="";
		}
		else
		{
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(eventDetail.getEventStartDate());
		if(startDateCalendar.get(Calendar.DAY_OF_MONTH)<10)
		{
			startDate +="0";
		}
		startDate += String.valueOf(startDateCalendar.get(Calendar.DAY_OF_MONTH));
		startDate += "/";
		if(startDateCalendar.get(Calendar.MONTH)<=8)
		{
			startDate +="0";
		}
		startDate += String.valueOf(1+ startDateCalendar.get(Calendar.MONTH));
		startDate += "/";
		startDate += String.valueOf(startDateCalendar.get(Calendar.YEAR));
		}
		String endDate = "";
		if(eventDetail.getEventEndDate()==null)
		{
			endDate = "";
		}
		else
		{
		endDate = "";
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(eventDetail.getEventEndDate());
		if(endDateCalendar.get(Calendar.DAY_OF_MONTH)<10)
		{
			endDate +="0";
		}
		endDate += String.valueOf(endDateCalendar.get(Calendar.DAY_OF_MONTH));
		endDate += "/";
		if(endDateCalendar.get(Calendar.MONTH)<=8)
		{
			endDate +="0";
		}
		endDate += String.valueOf(1+ endDateCalendar.get(Calendar.MONTH));
		endDate += "/";
		endDate += String.valueOf(endDateCalendar.get(Calendar.YEAR));
		}
		context.put("eventStartDate",startDate);
		context.put("eventEndDate",endDate);
		if (logger.isLoggable(Level.INFO)) {
			logger.finest("eventStartDate: "+eventDetail.getEventStartDate());
			logger.finest("eventEndDate: "+eventDetail.getEventEndDate());
		}
	
		
		boolean hasStartEndDate = true;
		if(eventDetail.getEventStartDate()==null &&eventDetail.getEventEndDate()==null)
		{
			if (logger.isLoggable(Level.INFO)) {
				logger.finest("both event start and end date are null, set hasStartEndDate to false");
			}
				
				hasStartEndDate = false;
					
		}
		if (logger.isLoggable(Level.INFO)) {
			logger.finest("hasStartEndDate: "+hasStartEndDate);
		}
		context.put("hasStartEndDate",hasStartEndDate);
		if (eventDetail.isHasTicketing()) {
			List<ChoiceItem> items = new ArrayList<>();
			String total = processChoiceLists (eventDetail, application, items);
			context.put("items", items);
			context.put("totalCost", total);
		}
		
		if (isUnitEvent) {
			context.put("unitSignOff", getMainDepartment(eventDetail));
			sendEmailForUnit(email, context, SUCCESS_EMAIL_SUBJECT, SUCCESS_EMAIL_BODY, eventDetail);
		} else {
			sendEmail (email, context, SUCCESS_EMAIL_SUBJECT, SUCCESS_EMAIL_BODY);
		}
	}
	
	
	private void emailUnsuccess (GenericEventDetail eventDetail, String email) {
		
		Map<String, Object> context = new HashMap<>();
		boolean isUnitEvent= isUnitEvent(eventDetail);
		context.put("title", eventDetail.getTitle());
		context.put("isUnitEvent", isUnitEvent);
		
		if (isUnitEvent) {
			context.put("unitSignOff", getMainDepartment(eventDetail));
			sendEmailForUnit(email, context, UNSUCCESS_EMAIL_SUBJECT, UNSUCCESS_EMAIL_BODY, eventDetail);
		} else {
			sendEmail (email, context, UNSUCCESS_EMAIL_SUBJECT, UNSUCCESS_EMAIL_BODY);
		}
	}
	
	private String processChoiceLists (GenericEventDetail eventDetail, GenericEventApplication application, List<ChoiceItem> choiceItems) {
		
		DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
		
		Map<String, Integer> choiceMap = getChoiceMap (application.getChoices());
		
		double totalCost = 0;
		
		for (GETicketingSection section : eventDetail.getTicketingSections()) {
			for (GETicketingOption option : section.getTicketingOption()) {
				if (choiceMap.containsKey(option.getId())) {
					String title = option.getTitle();
					int quantity = choiceMap.get(option.getId());
					
					double cost = option.getUnitCost() * quantity;
					totalCost = totalCost + cost;
					
					String costString = decimalFormat.format(cost);
					
					ChoiceItem item = new ChoiceItem (title, quantity, costString);
					choiceItems.add(item);
				}
			}
		}
		
		return decimalFormat.format(totalCost);
	}
	
	private Map<String,Integer> getChoiceMap (List<GETicketingChoice> ticketingChoices) {
		
		Map<String,Integer> result = new HashMap<>();
		
		for (GETicketingChoice choice : ticketingChoices) {
			String optionId = choice.getOptionId();
			int value = choice.getValue();
			
			if (value > 0) {
				result.put(optionId, value);
			}
		}
		
		return result;
	}
	
	
	private void sendEmail (String emailAddress, Map<String, Object> context, String subjectTemplate, String bodyTemplate) {
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(GEM_Module);
				
		TemplateUtil templateUtil = TemplateUtil.getInstance();
		
		String subject = templateUtil.format(subjectTemplate, context);
		String body = templateUtil.format(bodyTemplate, context);
		
		ElectronicMail electronicMail = new ElectronicMail();
		electronicMail.setSubject(subject);
		electronicMail.setHtmlContent(true);
		electronicMail.setText(body);
		electronicMail.setUserAddress(mailSenderConfig.senderAddress());
		String encryptionKey = EnvironmentUtils.getEncryptionKey();
		String password = mailSenderConfig.senderPassword();
		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				password = encipher.decrypt(password);
			}
			catch (Exception e) {
				logger.info("Error while decrypting the configured password");
			}
		}
		electronicMail.setUserPassword(password);
		
		electronicMail.setToAddress(emailAddress);

		try {
			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send email notification to " + Util.replaceNewLine(emailAddress), e);
		}
	}
	
	private void sendEmailForUnit (String emailAddress, Map<String, Object> context, String subjectTemplate, String bodyTemplate, GenericEventDetail eventDetail) {
				
		TemplateUtil templateUtil = TemplateUtil.getInstance();
		
		String subject = templateUtil.format(subjectTemplate, context);
		String body = templateUtil.format(bodyTemplate, context);
		
		ElectronicMail electronicMail = new ElectronicMail();
		electronicMail.setSubject(subject);
		electronicMail.setHtmlContent(true);
		electronicMail.setText(body);
		electronicMail.setUserAddress(eventDetail.getSenderEmailAddress());
		electronicMail.setUserPassword(eventDetail.getSenderEmailPassword());
		
		electronicMail.setToAddress(emailAddress);

		try {
			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send email notification to " + Util.replaceNewLine(emailAddress), e);
		}
	}
	
	private void smsSuccess (GenericEventDetail eventDetail, String phone) {
		
		Map<String, Object> context = new HashMap<>();
		context.put("title", eventDetail.getTitle());
		
		sendSMS (phone, context, SUCCESS_SMS);
	}
	
	private void smsUnsuccess (GenericEventDetail eventDetail, String phone) {
		
		Map<String, Object> context = new HashMap<>();
		context.put("title", eventDetail.getTitle());
		
		sendSMS (phone, context, UNSUCCESS_SMS);
	}
	
	private void sendSMS (String phone, Map<String, Object> context, String smsTemplate) {
		
		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String text = templateUtil.format(smsTemplate, context);

		try {
			notificationService.sendExtSMS(phone, text);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send SMS to " + Util.replaceNewLine(phone), e);
		}
	}
	
	
	public void informOfCancellation (GenericEventDetail eventDetail) {
		
		try {
			String announcementId = eventDetail.getAnnouncementId();
			
			Announcement announcement = announcementDAO.getAnnouncement(announcementId);
			
			Set<PersonalDetail> targetedUsers = announcementUtil.getTargetedUsers(announcement);
			
			Set<String> targetedEmails = new HashSet<>();
			Set<String> targetedMobiles = new HashSet<>();

			// Get the email list for the targeted users 
			announcementUtil.retrieveTargetedUserContacts (announcement, targetedUsers, targetedEmails, targetedMobiles);
			
			emailCancellation (eventDetail, targetedEmails);
			
			smsCancellation (eventDetail, targetedMobiles);
		}
		catch (HibernateException e) {
			logger.log(Level.SEVERE, "Error while inform of event cancellation.", e);
		}
	}
	
	
	private void emailCancellation (GenericEventDetail eventDetail, Set<String> targetedEmails) {
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(GEM_Module);
		
		Map<String, Object> context = new HashMap<>();
		context.put("title", eventDetail.getTitle());
		
		TemplateUtil templateUtil = TemplateUtil.getInstance();
		
		String subject = templateUtil.format(CANCEL_EMAIL_SUBJECT, context);
		String body = templateUtil.format(CANCEL_EMAIL_BODY, context);
		
		boolean isUnitEvent = isUnitEvent(eventDetail);
		
		BatchElectronicMail electronicMail = new BatchElectronicMail ();
		electronicMail.setSubject(subject);
		electronicMail.setHtmlContent(true);
		electronicMail.setText(body);
		//lapkan 20200229: added the check to cater to unit event
		String password = "";
		if(isUnitEvent)
		{
			logger.info("This cancelled event is an unit event");
			if(eventDetail.getSenderEmailAddress()!=""&&eventDetail.getSenderEmailAddress()!=null&&eventDetail.getSenderEmailPassword()!=""&&eventDetail.getSenderEmailPassword()!=null)
			{
				electronicMail.setUserAddress(eventDetail.getSenderEmailAddress());
				password = eventDetail.getSenderEmailPassword();
			}
			else
			{
				logger.info("Missing email address or password");
			}
					
		}
		else
		{
			logger.info("This cancelled event is a PCWF event");
			electronicMail.setUserAddress(mailSenderConfig.senderAddress());
			password = mailSenderConfig.senderPassword();
		}
		
		
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
		electronicMail.setUserPassword(password);
		
		electronicMail.setToRecipients(new ArrayList<>(targetedEmails));
		
		try {
			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send email notification for cancellation.", e);
		}
	}
	
	
	private void smsCancellation (GenericEventDetail eventDetail, Set<String> targetedMobiles) {
		
		Map<String, Object> context = new HashMap<>();
		context.put("title", eventDetail.getTitle());
		
		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String text = templateUtil.format(CANCEL_SMS, context);

		try {
			notificationService.sendExtSMS(new ArrayList<>(targetedMobiles), text);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send SMS for cancellation.", e);
		}
	}


	public void informOfPostpone (GenericEventDetail eventDetail) {
		
		try {
			String announcementId = eventDetail.getAnnouncementId();
			
			Announcement announcement = announcementDAO.getAnnouncement(announcementId);
			Date todayDate = java.util.Calendar.getInstance().getTime();
			
			Set<String> targetedEmails = new HashSet<>();
			Set<String> targetedMobiles = new HashSet<>();
			List<GenericEventApplication> successList = null;
			String nric = "";
			PersonalDetail personalDetail = new PersonalDetail();
			if(eventDetail.getRegistrationEndDate().compareTo(todayDate)<0)
			{
				logger.info("Registration End Date has passed, send only to successful applicants");
				successList = dao.getGenericEventApplications(eventDetail.getId(), GEApplicationStatus.SUCCESSFUL);
				//logger.info
				Set<PersonalDetail> targetedSuccessfulUsers  = new HashSet<>();
				for (int i=0; i<successList.size(); i++) {
					
					nric = successList.get(i).getNric();
					if ( logger.isLoggable( Level.INFO ) ) {
						logger.info(String.format("Successful Applicant: %s", nric));
					}
					try {
						personalDetail = dao.getPersonal(nric);
					} catch (Exception e) {
						logger.severe(String.valueOf(e));
					}
					targetedSuccessfulUsers.add(personalDetail);
					
				}
				announcementUtil.retrieveTargetedUserContacts (announcement, targetedSuccessfulUsers, targetedEmails, targetedMobiles);
			}
			else
			{
				Set<PersonalDetail> targetedUsers = announcementUtil.getTargetedUsers(announcement);
				logger.info("targetedUsers: "+targetedUsers);
				announcementUtil.retrieveTargetedUserContacts (announcement, targetedUsers, targetedEmails, targetedMobiles);
			}

			// Get the email list for the targeted users 
			if ( logger.isLoggable( Level.INFO ) ) {
				logger.info(String.format("TargetedEmails: %s", targetedEmails));
				logger.info(String.format("TargetedMobiles: %s", targetedMobiles));
			}
			emailPostpone (eventDetail, targetedEmails);
			
			smsPostpone(eventDetail, targetedMobiles);
		}
		catch (HibernateException e) {
			logger.log(Level.SEVERE, "Error while inform of event cancellation.", e);
		}
	}
	
private void emailPostpone (GenericEventDetail eventDetail, Set<String> targetedEmails) {
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(GEM_Module);
		
		Map<String, Object> context = new HashMap<>();
		String startDate = "";
		if(eventDetail.getEventStartDate()==null)
		{
			startDate ="";
		}
		else
		{
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(eventDetail.getEventStartDate());
		if(startDateCalendar.get(Calendar.DAY_OF_MONTH)<10)
		{
			startDate +="0";
		}
		startDate += String.valueOf(startDateCalendar.get(Calendar.DAY_OF_MONTH));
		startDate += "/";
		if(startDateCalendar.get(Calendar.MONTH)<=8)
		{
			startDate +="0";
		}
		startDate += String.valueOf(1+ startDateCalendar.get(Calendar.MONTH));
		startDate += "/";
		startDate += String.valueOf(startDateCalendar.get(Calendar.YEAR));
		}
		String endDate = "";
		if(eventDetail.getEventEndDate()==null)
		{
			endDate = "";
		}
		else
		{
		endDate = "";
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(eventDetail.getEventEndDate());
		if(endDateCalendar.get(Calendar.DAY_OF_MONTH)<10)
		{
			endDate +="0";
		}
		endDate += String.valueOf(endDateCalendar.get(Calendar.DAY_OF_MONTH));
		endDate += "/";
		if(endDateCalendar.get(Calendar.MONTH)<=8)
		{
			endDate +="0";
		}
		endDate += String.valueOf(1+ endDateCalendar.get(Calendar.MONTH));
		endDate += "/";
		endDate += String.valueOf(endDateCalendar.get(Calendar.YEAR));
		}
		
		context.put("title", eventDetail.getTitle());
		context.put("eventStartDate", startDate);
		context.put("eventEndDate", endDate);
		context.put("isUnitEvent", isUnitEvent(eventDetail));
		context.put("unitSignOff", getMainDepartment(eventDetail));
		TemplateUtil templateUtil = TemplateUtil.getInstance();
		
		String subject = templateUtil.format(POSTPONE_EMAIL_SUBJECT, context);
		String body = templateUtil.format(POSTPONE_EMAIL_BODY, context);
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("subject for postpone email is: %s", subject));
			logger.info(String.format("body for postpone email is: %s", body));
		}
		BatchElectronicMail electronicMail = new BatchElectronicMail ();
		electronicMail.setSubject(subject);
		electronicMail.setHtmlContent(true);
		electronicMail.setText(body);
		String password = "";
		boolean isUnitEvent = isUnitEvent(eventDetail);
		if(isUnitEvent)
		{
			logger.info("This postponed event is an unit event");
			electronicMail.setUserAddress(eventDetail.getSenderEmailAddress());
			password = eventDetail.getSenderEmailPassword();
					
		}
		else
		{
			logger.info("This postponed event is an PCWF event");
			electronicMail.setUserAddress(mailSenderConfig.senderAddress());
			password = mailSenderConfig.senderPassword();
		}
		
		
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
		electronicMail.setUserPassword(password);
		
		electronicMail.setToRecipients(new ArrayList<>(targetedEmails));
		
		try {
			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send email notification for postpone.", e);
		}
	}
	
	
	private void smsPostpone (GenericEventDetail eventDetail, Set<String> targetedMobiles) {
		
		Map<String, Object> context = new HashMap<>();
		context.put("title", eventDetail.getTitle());
		context.put("eventStartDate", eventDetail.getEventStartDate());
		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String text = templateUtil.format(POSTPONE_SMS, context);

		try {
			notificationService.sendExtSMS(new ArrayList<>(targetedMobiles), text);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Fail to send SMS for postpone.", e);
		}
	}
	

	private boolean isUnitEvent (GenericEventDetail eventDetail) {
		return (!eventDetail.getDepartments().isEmpty());
	}
	
	private String getMainDepartment (GenericEventDetail eventDetail) {
		String departmentStr = "";
		if (eventDetail != null) {
			for (GenericEventDepartment department : eventDetail.getDepartments()) {
				if (department.isMainDepartment()) {
					departmentStr = department.getDepartment();
					break;
				}
			}
		}
		return departmentStr;
	}

	
}
