package com.stee.spfcore.webapi.model;

import java.util.logging.Logger;

public class UserSessionUtil {
	
	private static final Logger logger = Logger.getLogger(UserSessionUtil.class.getName());
	
	private static ThreadLocal<String> currentUser = new ThreadLocal<String>();
	
	//disable constructor to guaranty a single instance
	private UserSessionUtil () {
		
	}
	
	//Username to be used for auditing, need to set username before
	//any db operation to ensure that username will be store in the audit table
	public static void setUser(String username) {
		currentUser.set(username);
	}
	
	public static String getUser() {
		return currentUser.get();
	}
}