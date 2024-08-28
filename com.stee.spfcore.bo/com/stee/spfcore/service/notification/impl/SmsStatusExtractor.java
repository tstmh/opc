package com.stee.spfcore.service.notification.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class SmsStatusExtractor {

	private SmsStatusExtractor(){}
	private static final Logger logger = Logger.getLogger(SmsStatusExtractor.class.getName());
			
	public static String extract (String response) throws JDOMException, IOException {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		
		Document doc = builder.build(new ByteArrayInputStream(response.getBytes()));
		Element rootElement = doc.getRootElement();
		Element statusElement = rootElement.getChild("SMS").getChild("STATUSCODE");
			
		return statusElement.getTextTrim();
	}
	
	
	public static void logStatus (String response) {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		
		Document doc;
		try {
			doc = builder.build(new ByteArrayInputStream(response.getBytes()));
		}
		catch (JDOMException | IOException e) {
			logger.log(Level.SEVERE, "Fail to process SMS status");
			return;
		}
		
		Element rootElement = doc.getRootElement();
		List<Element> children = rootElement.getChildren("SMS");
		
		for (Element element : children) {
			String number = element.getChild("DEST").getTextTrim();
			String status = element.getChild("STATUSCODE").getTextTrim ();
			
			if ("10000".equals(status) && logger.isLoggable( Level.INFO )) {
				logger.info(String.format("Success in sending sms to %s", number));
			}
			else if ( logger.isLoggable( Level.INFO ) ) {
				logger.warning(String.format("Fail to send sms to %s : %s", number,status));
			}
			
		}
		
	}
}
