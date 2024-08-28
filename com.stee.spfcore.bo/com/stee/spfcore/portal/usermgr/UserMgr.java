package com.stee.spfcore.portal.usermgr;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.portal.usermgr.model.Group;
import com.stee.spfcore.portal.usermgr.model.User;
import com.stee.spfcore.portal.usermgr.utils.CreateGroupMsgBuilder;
import com.stee.spfcore.portal.usermgr.utils.CreateUserMsgBuilder;
import com.stee.spfcore.portal.usermgr.utils.GroupListExtractor;
import com.stee.spfcore.portal.usermgr.utils.GroupMembershipMsgBuilder;
import com.stee.spfcore.portal.usermgr.utils.ObectIdExtractor;
import com.stee.spfcore.portal.usermgr.utils.ObjectIdListExtractor;
import com.stee.spfcore.service.configuration.IPortalUserConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.rest.RESTInvoker;

public class UserMgr {

	private static final Logger logger = Logger.getLogger(UserMgr.class.getName());
	
	private static final String REST_USER_PROFILE_PATH = "/users/profiles";
	private static final String REST_GROUP_PROFILE_PATH = "/groups/profiles";
	private static final String REST_GROUP_MEMBERSHIP_PATH = "/groupmembership";
	private static final String REST_CONTENT_TYPE = "application/atom+xml";
	private static final String REST_BASE_URL = "/wps/mycontenthandler/!ut/p/um/secure";
	private static final String REST_MEMBER_OF = "/users/profiles?memberOf=";
		
	private static UserMgr instance = null;
	
	public static synchronized UserMgr getInstance () {
		if (instance == null) {
			instance = new UserMgr();
		}
		
		return instance;
	}
	
	private RESTInvoker invoker;
	
	private UserMgr () {
		
		IPortalUserConfig config = ServiceConfig.getInstance().getPortalUserConfig();
		
		String baseURL = "http://" + config.pumaRestHostName() + ":" + config.pumaRestPort() + REST_BASE_URL;
		String restUsername = config.pumaRestUsername();
		String restPassword = config.pumaRestPassword();
		
		String encryptionKey = EnvironmentUtils.getEncryptionKey();
		
		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				restPassword = encipher.decrypt(restPassword);
			} 
			catch (GeneralSecurityException e) {
				logger.log(Level.SEVERE, "Error while decrypting the configured password.", e);
			}
		}
		
		invoker = new RESTInvoker(baseURL, restUsername, restPassword);
	}
	
	
	public synchronized void createUser (User user) throws UserMgrException {
		
		String content = CreateUserMsgBuilder.build(user);
		
		try {
			String output = invoker.post(REST_USER_PROFILE_PATH, REST_CONTENT_TYPE, REST_CONTENT_TYPE, content);
			if (logger.isLoggable(Level.FINEST)) {
				logger.fine (String.format( "User created. Response: %s", output));
			}
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to create user: %s", user.getUsername() + e);
		}
	}
	
	
	public synchronized void createGroup (Group group) throws UserMgrException {
		
		String content = CreateGroupMsgBuilder.build(group);
		
		try {
			String output = invoker.post(REST_GROUP_PROFILE_PATH, REST_CONTENT_TYPE, REST_CONTENT_TYPE, content);
			
			if (logger.isLoggable(Level.FINEST)) {
				logger.fine (String.format("Group created. Response: %s", output));
			}
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to create group: %s", group.getId()+ e);
		}
	}
	
	public synchronized void addUserToGroup (String username, String groupId) throws UserMgrException {
		
		String groupObjId = getGroupObjectId (groupId);
		String userObjId = getUserObjectId (username);
		
		String content = GroupMembershipMsgBuilder.build(groupObjId);
		
		try {
			String path = REST_GROUP_MEMBERSHIP_PATH + "/" + userObjId + "?update=merge";
			
			String output = invoker.post(path, REST_CONTENT_TYPE, REST_CONTENT_TYPE, content);
			
			if (logger.isLoggable(Level.FINEST)) {
				logger.fine (String.format("Group created. Response: %s", output));
			}
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to add user %s to group %s %s",username, groupId, e));
		}
	}
	
	
	public synchronized List<String> getGroupUsers (String groupId) throws UserMgrException {
		
		String groupObjId = getGroupObjectId (groupId);
		
		try {
			String path = REST_MEMBER_OF + groupObjId;
			
			String output = invoker.get(path, REST_CONTENT_TYPE);
			
			return ObjectIdListExtractor.extract(output);
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to retrieve all user of group %s %s", groupId, e));
			return Collections.singletonList(e.getMessage());
		}
	}
	
	
	public synchronized void deleteUserByObjectId (String userObjectId) throws UserMgrException {
		
		String path = REST_USER_PROFILE_PATH + "/" + userObjectId;
		
		try {
			invoker.delete (path);
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to delete user %s %s", userObjectId, e));
        }
	}
	
	public synchronized boolean isUserExists (String username) {
		
		String objId = null;
		try {
			objId = getUserObjectId (username);
		} 
		catch (Exception e) {
			logger.info(String.format("Fail to retrieve user %s", username));
		}

		return objId != null && !objId.isEmpty();
	}
	
	public synchronized void deleteUser (String username) throws UserMgrException {
		String objId = getUserObjectId(username);
		deleteUserByObjectId (objId);
	}
	
	private String getGroupObjectId (String groupId) throws UserMgrException {
		
		String path = REST_GROUP_PROFILE_PATH + "?identifier=cn=" + groupId + ",o=defaultWIMFileBasedRealm";
		
		try {
			String output = invoker.get (path, REST_CONTENT_TYPE);
			
			return ObectIdExtractor.extract(output);
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to retrieve group object id: %s %s", groupId, e.getMessage()));
			return Collections.singletonList(e.getMessage()).toString();
		}
	}
	
	public synchronized List<String> getUserGroups (String username) throws UserMgrException {
		
		String userObjId = getUserObjectId (username);
		
		String path = REST_GROUP_MEMBERSHIP_PATH + "/" + userObjId + "?expandRefs=true";
		
		try {
			
			String output = invoker.get(path, REST_CONTENT_TYPE);
			
			return GroupListExtractor.extract(output);
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to retrieve all groups for user %s %s", username ,e));
			return Collections.singletonList(e.getMessage());
		}
	}
	
	private String getUserObjectId (String username) throws UserMgrException {
		
		String path = REST_USER_PROFILE_PATH + "?identifier=uid=" + username + ",o=defaultWIMFileBasedRealm";
				
		try {
			String output = invoker.get (path, REST_CONTENT_TYPE);
			
			return ObectIdExtractor.extract(output);
		} 
		catch (Exception e) {
			logger.severe(String.format( "Fail to retrieve user object id: %s %s", username, e));
			return e.getMessage();
		}
	}
	
}
