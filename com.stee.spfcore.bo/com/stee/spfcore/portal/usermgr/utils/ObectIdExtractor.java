package com.stee.spfcore.portal.usermgr.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class ObectIdExtractor {

	private ObectIdExtractor(){}
	private static final Logger logger = Logger.getLogger(ObectIdExtractor.class.getName());
	
	public static String extract (String content) throws IOException, JDOMException  {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		try {			
			Namespace namespace = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
			
			Document doc = builder.build(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
			Element rootElement = doc.getRootElement();
			Element entryElement = rootElement.getChild("entry", namespace);
			Element idElement = entryElement.getChild("id", namespace);
			
			String text = idElement.getText();
			
			return text.substring(text.lastIndexOf("/") + 1);
		}
		catch (JDOMException | IOException e) {
			logger.log(Level.SEVERE, "Failed to extract object id from content. Reason: {}", e.getMessage());
			return Collections.singletonList(e.getMessage()).toString();
		}
	}
}
