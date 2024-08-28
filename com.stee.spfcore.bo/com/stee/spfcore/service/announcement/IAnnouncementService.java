package com.stee.spfcore.service.announcement;

import java.util.List;
import java.util.Set;

import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.vo.announcement.AnnouncementStatistic;

public interface IAnnouncementService {

	public String addAnnouncement (Announcement announcement, String requester) throws AnnouncementServiceException;
	
	public void updateAnnouncement (Announcement announcement, String requester) throws AnnouncementServiceException;
	
	public void deleteAnnouncement (String id, String requester) throws AnnouncementServiceException;
	
	public Announcement getAnnouncement (String id) throws AnnouncementServiceException;
	
	public List<Announcement> getAnnouncements (String module) throws AnnouncementServiceException;
	
	public boolean activateAnnouncement (String id, String requester) throws AnnouncementServiceException;
	
	public void deactivateAnnouncement (String id, String requester) throws AnnouncementServiceException;
	
	public List<String> getAnnouncementSenderEmails () throws AnnouncementServiceException;
	
	public void processTask () throws AnnouncementServiceException;
	
	public AnnouncementStatistic getAnnouncementStatistic (String id) throws AnnouncementServiceException;
	
	public AnnouncementStatistic getAnnouncementContentStatistic (String contentId) throws AnnouncementServiceException;
	
	public void removeAnnouncementRecipient (String id, String nric, String requester) throws AnnouncementServiceException;
	
	public Set<PersonalDetail> getTargetedUsers (String id) throws AnnouncementServiceException;
	
	public void setModule(String id, String modName, String requester) throws AnnouncementServiceException;
	
	public boolean activateAnnouncementForUnit (String id, String senderEmail, String senderPassword, String requester ) throws AnnouncementServiceException;
	
	public boolean validateAnnouncementContent (String id) throws AnnouncementServiceException;
}
