package com.stee.spfcore.webapi.service.messaging;

import com.stee.spfcore.webapi.messaging.AbstractMessage;

public interface IMessagingService {
	
	/**
	 * Sending message
	 * @param message the message detail to be sent.
	 * @throws MessagingServiceException.
	 */
	public void send(AbstractMessage message) throws MessagingServiceException;
	
	/**
	 * Retrieving message context (for receiving message)
	 * @param 
	 * @throws MessagingServiceException.
	 */
	//public IMessagingContext getMessagingContext() throws MessagingServiceException;
}
