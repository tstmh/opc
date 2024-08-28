package com.stee.spfcore.service.personnel;

import com.stee.spfcore.service.personnel.impl.PersonnelService;

public class PersonnelServiceFactory {

	private PersonnelServiceFactory(){}
	private static IPersonnelService personnelService;
	
	
	public static synchronized IPersonnelService getPersonnelService () {
		if (personnelService == null) {
			personnelService = createPersonnelService ();
		}
		return personnelService;
	}
	
	private static IPersonnelService createPersonnelService () {
		
		return new PersonnelService();
		
	}
}
