package com.stee.spfcore.service.userAnnouncement;

import java.util.List;
import java.util.Set;

import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncement;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncementModule;

public interface IUserAnnouncementService {

	public List<UserAnnouncement> getUserAnnouncements (String nric) throws UserAnnouncementServiceException;
	
	public void createUserAnnouncements (UserAnnouncementModule module, String referenceId, Set<PersonalDetail> targetedUser, 
											MarketingContentSet contentSet, String requester) throws UserAnnouncementServiceException;
	
	public void createUserAnnouncement (UserAnnouncementModule module, String referenceId, PersonalDetail targetedUser, 
			MarketingContent content, String requester) throws UserAnnouncementServiceException;

	public void removePendingUserAnnouncements (UserAnnouncementModule module, String referenceId, String requester) 
			throws UserAnnouncementServiceException;
	
	public void removeUserAnnouncement (UserAnnouncementModule module, String referenceId, String nric, 
											String contentId, String requester) throws UserAnnouncementServiceException;
	
	public List<UserAnnouncement> getNonPublishedUserAnnouncements (UserAnnouncementModule module, 
											String referenceId) throws UserAnnouncementServiceException;
	
	public void deleteUserAnnouncement (UserAnnouncement userAnnouncement, String requester) throws UserAnnouncementServiceException;
	
	public void removePendingUserAnnouncements (UserAnnouncementModule module, String referenceId, String nric, String requester) 
			throws UserAnnouncementServiceException;
	
	public List<String> getUserAnnouncementNrics (UserAnnouncementModule module, String referenceId, String contentId)
				throws UserAnnouncementServiceException;
}
