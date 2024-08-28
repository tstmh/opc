package com.stee.spfcore.service.userRoleManagement;

import com.stee.spfcore.service.userAnnouncement.UserAnnouncementServiceFactory;
import com.stee.spfcore.service.userRoleManagement.impl.UserRoleManagementService;
import com.stee.spfcore.utils.EnvironmentUtils;

public class UserRoleManagementServiceFactory {
    private UserRoleManagementServiceFactory(){}
    private static IUserRoleManagementService instance;

    public static synchronized IUserRoleManagementService getInstance() {
        if ( instance == null ) {
            instance = createInstance();
        }
        return instance;
    }

    private static IUserRoleManagementService createInstance() {
        if ( EnvironmentUtils.isIntranet() ) {
            return new UserRoleManagementService();
        }
        return null;
    }

}
