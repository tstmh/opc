package com.stee.spfcore.service.userAnnouncement;

import com.stee.spfcore.service.userAnnouncement.impl.UserAnnouncementService;

public class UserAnnouncementServiceFactory {

	private UserAnnouncementServiceFactory(){}
	private static IUserAnnouncementService instance;
	
	public static synchronized IUserAnnouncementService getInstance () {
		if (instance == null) {
			instance = createInstance ();
		}
		return instance;
	}
	
	private static IUserAnnouncementService createInstance () {
		
		return new UserAnnouncementService();
		
		
	}
}
