package com.stee.spfcore.service.userRoleManagement.impl;

import com.stee.spfcore.dao.BpmsUserSessionFactoryUtil;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.UserRoleManagementDAO;
import com.stee.spfcore.model.userRoleManagement.UrmBpmGroup;
import com.stee.spfcore.model.userRoleManagement.UrmBrpGroupUser;
import com.stee.spfcore.service.configuration.IDAOSessionConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.userRoleManagement.IUserRoleManagementService;
import com.stee.spfcore.service.userRoleManagement.UserRoleManagementServiceException;
import com.stee.spfcore.vo.userRoleManagement.SearchBpmsGroupResults;
import com.stee.spfcore.vo.userRoleManagement.SearchBpmsUserResults;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ibm.java.diagnostics.utils.Context.logger;

public class UserRoleManagementService implements IUserRoleManagementService {
    protected static final Logger LOGGER = Logger.getLogger( UserRoleManagementService.class.getName() );

    private IDAOSessionConfig config;
    private UserRoleManagementDAO dao;

    public UserRoleManagementService() {
        config = ServiceConfig.getInstance().getDaoSessionConfig();
        dao = new UserRoleManagementDAO();
    }

    @Override
    public SearchBpmsUserResults searchBpmsUser( String userId, String userName, Integer maxResults ) throws UserRoleManagementServiceException {
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("start BPMUserRoleManagementService.searchBpmsUser(), userId=%s, userName=%s, maxResults=%s", userId, userName, maxResults));
        }
        SearchBpmsUserResults result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            BpmsUserSessionFactoryUtil.beginTransaction();

            result = dao.searchBpmsUser( config, userId, userName, maxResults );

            SessionFactoryUtil.commitTransaction();
            BpmsUserSessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.searchBpmsUser()";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            BpmsUserSessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end BPMUserRoleManagementService.searchBpmsUser(), userId=%s, userName=%s, maxResults=%s", userId, userName, maxResults));
        }
        return result;
    }

    @Override
    public SearchBpmsUserResults searchBpmsUser( String userId, String userName, List< String > roles, List< String > departmentCodes, Integer maxResults ) throws UserRoleManagementServiceException {
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("start BPMUserRoleManagementService.searchBpmsUser(), userId=%s, userName=%s, roles=%s, departmentCodes=%s, maxResults=%s", userId, userName, roles, departmentCodes, maxResults));
        }
        SearchBpmsUserResults result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            BpmsUserSessionFactoryUtil.beginTransaction();

            result = dao.searchBpmsUser( config, userId, userName, roles, departmentCodes, maxResults );

            SessionFactoryUtil.commitTransaction();
            BpmsUserSessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.searchBpmsUser()";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            BpmsUserSessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end BPMUserRoleManagementService.searchBpmsUser(), userId=%s, userName=%s, roles=%s, departmentCodes=%s, maxResults=%s", userId, userName, roles, departmentCodes, maxResults));
        }
        return result;
    }

    public SearchBpmsGroupResults searchBpmsGroup( String groupName, String groupDescription, Integer maxResults ) throws UserRoleManagementServiceException {
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("start BPMUserRoleManagementService.searchBpmsGroup(), groupName=%s, groupDescription=%s, maxResults=%s", groupName, groupDescription, maxResults));
        }
        SearchBpmsGroupResults result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            BpmsUserSessionFactoryUtil.beginTransaction();

            result = dao.searchBpmsGroup( config, groupName, groupDescription, maxResults );

            SessionFactoryUtil.commitTransaction();
            BpmsUserSessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.searchBpmsGroup()";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            BpmsUserSessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end BPMUserRoleManagementService.searchBpmsGroup(), groupName=%s, groupDescription=%s, maxResults=%s", groupName, groupDescription, maxResults));
        }
        return result;
    }
    
    public void AddUserGroup (UrmBpmGroup urmBpmGroup, String requester) throws UserRoleManagementServiceException {

        try {
            SessionFactoryUtil.beginTransaction();
            dao.addUserGroup(urmBpmGroup, requester);
            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.saveUserGroup()";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }
    }
    
    public void AddUserIntoGroup (UrmBrpGroupUser urmBrpGroupUser, String requester) throws UserRoleManagementServiceException {

        try {
            SessionFactoryUtil.beginTransaction();
            dao.addUserIntoGroup(urmBrpGroupUser, requester);
            LOGGER.info( "finish dao.addUserIntoGroup");
            SessionFactoryUtil.commitTransaction();
            LOGGER.info( "finish commit Transaction");
        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.saveUserGroup()";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }
    }
    
    public List< UrmBpmGroup > GetUrmBpmGroup (int bpmGroupId) throws UserRoleManagementServiceException {

    	List< UrmBpmGroup >  urmBpmGroup = null;
        try {
            SessionFactoryUtil.beginTransaction();
            urmBpmGroup = dao.getUrmBpmGroup(bpmGroupId);
            LOGGER.info( "finish dao.getUrmBpmGroup");
            SessionFactoryUtil.commitTransaction();
            LOGGER.info( "finish commit Transaction");

        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.getUrmBpmGroup";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }
        return urmBpmGroup;
		
    }
    
    public void saveUrmBrpGroupUser (UrmBrpGroupUser urmBrpGroupUser, String requester) throws UserRoleManagementServiceException {

        try {
            SessionFactoryUtil.beginTransaction();
            dao.saveUrmBrpGroupUser(urmBrpGroupUser, requester);
            LOGGER.info( "finish dao.saveUrmBrpGroupUser");
            SessionFactoryUtil.commitTransaction();
            LOGGER.info( "finish commit Transaction");

        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.saveUrmBrpGroupUser";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }
		
    }
    
    public List< UrmBrpGroupUser > getUrmBrpGroupUser (String bpmUserId, String urmBpmGroupId, String status) throws UserRoleManagementServiceException {

    	List< UrmBrpGroupUser > urmBrpGroupUser = null;
        try {
            SessionFactoryUtil.beginTransaction();
            urmBrpGroupUser= dao.getUrmBrpGroupUser(bpmUserId, urmBpmGroupId, status);
            LOGGER.info( "finish dao.getUrmBrpGroupUser");
            SessionFactoryUtil.commitTransaction();
            LOGGER.info( "finish commit Transaction");

        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.getUrmBrpGroupUser";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }
		return urmBrpGroupUser;
		
    }
    
    public SearchBpmsUserResults searchBpmsUserByUnit( String userId, String userName, List< String > departmentCodes, String subunit,Integer maxResults ) throws UserRoleManagementServiceException {
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("start BPMUserRoleManagementService.searchBpmsUserByUnit(), userId=%s, userName=%s, departmentCodes=%s, subunit=%s, maxResults=%s", userId, userName, departmentCodes, subunit, maxResults));
        }
        SearchBpmsUserResults result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            BpmsUserSessionFactoryUtil.beginTransaction();

            result = dao.searchBpmsUserByUnit(config, userId, userName, departmentCodes, subunit, maxResults);

            SessionFactoryUtil.commitTransaction();
            BpmsUserSessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String errMsg = "failed BPMUserRoleManagementService.searchBpmsUserByUnit()";
            LOGGER.severe( errMsg );
            LOGGER.severe(String.valueOf(e));
            SessionFactoryUtil.rollbackTransaction();
            BpmsUserSessionFactoryUtil.rollbackTransaction();
            throw new UserRoleManagementServiceException( errMsg );
        }

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end BPMUserRoleManagementService.searchBpmsUserByUnit(), userId=%s, userName=%s, departmentCodes=%s, maxResults=%s", userId, userName, departmentCodes, maxResults));
        }
        return result;
    }
}
