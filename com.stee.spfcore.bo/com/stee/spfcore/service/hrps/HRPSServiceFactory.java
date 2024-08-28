package com.stee.spfcore.service.hrps;

import com.stee.spfcore.service.hrps.impl.HRPSService;

public class HRPSServiceFactory {

	private HRPSServiceFactory(){}
	private static IHRPSService hrpsService;
	
	public static synchronized IHRPSService getHRPSService() {
		if (hrpsService == null) {
			hrpsService = new HRPSService();
		}
		
		return hrpsService;
	}
}
