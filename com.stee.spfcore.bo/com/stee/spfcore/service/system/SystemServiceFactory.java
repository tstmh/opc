package com.stee.spfcore.service.system;

import com.stee.spfcore.service.system.impl.SystemService;

public class SystemServiceFactory {

	private SystemServiceFactory(){}
	private static ISystemService service = null;
	
	public static synchronized ISystemService getInstance() {
		if (service == null) {
			service = createSystemService();
		}
		
		return service;
	}
	
	private static ISystemService createSystemService () {
		
		return new SystemService();
		
	}
}
