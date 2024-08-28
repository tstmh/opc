package com.stee.spfcore.service.hr;

import com.stee.spfcore.service.hr.impl.HRService;

public class HRServiceFactory {

	private HRServiceFactory(){}
	private static IHRService hrService;
	
	public static synchronized IHRService getHRService () {
		if (hrService == null) {
			hrService = new HRService();
		}
		
		return hrService;
	}
	
}
