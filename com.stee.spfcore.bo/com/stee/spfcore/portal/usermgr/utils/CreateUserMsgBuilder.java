package com.stee.spfcore.portal.usermgr.utils;


import org.apache.commons.lang.StringUtils;

import com.stee.spfcore.portal.usermgr.model.User;

public class CreateUserMsgBuilder {

	private CreateUserMsgBuilder(){}
	// Decided to code it here instead of read from external file as the string is not long.
	private static String createUserMsgStart =	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
																				 				"<um:profile type=\"user\" xmlns:um=\"http://www.ibm.com/xmlns/prod/websphere/um.xsd\">" +
																				 					"<um:attribute name=\"ibm-primaryEmail\" type=\"string\">" +
																				 						"<um:attributeValue>newuser@ibm.com</um:attributeValue>" +
																				 					"</um:attribute>";
	
	private static String uidAttr = 	"<um:attribute name=\"uid\" type=\"string\">" +
																			"<um:attributeValue>";
			
	private static String attrEnd = 		"</um:attributeValue>" +
																		"</um:attribute>";
	
	private static String snAttr = "<um:attribute name=\"sn\" type=\"string\">" +
																		"<um:attributeValue>";
	
	private static String cnAttr = "<um:attribute name=\"cn\" type=\"string\">" +
																		"<um:attributeValue>";
	
	private static String givenNameAttr = "<um:attribute name=\"givenName\" type=\"string\">" +
																				 			"<um:attributeValue>";
			
	private static String passwordAttr = "<um:attribute name=\"password\" type=\"string\">" +
																				 			"<um:attributeValue>";
	
	private static String createUserMsgEnd = "</um:profile>";
			
	
	public static String build (User user)  {
		
		StringBuilder builder = new StringBuilder();
		builder.append(createUserMsgStart);
		
		builder.append(uidAttr).append(user.getUsername()).append(attrEnd);
		
		builder.append(snAttr).append(user.getUsername()).append(attrEnd);
		
		builder.append(cnAttr).append(user.getUsername()).append(attrEnd);
		
		// Trim the name as WebSphere Portal only allow max 60 characters for name
		String name = StringUtils.abbreviate(user.getName(), 60);
		builder.append(givenNameAttr).append(name).append(attrEnd);
		
		builder.append(passwordAttr).append(user.getPassword()).append(attrEnd);
		
		builder.append(createUserMsgEnd);
		
		return builder.toString();
	}
	
}
