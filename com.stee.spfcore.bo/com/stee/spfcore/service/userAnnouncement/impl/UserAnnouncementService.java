package com.stee.spfcore.service.userAnnouncement.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.UserAnnouncementDAO;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncement;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;
import com.stee.spfcore.service.userAnnouncement.IUserAnnouncementService;
import com.stee.spfcore.service.userAnnouncement.UserAnnouncementServiceException;
import com.stee.spfcore.utils.Util;

public class UserAnnouncementService implements IUserAnnouncementService {

	protected static final Logger logger = Logger.getLogger(UserAnnouncementService.class.getName());

	private UserAnnouncementDAO dao;

	public UserAnnouncementService() {
		dao = new UserAnnouncementDAO();
	}

	@Override
	public List<UserAnnouncement> getUserAnnouncements(String nric) throws UserAnnouncementServiceException {
		throw new UnsupportedOperationException("Internet only operation.");
	}

	@Override
	public void createUserAnnouncements(UserAnnouncementModule module, String referenceId,
			Set<PersonalDetail> targetedUsers, MarketingContentSet contentSet, String requester)
			throws UserAnnouncementServiceException {

		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {
			beginTransaction(isNestedTx);

			List<UserAnnouncement> userAnnouncements = new ArrayList<>();

			// Add User Announcement tasks
			for (MarketingContent content : contentSet.getContents()) {
				
				for (PersonalDetail personalDetail : targetedUsers) {
					UserAnnouncement userAnnouncement = new UserAnnouncement(null, personalDetail.getNric(), module, referenceId,
							content.getTitle(), content.getId(), content.getActualPublishDate());
					userAnnouncements.add(userAnnouncement);

				}
			}

			dao.addUserAnnouncements(userAnnouncements, requester);
			
			logger.info("Total user announcement to send:" + userAnnouncements.size());

			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to create user announcement", e);
		}
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
	public void removePendingUserAnnouncements (UserAnnouncementModule module, String referenceId, String requester)
			throws UserAnnouncementServiceException {

		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {
			beginTransaction(isNestedTx);

			// Delete user announcement that has yet to reach the published date.
			List<UserAnnouncement> userAnnouncements = dao.getNonPublishedUserAnnouncements(module, referenceId);
			
			logger.info("Total user announcement to remove:" + userAnnouncements.size());
			
			for (UserAnnouncement userAnnouncement : userAnnouncements) {
				dao.deleteUserAnnouncement(userAnnouncement, requester);
			}
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to remove user announcement", e);
		}
	}

	@Override
	public void createUserAnnouncement(UserAnnouncementModule module, String referenceId, PersonalDetail targetedUser,
			MarketingContent content, String requester) throws UserAnnouncementServiceException {

		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {
			beginTransaction(isNestedTx);

			UserAnnouncement userAnnouncement = dao.getUserAnnouncement(module, referenceId,  targetedUser.getNric(), content.getId());
			
			if (userAnnouncement == null) {
				userAnnouncement = new UserAnnouncement(null, targetedUser.getNric(), module, referenceId,
					content.getTitle(), content.getId(), content.getActualPublishDate());

				dao.addUserAnnouncement(userAnnouncement, requester);

			}
			else {
				logger.warning("User announcement already exists:" + Util.replaceNewLine( userAnnouncement.getId() ));
			}
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to create user announcement", e);
		}

	}

	@Override
	public void removeUserAnnouncement(UserAnnouncementModule module, String referenceId, String nric, String contentId,
			String requester) throws UserAnnouncementServiceException {

		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {
			beginTransaction(isNestedTx);

			UserAnnouncement userAnnouncement = dao.getUserAnnouncement(module, referenceId, nric, contentId);
			
			if (userAnnouncement != null) {
				dao.deleteUserAnnouncement(userAnnouncement, requester);
			}
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to delete user announcement", e);
		}
	}

	@Override
	public List<UserAnnouncement> getNonPublishedUserAnnouncements(UserAnnouncementModule module, String referenceId)
												throws UserAnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();
		
		List<UserAnnouncement> userAnnouncements = null;
		
		try {
			beginTransaction(isNestedTx);

			userAnnouncements = dao.getNonPublishedUserAnnouncements(module, referenceId);
			
			logger.info("getNonPublishedUserAnnouncements. Result:" + userAnnouncements.size());
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to get Non Published user announcements", e);
		}
			
		return userAnnouncements;
	}

	@Override
	public void deleteUserAnnouncement(UserAnnouncement userAnnouncement, String requester)
			throws UserAnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();
			
		try {
			beginTransaction(isNestedTx);

			dao.deleteUserAnnouncement(userAnnouncement, requester);
			
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to delete user announcements", e);
		}
	}

	@Override
	public void removePendingUserAnnouncements(UserAnnouncementModule module, String referenceId, String nric,
			String requester) throws UserAnnouncementServiceException {

		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		try {
			beginTransaction(isNestedTx);

			// Delete user announcement that has yet to reach the published date.
			List<UserAnnouncement> userAnnouncements = dao.getNonPublishedUserAnnouncements(module, referenceId, nric);

			logger.info("Total user announcement to remove:" + userAnnouncements.size());

			for (UserAnnouncement userAnnouncement : userAnnouncements) {
				dao.deleteUserAnnouncement(userAnnouncement, requester);
			}

			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to remove user announcement", e);
		}

	}

	@Override
	public List<String> getUserAnnouncementNrics(UserAnnouncementModule module, String referenceId, 
													String contentId) throws UserAnnouncementServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();
			
		List<String> result = null;
			
		try {
			beginTransaction(isNestedTx);

			result = dao.getUserAnnouncementNrics (module, referenceId, contentId);

			if ( logger.isLoggable( Level.INFO ) ) {
				logger.info(String.format("getUserAnnouncementNrics. Result: %s", result.size()));
			}
			commitTransaction(isNestedTx);
		}
		catch (Exception e) {
			rollbackTransaction(isNestedTx);
			throw new UserAnnouncementServiceException("Fail to get user announcements nrics for specified content", e);
		}
				
		return result;
	}
	
}
