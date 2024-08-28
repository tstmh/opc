package com.stee.spfcore.service.marketingContent;

import com.stee.spfcore.service.marketing.MarketingServiceFactory;
import com.stee.spfcore.service.marketingContent.impl.MarketingContentService;

public class MarketingContentServiceFactory {

	private MarketingContentServiceFactory(){}
	private static IMarketingContentService instance;
	
	public static synchronized IMarketingContentService getInstance () {
		if (instance == null) {
			instance = createMarketingContentService ();
		}
		return instance;
	}
	
	private static IMarketingContentService createMarketingContentService () {

		return new MarketingContentService();
		
	}
	
}
