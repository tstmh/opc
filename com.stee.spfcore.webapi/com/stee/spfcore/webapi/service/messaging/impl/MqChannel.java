package com.stee.spfcore.webapi.service.messaging.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.IMessagingChannel;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.service.messaging.MessagingServiceException;

public class MqChannel implements IMessagingChannel {
	
	private static final Logger logger = Logger.getLogger(MqChannel.class.getName());
			
	private MQQueueManager manager;
	private MQQueue queue;
	
	private Integer mailId;
	private Integer mailNumber;
	private Integer subjectId;
	private Integer sourceSystemId;
	
	public MqChannel(Integer mailId, Integer requestNumber,
			Integer mailNumber, Integer subjectId, Integer sourceSystemId) {
		this.mailId = mailId;
		this.mailNumber = mailNumber;
		this.subjectId = subjectId;
		this.sourceSystemId = sourceSystemId;
	}
	
	public static void init(String hostname, String channel, int port,
			boolean isSSLEnabled, String sslCipherSuite, String sslTrustStorePath,
			String sslKeyStorePath, String sslPassword) throws MessagingServiceException {
		
		FileInputStream trustStoreStream = null;
		FileInputStream keyStoreStream = null;
		
		try {
			MQEnvironment.hostname = hostname;
			MQEnvironment.channel = channel;
			MQEnvironment.port = port;

			if (isSSLEnabled) {
				if (logger.isLoggable(Level.FINER)) {
					logger.log(Level.FINER, "Adding SSL certificates to the environment.");
				}

				MQEnvironment.sslCipherSuite = sslCipherSuite;
				
				// Open the file and read the trust store
				KeyStore trustStore = KeyStore.getInstance("JKS");
				trustStoreStream = new FileInputStream(sslTrustStorePath);
				trustStore.load(trustStoreStream, sslPassword.toCharArray());
				
				// Open the file and read the key store
				KeyStore keyStore = KeyStore.getInstance("JKS");
				keyStoreStream = new FileInputStream(sslKeyStorePath);
				keyStore.load(keyStoreStream, sslPassword.toCharArray());
				
				// Create a default trust and key manager
				TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				trustManagerFactory.init(trustStore);
				
				KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				keyManagerFactory.init(keyStore,  sslPassword.toCharArray());
				
				SSLContext sslContext = SSLContext.getInstance("TLSv1");
				sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
				
				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				
				MQEnvironment.sslSocketFactory = sslSocketFactory;
			}
		}
		catch (IllegalArgumentException exception) {
			logger.log(Level.SEVERE, "Illegal argument exception: ", exception);
			throw new MessagingServiceException ("Illegal argument exception:", exception);
		}
		catch (Exception exception) {
			logger.log(Level.SEVERE, "Exception: ", exception);
			throw new MessagingServiceException ("Fail to init mq:", exception);
		}
		finally {
			
			try {
				if (keyStoreStream != null)
					keyStoreStream.close();
			} 
			catch (IOException exception) {
				logger.log(Level.INFO, "Fail to close FileInputStream to key store", exception);
				//throw new MessagingServiceException ("Fail to close FileInputStream to key store:", exception);
			}
			
			try {
				if (trustStoreStream != null)
					trustStoreStream.close();
			} 
			catch (IOException exception) {
				logger.log(Level.INFO, "Fail to close FileInputStream to trust store", exception);
				//throw new MessagingServiceException ("Fail to close FileInputStream to trust store:", exception);
			}
		}
	}
	
	public void openAsSender(String manager, String queue) throws MessagingServiceException {
		try {
			int openOptions = MQConstants.MQOO_OUTPUT + MQConstants.MQOO_FAIL_IF_QUIESCING;
		
			this.manager = new MQQueueManager(manager);
			this.queue = this.manager.accessQueue(queue, openOptions,
					null,  // default queue manager
					null,  // no dynamic queue name
					null); // no alternate user id
			
			
			logger.info("Queue connection has been made successfully.");
			
		}
		catch (MQException exception) {
			logger.log(Level.SEVERE, "Mq exception: ", exception);
			throw new MessagingServiceException ("Fail to open mq as sender:", exception);
		}
		catch (Exception exception) {
			logger.log(Level.SEVERE, "Exception: ", exception);
			throw new MessagingServiceException ("Fail to open mq as sender:", exception);
		}
	}
	
	public void openAsReceiver(String manager, String queue) throws MessagingServiceException {
		try {
			int openOptions = MQConstants.MQOO_INQUIRE + MQConstants.MQOO_FAIL_IF_QUIESCING + MQConstants.MQOO_INPUT_SHARED;
		
			this.manager = new MQQueueManager(manager);
			this.queue = this.manager.accessQueue(queue, openOptions,
					null,  // default queue manager
					null,  // no dynamic queue name
					null); // no alternate user id
			
			if (logger.isLoggable(Level.FINER)) {
				logger.log(Level.FINER, "Queue connection has been made successfully.");
			}
		}
		catch (MQException exception) {
			logger.log(Level.SEVERE, "Mq exception: ", exception);
			throw new MessagingServiceException ("Fail to open mq as receiver:", exception);
		}
		catch (Exception exception) {
			logger.log(Level.SEVERE, "Exception: ", exception);
			throw new MessagingServiceException ("Fail to open mq as receiver:", exception);
		}
	}
	
	@Override
	public void close() throws MessagingServiceException {
		try {
			this.queue.close();
			this.manager.disconnect();
		}
		catch (Exception exception) {
			logger.log(Level.SEVERE, "Exception: ", exception);
			throw new MessagingServiceException ("Fail to close mq:", exception);
		}
	}
	
	@Override
	public void write(AbstractMessage requestMessage) throws MessagingServiceException {
		try {
			MessageStream stream = new MessageStream();
			String payload = stream.write(requestMessage);

			if (logger.isLoggable(Level.FINER)) {
				if (payload.length() > 1000) {
					logger.log(Level.FINER, "Message payload: " + payload.substring(0,1000));
				}
				else {
					logger.log(Level.FINER, "Message payload: " + payload);
				}
			}
			
			SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String uniqueId = date.format(new Date());

			String strMailId = "<MailID>" + this.mailId + "</MailID>";
			String strRequestNumber = "<RequestNumber>" + uniqueId + "</RequestNumber>";
			// String strRequestNumber = "<RequestNumber>" + this.requestNumber + "</RequestNumber>";
			String strMailNumber = "<MailNumber>" + this.mailNumber + "</MailNumber>";
			String strSubjectId = "<SubjectID>" + this.subjectId + "</SubjectID>";
			String strDataLength = "<DataLength>" + payload.length() + "</DataLength>";
			String strSourceSystemId = "<SourceSystemID>" + this.sourceSystemId + "</SourceSystemID>";
			
			String nameValueData = "<usr>" + strMailId + strRequestNumber + strMailNumber +
									strSubjectId + strDataLength + strSourceSystemId + "</usr>";
			
			while (nameValueData.length() % 4 != 0) {
				nameValueData += " ";
			}

			MQMessage mqMessage = new MQMessage();
            mqMessage.clearMessage();
			
			mqMessage.format = MQConstants.MQFMT_RF_HEADER_2;
			mqMessage.messageType = MQConstants.MQMT_DATAGRAM;
			mqMessage.feedback = MQConstants.MQFB_NONE;
			mqMessage.expiry = MQConstants.MQEI_UNLIMITED;
			mqMessage.priority = MQConstants.MQPRI_PRIORITY_AS_Q_DEF;
			mqMessage.originalLength = MQConstants.MQOL_UNDEFINED;
			mqMessage.characterSet = 1208;  // set to UTF-8 with IBM PUA
			// mqMessage.characterSet = 819;  // set to ISO 8859-1 ASCII
			mqMessage.offset = 0;
			
			mqMessage.messageId = uniqueId.getBytes();
			mqMessage.correlationId = "0".getBytes();

			mqMessage.writeString(MQConstants.MQRFH_STRUC_ID);
			mqMessage.writeInt4(MQConstants.MQRFH_VERSION_2);
			mqMessage.writeInt4(MQConstants.MQRFH_STRUC_LENGTH_FIXED_2 + nameValueData.length() + 4);
			mqMessage.writeInt4(MQConstants.MQENC_NATIVE);
			mqMessage.writeInt4(912);
			mqMessage.writeString(MQConstants.MQFMT_NONE);
			mqMessage.writeInt4(MQConstants.MQRFH_NO_FLAGS);
			mqMessage.writeInt4(1208);

			mqMessage.writeInt4(nameValueData.length());
			mqMessage.writeString(nameValueData);
			mqMessage.writeString(payload);
			
			MQPutMessageOptions putOptions = new MQPutMessageOptions();
			// putOptions.options = MQConstants.MQPMO_SYNCPOINT | MQConstants.MQGMO_FAIL_IF_QUIESCING | MQConstants.MQPMO_DEFAULT_CONTEXT;
			putOptions.options = MQConstants.MQGMO_FAIL_IF_QUIESCING | MQConstants.MQPMO_DEFAULT_CONTEXT;
			
			this.queue.put(mqMessage, putOptions);
		}
		catch (MQException exception) {
			logger.log(Level.SEVERE, "Mq Exception: ", exception);
			throw new MessagingServiceException ("Fail to write to mq:", exception);
		}
		catch (IOException exception) {
			logger.log(Level.SEVERE, "IO exception: ", exception);
			throw new MessagingServiceException ("Fail to write to mq:", exception);
		}
		catch (Exception exception) {
			logger.log(Level.SEVERE, "Exception: ", exception);
			throw new MessagingServiceException ("Fail to write to mq:", exception);
		}		
	}
	
	/*@Override
	public List<AbstractMessage> read(IMessagingContext context) throws MessagingServiceException {		
		List<AbstractMessage> messages = new ArrayList<AbstractMessage>();
		AbstractMessage message = this.readWait(context);
		
		if (message != null) {
			messages.add(message);
		}
		
		return messages;
	}
	
	@SuppressWarnings("deprecation")
	public AbstractMessage readWait(IMessagingContext context) throws MessagingServiceException {		
		AbstractMessage requestMessage = null;
		
		MQGetMessageOptions getOptions = new MQGetMessageOptions();
		getOptions.options = MQConstants.MQGMO_WAIT;
		getOptions.waitInterval = MQConstants.MQWI_UNLIMITED;

		try {				
			MQMessage mqMessage = new MQMessage();
			this.queue.get(mqMessage, getOptions);
			
			byte[] bytes = new byte[mqMessage.getMessageLength()];
			mqMessage.readFully(bytes);
			String payload = new String(bytes, StandardCharsets.UTF_8);
			
			if (logger.isLoggable(Level.FINER)) {
				logger.log(Level.FINER, "Payload: " + payload);
			}
			
			MessageStream stream = new MessageStream();
			requestMessage = stream.read(payload, context);

			mqMessage.clearMessage();
		}
		catch (IOException exception) {
			logger.log(Level.SEVERE, "Exception while reading message: ", exception);
			throw new MessagingServiceException ("Fail to read from mq:", exception);
		}		
		catch (MQException exception) {
			if (exception.completionCode == 2 &&
				exception.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE) {
				
				logger.log(Level.SEVERE, "Mq Exception: ", exception);
				throw new MessagingServiceException ("Fail to read from mq:", exception);
			}
		}	

		return requestMessage;
	}*/
}