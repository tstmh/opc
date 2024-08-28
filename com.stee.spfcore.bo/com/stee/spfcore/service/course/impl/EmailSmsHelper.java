package com.stee.spfcore.service.course.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.CourseDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseParticipant;
import com.stee.spfcore.model.course.CourseType;
import com.stee.spfcore.model.course.ParticipantStatus;
import com.stee.spfcore.model.course.ReminderAttachment;
import com.stee.spfcore.model.course.Slot;
import com.stee.spfcore.model.course.internal.ReminderStatus;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.service.configuration.IMailRecipientConfig;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.marketingContent.MarketingContentException;
import com.stee.spfcore.service.marketingContent.impl.ECMUtil;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.service.report.IReportService;
import com.stee.spfcore.service.report.ReportServiceException;
import com.stee.spfcore.service.report.ReportServiceFactory;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.UserGroupUtil;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.template.TemplateUtil;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;

public class EmailSmsHelper {

	private static final Logger logger = Logger.getLogger(EmailSmsHelper.class.getName());

	private static final ParticipantStatus[] COMPULSORY_CANCEL = new ParticipantStatus[] { ParticipantStatus.NOMINATED,
			ParticipantStatus.SUCCESSFUL };
	
	private static final ParticipantStatus[] NON_COMPULSORY_CANCEL = new ParticipantStatus[] {
			ParticipantStatus.REGISTERED, ParticipantStatus.SUCCESSFUL, ParticipantStatus.SELECTED };

	private static final ParticipantStatus[] SUCCESS_PARTICIPANTS = new ParticipantStatus [] {ParticipantStatus.SUCCESSFUL};
	
	private static final String CANCELLATION_EMAIL_SUBJECT = "WFC-E015_Subject.vm";
	private static final String CANCELLATION_EMAIL_BODY = "WFC-E015_Body.vm";
	private static final String CANCELLATION_SMS_TEXT = "WFC-S012.vm";
	private static final String CANCELLATION_EMAIL_SUPERVISOR_SUBJECT = "WFC-E016_Subject.vm";
	private static final String CANCELLATION_EMAIL_SUPERVISOR_BODY = "WFC-E016_Body.vm";
	private static final String REMINDER_EMAIL_SUBJECT = "WFC-E012_Subject.vm";
	private static final String REMINDER_EMAIL_BODY = "WFC-E012_Body.vm";
	private static final String REMINDER_SMS_TEXT = "WFC-S009.vm";
	private static final String SUCCESS_EMAIL_SUBJECT = "WFC-E006_Subject.vm";
	private static final String SUCCESS_EMAIL_BODY = "WFC-E006_Body.vm";
	private static final String SUCCESS_SMS_TEXT = "WFC-S005.vm";
	private static final String SUCCESS_EMAIL_SUPERVISOR_SUBJECT = "WFC-E009_Subject.vm";
	private static final String SUCCESS_EMAIL_SUPERVISOR_BODY = "WFC-E009_Body.vm";
	private static final String UNSUCCESS_EMAIL_SUBJECT = "WFC-E007_Subject.vm";
	private static final String UNSUCCESS_EMAIL_BODY = "WFC-E007_Body.vm";
	private static final String UNSUCCESS_SMS_TEXT = "WFC-S006.vm";
	private static final String WAITLIST_EMAIL_SUBJECT = "WFC-E008_Subject.vm";
	private static final String WAITLIST_EMAIL_BODY = "WFC-E008_Body.vm";
	private static final String WAITLIST_SMS_TEXT = "WFC-S007.vm";
	private static final String WITHDRAW_EMAIL_SUPERVISOR_SUBJECT = "WFC-E010_Subject.vm";
	private static final String WITHDRAW_EMAIL_SUPERVISOR_BODY = "WFC-E010_Body.vm";
	private static final String SELECTED_EMAIL_SUBJECT = "WFC-E011_Subject.vm";
	private static final String SELECTED_EMAIL_BODY = "WFC-E011_Body.vm";
	private static final String SELECTED_SMS_TEXT = "WFC-S008.vm";
	private static final String CONFIRM_REGISTRATION_OUTCOME_EMAIL_SUBJECT = "WFC-E017_Subject.vm";
	private static final String CONFIRM_REGISTRATION_OUTCOME_EMAIL_BODY = "WFC-E017_Body.vm";
	private static final String REMIND_CONFIRM_REGISTRATION_OUTCOME_EMAIL_SUBJECT = "WFC-E018_Subject.vm";
	private static final String REMIND_CONFIRM_REGISTRATION_OUTCOME_EMAIL_BODY = "WFC-E018_Body.vm";
	private static final String REGISTER_EMAIL_SUBJECT = "WFC-E019_Subject.vm";
	private static final String REGISTER_EMAIL_BODY = "WFC-E019_Body.vm";
	private static final String REGISTER_SMS_TEXT = "WFC-S010.vm";
	private static final String SLOT_REPORT_NAME = "WFC Course Slot Report";
	private static final String COOLING_END_EMAIL_SUBJECT = "WFC-E020_Subject.vm";
	private static final String COOLING_END_EMAIL_BODY = "WFC-E020_Body.vm";
	private static final String NO_REPLACEMENT_EMAIL_SUBJECT = "WFC-E021_Subject.vm";
	private static final String NO_REPLACEMENT_EMAIL_BODY = "WFC-E021_Body.vm";
	private static final String REPLACEMENT_SELECTION_EMAIL_SUBJECT = "WFC-E022_Subject.vm";
	private static final String REPLACEMENT_SELECTION_EMAIL_BODY = "WFC-E022_Body.vm";
	
	
	private CourseDAO courseDao;
	private PersonnelDAO personnelDAO;
	private INotificationService notificationService;
	private IReportService reportService;
	private EmailUtil emailUtil;
	private ECMUtil ecmUtil;
	
	public EmailSmsHelper() {
		courseDao = new CourseDAO();
		personnelDAO = new PersonnelDAO();
		notificationService = NotificationServiceFactory.getInstance();
		reportService = ReportServiceFactory.getInstance();
		emailUtil = new EmailUtil();
		
		try {
			ecmUtil = new ECMUtil();
		}
		catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Fail to construct ECMUtil.", e);
		}
		
	}

	public void informOfCancellation(Course course) {

		try {
			List<Slot> notStartedSlotList = findSlotNotStarted(course);

			// Don't need to send. All slot over.
			if (notStartedSlotList.isEmpty()) {
				logger.finest("All slots are started. Don\'t need to inform.");
				return;
			}

			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}

			Map<String, Object> context = new HashMap<>();
			context.put("isEvent", course.isEvent());
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());

			ParticipantStatus[] participantStatus = null;
			boolean sendToOffice = false;

			if (course.getType() == CourseType.COMPULSORY) {
				participantStatus = COMPULSORY_CANCEL;
				sendToOffice = true;
			}
			else {
				participantStatus = NON_COMPULSORY_CANCEL;
				sendToOffice = false;
			}

			for (Slot slot : notStartedSlotList) {
				informCancellation(course, slot, context, participantStatus, sendToOffice, senderEmail, senderPassword);
			}

		}
		catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "Error while inform of course cancellation.", e);
		}
	}

	private void informCancellation(Course course, Slot slot, Map<String, Object> context,
			ParticipantStatus[] participantStatus, boolean sendToOffice, String senderEmail, String senderPassword) throws AccessDeniedException {

		populateSlotInfo(course, slot, context);

		List<CourseParticipant> participants = courseDao.getCourseParticipants(course.getId(), slot.getId(),
				participantStatus);

		for (CourseParticipant participant : participants) {
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			CourseUtil.retrieveParticipantContacts(sendToOffice, personalDetail, emails, phones);

			context.put("participantName", personalDetail.getName());

			if (!emails.isEmpty()) {
				try {
					sendEmail(emails, context, CANCELLATION_EMAIL_SUBJECT, CANCELLATION_EMAIL_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course cancellation email to participant:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
			else if (!phones.isEmpty()) {
				try {
					sendSMS(phones, context, CANCELLATION_SMS_TEXT);
				}
				catch (NotificationServiceException e) {
					// SMS sending may fail due to wrong SMS enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course cancellation sms to participant:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}

			// Non-compulsory, send to supervisor
			if (course.getType() == CourseType.NON_COMPULSORY && participant.getSupervisor() != null) {
				String supervisorEmail = participant.getSupervisor().getEmail();
				try {
					sendEmail(supervisorEmail, context, CANCELLATION_EMAIL_SUPERVISOR_SUBJECT,
							CANCELLATION_EMAIL_SUPERVISOR_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course cancellation email to Supervisor:" + Util.replaceNewLine( supervisorEmail ), e);
				}
			}
		}
	}

	private void populateSlotInfo(Course course, Slot slot, Map<String, Object> context) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		context.put("startDate", dateFormat.format(slot.getStartDate()));
		context.put("endDate", dateFormat.format(slot.getEndDate()));
		context.put("startTime", timeFormat.format(slot.getStartDate()));
		context.put("endTime", timeFormat.format(slot.getEndDate()));
		
		context.put("venue", slot.getVenue());
		
		if (course.getCoolingPeriodEndDate() != null) {
			context.put("coolingPeriodEnd", dateFormat.format(course.getCoolingPeriodEndDate()));
		}
		else {
			context.put("coolingPeriodEnd", "");
		}
	}

	private List<Slot> findSlotNotStarted(Course course) {

		List<Slot> result = new ArrayList<>();
		Date now = new Date();

		for (Slot slot : course.getSlots()) {
			if (now.before(slot.getStartDate())) {
				result.add(slot);
			}
		}
		return result;
	}

	private void sendEmail(Set<String> emails, Map<String, Object> context, String subjectTemplate, String bodyTemplate,
			String senderEmail, String senderPassword) throws NotificationServiceException {

		String address = convertToSingleAddressString(emails);
		sendEmail (address, context, subjectTemplate, bodyTemplate, senderEmail, senderPassword);
	}

	
	private void sendEmail(String email, Map<String, Object> context, String subjectTemplate, String bodyTemplate,
			String senderEmail, String senderPassword) throws NotificationServiceException {

		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String subject = templateUtil.format(subjectTemplate, context);
		String body = templateUtil.format(bodyTemplate, context);

		ElectronicMail electronicMail = new ElectronicMail();
		electronicMail.setSubject(subject);
		electronicMail.setHtmlContent(true);
		electronicMail.setText(body);
		electronicMail.setUserAddress(senderEmail);
		electronicMail.setUserPassword(senderPassword);
		//To cc SWB Team
		electronicMail.setCcAddress(senderEmail);
		electronicMail.setToAddress(email);

		notificationService.send(electronicMail);
	}
	
	private void sendEmailWithCc (String emails,Set<String> ccEmails, Map<String, Object> context, String subjectTemplate, String bodyTemplate,
			String senderEmail, String senderPassword) throws NotificationServiceException {
		
		String ccAddresses = convertToSingleAddressString(ccEmails);
		
		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String subject = templateUtil.format(subjectTemplate, context);
		String body = templateUtil.format(bodyTemplate, context);

		ElectronicMail electronicMail = new ElectronicMail();
		electronicMail.setSubject(subject);
		electronicMail.setHtmlContent(true);
		electronicMail.setText(body);
		electronicMail.setUserAddress(senderEmail);
		electronicMail.setUserPassword(senderPassword);
		electronicMail.setCcAddress(ccAddresses);
		electronicMail.setToAddress(emails);

		notificationService.send(electronicMail);
	}

	private String convertToSingleAddressString(Set<String> emails) {
		Iterator<String> iterator = emails.iterator();
		
		StringBuilder builder = new StringBuilder(iterator.next());
		while (iterator.hasNext()) {
			builder.append(",").append(iterator.next());
		}
		
		return builder.toString();
	}

	private void sendSMS (Set<String> phones, Map<String, Object> context, String smsTemplate)
			throws NotificationServiceException {
		TemplateUtil templateUtil = TemplateUtil.getInstance();

		String text = templateUtil.format(smsTemplate, context);
		notificationService.sendExtSMS(new ArrayList<>(phones), text);
	}
	
	
	public void sendCourseReminder (Course course) {
		
		try {
			List<Slot> slots = findSlotNeedToSendReminder (course);

			// Don't need to send. No slot required send reminder.
			if (slots.isEmpty()) {
				logger.log(Level.FINEST, "No slot required to send reminder now.");
				return;
			}

			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}

			// Will just send to office email as there is attachment
			boolean sendToOffice = true;

			for (Slot slot : slots) {
				sendReminder (course, slot, sendToOffice, senderEmail, senderPassword);
			}

		}
		catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "Error while inform of course reminder.", e);
		}
		
	}
	
	@SuppressWarnings("unused")
	private void sendReminder (Course course, Slot slot, Map<String, Object> context, boolean sendToOffice, 
															String senderEmail, String senderPassword) throws AccessDeniedException {

		populateSlotInfo(course, slot, context);

		List<CourseParticipant> participants = courseDao.getCourseParticipants(course.getId(), slot.getId(),
				SUCCESS_PARTICIPANTS);

		for (CourseParticipant participant : participants) {
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			CourseUtil.retrieveParticipantContacts(sendToOffice, personalDetail, emails, phones);

			context.put("participantName", personalDetail.getName());

			if (!emails.isEmpty()) {
				try {
					sendEmail(emails, context, REMINDER_EMAIL_SUBJECT, REMINDER_EMAIL_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course reminder email:" + personalDetail.getName(), e);
				}
			}
			else if (!phones.isEmpty()) {
				try {
					sendSMS(phones, context, REMINDER_SMS_TEXT);
				}
				catch (NotificationServiceException e) {
					// SMS sending may fail due to wrong SMS enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course reminder sms:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
		}
		
		ReminderStatus status = new ReminderStatus(course.getId(), slot.getId(), true);
		courseDao.saveReminderStatus(status);
	}
	
	private void sendReminder(Course course, Slot slot, boolean sendToOffice, String senderEmail, String senderPassword) throws AccessDeniedException {
		List<CourseParticipant> participants = courseDao.getCourseParticipants(course.getId(), slot.getId(),
				SUCCESS_PARTICIPANTS);
		
		List<BinaryFile> binaryFiles = new ArrayList<>();
		
		for (ReminderAttachment attachment: slot.getAttachments()) {
			BinaryFile binaryFile;
			try {
				binaryFile = ecmUtil.download(attachment.getDocId());
				binaryFiles.add(binaryFile);
			} catch (MarketingContentException e) {
				// Download will fail, will ignore
				logger.log(Level.WARNING, "Fail to download attachment: " + attachment.getDocId(), e);
			}
		}
		
		List<String> ccAddress = new ArrayList<>();
		ccAddress.add(senderEmail);
		
		String subject = slot.getEmailSubject();
		String body = formatString(slot.getEmailBody());
		
		for (CourseParticipant participant : participants) {
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			CourseUtil.retrieveParticipantContacts(sendToOffice, personalDetail, emails, phones);
			if (!emails.isEmpty()) {
				emailUtil.sendEmailWithAttachment(subject, body, senderEmail, senderPassword, new ArrayList<>(emails), binaryFiles, null);
			} else if (!phones.isEmpty()) {
				try {
					notificationService.sendExtSMS(new ArrayList<>(phones), slot.getSmsText());
				} catch (NotificationServiceException e) {
					// SMS sending may fail due to wrong SMS enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course reminder sms:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
		}
		
		ReminderStatus status = new ReminderStatus(course.getId(), slot.getId(), true);
		courseDao.saveReminderStatus(status);
	}
	
	
	private List<Slot> findSlotNeedToSendReminder (Course course) {

		List<Slot> result = new ArrayList<>();
		Date now = new Date();

		for (Slot slot : course.getSlots()) {
			if (now.after (slot.getReminderDate())) {
				ReminderStatus reminderStatus = courseDao.getReminderStatus(course.getId(), slot.getId());
				
				if (reminderStatus != null && reminderStatus.isSent()) {
					continue;
				}
				
				result.add(slot);
			}
		}
		return result;
	}
	
	
	public void informAllocationStatus (Course course) {
		
		try {
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}

			Map<String, Object> context = new HashMap<>();
			context.put("isEvent", course.isEvent());
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			context.put("confirm", course.isConfirm());

			for (Slot slot : course.getSlots()) {
				informAllocationStatus (course, slot, context, senderEmail, senderPassword);
			}

		}
		catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "Error while inform of course allocation status.", e);
		}
	}
	
	
	private void informAllocationStatus (Course course, Slot slot, Map<String, Object> context, 
													String senderEmail, String senderPassword) throws AccessDeniedException {

		populateSlotInfo(course, slot, context);
		
		informSuccessfulStatus (course, slot, context, senderEmail, senderPassword);
		
		informUnsuccessfulStatus (course, slot, context, senderEmail, senderPassword);
		
		informWaitlistStatus (course, slot, context, senderEmail, senderPassword);
	}
	
	
	private void informSuccessfulStatus (Course course, Slot slot, Map<String, Object> context, 
			String senderEmail, String senderPassword) throws AccessDeniedException {

		
		List<CourseParticipant> participants = courseDao.getCourseParticipants(course.getId(), slot.getId(), ParticipantStatus.SUCCESSFUL);
		
		for (CourseParticipant participant : participants) {
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			// Non-compulsory only send to preferred communication.
			CourseUtil.retrieveParticipantContacts(false, personalDetail, emails, phones);

			context.put("participantName", personalDetail.getName());

			if (!emails.isEmpty()) {
				try {
					sendEmail(emails, context, SUCCESS_EMAIL_SUBJECT, SUCCESS_EMAIL_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course success email:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
			else if (!phones.isEmpty()) {
				try {
					sendSMS(phones, context, SUCCESS_SMS_TEXT);
				}
				catch (NotificationServiceException e) {
					// SMS sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course success SMS:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
			
			if (participant.getSupervisor() != null && participant.getSupervisor().getEmail() != null) {
				String supervisorEmail = participant.getSupervisor().getEmail();
				try {
					sendEmail(supervisorEmail, context, SUCCESS_EMAIL_SUPERVISOR_SUBJECT,
							SUCCESS_EMAIL_SUPERVISOR_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course success email to Supervisor:" + Util.replaceNewLine( supervisorEmail ), e);
				}
			}
			
		}
	}
	
	public void informSupervisorConfirmation (CourseParticipant participant) {
		
		if (participant.getSupervisor() != null && participant.getSupervisor().getEmail() != null) {
			
			Course course = courseDao.getCourse(participant.getCourseId());
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}

			Map<String, Object> context = new HashMap<>();
			context.put("isEvent", course.isEvent());
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());

			Slot slot = null;
			
			for (Slot courseSlot : course.getSlots()) {
				if (courseSlot.getId().equals(participant.getSlotId())) {
					slot = courseSlot;
				}
			} 
			
			if (slot == null) {
				logger.log(Level.SEVERE, "Unable to get slot for participant: " + Util.replaceNewLine( participant.getNric() ));
				return;
			}
			
			populateSlotInfo(course, slot, context);
			
			PersonalDetail personalDetail = null;
			try {
				personalDetail = personnelDAO.getPersonal(participant.getNric());
			}
			catch (AccessDeniedException e) {
				logger.log(Level.SEVERE, "Error while retrieving personal detail of " + Util.replaceNewLine( participant.getNric() ), e);
				return;
			}
			
			context.put("participantName", personalDetail.getName());
			
			String supervisorEmail = participant.getSupervisor().getEmail();
			try {
				sendEmail(supervisorEmail, context, SUCCESS_EMAIL_SUPERVISOR_SUBJECT,
						SUCCESS_EMAIL_SUPERVISOR_BODY, senderEmail, senderPassword);
			}
			catch (NotificationServiceException e) {
				// Email sending may fail due to wrong email enter by end user.
				// Will just ignore.
				logger.log(Level.WARNING, "Fail to send course success email to Supervisor:" + Util.replaceNewLine( supervisorEmail ), e);
			}
		}
		else {
			logger.log(Level.WARNING, "Supervisor email not specified for participant: " + Util.replaceNewLine( participant.getNric() ));
		}
	}
	
	
	
	private void informUnsuccessfulStatus (Course course, Slot slot, Map<String, Object> context, 
			String senderEmail, String senderPassword) throws AccessDeniedException {
		
		List<CourseParticipant> participants = courseDao.getCourseParticipants(course.getId(), slot.getId(), ParticipantStatus.UNSUCCESSFUL);
		
		for (CourseParticipant participant : participants) {
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			// Non-compulsory only send to preferred communication.
			CourseUtil.retrieveParticipantContacts(false, personalDetail, emails, phones);

			context.put("participantName", personalDetail.getName());

			if (!emails.isEmpty()) {
				try {
					sendEmail(emails, context, UNSUCCESS_EMAIL_SUBJECT, UNSUCCESS_EMAIL_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course unsuccessful email:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
			else if (!phones.isEmpty()) {
				try {
					sendSMS(phones, context, UNSUCCESS_SMS_TEXT);
				}
				catch (NotificationServiceException e) {
					// SMS sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course unsuccessful SMS:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
		}
	}
	
	private void informWaitlistStatus (Course course, Slot slot, Map<String, Object> context, 
			String senderEmail, String senderPassword) throws AccessDeniedException {
		
		List<CourseParticipant> participants = courseDao.getCourseParticipants(course.getId(), slot.getId(), ParticipantStatus.WAIT_LIST);
		
		for (CourseParticipant participant : participants) {
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			// Non-compulsory only send to preferred communication.
			CourseUtil.retrieveParticipantContacts(false, personalDetail, emails, phones);

			context.put("participantName", personalDetail.getName());

			if (!emails.isEmpty()) {
				try {
					sendEmail(emails, context, WAITLIST_EMAIL_SUBJECT, WAITLIST_EMAIL_BODY, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					// Email sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course waitlist email:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
			else if (!phones.isEmpty()) {
				try {
					sendSMS(phones, context, WAITLIST_SMS_TEXT);
				}
				catch (NotificationServiceException e) {
					// SMS sending may fail due to wrong email enter by end user.
					// Will just ignore.
					logger.log(Level.WARNING, "Fail to send course waitlist SMS:" + Util.replaceNewLine(personalDetail.getName()), e);
				}
			}
		}
	}
	
	
	public void informSupervisorWithdrawal (CourseParticipant participant) {
		
		try {
			Course course = courseDao.getCourse(participant.getCourseId());
			
			PersonalDetail detail = personnelDAO.getPersonal(participant.getNric());
			
			// Get preferred email for participant
			PersonalPreferredContacts preferredContact = new PersonalPreferredContacts(detail);
			
			Set<String> ccAddresses = new HashSet<>();
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			//CC to SWB Team
			ccAddresses.add(senderEmail);
			
			//Add participant preferred email
			logger.info("Preferred Email: " + preferredContact.getPreferredEmail());
			if (preferredContact.getPreferredEmail() != null ) {
				ccAddresses.add(preferredContact.getPreferredEmail());
			}
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}

			Map<String, Object> context = new HashMap<>();
			context.put("isEvent", course.isEvent());
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());

			for (Slot slot : course.getSlots()) {
				if (slot.getId().equals(participant.getSlotId())) {
					informSupervisorWithdrawal (course, slot, participant, context, senderEmail, senderPassword, ccAddresses);
					break;
				}
			}

		}
		catch (AccessDeniedException e) {
			logger.log(Level.SEVERE, "Error while inform of course allocation status.", e);
		}
	}
	
	
	private void informSupervisorWithdrawal (Course course, Slot slot, CourseParticipant participant, Map<String, Object> context, 
			String senderEmail, String senderPassword) throws AccessDeniedException {

		populateSlotInfo(course, slot, context);
		
		context.put("participantName", participant.getName());
		
		if (participant.getSupervisor() != null && participant.getSupervisor().getEmail() != null) {
			String supervisorEmail = participant.getSupervisor().getEmail();
			try {
				sendEmail(supervisorEmail, context, WITHDRAW_EMAIL_SUPERVISOR_SUBJECT,
							WITHDRAW_EMAIL_SUPERVISOR_BODY, senderEmail, senderPassword);
			}
			catch (NotificationServiceException e) {
				// Email sending may fail due to wrong email enter by end user.
				// Will just ignore.
				logger.log(Level.WARNING, "Fail to send course withdrawal email to supervisor:" + Util.replaceNewLine(supervisorEmail), e);
			}
		}
	}
	
	private void informSupervisorWithdrawal (Course course, Slot slot, CourseParticipant participant, Map<String, Object> context, 
			String senderEmail, String senderPassword, Set<String> ccAddresses) throws AccessDeniedException {

		populateSlotInfo(course, slot, context);
		
		context.put("participantName", participant.getName());
		
		if (participant.getSupervisor() != null && participant.getSupervisor().getEmail() != null) {
			String supervisorEmail = participant.getSupervisor().getEmail();
			
			try {
				sendEmailWithCc(supervisorEmail, ccAddresses, context, WITHDRAW_EMAIL_SUPERVISOR_SUBJECT, 
						WITHDRAW_EMAIL_SUPERVISOR_BODY, senderEmail, senderPassword);
			}
			catch (NotificationServiceException e) {
				// Email sending may fail due to wrong email enter by end user.
				// Will just ignore.
				logger.log(Level.WARNING, "Fail to send course withdrawal email to supervisor:" + Util.replaceNewLine(supervisorEmail), e);
			}
		}
	}
	
	
	public void informSelectedStatus (Course course, Slot slot, CourseParticipant participant) {
		
		try {
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			Map<String, Object> context = new HashMap<>();
			context.put("isEvent", course.isEvent());
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			
			populateSlotInfo(course, slot, context);
			
			context.put("participantName", participant.getName());
			
			Set<String> emails = new HashSet<>();
			Set<String> phones = new HashSet<>();

			PersonalDetail personalDetail = personnelDAO.getPersonal(participant.getNric());
			
			// Non-compulsory only send to preferred communication.
			CourseUtil.retrieveParticipantContacts(false, personalDetail, emails, phones);

			context.put("participantName", personalDetail.getName());

			if (!emails.isEmpty()) {
				sendEmail(emails, context, SELECTED_EMAIL_SUBJECT, SELECTED_EMAIL_BODY, senderEmail, senderPassword);
			}
			else if (!phones.isEmpty()) {
				sendSMS(phones, context, SELECTED_SMS_TEXT);
			}
		}
		catch (AccessDeniedException | NotificationServiceException e) {
			logger.log(Level.SEVERE, "Error while inform of selected for replacement status.", e);
		}
	}
	
	
	public void informConfirmRegistrationOutcome (Course course, Date cutoffDate) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}

			IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(course.getCategoryId());
			
			Map<String, Object> context = new HashMap<>();
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			context.put("endDate", dateFormat.format(cutoffDate));
			
			TemplateUtil templateUtil = TemplateUtil.getInstance();

			String subject = templateUtil.format(CONFIRM_REGISTRATION_OUTCOME_EMAIL_SUBJECT, context);
			String body = templateUtil.format(CONFIRM_REGISTRATION_OUTCOME_EMAIL_BODY, context);

			String toAddress = retrieveGroupEmails (recipientConfig.toRecipientGroups());
			String ccAddress = retrieveGroupEmails (recipientConfig.ccRecipientGroups());
			
			ElectronicMail electronicMail = new ElectronicMail();
			electronicMail.setSubject(subject);
			electronicMail.setHtmlContent(true);
			electronicMail.setText(body);
			electronicMail.setUserAddress(senderEmail);
			electronicMail.setUserPassword(senderPassword);

			electronicMail.setToAddress(toAddress);
			electronicMail.setCcAddress(ccAddress);

			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Error while inform course admin to confirm registration outcome.", e);
		}
	} 
	
	
	private String retrieveGroupEmails (List<String> groups) {
		
		List<String> users = UserGroupUtil.getUsersInGroups(groups);
		if (users == null || users.isEmpty()) {
			return null;
		}
		
		List<String> addresses = personnelDAO.getOfficeEmailAddress(users);
		
		if (addresses == null || addresses.isEmpty()) {
			return null;
		}
		
		return convertToSingleAddressString (new HashSet<>(addresses));
	}
	
	private List<String> retrieveGroupEmailList (List<String> groups) {
		
		List<String> users = UserGroupUtil.getUsersInGroups(groups);
		if (users == null || users.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<String> addresses = personnelDAO.getOfficeEmailAddress(users);
		
		if (addresses == null || addresses.isEmpty()) {
			return Collections.emptyList();
		}
		
		return addresses;
	}
	
	
	public void remindConfirmRegistrationOutcome (Course course, Date cutoffDate) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(course.getCategoryId());
			
			Map<String, Object> context = new HashMap<>();
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			context.put("endDate", dateFormat.format(cutoffDate));
			
			TemplateUtil templateUtil = TemplateUtil.getInstance();

			String subject = templateUtil.format(REMIND_CONFIRM_REGISTRATION_OUTCOME_EMAIL_SUBJECT, context);
			String body = templateUtil.format(REMIND_CONFIRM_REGISTRATION_OUTCOME_EMAIL_BODY, context);

			String toAddress = retrieveGroupEmails (recipientConfig.toRecipientGroups());
			String ccAddress = retrieveGroupEmails (recipientConfig.ccRecipientGroups());
			
			ElectronicMail electronicMail = new ElectronicMail();
			electronicMail.setSubject(subject);
			electronicMail.setHtmlContent(true);
			electronicMail.setText(body);
			electronicMail.setUserAddress(senderEmail);
			electronicMail.setUserPassword(senderPassword);

			electronicMail.setToAddress(toAddress);
			electronicMail.setCcAddress(ccAddress);

			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Error while remind course admin to confirm registration outcome.", e);
		}
	} 
	
	public void informParticipantStatus (Course course, Slot slot, CourseParticipant participant, PersonalDetail personalDetail, Set<String> emails, Set<String> phones ) {
		try {
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			String emailBody = "";
			String emailSubject = "";
			String smsBody = "";
			String supervisorEmailBody = "";
			String supervisorEmailSubject = "";
			boolean informSupervisor = false;
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			
			Map<String, Object> context = new HashMap<>();
			
			context.put("isEvent", course.isEvent());
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			context.put("participantName", personalDetail.getName());
			context.put("confirm", course.isConfirm());
			
			populateSlotInfo(course, slot, context);
			
			if (course.getType() == CourseType.COMPULSORY && participant.getStatus() == ParticipantStatus.SUCCESSFUL) {
				
				informSupervisor = true;
				emailSubject = SUCCESS_EMAIL_SUBJECT;
				emailBody = SUCCESS_EMAIL_BODY;
				smsBody = SUCCESS_SMS_TEXT;
				supervisorEmailSubject = SUCCESS_EMAIL_SUPERVISOR_SUBJECT;
				supervisorEmailBody = SUCCESS_EMAIL_SUPERVISOR_BODY;
			
			} else if (course.getType() == CourseType.COMPULSORY && participant.getStatus() == ParticipantStatus.UNSUCCESSFUL) {
			
				emailSubject = UNSUCCESS_EMAIL_SUBJECT;
				emailBody = UNSUCCESS_EMAIL_BODY;
				smsBody = UNSUCCESS_SMS_TEXT;
				
			} else if (course.getType() == CourseType.NON_COMPULSORY && participant.getStatus() == ParticipantStatus.REGISTERED) {
				
				emailSubject = REGISTER_EMAIL_SUBJECT;
				emailBody = REGISTER_EMAIL_BODY;
				smsBody = REGISTER_SMS_TEXT;
				
			} else if (course.getType() == CourseType.NON_COMPULSORY && participant.getStatus() == ParticipantStatus.SELECTED) {
				
				emailSubject = SELECTED_EMAIL_SUBJECT;
				emailBody = SELECTED_EMAIL_BODY;
				smsBody = SELECTED_SMS_TEXT;
				
			} else if (course.getType() == CourseType.NON_COMPULSORY && participant.getStatus() == ParticipantStatus.WITHDRAWN) {
				
				informSupervisor = true;
				supervisorEmailSubject = WITHDRAW_EMAIL_SUPERVISOR_SUBJECT;
				supervisorEmailBody = WITHDRAW_EMAIL_SUPERVISOR_BODY;
				
			} else if (course.getType() == CourseType.NON_COMPULSORY && participant.getStatus() == ParticipantStatus.SUCCESSFUL) {
				
				informSupervisor = true;
				emailSubject = SUCCESS_EMAIL_SUBJECT;
				emailBody = SUCCESS_EMAIL_BODY;
				smsBody = SUCCESS_SMS_TEXT;
				supervisorEmailSubject = SUCCESS_EMAIL_SUPERVISOR_SUBJECT;
				supervisorEmailBody = SUCCESS_EMAIL_SUPERVISOR_BODY;
				
			} else if (course.getType() == CourseType.NON_COMPULSORY && participant.getStatus() == ParticipantStatus.UNSUCCESSFUL) {
				
				emailSubject = UNSUCCESS_EMAIL_SUBJECT;
				emailBody = UNSUCCESS_EMAIL_BODY;
				smsBody = UNSUCCESS_SMS_TEXT;
				
			} else if (course.getType() == CourseType.NON_COMPULSORY && participant.getStatus() == ParticipantStatus.WAIT_LIST) {

				emailSubject = WAITLIST_EMAIL_SUBJECT;
				emailBody = WAITLIST_EMAIL_BODY;
				smsBody = WAITLIST_SMS_TEXT;
			}
			
			//Send Email and SMS based on STATUS
			if (!emails.isEmpty()) {
				try {
					sendEmail(emails, context, emailSubject, emailBody, senderEmail, senderPassword);
				}
				catch (NotificationServiceException e) {
					logger.log(Level.WARNING, "Fail to inform participant status via email " + Util.replaceNewLine(participant.getNric()), e);
				}
			}
			else if (!phones.isEmpty()) {
				try {
					sendSMS(phones, context, smsBody);
				}
				catch (NotificationServiceException e) {
					logger.log(Level.WARNING, "Fail to inform participant status via sms " + Util.replaceNewLine(participant.getNric()), e);
				}
			}
			// Only inform supervisor on SUCCESSFUL and WITHDRAWAL
			if (informSupervisor) {
				logger.log(Level.INFO, "Sending email to supervisor");
				logger.log(Level.INFO, participant.getSupervisor().getEmail());
				if (participant.getSupervisor().getEmail() != null) {
					String supervisorEmail = participant.getSupervisor().getEmail();
					try {
						
						sendEmail(supervisorEmail, context, supervisorEmailSubject,
								supervisorEmailBody, senderEmail, senderPassword);
					}
					catch (NotificationServiceException e) {
						logger.log(Level.WARNING, "Fail to send email to inform Supervisor: " + Util.replaceNewLine( supervisorEmail ), e);
					}
				}
			}
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "Fail to inform participant status" + Util.replaceNewLine(participant.getNric()), e);
		}
	}
	
	public void sendCoolingEndEmail (Course course) {
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
		String senderEmail = mailSenderConfig.senderAddress();
		String senderPassword = mailSenderConfig.senderPassword();
		
		String encryptionKey = EnvironmentUtils.getEncryptionKey();
		
		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				senderPassword = encipher.decrypt(senderPassword);
			}
			catch (Exception e) {
				logger.info("Error while decrypting the configured password");
			}
		}
		IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(course.getCategoryId());
		
		List<String> toAddresses = retrieveGroupEmailList(recipientConfig.toRecipientGroups());
		List<String> ccAddresses = retrieveGroupEmailList(recipientConfig.ccRecipientGroups());
		
		for (Slot slot : course.getSlots()) {
			
			List<BinaryFile> binaryFiles = new ArrayList<>();
			BinaryFile binaryFile = null;
			
			Map<String, String> parameters = new HashMap<>();
			parameters.put("category", course.getCategoryId());
			parameters.put("courseId", course.getId());
			parameters.put("slotId", slot.getId());
			parameters.put("rs:Command", "Render");
			parameters.put("rs:Format","Excel");
			
			try {
				binaryFile = reportService.downloadReport(SLOT_REPORT_NAME, parameters);
				logger.info(String.format("File Name=%s, Content Type=%s, Content=%s", binaryFile.getName(), binaryFile.getContentType(), binaryFile.getContent()));
		
			} catch (ReportServiceException e) {
				logger.log(Level.WARNING, "Fail to download report" , e);
			}
			
			binaryFiles.add(binaryFile);
			
			Map<String, Object> context = new HashMap<>();
			context.put("title", course.getTitle());
			context.put("categoryId", course.getCategoryId());
			context.put("slotId", slot.getId());
			
			populateSlotInfo(course, slot, context);
			
			TemplateUtil templateUtil = TemplateUtil.getInstance();

			String subject = templateUtil.format(COOLING_END_EMAIL_SUBJECT, context);
			String body = templateUtil.format(COOLING_END_EMAIL_BODY, context);
			
			emailUtil.sendEmailWithAttachment(subject, body, senderEmail, senderPassword, toAddresses, binaryFiles, ccAddresses);
		}
	}
	
	public void informNoReplacement (Course course, Date withdrawalDate) {
		
		try {
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(course.getCategoryId());

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			Map<String, Object> context = new HashMap<>();
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			context.put("withdrawalDate", dateFormat.format(withdrawalDate));
			
			TemplateUtil templateUtil = TemplateUtil.getInstance();

			String subject = templateUtil.format(NO_REPLACEMENT_EMAIL_SUBJECT, context);
			String body = templateUtil.format(NO_REPLACEMENT_EMAIL_BODY, context);

			String toAddress = retrieveGroupEmails (recipientConfig.toRecipientGroups());
			String ccAddress = retrieveGroupEmails (recipientConfig.ccRecipientGroups());
			
			ElectronicMail electronicMail = new ElectronicMail();
			electronicMail.setSubject(subject);
			electronicMail.setHtmlContent(true);
			electronicMail.setText(body);
			electronicMail.setUserAddress(senderEmail);
			electronicMail.setUserPassword(senderPassword);

			electronicMail.setToAddress(toAddress);
			electronicMail.setCcAddress(ccAddress);

			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Error while inform course admin no replacement selection.", e);
		}
	} 
	
	public void informWithdrawalTask (Course course, Date cutoffDate, Date withdrawalDate) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			
			IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
			String senderEmail = mailSenderConfig.senderAddress();
			String senderPassword = mailSenderConfig.senderPassword();
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
					senderPassword = encipher.decrypt(senderPassword);
				}
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			IMailRecipientConfig recipientConfig = ServiceConfig.getInstance().getMailRecipientConfig(course.getCategoryId());
			
			Map<String, Object> context = new HashMap<>();
			context.put("categoryId", course.getCategoryId());
			context.put("category", course.getCategory());
			context.put("title", course.getTitle());
			context.put("withdrawalDate", dateFormat.format(withdrawalDate));
			context.put("endDate", dateFormat.format(cutoffDate));
			
			TemplateUtil templateUtil = TemplateUtil.getInstance();

			String subject = templateUtil.format(REPLACEMENT_SELECTION_EMAIL_SUBJECT, context);
			String body = templateUtil.format(REPLACEMENT_SELECTION_EMAIL_BODY, context);

			String toAddress = retrieveGroupEmails (recipientConfig.toRecipientGroups());
			String ccAddress = retrieveGroupEmails (recipientConfig.ccRecipientGroups());
			
			ElectronicMail electronicMail = new ElectronicMail();
			electronicMail.setSubject(subject);
			electronicMail.setHtmlContent(true);
			electronicMail.setText(body);
			electronicMail.setUserAddress(senderEmail);
			electronicMail.setUserPassword(senderPassword);

			electronicMail.setToAddress(toAddress);
			electronicMail.setCcAddress(ccAddress);

			notificationService.send(electronicMail);
		}
		catch (NotificationServiceException e) {
			logger.log(Level.SEVERE, "Error while remind course admin to confirm registration outcome.", e);
		}
	}
	
	private String formatString (String text) {
		String[] lines = text.split("\\r?\\n");
		
		StringBuilder builder = new StringBuilder (lines[0]);
		for (int i = 1; i < lines.length; i++) {
			builder.append("<BR>");
			builder.append(lines[i]);
		}
		
		return builder.toString();
	}
}
