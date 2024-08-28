package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.department.DepartmentDetail;

public class DepartmentDAO {
    @SuppressWarnings( "unchecked" )
    public List< DepartmentDetail > getDepartments() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "select da from DepartmentDetail da" );
        return ( List< DepartmentDetail > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public DepartmentDetail getDepartment( String department ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "select da from DepartmentDetail da where da.department = :department" );
        query.setParameter( "department", department );
        List< DepartmentDetail > results = ( List< DepartmentDetail > ) query.list();
        DepartmentDetail result = null;
        if ( results.size() > 0 ) {
            result = results.get( 0 );
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public DepartmentDetail getDepartmentByLocation( String location ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "select da from DepartmentDetail da where da.address.buildingName = :location" );
        query.setParameter( "location", location );
        List< DepartmentDetail > results = ( List< DepartmentDetail > ) query.list();
        DepartmentDetail result = null;
        if ( results.size() > 0 ) {
            result = results.get( 0 );
        }
        return result;
    }

    public void updateDepartment( DepartmentDetail departmentDetail ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.update( departmentDetail );
        session.flush();
    }

    public void addDepartment( DepartmentDetail departmentDetail ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        session.save( departmentDetail );
        session.flush();
    }
    
    public void saveOrUpdateDepartment( DepartmentDetail departmentDetail, String requestor ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        SessionFactoryUtil.setUser( requestor );
        DepartmentDetail temp = getDepartmentByLocation(departmentDetail.getAddress().getBuildingName());
        boolean hasRecord = false;
        if(temp != null && temp.getId() != null)
        {
        	hasRecord = true;
        }
        if(hasRecord)
        {
        	session.merge(departmentDetail);
        }
        else
        {
        	session.save(departmentDetail);
        }
        session.flush();
    }
    
    public void deleteDepartment( DepartmentDetail departmentDetail, String requestor )
    {
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        SessionFactoryUtil.setUser( requestor );
        session.delete(session.get(DepartmentDetail.class, departmentDetail.getId()));
        session.flush();
    }
}
