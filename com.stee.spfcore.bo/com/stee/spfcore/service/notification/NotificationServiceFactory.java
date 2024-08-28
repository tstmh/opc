package com.stee.spfcore.service.notification;

import com.stee.spfcore.service.notification.impl.NotificationService;

public class NotificationServiceFactory {

	private NotificationServiceFactory(){}
	private static INotificationService service = null;
	
	public static synchronized INotificationService getInstance() {
		if (service == null) {
			service = new NotificationService();
		}
		
		return service;
	}
}
