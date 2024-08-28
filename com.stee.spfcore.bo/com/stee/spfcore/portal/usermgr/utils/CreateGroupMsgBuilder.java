package com.stee.spfcore.portal.usermgr.utils;

import com.stee.spfcore.portal.usermgr.model.Group;

public class CreateGroupMsgBuilder {

	private static final char[] CREATE_GROUP_START = new char[0];

	private CreateGroupMsgBuilder(){}
	private static String createGroupStart = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<profile type=\"group\" xmlns=\"http://www.ibm.com/xmlns/prod/websphere/um.xsd\">";

	private static String descAttrStart = "<attribute name=\"description\" type=\"string\">" + "<attributeValue>";

	private static String attrEnd = "</attributeValue></attribute>";

	private static String cnAttrStart = "<attribute name=\"cn\" type=\"string\">" + "<attributeValue>";

	private static String createGroupEnd = "</profile>";

	public static String build(Group group) {

		StringBuilder builder = new StringBuilder();
		builder.append(CREATE_GROUP_START);

		builder.append(descAttrStart).append(group.getDescription()).append(attrEnd);

		builder.append(cnAttrStart).append(group.getId()).append(attrEnd);

		builder.append(createGroupEnd);

		return builder.toString();
	}

}
