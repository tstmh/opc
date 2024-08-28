package com.stee.spfcore.service.department;

import com.stee.spfcore.service.department.impl.DepartmentService;
import com.stee.spfcore.utils.EnvironmentUtils;

public class DepartmentServiceFactory {

    private DepartmentServiceFactory(){}
    private static IDepartmentService departmentService;

    public static synchronized IDepartmentService getDepartmentService() {

        if ( departmentService == null ) {
            departmentService = createDepartmentService();
        }

        return departmentService;
    }

    private static IDepartmentService createDepartmentService() {

        if ( EnvironmentUtils.isIntranet() ) {
            return new DepartmentService();
        }

        return null;
    }
}
