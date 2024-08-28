package com.stee.spfcore.webapi.messaging;

import java.util.List;

import com.stee.spfcore.webapi.service.messaging.MessagingServiceException;

public interface IMessagingChannel {

	public void write(AbstractMessage message) throws MessagingServiceException;
	//public List<AbstractMessage> read(IMessagingContext context) throws MessagingServiceException;
	public void close() throws MessagingServiceException;
}
