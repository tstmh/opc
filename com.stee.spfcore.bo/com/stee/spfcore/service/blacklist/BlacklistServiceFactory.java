package com.stee.spfcore.service.blacklist;

import com.stee.spfcore.service.blacklist.impl.BlacklistService;

public class BlacklistServiceFactory {

	private BlacklistServiceFactory(){}
	private static IBlacklistService instance;
	
	public static IBlacklistService getInstance () {
		
		if (instance == null) {
			instance = createBlacklistService ();
		}
		
		return instance;
	}
	
	private static IBlacklistService createBlacklistService () {
		
		return new BlacklistService();
	}
	
	
}
