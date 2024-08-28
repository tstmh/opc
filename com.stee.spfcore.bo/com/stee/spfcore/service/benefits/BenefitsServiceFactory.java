package com.stee.spfcore.service.benefits;

import com.stee.spfcore.service.benefits.impl.BenefitsService;

public class BenefitsServiceFactory {

	private BenefitsServiceFactory(){}
	private static IBenefitsService benefitsService;
	
	
	public static synchronized IBenefitsService getBenefitsService () {
		if (benefitsService == null) {
			benefitsService = createBenefitsService ();
		}
		return benefitsService;
	}
	
	private static IBenefitsService createBenefitsService () {
		
		return new BenefitsService();
	}
}
