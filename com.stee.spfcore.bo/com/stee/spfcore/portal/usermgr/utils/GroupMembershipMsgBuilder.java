package com.stee.spfcore.portal.usermgr.utils;


public class GroupMembershipMsgBuilder {

	private GroupMembershipMsgBuilder(){}
	private static String groupMembershipStart = "<groupMembershipList xmlns=\"http://www.ibm.com/xmlns/prod/websphere/um.xsd\">" +
																										"<profileRef uri=\"um:groups/profiles/";
	
	private static String groupMembershipEnd = "\"/></groupMembershipList>";
	
	
	public static String build (String groupObjId) {

		StringBuilder builder = new StringBuilder();
		builder.append(groupMembershipStart);
		builder.append(groupObjId);
		builder.append(groupMembershipEnd);
		
		return builder.toString();
	}
	
}
