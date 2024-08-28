package com.stee.spfcore.portal.usermgr.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class ObjectIdListExtractor {

	private ObjectIdListExtractor(){}
	private static final Logger logger = Logger.getLogger(ObjectIdListExtractor.class.getName());
	
	public static List<String> extract (String content) throws JDOMException, IOException {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		try {
			Namespace namespace = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
			
			Document doc = builder.build(new ByteArrayInputStream(content.getBytes()));
			Element rootElement = doc.getRootElement();
			
			List<String> objIdList = new ArrayList<>();
			List<Element> elementList = rootElement.getChildren("entry", namespace);
			for (Element entryElement : elementList) {
				Element idElement = entryElement.getChild("id", namespace);
				String text = idElement.getText();
				objIdList.add(text.substring(text.lastIndexOf("/") + 1));
			}
			
			return objIdList;
		}
		catch (JDOMException | IOException e) {
			logger.log(Level.SEVERE, "Exception while extracting object id list from: {}", e.getMessage());
			return Collections.singletonList(e.getMessage());
		}
	}
}
