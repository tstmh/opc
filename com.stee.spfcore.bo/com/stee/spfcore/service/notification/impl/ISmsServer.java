package com.stee.spfcore.service.notification.impl;

import com.stee.spfcore.notification.BatchShortMessage;
import com.stee.spfcore.notification.ShortMessage;
import com.stee.spfcore.service.notification.NotificationServiceException;

public interface ISmsServer {

	public void send (ShortMessage sms) throws NotificationServiceException;
	
	public void send (BatchShortMessage sms) throws NotificationServiceException;
}
