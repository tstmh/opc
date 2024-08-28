package com.stee.spfcore.webapi.service.notification.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.stee.spfcore.webapi.notification.BatchShortMessage;
import com.stee.spfcore.webapi.notification.ShortMessage;
import com.stee.spfcore.webapi.service.notification.NotificationServiceException;
import com.stee.spfcore.webapi.utils.Util;
import com.stee.spfcore.webapi.utils.rest.RESTInvoker;

public class SmsServer implements ISmsServer {

	private static final Logger logger = Logger.getLogger(SmsServer.class.getName());
			
	//private static final String REST_BASE_URL = "/sms/SendSMS";
	
	private static final String REST_BASE_URL = "/soap";
	private static final String CONTENT_TYPE = "text/xml";
	
	private RESTInvoker invoker;
	private boolean isSSL = false;
	private SSLContext sslContext;
	
	
	public SmsServer (String hostname, int port, String protocol, String trustStoreFile, String trustStorePassword) {
		
		String prefix = null;
		
		if ("ssl".equals(protocol)) {
			prefix = "https://";
			isSSL = true;
			buildSSLContext (trustStoreFile, trustStorePassword);
		}
		else {
			prefix = "http://";
			isSSL = false;
		}
		
		String url =  prefix + hostname + ":" + port + REST_BASE_URL;
		
		invoker = new RESTInvoker(url);
	}
	
	private void buildSSLContext (String trustStoreFile, String trustStorePassword) {
		
		FileInputStream inputStream = null;
		
		try {
			KeyStore trustStore = KeyStore.getInstance("JKS");
			
			inputStream = new FileInputStream (trustStoreFile);
			
			// Open our file and read the truststore
			trustStore.load(inputStream, trustStorePassword.toCharArray());
			
			// Create a default trust and key manager
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			  
			// Initialise the managers
			trustManagerFactory.init(trustStore);
			  
			sslContext = SSLContext.getInstance("TLSv1");
			  
			sslContext.init (null, trustManagerFactory.getTrustManagers(), null);
		} 
		catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | KeyManagementException e) {
			logger.log(Level.SEVERE, "Fail to initialize SSL Context", e);
		}
		finally{
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				logger.log(Level.INFO, "Fail to close the FileInputStream", e);
			}
		}
	}
	
	@Override
	public void send(ShortMessage sms) throws NotificationServiceException {
		
		//String content = SmsRequestBuilder.build(sms);
		String content = SmsRequestBuilder.buildXML(sms);
		
		try {
			String response = null;
			
			if (isSSL) {
				response = invoker.postWithSSL(CONTENT_TYPE, content, sslContext);
			}
			else {
				response = invoker.post(CONTENT_TYPE, content);
			}
			
			//String status = SmsStatusExtractor.extract(response);
			
			if ("200".equals(response)) {
				logger.log(Level.INFO, "Success in sending sms to " + Util.replaceNewLine( sms.getRecipient() ));
			}
			else {
				logger.log(Level.SEVERE, "Fail to send sms to " + Util.replaceNewLine( sms.getRecipient() ) + " with status " + response);
			}
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to send SMS to " + Util.replaceNewLine( sms.getRecipient() ), e);
		}
	}

	@Override
	public void send(BatchShortMessage sms) throws NotificationServiceException {
		
		if (sms.getRecipients().isEmpty()) {
			return;
		}
		
		List<List<String>> partitionedList = partition (sms.getRecipients());
		
		for (List<String> subList: partitionedList) {
			sendBatch (sms.getText(), subList);
		}
	}
	
	private List<List<String>> partition (List<String> list) {
		
		List<List<String>> result = new ArrayList<List<String>> ();
		
		List<String> subList = new ArrayList<String> ();
		
		for (String value : list) {
			subList.add(value);
			
			// Partition into 100 per sublist
			if (subList.size() >= 100) {
				result.add(subList);
				subList = new ArrayList<String> ();
			}
		}
		
		if (!subList.isEmpty()) {
			result.add(subList);
		}
		
		return result;
	}
	
	
	private void sendBatch (String smsContent, List<String> recipients) {
		
		//String message = SmsRequestBuilder.buildBatch (smsContent, recipients);
		String message = SmsRequestBuilder.buildXMLBatch(smsContent, recipients);
		
		try {
			String response = null;
			
			if (isSSL) {
				response = invoker.postWithSSL(CONTENT_TYPE, message, sslContext);
			}
			else {
				response = invoker.post(CONTENT_TYPE, message);
			}
			
			//SmsStatusExtractor.logStatus (response);
			if ("200".equals(response)) {
				logger.log(Level.INFO, "Success in sending sms to ");
			}
			else {
				logger.log(Level.SEVERE, "Fail to send sms to " + " with status " + response);
			}
			
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to send SMS:" + smsContent, e);
		}
	}
}
