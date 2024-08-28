package com.stee.spfcore.security;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.websphere.security.auth.WSSubject;
import com.stee.spfcore.security.impl.BPMSecurityInfo;
import com.stee.spfcore.service.configuration.ISecurityConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.EnvironmentUtils;

/**
 * Security Info of current user.
 * Note that this class is not thread-safe and it only hold the information of 
 * the current user upon creation.
 * Application should not store it as instance variable. 
 * 
 * @author tbs
 */
public abstract class SecurityInfo {

	protected static final Logger LOGGER = Logger.getLogger(SecurityInfo.class.getName());
	
	/**
	 * Get the security info of the current calling user.
	 * @return
	 */
	public static BPMSecurityInfo createInstance () {
		return new BPMSecurityInfo();
	}
	
	/**
	 * System users
	 */
	protected List<String> systemUsers;
	
	protected List<String> welfareGroups;
	protected List<String> spfUPOGroups;
	protected List<String> nonSpfUPOGroups;
	protected List<String> spfLGGroups;
	
	protected String username;
	
	
	protected SecurityInfo () {
		username = WSSubject.getCallerPrincipal();
		
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Caller principal is: " + username);
		}
		
		ISecurityConfig securityConfig = ServiceConfig.getInstance().getSecurityConfig();
		// Preprocess the configured list to support wildcard.
		systemUsers = preprocess (securityConfig.systemUsers());
		
		// Preprocess the configured list to support wildcare.
		welfareGroups = preprocess(securityConfig.welfareGroups());

		// Preprocess the configured list to support wildcare.
		spfUPOGroups = preprocess(securityConfig.spfUPOGroups());
		
		// Preprocess the configured list to support wildcare.
		nonSpfUPOGroups = preprocess(securityConfig.nonSpfUPOGroups());
		
		// Configure Group for LGOfficers. Preprocess the configured list.
		spfLGGroups = preprocess( securityConfig.spfLGGroups() );
	}
	
	/**
	 * Preprocess the configured list to support wildcard characters.
	 * @param orgList
	 * @return
	 */
	protected List<String> preprocess (List<String> orgList) {
		List<String> result = new ArrayList<>();
		
		for (String str : orgList) {
			result.add(str.replace("?", ".?").replace("*", ".*?"));
		}
		
		return result;
	}
	
	/**
	 * Return the username of current caller user
	 * @return
	 */
	public String getUsername () {
		return username;
	}
	
	/**
	 * Check if the caller is a System user. 
	 * @return true if yes, false otherwise.
	 */
	public boolean isSystemUser () {
		
		// if username is null, will assume is system user.
		if (username == null || username.isEmpty()) {
			LOGGER.warning("Caller principal is empty. Assume system user.");
			return true;
		}
		
		for (String systemUser : systemUsers) {
			if (username.matches (systemUser)) {
				if ( LOGGER.isLoggable( Level.INFO ) ) {
					LOGGER.info(String.format("Caller is system user: %s", username));
				}
				return true;
			}
		}
		
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Caller is non system user: " + username);
		}
		
		return false;
	}
	
	/**
	 * Check if the caller is a Welfare Office. 
	 * @return true if yes, false otherwise.
	 */
	public boolean isWelfareOfficer () {
		// if username is null, will assume is not welfare officer.
		if (username == null || username.isEmpty()) {
			LOGGER.warning("Caller principal is empty. Assume not welfare officer.");
			return false;
		}

		List<String> groups = getUserGroups();

		for (String group : groups) {
			for (String welfareGroup : welfareGroups) {
				if (group.matches(welfareGroup)) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("Caller is welfare officer: %s", username));
					}
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * Check if is SPF Unit Processing Officer by checking if the group that
	 * caller belong to is one of the SPF Unit Processing group.
	 */
	public boolean isSPFUnitProcessingOfficer () {
		// if username is null, will assume is not unit processing officer.
		if (username == null || username.isEmpty()) {
			LOGGER.warning("Caller principal is empty. Assume not unit processing officer.");
			return false;
		}

		List<String> groups = getUserGroups();

		for (String group : groups) {
			for (String upoGroup : spfUPOGroups) {
				if (group.matches(upoGroup)) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("Caller is SPF unit processing officer: %s", username));
					}
					return true;
				}
			}
		}

		return false;
	}
	
	public boolean isNonSPFUnitProcessingOfficer () {
		// if username is null, will assume is not unit processing officer.
		if (username == null || username.isEmpty()) {
			LOGGER.warning("Caller principal is empty. Assume not unit processing officer.");
			return false;
		}

		List<String> groups = getUserGroups();

		for (String group : groups) {
			for (String upoGroup : nonSpfUPOGroups) {
				if (group.matches(upoGroup)) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("Caller is non SPF unit processing officer: %s", username));
					}
					return true;
				}
			}
		}

		return false;
	}
	
	public boolean isLGOfficer () {
		// if username is null, will assume is not unit processing officer.
		if (username == null || username.isEmpty()) {
			LOGGER.warning("Caller principal is empty. Assume not unit processing officer.");
			return false;
		}

		List<String> groups = getUserGroups();

		for (String group : groups) {
			for (String lgGroup : spfLGGroups) {
				if (group.matches(lgGroup)) {
					if ( LOGGER.isLoggable( Level.INFO ) ) {
						LOGGER.info(String.format("Caller is LG officer: %s", username));
					}
					return true;
				}
			}
		}

		return false;
	}
	
	protected abstract List<String> getUserGroups();
}
