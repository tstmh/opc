package com.stee.spfcore.service.course.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.CourseDAO;
import com.stee.spfcore.dao.MarketingContentDAO;
import com.stee.spfcore.dao.MarketingDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseType;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.marketingContent.IMarketingContentService;
import com.stee.spfcore.service.marketingContent.MarketingContentServiceFactory;
import com.stee.spfcore.service.userAnnouncement.IUserAnnouncementService;
import com.stee.spfcore.service.userAnnouncement.UserAnnouncementServiceFactory;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;

public class CourseActivateThread implements Runnable{
	
	protected static final Logger logger = Logger.getLogger(CourseActivateThread.class.getName());
	private Course course;
	private String requester;
	private CourseDAO dao;
	private MarketingDAO marketingDAO;
	private MarketingContentDAO marketingContentDAO;
	private IMarketingContentService marketingContentService;
	private IUserAnnouncementService userAnnouncementService;
	
	public CourseActivateThread (Course course, String requester) {
		this.course = course;
		this.requester = requester;
		this.dao = new CourseDAO();
		this.marketingDAO = new MarketingDAO();
		this.marketingContentDAO = new MarketingContentDAO();
		this.marketingContentService = MarketingContentServiceFactory.getInstance();
		this.userAnnouncementService = UserAnnouncementServiceFactory.getInstance();
	}
	
	@Override
	public void run() {
		logger.log(Level.INFO, "CourseActivateThread Started..");
		
		Set<String> targetedEmails = new HashSet<>();
		Set<String> targetedMobiles = new HashSet<>();
		
		IMailSenderConfig mailSenderConfig = ServiceConfig.getInstance().getMailSenderConfig(course.getCategoryId());
		Set<PersonalDetail> personalDetails = new HashSet<>();
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(course.getContentSetId());
			
			if (course.getType() == CourseType.COMPULSORY) {
				personalDetails.addAll(dao.getNominatedCourseParticipantDetails (course.getId()));
				
				// Compulsory will send to office email and preferred contact
				retrieveTargetedUserContacts (true, personalDetails, targetedEmails, targetedMobiles);
			}
			else if (course.getType() == CourseType.NON_COMPULSORY) {
				List<String> memberGroupIds = course.getMemberGroupIds();
				for (String memberGroupId : memberGroupIds) {
					personalDetails.addAll(marketingDAO.getPersonnelInGroup(memberGroupId));
				}
				
				// Non compulsory will send to preferred contact only
				retrieveTargetedUserContacts (false, personalDetails, targetedEmails, targetedMobiles);
			}
			
			String password = mailSenderConfig.senderPassword();
			
			String encryptionKey = EnvironmentUtils.getEncryptionKey();
			
			if (encryptionKey != null && !encryptionKey.isEmpty()) {
				try {
					Encipher encipher = new Encipher(encryptionKey);
                    encipher.decrypt(password);
                }
				catch (Exception e) {
					logger.info("Error while decrypting the configured password");
				}
			}
			marketingContentService.publishContentSet(contentSet.getId(), new ArrayList<>(targetedEmails),
					new ArrayList<>(targetedMobiles), mailSenderConfig.senderAddress(),
								mailSenderConfig.senderPassword(), requester);
			
			userAnnouncementService.createUserAnnouncements(UserAnnouncementModule.COURSE, course.getId(), personalDetails, contentSet, requester);
			
			SessionFactoryUtil.commitTransaction();
			logger.log(Level.INFO, "CourseActivateThread Completed..");
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "CourseActivateThread Failed", e);
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
	private void retrieveTargetedUserContacts (boolean includeOffice, Set<PersonalDetail> personalDetails, Set<String> targetedEmails, Set<String> targetedMobiles) {
		
		for (PersonalDetail personalDetail : personalDetails) {
			
			// Retrieve office contacts
			if (includeOffice) {
				
				// If more than one office contacts, then first one will be selected.
				for (Email email : personalDetail.getEmailContacts()) {
					if (email.getLabel() == ContactLabel.WORK && email.getAddress() != null) {
						targetedEmails.add(email.getAddress().trim().toLowerCase());
						break;
					}
				}
			}
			
			// if personal preferred mode of contact is email, select the preferred email
			if (personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
						
				String preferredEmail = null;
						
				// Only interested in email other than office that is
				// configured as preferred.
				for (Email email : personalDetail.getEmailContacts()) {
					if (email.isPrefer() && email.getAddress() != null) {
						preferredEmail = email.getAddress().trim().toLowerCase();
						break;
					}
				}
				
				// As targetedEmails is a Set, duplicate will be removed
				if (preferredEmail != null) {
					targetedEmails.add(preferredEmail);
				}
			}
			else if (personalDetail.getPreferredContactMode() == ContactMode.SMS){
						
				// Select first phone number that is set to preferred.
				for (Phone phone : personalDetail.getPhoneContacts()) {
					if (phone.isPrefer()) {
						targetedMobiles.add(phone.getNumber());
						break;
					}
				}
			}
		}
	}
}
