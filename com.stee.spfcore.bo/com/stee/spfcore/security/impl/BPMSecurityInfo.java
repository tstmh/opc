package com.stee.spfcore.security.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.service.configuration.IProcessConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.rest.RESTInvoker;

public class BPMSecurityInfo extends SecurityInfo {
	
	private static final Logger LOGGER = Logger.getLogger( BPMSecurityInfo.class.getName() );

	private static final String REST_BASE_URL = "/rest/bpm/wle/v1/user/";
	private static final String REST_PATH = "?includeInternalMemberships=false&refreshUser=false&parts=memberships";
	private static final String REST_ACCEPT_TYPE = "application/xml";

	private List<String> userGroups;

	public BPMSecurityInfo() {
		super();
	}

	protected List<String> getUserGroups() {

		if (userGroups != null) {
			return userGroups;
		}

		userGroups = new ArrayList<>();

		RESTInvoker invoker = createRESTInvoker();

		try {
			String output = invoker.get(REST_PATH, REST_ACCEPT_TYPE);
			parseUserGroups(output);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Fail to retrieve user group membership info from BPM:" + username, e);
		}

		return userGroups;
	}

	private RESTInvoker createRESTInvoker() {

		IProcessConfig processConfig = ServiceConfig.getInstance().getProcessConfig();

		String baseURL = "http://" + processConfig.hostname() + ":" + processConfig.port() + REST_BASE_URL + username;

		String restUsername = processConfig.username();
		String restPassword = processConfig.password();

		String encryptionKey = EnvironmentUtils.getEncryptionKey();

		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				restPassword = encipher.decrypt(restPassword);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error while decrypting the configured password.", e);
			}
		}

		return new RESTInvoker(baseURL, restUsername, restPassword);
	}

	private void parseUserGroups(String output) throws JDOMException, IOException{

		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		try {
			Document document = builder.build(new ByteArrayInputStream(output.getBytes()));
			Element rootElement = document.getRootElement();
			Element dataElement = rootElement.getChild("data");

			List<Element> membershipElements = dataElement.getChildren("memberships");
			for (Element element : membershipElements) {
				userGroups.add(element.getTextTrim());
			}
		} catch (JDOMException | IOException e) {
			LOGGER.severe(String.format( "Fail to parse output content from BPM REST: %s %s",output, e));
		}

		if (LOGGER.isLoggable(Level.FINEST)) {
			for (String group : userGroups) {
				LOGGER.finest("User (" + username + ") is member of group: " + group);
			}
		}

	}
}
