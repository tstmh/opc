package com.stee.spfcore.webapi.service.notification;

import com.stee.spfcore.webapi.service.notification.impl.NotificationService;

public class NotificationServiceFactory {

	private static INotificationService service = null;
	
	public synchronized static INotificationService getInstance() {
		if (service == null) {
			service = new NotificationService();
		}
		
		return service;
	}
}
