package com.stee.spfcore.service.notification;

import java.util.List;

import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.notification.BatchShortMessage;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.notification.ShortMessage;

public interface INotificationService {

	/**
	 * Sending email
	 * @param email the email detail to be sent.
//	 * @throws NotificationServiceException.
	 */
	public void send(ElectronicMail email) throws NotificationServiceException;

	/**
	 * Sending mass email
	 * @param email the email detail to be sent.
//	 * @throws NotificationServiceException.
	 */
	public void send(BatchElectronicMail email) throws NotificationServiceException;

	/**
	 * Sending sms
	 * @param sms the sms detail to be sent.
//	 * @throws NotificationServiceException.
	 */
	public void send(ShortMessage sms) throws NotificationServiceException;

	/**
	 * Send sms to multiple recipients in batch
	 * @param sms
//	 * @throws NotificationServiceException
	 */
	public void send(BatchShortMessage sms) throws NotificationServiceException;

	/**
	 * Send sms to recipient using standalone sms app
	 * @param recipient, text
//	 * @throws NotificationServiceException
	 */
	public void sendExtSMS(String recipient, String text) throws NotificationServiceException;

	/**
	 * Send batch sms to recipient using standalone sms app
//	 * @param recipients, text
	 * @throws NotificationServiceException
	 */
	public void sendExtSMS(List<String> recipientList, String text)throws NotificationServiceException;
}
