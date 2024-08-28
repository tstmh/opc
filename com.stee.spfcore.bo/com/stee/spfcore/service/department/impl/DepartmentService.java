package com.stee.spfcore.service.department.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.DepartmentDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.department.DepartmentDetail;
import com.stee.spfcore.service.department.DepartmentException;
import com.stee.spfcore.service.department.IDepartmentService;

public class DepartmentService implements IDepartmentService {

    private static final Logger LOGGER = Logger.getLogger( DepartmentService.class.getName() );

    private DepartmentDAO dao;

    public DepartmentService() {
        dao = new DepartmentDAO();
    }

    public List< DepartmentDetail > getDepartments() throws DepartmentException {
        List< DepartmentDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getDepartments();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getDepartments() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public DepartmentDetail getDepartment( String department ) throws DepartmentException {
        DepartmentDetail result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getDepartment( department );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getDepartment() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public DepartmentDetail getDepartmentByLocation( String location ) throws DepartmentException {
        DepartmentDetail result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getDepartmentByLocation( location );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getDepartmentByLocation() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public void updateDepartment( DepartmentDetail departmentDetail ) throws DepartmentException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addDepartment( departmentDetail );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "updateDepartment() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    public void saveDepartment( DepartmentDetail departmentDetail ) throws DepartmentException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addDepartment( departmentDetail );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "saveDepartment() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
	public void saveOrUpdateDepartmentList(
			List<DepartmentDetail> departmentDetailList, String currentUser)
			throws DepartmentException {
		 int index = 0;
	        try {
	            SessionFactoryUtil.beginTransaction();

	            if ( departmentDetailList != null ) {
	            	
	                for ( DepartmentDetail departmentDetail : departmentDetailList ) {

	                    index = departmentDetailList.indexOf( departmentDetail );

                        if ( LOGGER.isLoggable( Level.INFO ) ) {
                            LOGGER.info(String.format("Saving department [ %s ]", index));
                        }
	                    dao.saveOrUpdateDepartment(departmentDetail, currentUser);
	                }
	            }

	            SessionFactoryUtil.commitTransaction();

	        }
	        catch ( Exception e ) {
                if (departmentDetailList!=null) {
                    LOGGER.log(Level.WARNING, "Failed to Save Department Detail List: " + index + " --- " + departmentDetailList.get(index).toString(), e);
                }
	            SessionFactoryUtil.rollbackTransaction();
	        }
		
	}

	@Override
	public void deleteDepartmentList(
			List<DepartmentDetail> departmentDetailList, String currentUser)
			throws DepartmentException {
		try {
			SessionFactoryUtil.beginTransaction();
			int index = 0;
			if ( departmentDetailList != null ) {
				for ( DepartmentDetail departmentDetail : departmentDetailList ) {
					index = departmentDetailList.indexOf( departmentDetail );
					LOGGER.log( Level.INFO, "deleting department [" + index + "]: "+departmentDetail.getAddress().getBuildingName() );
					if(departmentDetail.getId() != null && departmentDetail.getId() != "")
						dao.deleteDepartment(departmentDetail, currentUser);
                    if ( LOGGER.isLoggable( Level.INFO ) ) {
                        LOGGER.info(String.format("Skip deleting for index %s because ID is invalid ", index));
                    }
				}
			}
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			LOGGER.log( Level.WARNING, "Failed to Delete Department Detail List", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
	}
}
