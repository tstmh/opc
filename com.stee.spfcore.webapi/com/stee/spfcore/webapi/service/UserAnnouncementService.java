package com.stee.spfcore.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.UserAnnouncementDAO;
import com.stee.spfcore.webapi.model.userAnnouncement.UserAnnouncement;

@Service
public class UserAnnouncementService {

	private UserAnnouncementDAO userAnnouncementDAO;
	
	@Autowired
	public UserAnnouncementService (UserAnnouncementDAO userAnnouncementDAO) {
		this.userAnnouncementDAO = userAnnouncementDAO;
	}
	
	@Transactional
	public List<UserAnnouncement> getUserAnnouncements(String nric) {
		return userAnnouncementDAO.getUserAnnouncements(nric);
	}
	
	
}
