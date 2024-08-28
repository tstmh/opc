package com.stee.spfcore.service.genericEvent;

import com.stee.spfcore.service.genericEvent.impl.GenericEventService;

public class GenericEventServiceFactory {

	private GenericEventServiceFactory(){}
	private static IGenericEventService instance;
	
	public static synchronized IGenericEventService getInstance () {
		
		if (instance == null) {
			instance = createInstance ();
		}
		return instance;
	}
	
	private static IGenericEventService createInstance () {
		
		return new GenericEventService();
	}
	
}
