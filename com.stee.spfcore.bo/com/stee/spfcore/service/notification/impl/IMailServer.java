package com.stee.spfcore.service.notification.impl;

import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.service.notification.NotificationServiceException;

public interface IMailServer {

	public void send(ElectronicMail email) throws NotificationServiceException;
	
	public void send(BatchElectronicMail email) throws NotificationServiceException;
}
