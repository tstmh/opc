package com.stee.spfcore.service.sag;

import com.stee.spfcore.service.sag.impl.SAGService;

public class SAGServiceFactory {

	private SAGServiceFactory(){}
	private static ISAGService sagService;

	public static synchronized ISAGService getSagService() {
		if(sagService == null){
			sagService = createSAGService();
		}
		return sagService;
	}
	
	private static ISAGService createSAGService() {

		return new SAGService();
		
	}
}
