package com.stee.spfcore.service.announcement.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.stee.spfcore.dao.MarketingContentDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.announcement.AnnouncementSender;
import com.stee.spfcore.model.announcement.AnnouncementState;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;
import com.stee.spfcore.service.announcement.AnnouncementServiceException;
import com.stee.spfcore.service.marketingContent.IMarketingContentService;
import com.stee.spfcore.service.marketingContent.MarketingContentServiceFactory;
import com.stee.spfcore.service.userAnnouncement.IUserAnnouncementService;
import com.stee.spfcore.service.userAnnouncement.UserAnnouncementServiceFactory;
import com.stee.spfcore.vo.announcement.AnnouncementStatistic;

public class AnnouncementService extends AbstractAnnouncementService {

	private IMarketingContentService marketingContentService;
	
	private MarketingContentDAO marketingContentDAO;
	private IUserAnnouncementService userAnnouncementService;
	private AnnouncementUtil announcementUtil;
	
	public AnnouncementService() {
		super();
		this.marketingContentService = MarketingContentServiceFactory.getInstance();
		this.marketingContentDAO = new MarketingContentDAO();
		this.userAnnouncementService = UserAnnouncementServiceFactory.getInstance();
		this.announcementUtil = new AnnouncementUtil();
	}

	@Override
	public String addAnnouncement(Announcement announcement, String requester) throws AnnouncementServiceException {
		
		String id = null;
		try {
			SessionFactoryUtil.beginTransaction();
			id = dao.addAnnouncement(announcement, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add announcement", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new AnnouncementServiceException ("Fail to add announcement", e);
		}
		return id;
	}

	@Override
	public void updateAnnouncement(Announcement announcement, String requester) throws AnnouncementServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateAnnouncement(announcement, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update announcement", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new AnnouncementServiceException ("Fail to update announcement", e);
		}
	}

	@Override
	public void deleteAnnouncement(String id, String requester) throws AnnouncementServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Announcement announcement = dao.getAnnouncement(id);
			
			if (announcement.getState() != AnnouncementState.DRAFT) {
				throw new AnnouncementServiceException("Only draft announcement can be deleted.");
			}
			
			dao.deleteAnnouncement(announcement, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete announcement", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new AnnouncementServiceException ("Fail to delete announcement", e);
		}
	}

	
	@Override
	public boolean activateAnnouncement(String id, String requester) throws AnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		boolean result = false;
		
		try {
			beginTransaction(isNestedTx);
			
			Announcement announcement = dao.getAnnouncement(id);
			if (announcement.getState() == AnnouncementState.ACTIVATED || announcement.getState() == AnnouncementState.DEACTIVATED) {
				throw new AnnouncementServiceException ("Only draft announcement can be activated.");
			}
			
			if (announcement.getMemberGroupIds() == null || announcement.getMemberGroupIds().isEmpty()) {
				throw new AnnouncementServiceException ("Member group not specified for announcement.");
			}
			
			// Get the content.
			MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(announcement.getContentSetId());
			if (contentSet == null) {
				throw new AnnouncementServiceException("Content is not configurated for this announcement.");
			}
					
			// Make sure no past date in the content to be publish
			boolean isDateOk = validateContentDate (contentSet);
			
			if (isDateOk) {
				announcement.setState (AnnouncementState.ACTIVATED);
				announcement.setUpdatedBy(requester);
				announcement.setUpdatedOn(new Date ());
				dao.updateAnnouncement(announcement, requester);
			
				// Get targeted user list
				Set<PersonalDetail> targetedUsers = announcementUtil.getTargetedUsers (announcement);
			
				Set<String> targetedEmails = new HashSet<> ();
				Set<String> targetedMobiles = new HashSet<> ();

				// Get the email list for the targeted users 
				announcementUtil.retrieveTargetedUserContacts (announcement, targetedUsers, targetedEmails, targetedMobiles);
			
				String senderEmail = announcement.getSenderEmail();
			
				AnnouncementSender sender = dao.getAnnouncementSender(senderEmail);
			
				String senderEmailPassword = sender.getSenderEmailPassword();
			
				// Publish the content set.
				marketingContentService.publishContentSet(announcement.getContentSetId(), new ArrayList<String>(targetedEmails), 
							new ArrayList<String>(targetedMobiles), senderEmail, senderEmailPassword, requester);
			
				userAnnouncementService.createUserAnnouncements(UserAnnouncementModule.MARKETING, announcement.getId(), targetedUsers, contentSet, requester);
				
				result = true;
			}
			
			commitTransaction(isNestedTx);
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate announcement", e);
			rollbackTransaction(isNestedTx);
			throw new AnnouncementServiceException ("Fail to activate announcement", e);
		}
		
		return result;
	}

	
	private boolean validateContentDate (MarketingContentSet contentSet) {
		
		logger.log(Level.INFO, "Validate content date");
		
		Date now = new Date ();
		boolean result = true;
		
		for (MarketingContent content : contentSet.getContents()) {
			if (now.after(content.getActualPublishDate())) {
				
				logger.log(Level.INFO, "Date is past for " + content.getId() + ": " + content.getActualPublishDate().toString());
				
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public void deactivateAnnouncement(String id, String requester) throws AnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {
			
			beginTransaction(isNestedTx);
			
			Announcement announcement = dao.getAnnouncement(id);
			if (announcement.getState() != AnnouncementState.ACTIVATED) {
				throw new AnnouncementServiceException ("Only activated announcement can be deactivated.");
			}
			
			announcement.setState (AnnouncementState.DEACTIVATED);
			announcement.setUpdatedBy(requester);
			announcement.setUpdatedOn(new Date ());
			dao.updateAnnouncement(announcement, requester);
			
			// Cancel announcement tasks 
			marketingContentService.cancelPublishContentSet(announcement.getContentSetId(), requester);
			
			userAnnouncementService.removePendingUserAnnouncements (UserAnnouncementModule.MARKETING, announcement.getId(), requester);
			
			commitTransaction(isNestedTx);
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to deactivate announcement", e);
			rollbackTransaction(isNestedTx);
			throw new AnnouncementServiceException ("Fail to deactivate announcement", e);
		}
	}

	@Override
	public List<String> getAnnouncementSenderEmails() throws AnnouncementServiceException {
		
		List<String> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getAnnouncementSenderEmails();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get announcement sender emails", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new AnnouncementServiceException ("Fail to get announcement sender emails", e);
		}
		
		return result;
	}

	@Override
	public void processTask() throws AnnouncementServiceException {
		// Do nothing
	}

	@Override
	public AnnouncementStatistic getAnnouncementStatistic(String id) throws AnnouncementServiceException {
		
		AnnouncementStatistic result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getAnnouncementStatistic(id);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get announcement statistic", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new AnnouncementServiceException ("Fail to get announcement statistic", e);
		}
		
		return result;
	}
	
	private void beginTransaction(boolean isNestedTx) {
		// Don't started new transaction if transaction already started.
		// Nested transaction not supported.
		if (!isNestedTx) {
			SessionFactoryUtil.beginTransaction();
		}
	}

	private void commitTransaction(boolean isNestedTx) {
		// Can not commit a nested transaction.
		// Let outer transaction commit transaction.
		if (!isNestedTx) {
			SessionFactoryUtil.commitTransaction();
		}
	}

	private void rollbackTransaction(boolean isNestedTx) {
		// Can not rollback nested transaction.
		// Let outer transaction rollback transaction.
		if (!isNestedTx) {
			SessionFactoryUtil.rollbackTransaction();
		}
	}

	@Override
	public void removeAnnouncementRecipient(String id, String nric, String requester) throws AnnouncementServiceException {

		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {

			beginTransaction(isNestedTx);

			Announcement announcement = dao.getAnnouncement(id);
			if (announcement.getState() != AnnouncementState.ACTIVATED) {
				throw new AnnouncementServiceException("Announcement is not activated.");
			}
			
			PersonnelDAO personnelDAO = new PersonnelDAO();
			PersonalDetail personalDetail = personnelDAO.getPersonal(nric);
			
			if (personalDetail == null) {
				throw new AnnouncementServiceException("Specified user not found.");
			}
			
			// Remove UserAnnouncement that has yet to be publish.
			userAnnouncementService.removePendingUserAnnouncements (UserAnnouncementModule.MARKETING, id, nric, requester); 
			
			
			Set<String> targetedEmails = new HashSet<> ();
			Set<String> targetedMobiles = new HashSet<> ();
		
			// Get the email list for the targeted users 
			announcementUtil.retrieveTargetedUserContacts (announcement, targetedEmails, targetedMobiles, personalDetail);
		
			Date now = new Date ();
			MarketingContentDAO daoMarketingContent = new MarketingContentDAO();
			MarketingContentSet contentSet = daoMarketingContent.getMarketingContentSet(announcement.getContentSetId());
			for (MarketingContent content : contentSet.getContents()) {
				//Only remove from those that yet to publish
				if (now.before(content.getActualPublishDate())) {
					marketingContentService.removeContentRecipients(content.getId(), new ArrayList<String>(targetedEmails), 
												new ArrayList<String>(targetedMobiles), requester);
				}
			}
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to remove announcement recipient", e);
			rollbackTransaction(isNestedTx);
			throw new AnnouncementServiceException("Fail to remove announcement recipient", e);
		}

	}

	@Override
	public AnnouncementStatistic getAnnouncementContentStatistic (String contentId) throws AnnouncementServiceException {
		AnnouncementStatistic result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getAnnouncementContentStatistic(contentId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get announcement content statistic", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new AnnouncementServiceException ("Fail to get announcement content statistic", e);
		}
		
		return result;
	}

	
	@Override
	public Set<PersonalDetail> getTargetedUsers (String id) throws AnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();
		
		Set<PersonalDetail> result = null;
		
		try {
			beginTransaction(isNestedTx);
			
			Announcement announcement = dao.getAnnouncement(id);
			
			if (announcement.getMemberGroupIds() == null || announcement.getMemberGroupIds().isEmpty()) {
				throw new AnnouncementServiceException("Member group not specified for announcement.");
			}
			
			result = announcementUtil.getTargetedUsers(announcement);
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get targeted users", e);
			rollbackTransaction(isNestedTx);
			throw new AnnouncementServiceException("Fail to get targeted users", e);
		}
		
		return result;
		
	}
	
	@Override
	public boolean activateAnnouncementForUnit(String id, String senderEmail, String senderPassword, String requester) throws AnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		boolean result = false;
		
		try {
			beginTransaction(isNestedTx);
			
			Announcement announcement = dao.getAnnouncement(id);
			if (announcement.getState() == AnnouncementState.ACTIVATED || announcement.getState() == AnnouncementState.DEACTIVATED) {
				throw new AnnouncementServiceException ("Only draft announcement can be activated.");
			}
			
			if (announcement.getMemberGroupIds() == null || announcement.getMemberGroupIds().isEmpty()) {
				throw new AnnouncementServiceException ("Member group not specified for announcement.");
			}
			
			// Get the content.
			MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(announcement.getContentSetId());
			if (contentSet == null) {
				throw new AnnouncementServiceException("Content is not configurated for this announcement.");
			}
					
			// Make sure no past date in the content to be publish
			boolean isDateOk = validateContentDate (contentSet);
			
			if (isDateOk) {
				announcement.setState (AnnouncementState.ACTIVATED);
				announcement.setUpdatedBy(requester);
				announcement.setUpdatedOn(new Date ());
				dao.updateAnnouncement(announcement, requester);
			
				// Get targeted user list
				Set<PersonalDetail> targetedUsers = announcementUtil.getTargetedUsers (announcement);
			
				Set<String> targetedEmails = new HashSet<> ();
				Set<String> targetedMobiles = new HashSet<> ();
			
				// Get the email list for the targeted users 
				announcementUtil.retrieveTargetedUserContacts (announcement, targetedUsers, targetedEmails, targetedMobiles);
			
				// Publish the content set.
				marketingContentService.publishContentSet(announcement.getContentSetId(), new ArrayList<String>(targetedEmails), 
							new ArrayList<String>(targetedMobiles), senderEmail, senderPassword, requester);
			
				userAnnouncementService.createUserAnnouncements(UserAnnouncementModule.MARKETING, announcement.getId(), targetedUsers, contentSet, requester);
				
				result = true;
			}
			
			commitTransaction(isNestedTx);
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate announcement", e);
			rollbackTransaction(isNestedTx);
			throw new AnnouncementServiceException ("Fail to activate announcement", e);
		}
		
		return result;
	}
	
	@Override
	public boolean validateAnnouncementContent(String id) throws AnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		boolean result = true;
		
		try {
			beginTransaction(isNestedTx);
			
			Announcement announcement = dao.getAnnouncement(id);
			
			if (announcement.getMemberGroupIds() == null || announcement.getMemberGroupIds().isEmpty()) {
				logger.log(Level.INFO, "Member group not specified for announcement.");
				result = false;
			}
			
			// Get the content.
			MarketingContentSet contentSet = marketingContentDAO.getMarketingContentSet(announcement.getContentSetId());
			if (contentSet == null) {
				logger.log(Level.INFO, "Content is not configurated for this announcement.");
				result = false;
			}
					
			// Make sure no past date in the content to be publish
			boolean isDateOk = validateContentDate (contentSet);
			if (!isDateOk){
				logger.log(Level.INFO, "Past dates in content for this announcement.");
				result = false;
			}
			
			commitTransaction(isNestedTx);
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to validate announcement", e);
			rollbackTransaction(isNestedTx);
			throw new AnnouncementServiceException ("Fail to validate announcement", e);
		}
		
		return result;
	}
	
	
}
