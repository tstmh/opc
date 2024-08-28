package com.stee.spfcore.webapi.service.messaging.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.stee.spfcore.webapi.config.MessagingConfig;
import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.service.messaging.IMessagingService;
import com.stee.spfcore.webapi.service.messaging.MessagingServiceException;

@Service
public class MessagingService implements IMessagingService {
	
	private static final Logger logger = Logger.getLogger(MessagingService.class.getName());

	private MqChannel sender = null;
	//private IMessagingContext context = null
	private boolean initialized = false;
	
	private MessagingConfig config;
	
	public MessagingService() {
		super();
	}
	
	@Autowired
    public MessagingService(MessagingConfig config) {
    	this.config = config;
    }
	
	@Override
	public void send(AbstractMessage message) throws MessagingServiceException {

		if (config.isDisabled()) {
			if (logger.isLoggable(Level.FINER)) {
				logger.log(Level.FINER, "Messaging is disabled ...");
			}
			
			return;
		}
		
		try {
			if (sender == null) {
				logger.info("Initlize MQ Channel again.....................");
				sender = getMessagingChannel();
			}
			else
				logger.info("MQ Sender is not null....................");
			
			sender.write(message);

		    logger.info("Message sent : " + message.getClass().toString());
		}
		catch (MessagingServiceException e) {
			// Try to send the message again if there was a channel disruption,
			// and if it failed again, declare failure.
			try {
				sender = getMessagingChannel();
				sender.write(message);

				logger.info("Message sent : " + message.getClass().toString());
			}
			catch (MessagingServiceException exception) {
				sender = null;
				throw new MessagingServiceException ("Fail to send message to mq:", exception);
			}
		}
	}

	private synchronized MqChannel getMessagingChannel() throws MessagingServiceException {

		if (!initialized) {
			logger.info("Initialize");
			MqChannel.init(config.getHostname(), config.getChannel(), config.getPort(),
					config.isSslEnabled(), config.getSslCipherSuite(),
					config.getSslTrustStorePath(), config.getSslKeyStorePath(),
					config.getSslPassword());
			
			initialized = true;
		}
		
		MqChannel channel = new MqChannel(config.getMailId(), config.getRequestNumber(),
					config.getMailNumber(), config.getSubjectId(), config.getSourceSystemId());
		
		channel.openAsSender(config.getManager(), config.getQueue());

	    
			logger.info("Messaging channel created : " +
					config.getHostname() + ":" + config.getChannel() + ":" + config.getPort() + ":" + 
					config.isSslEnabled() + ":" + config.getSslCipherSuite() + ":" + 
					config.getSslTrustStorePath() + ":" + config.getSslKeyStorePath() + ":" + 
					config.getSslPassword() + ":" + config.getManager() + ":" + config.getQueue() + ":" + 
					config.getMailId() + ":" + config.getRequestNumber() + ":" + config.getMailNumber() + ":" +
					config.getSubjectId() + ":" + config.getSourceSystemId());
		
		
		return channel;
	}
}
