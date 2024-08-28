package com.stee.spfcore.webapi.service.notification.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.stee.spfcore.webapi.notification.ShortMessage;

public class SmsRequestBuilder {

	private static final Logger logger = Logger.getLogger(SmsRequestBuilder.class.getName());

	public static String build (ShortMessage message) {
		
	// Root element
    Element rootElement = new Element ("MTREQUEST");
    Document doc = new Document (rootElement);	
		
    Element requestIDElement = new Element ("REQUESTID");
    requestIDElement.setText(String.valueOf(System.currentTimeMillis()));
    rootElement.addContent(requestIDElement);
    
    Element appCodeElement = new Element("APPCODE");
    appCodeElement.setText("SPFAPP01");
    rootElement.addContent(appCodeElement);
    
    Element cpCodeElement = new Element("CPCODE");
    cpCodeElement.setText("NCS78794");
    rootElement.addContent(cpCodeElement);
    
    Element usetPoaElement = new Element("USETPOA");
    usetPoaElement.setText("1");
    rootElement.addContent(usetPoaElement);
    
    Date currentDate = new Date ();
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Element reqDateElement = new Element("REQDATE");
    reqDateElement.setText(dateFormat.format(currentDate));
    rootElement.addContent(reqDateElement);
    
    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
    Element reqTimeElement = new Element("REQTIME");
    reqTimeElement.setText(timeFormat.format(currentDate));
    rootElement.addContent(reqTimeElement);
    
    Element dcsElement = new Element("DCS");
    dcsElement.setText("001");
    rootElement.addContent(dcsElement);
    
		Element smsElement = new Element ("SMS");
		
		Element smsRefElement = new Element ("SMSREF");
		smsRefElement.setText("1");
		smsElement.addContent(smsRefElement);
		
		Element destElement = new Element ("DEST");
		destElement.setText(message.getRecipient());
		smsElement.addContent(destElement);
		
		Element msgElement = new Element ("MSG");
		CDATA cdata = new CDATA(message.getText());
		msgElement.addContent(cdata);
		smsElement.addContent(msgElement);
		
		rootElement.addContent(smsElement);
    
    XMLOutputter xmlOutput = new XMLOutputter();
		
		xmlOutput.setFormat(Format.getPrettyFormat());
    
		StringWriter writer = new StringWriter();
		
    try {
			xmlOutput.output(doc, writer);
		} 
    catch (IOException e) {
    	logger.log(Level.SEVERE, "Fail to generate SMS Request XML.");
    	return null;
		} 
    
		return writer.toString();
	}
	
	
	public static String buildBatch (String smsContent, List<String> recipients) {
		
		// Root element
    Element rootElement = new Element ("MTBATCHREQUEST");
    Document doc = new Document (rootElement);	
    
    Element requestIDElement = new Element ("REQUESTID");
    requestIDElement.setText("MC" + String.valueOf(System.currentTimeMillis()));
    rootElement.addContent(requestIDElement);
		
    Element appCodeElement = new Element("APPCODE");
    appCodeElement.setText("SPFAPP01");
    rootElement.addContent(appCodeElement);
    
    Element cpCodeElement = new Element("CPCODE");
    cpCodeElement.setText("NCS78794");
    rootElement.addContent(cpCodeElement);
    
    Element usetPoaElement = new Element("USETPOA");
    usetPoaElement.setText("1");
    rootElement.addContent(usetPoaElement);
    
    Date currentDate = new Date ();
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Element reqDateElement = new Element("REQDATE");
    reqDateElement.setText(dateFormat.format(currentDate));
    rootElement.addContent(reqDateElement);
    
    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
    Element reqTimeElement = new Element("REQTIME");
    reqTimeElement.setText(timeFormat.format(currentDate));
    rootElement.addContent(reqTimeElement);
    
    Element dcsElement = new Element("DCS");
    dcsElement.setText("001");
    rootElement.addContent(dcsElement);
    
    Element msgElement = new Element ("MSG");
		CDATA cdata = new CDATA(smsContent);
		msgElement.addContent(cdata);
		rootElement.addContent(msgElement);
		
		for (int i = 0; i < recipients.size(); i++) {
			
			String recipient = recipients.get(i);
			String id = String.valueOf(i + 1);
			
			Element smsElement = new Element ("SMS");
			
			Element smsRefElement = new Element ("SMSREF");
			smsRefElement.setText(id);
			smsElement.addContent(smsRefElement);
			
			Element destElement = new Element ("DEST");
			destElement.setText(recipient);
			smsElement.addContent(destElement);
			
			rootElement.addContent(smsElement);
		}
		
		XMLOutputter xmlOutput = new XMLOutputter();
		
		xmlOutput.setFormat(Format.getPrettyFormat());
    
		StringWriter writer = new StringWriter();
		
    try {
			xmlOutput.output(doc, writer);
		} 
    catch (IOException e) {
    	logger.log(Level.SEVERE, "Fail to generate SMS Request XML.");
    	return null;
		} 
    
		return writer.toString();
	}
	
	public static String buildXML( ShortMessage message ) {
		
		logger.info("Building XML");
		
		String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
	    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
	    "xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
	    "<soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">";
		
		String sendSMSTag = "<SendSMS xmlns=\"https://appsms.sgmail.sgnet.gov.sg/soap\">";
		
		String authidTag = "<authid xsi:type=\"xsd:string\">SPFAPP009</authid>";
		
		String authcodeTag = "<authcode xsi:type=\"xsd:string\">Spf@a$pp09!123</authcode>";
		
		String messageTag = "<message xsi:type=\"xsd:string\">" + message.getText() + "</message>";
		
		String destinationTag = "<destination xsi:type=\"xsd:string\">" + message.getRecipient() + "</destination>";
		
		StringBuilder builder = new StringBuilder(body);
		
		builder.append(sendSMSTag);
		builder.append(authidTag);
		builder.append(authcodeTag);
		builder.append(messageTag);
		builder.append(destinationTag);
		builder.append("</SendSMS>");
		builder.append("</soap:Body>");
		builder.append("</soap:Envelope>");
		
		logger.info("XML String: " + builder.toString());
		
		return builder.toString();
	}
	
	public static String buildXMLBatch( String smsContent, List<String> recipients ) {
		
		logger.info("Building XML Batch");
		
		String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
		"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
	    "xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" " +
		"xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
		"<soap:Body soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">";
				
		String sendSMSTag = "<SendSMS xmlns=\"https://appsms.sgmail.sgnet.gov.sg/soap\">";
				
		String authidTag = "<authid xsi:type=\"xsd:string\">SPFAPP009</authid>";
				
		String authcodeTag = "<authcode xsi:type=\"xsd:string\">Spf@a$pp09!123</authcode>";
				
		String messageTag = "<message xsi:type=\"xsd:string\">" + smsContent + "</message>";
				
		String destinationTag = "<destination xsi:type=\"xsd:string\">";
		
		String recipientList = "";
		
		if (!recipients.isEmpty()) {
			for ( int i = 0; i < recipients.size(); i++ ) {
				
				String recipient = recipients.get(i);
				
				if(i == (recipients.size() - 1)) {
					//append last element with closing tag
					recipientList += recipient + "</destination>";
				}
				else {
					recipientList += recipient + "\n";
				}
			}
			destinationTag += recipientList;
		}
		else {
			destinationTag += "</destination>";
		}
				
		StringBuilder builder = new StringBuilder(body);
				
		builder.append(sendSMSTag);
		builder.append(authidTag);
		builder.append(authcodeTag);
		builder.append(messageTag);
		builder.append(destinationTag);
		builder.append("</SendSMS>");
		builder.append("</soap:Body>");
		builder.append("</soap:Envelope>");
		
		logger.info("XML String: " + builder.toString());
		
		return builder.toString();
	}
	
}
