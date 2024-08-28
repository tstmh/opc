package com.stee.spfcore.dao.dac;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.SecurityInfo;

public class DataAccessCheck {

    private DataAccessCheck(){}
    private static final Logger LOGGER = Logger.getLogger( DataAccessCheck.class.getName() );

    /**
     * Check if current user, represented by the SecurityInfo object, can access
     * the data belonging to the specified owner
     * 
     * @param info
     *            the current user security info
     * @param ownerNric
     *            the owner of the data that current user want to access.
     * @return true if allow access, false otherwise.
     */
    public static boolean canAccess( SecurityInfo info, String ownerNric ) 
    {
        if ( info.isSystemUser() || info.isWelfareOfficer() ) 
        {
            // System user and Welfare officer can access all data.
            return true;
        }
        else if ( info.isSPFUnitProcessingOfficer() || info.isNonSPFUnitProcessingOfficer() ) 
        {
            // Unit processing officer can only access personnel data from same department and sub-unit.
            return isSameDepartmentAndSubUnit( info.getUsername(), ownerNric );
        }
        else 
        {
            // Normal user can only access own data.
            return ownerNric.equals( info.getUsername() );
        }
    }

    /**
     * Check if current user, represented by the SecurityInfo object, can access
     * the specified personal data
     * 
     * @param info
     *            the current user security info
     * @param personal
     *            data that current user want to access.
     * @return true if allow access, false otherwise.
     */
    public static boolean canAccess( SecurityInfo info, PersonalDetail personal ) {

        if ( info.isSystemUser() || info.isWelfareOfficer() ) {
            // System user and Welfare officer can access all data.
            return true;
        }
        else if ( info.isSPFUnitProcessingOfficer() || info.isNonSPFUnitProcessingOfficer() ) {
            // Unit processing officer can only access personnel data from same department and sub-unit.
            DepartmentAndSubUnit deptSubunit = getDepartmentAndSubUnit( info.getUsername() );
            Employment employment = personal.getEmployment();
            if ( deptSubunit != null && !employment.getOrganisationOrDepartment().equals( deptSubunit.getDepartment() ) ) {
                return false;
            }

            return !info.isNonSPFUnitProcessingOfficer() ||
                    (employment.getSubunit() != null && !employment.getSubunit().isEmpty() &&
                            (deptSubunit == null || employment.getSubunit().equals(deptSubunit.getSubunit())));
        }
        else {
            // Normal user can only access own data.
            return personal.getNric().equals( info.getUsername() );
        }
    }

    public static boolean isSameDepartmentAndSubUnit( String caller, String owner ) 
    {
        DepartmentAndSubUnit callerDep = getDepartmentAndSubUnit( caller );
        DepartmentAndSubUnit ownerDep = getDepartmentAndSubUnit( owner );

        if ( callerDep != null ) 
        {
            boolean isSameDepartmentAndSubUnit = callerDep.equals( ownerDep );
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("isSameDepartmentAndSubUnit=%s", isSameDepartmentAndSubUnit));
            }
            return isSameDepartmentAndSubUnit;
        }
        else 
        {
            // Something wrong. Shouldn't happen.
            LOGGER.log( Level.SEVERE,"Employment data not found for caller: {}", caller );
            return false;
        }

    }

    @SuppressWarnings( "rawtypes" )
    public static DepartmentAndSubUnit getDepartmentAndSubUnit( String nric ) 
    {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        
        Query query = session.createQuery( "SELECT c.organisationOrDepartment as department, c.subunit as subunit FROM Employment as c WHERE c.nric = :nric" );

        query.setParameter( "nric", nric );

        query.setResultTransformer( Transformers.aliasToBean( DepartmentAndSubUnit.class ) );

        List result = query.list();
  
        if ( result.isEmpty() ) 
        {
            return null;
        }
        else 
        {
            return ( DepartmentAndSubUnit ) result.get( 0 );
        }
    }
}
