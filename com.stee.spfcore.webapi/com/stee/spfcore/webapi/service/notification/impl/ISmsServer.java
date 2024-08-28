package com.stee.spfcore.webapi.service.notification.impl;

import com.stee.spfcore.webapi.notification.BatchShortMessage;
import com.stee.spfcore.webapi.notification.ShortMessage;
import com.stee.spfcore.webapi.service.notification.NotificationServiceException;

public interface ISmsServer {

	public void send (ShortMessage sms) throws NotificationServiceException;
	
	public void send (BatchShortMessage sms) throws NotificationServiceException;
}
