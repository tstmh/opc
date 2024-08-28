package com.stee.spfcore.webapi.service.notification.impl;

import com.stee.spfcore.webapi.notification.BatchElectronicMail;
import com.stee.spfcore.webapi.notification.ElectronicMail;
import com.stee.spfcore.webapi.service.notification.NotificationServiceException;

public interface IMailServer {

	public void send(ElectronicMail email) throws NotificationServiceException;
	
	public void send(BatchElectronicMail email) throws NotificationServiceException;
}
