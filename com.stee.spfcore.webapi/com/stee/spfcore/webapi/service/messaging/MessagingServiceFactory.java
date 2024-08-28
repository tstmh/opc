package com.stee.spfcore.webapi.service.messaging;

import com.stee.spfcore.webapi.service.messaging.impl.MessagingService;

public class MessagingServiceFactory {

	private static IMessagingService service = null;
	
	public synchronized static IMessagingService getInstance() {
		if (service == null) {
			service = new MessagingService();
		}
		
		return service;
	}
}
