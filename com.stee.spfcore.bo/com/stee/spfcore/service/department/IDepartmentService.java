package com.stee.spfcore.service.department;

import java.util.List;

import com.stee.spfcore.model.department.DepartmentDetail;

public interface IDepartmentService {
    public List< DepartmentDetail > getDepartments() throws DepartmentException;

    public DepartmentDetail getDepartment( String department ) throws DepartmentException;

    public DepartmentDetail getDepartmentByLocation( String location ) throws DepartmentException;

    public void updateDepartment( DepartmentDetail departmentDetail ) throws DepartmentException;

    public void saveDepartment( DepartmentDetail departmentDetail ) throws DepartmentException;
    
    public void saveOrUpdateDepartmentList(List<DepartmentDetail> departmentDetailList,
			String currentUser) throws DepartmentException;
    
    public void deleteDepartmentList(List<DepartmentDetail> departmentDetailList, String currentUser) throws DepartmentException;
}
