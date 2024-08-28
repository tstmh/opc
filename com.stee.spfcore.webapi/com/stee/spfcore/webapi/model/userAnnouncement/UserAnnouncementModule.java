package com.stee.spfcore.webapi.model.userAnnouncement;

public enum UserAnnouncementModule {

	MARKETING ("Marketing"),
	COURSE ("WelfareCourse"),
	FAMILY_DAY ("FamilyDay"),
	HIV_HEALTH ("HivHealth");
	
	private String value;
	
	private UserAnnouncementModule (String value) {
		this.value = value;
	}
	
	public String toString () {
		return value;
	}
	
	public static UserAnnouncementModule getAnnouncementModule (String value) {
		
		UserAnnouncementModule [] modules = UserAnnouncementModule.values();
		
		for (UserAnnouncementModule module : modules) {
			if (module.value.equals(value)) {
				return module;
			}
		}
		
		return null;
	}
}
