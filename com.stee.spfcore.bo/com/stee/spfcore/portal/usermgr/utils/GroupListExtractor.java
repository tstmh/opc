package com.stee.spfcore.portal.usermgr.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public class GroupListExtractor {

	private GroupListExtractor(){}
	private static final Logger logger = Logger.getLogger(GroupListExtractor.class.getName());
	
	public static List<String> extract (String content) throws IOException {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		try {
			Namespace atomNamespace = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");
			Namespace umNamespace = Namespace.getNamespace("um", "http://www.ibm.com/xmlns/prod/websphere/um.xsd");
			
			Document doc = builder.build(new ByteArrayInputStream(content.getBytes()));
			Element rootElement = doc.getRootElement();
			
			List<String> groups = new ArrayList<>();
			Element contentElement = rootElement.getChild("content", atomNamespace);
			Element groupMemberListElement = contentElement.getChild("groupMembershipList", umNamespace);
			
			List<Element> elementList = groupMemberListElement.getChildren("profileRef", umNamespace);
			for (Element profileRefElement : elementList) {
				Element profileElement = profileRefElement.getChild("profile", umNamespace);
				Element attributeElement = profileElement.getChild("attribute", umNamespace);
				String attributeValue  = attributeElement.getChildText("attributeValue", umNamespace);
				
				groups.add(attributeValue);
			}
			
			return groups;
		} 
		catch (IOException e) {
			logger.severe(String.format("Exception while extracting group list from: %s %s", content, e));
			return Collections.singletonList(e.getMessage());
		} catch (JDOMException e) {
            return Collections.singletonList(e.getMessage());
        }

    }
}
