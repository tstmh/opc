package com.stee.spfcore.service.userRoleManagement;

import java.util.List;

import com.stee.spfcore.model.userRoleManagement.UrmBpmGroup;
import com.stee.spfcore.model.userRoleManagement.UrmBrpGroupUser;
import com.stee.spfcore.vo.userRoleManagement.SearchBpmsGroupResults;
import com.stee.spfcore.vo.userRoleManagement.SearchBpmsUserResults;

public interface IUserRoleManagementService {
    public SearchBpmsUserResults searchBpmsUser( String userId, String userName, Integer maxResults ) throws UserRoleManagementServiceException;

    public SearchBpmsUserResults searchBpmsUser( String userId, String userName, List< String > roles, List< String > departmentCodes, Integer maxResults ) throws UserRoleManagementServiceException;

    public SearchBpmsGroupResults searchBpmsGroup( String groupName, String groupDescription, Integer maxResults ) throws UserRoleManagementServiceException;
    
    public void AddUserGroup(UrmBpmGroup urmBpmGroup, String requester) throws UserRoleManagementServiceException;
    
    public void AddUserIntoGroup(UrmBrpGroupUser urmBrpGroupUser, String requester) throws UserRoleManagementServiceException;
    
    public  List< UrmBpmGroup > GetUrmBpmGroup(int bpmGroupId) throws UserRoleManagementServiceException;
    
    public  void saveUrmBrpGroupUser(UrmBrpGroupUser urmBrpGroupUser, String requester) throws UserRoleManagementServiceException;

    public  List< UrmBrpGroupUser > getUrmBrpGroupUser(String bpmUserId, String urmBpmGroupId, String status) throws UserRoleManagementServiceException;
    
    public SearchBpmsUserResults searchBpmsUserByUnit (String userId, String userName, List< String > departmentCodes, String subunit,Integer maxResults) throws UserRoleManagementServiceException;
}

