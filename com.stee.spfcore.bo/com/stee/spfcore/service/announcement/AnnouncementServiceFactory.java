package com.stee.spfcore.service.announcement;

import com.stee.spfcore.service.announcement.impl.AnnouncementService;

public class AnnouncementServiceFactory {

	private AnnouncementServiceFactory(){}
	private static IAnnouncementService instance;
	
	public static synchronized IAnnouncementService getInstance () {
		if (instance == null) {
			instance = createInstance ();
		}
		return instance;
	}
	
	private static IAnnouncementService createInstance () {
		
		return new AnnouncementService();
	} 
}
