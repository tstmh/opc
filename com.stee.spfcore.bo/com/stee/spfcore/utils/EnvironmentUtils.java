package com.stee.spfcore.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EnvironmentUtils {

	private EnvironmentUtils(){}
	private static final Logger logger = Logger.getLogger(EnvironmentUtils.class.getName());
	
	private static final String DEFAULT_CONFIG_PATH = "c:/spfcore/config";
	private static final String ENVIRONMENT_VAR_NAME = "SPFCoreEnvironment";
	private static final String MODE_VAR_NAME = "SPFCoreEnvironmentMode";
	private static final String CONFIG_PATH_VAR_NAME = "SPFCoreConfigPath";
	private static final String KEY_VAR_NAME = "SPFCoreKey";
	
	
	private static EnvironmentType environmentType = EnvironmentType.INTRANET;
	private static EnvironmentMode environmentMode = EnvironmentMode.DEVELOPMENT;
	private static String configPath = DEFAULT_CONFIG_PATH;
	private static String encryptionKey = null;
	
	static {
		Context initial_ctx = null;
		
		try {
			initial_ctx = new InitialContext();
			String type = (String) initial_ctx.lookup(ENVIRONMENT_VAR_NAME);
			
			if ("internet".equals(type)) {
				environmentType = EnvironmentType.INTERNET;
			} else if ("intranet".equals(type)) {
				environmentType = EnvironmentType.INTRANET;
			} else {
				environmentType = EnvironmentType.JUNIT_TEST;
			}
		} 
		catch (NamingException e) {
			logger.log(Level.SEVERE, ENVIRONMENT_VAR_NAME + " variable not configured in WebSphere");
		}
		
		try {
			initial_ctx = new InitialContext();
			String type = (String) initial_ctx.lookup (MODE_VAR_NAME);
			
			if ("production".equals(type)) {
				environmentMode = EnvironmentMode.PRODUCTION;
			}
			else if ("development".equals(type)) {
				environmentMode = EnvironmentMode.DEVELOPMENT;
			}
		} 
		catch (NamingException e) {
			logger.log(Level.SEVERE, MODE_VAR_NAME + " variable not configured in WebSphere");
		}
		
		try {
			if (initial_ctx != null) {
				String path = (String) initial_ctx.lookup (CONFIG_PATH_VAR_NAME);
				if (path != null && !path.isEmpty()) {
					configPath = path;
				}
			}
		}
		catch (NamingException e) {
			logger.log(Level.SEVERE, CONFIG_PATH_VAR_NAME + " variable not configured in WebSphere");
		}
		
		try {
			if (initial_ctx != null) {
				encryptionKey = (String) initial_ctx.lookup(KEY_VAR_NAME);
				if (encryptionKey == null || encryptionKey.isEmpty()) {
					logger.log(Level.SEVERE, KEY_VAR_NAME + " variable configured in WebSphere is null/empty.");
				}
			}
		}
		catch (NamingException e) {
			logger.log(Level.SEVERE, KEY_VAR_NAME + " variable not configured in WebSphere");
		}
		
	}
	
	public static boolean isInternet () {
		return environmentType == EnvironmentType.INTERNET;
	}
	
	public static boolean isIntranet () {
		return environmentType == EnvironmentType.INTRANET;
	}
	
	public static boolean isProduction () {
		return environmentMode == EnvironmentMode.PRODUCTION;
	}
	
	public static boolean isDevelopment () {
		return environmentMode == EnvironmentMode.DEVELOPMENT;
	}
	
	public static EnvironmentType getEnvironmentType () {
		return environmentType;
	}
	
	public static EnvironmentMode getEnvironmentMode () {
		return environmentMode;
	}
	
	public static String getConfigPath () {
		return configPath;
	}
	
	public static String getEncryptionKey () {
		return encryptionKey;
	}
	
}
