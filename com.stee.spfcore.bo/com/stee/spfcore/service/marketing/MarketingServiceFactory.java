package com.stee.spfcore.service.marketing;

import com.stee.spfcore.service.marketing.impl.MarketingService;

public class MarketingServiceFactory {

	private MarketingServiceFactory(){}
	private static IMarketingService marketingService;
	
	public static synchronized IMarketingService getInstance () {
		
		if (marketingService == null) {
			marketingService = createMarketingService ();
		}
		
		return marketingService;
	}
	
	private static IMarketingService createMarketingService () {

		return new MarketingService();
	}
	
}
