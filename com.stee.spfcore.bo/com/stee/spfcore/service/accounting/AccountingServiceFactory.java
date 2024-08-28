package com.stee.spfcore.service.accounting;

import com.stee.spfcore.service.accounting.impl.AccountingService;

public class AccountingServiceFactory {

	private AccountingServiceFactory(){}
	private static IAccountingService instance;
	
	public static synchronized IAccountingService getInstance () {
		if (instance == null) {
			instance = new AccountingService();
		}
		return instance;
	}
}
