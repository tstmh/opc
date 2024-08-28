package com.stee.spfcore.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.dao.dac.DataFilter;
import com.stee.spfcore.model.blacklist.BlacklistModule;
import com.stee.spfcore.model.blacklist.Blacklistee;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.vo.blacklist.PersonalNameDepartment;

public class BlacklistDAO {

    private static final String SELECT_ONLY_ENABLED = "(v.effectiveDate is not null and v.effectiveDate <= current_date()) and (v.obsoleteDate is null or v.obsoleteDate > current_date())";

    public void addBlacklistee( Blacklistee blacklistee, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), blacklistee.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + blacklistee.getNric() );
        }

        session.save( blacklistee );

        session.flush();
    }

    public void updateBlacklistee( Blacklistee blacklistee, String requester ) throws AccessDeniedException {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), blacklistee.getNric() ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + blacklistee.getNric() );
        }

        session.merge( blacklistee );

        session.flush();
    }

    public int getBlacklistCount( boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM Blacklistee v" );

        if ( !includeDisabled ) {
            builder.append( " where " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    public int getBlacklistCount( String module, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM Blacklistee v where v.module like :module " );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "module", module );

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    public int getUserBlacklistCount( String nric, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM Blacklistee v where v.nric like :nric " );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "nric", nric );

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > getBlacklist( boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v" );

        if ( !includeDisabled ) {
            builder.append( " where " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );

        List< Blacklistee > blacklistees = ( List< Blacklistee > ) query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > getBlacklist( String module, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v where v.module like :module " );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "module", module );

        List< Blacklistee > blacklistees = query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getBlacklistNrics( String module ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select v.nric FROM Blacklistee v where v.module like :module " );

        builder.append( " and " );
        builder.append( SELECT_ONLY_ENABLED );

        builder.append( " order by v.effectiveDate" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "module", module );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > getUserBlacklist( String nric, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v where v.nric like :nric " );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "nric", nric );

        List< Blacklistee > blacklistees = query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > getBlacklist( int pageNum, int pageSize, boolean includeDisabled ) {

        int index = pageSize * ( pageNum - 1 );

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v" );

        if ( !includeDisabled ) {
            builder.append( " where " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );

        List< Blacklistee > blacklistees = query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > getBlacklist( int pageNum, int pageSize, String module, boolean includeDisabled ) {

        int index = pageSize * ( pageNum - 1 );

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v where v.module like :module " );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "module", module );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );

        List< Blacklistee > blacklistees = query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > getUserBlacklist( int pageNum, int pageSize, String nric, boolean includeDisabled ) {

        int index = pageSize * ( pageNum - 1 );

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v where v.nric like :nric " );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "nric", nric );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );

        List< Blacklistee > blacklistees = query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    public Blacklistee getBlacklistee( String nric, String module ) throws AccessDeniedException {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
            throw new AccessDeniedException( "Caller has not access to data belong to " + nric );
        }

        StringBuilder builder = new StringBuilder( "FROM Blacklistee v where v.nric like :nric and v.module like :module " );

        Query query = session.createQuery( builder.toString() );
        query.setMaxResults( 1 );

        query.setParameter( "nric", nric );
        query.setParameter( "module", module );

        List< ? > list = query.list();
        if ( !list.isEmpty() ) {
            return ( Blacklistee ) list.get( 0 );
        }
        else {
            return null;
        }
    }

    @SuppressWarnings( "unchecked" )
    public List< Blacklistee > searchBlacklist( int pageNum, int pageSize, String nric, String module, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        boolean hasNRIC = false;
        boolean hasModule = false;
        boolean isFirst = true;
        StringBuilder builder = new StringBuilder( "FROM Blacklistee v" );

        if ( nric != null && !nric.isEmpty() ) {
            builder.append( " where v.nric like :nric" );
            hasNRIC = true;
            isFirst = false;
        }

        if ( module != null && !module.isEmpty() ) {
            if ( isFirst ) {
                builder.append( " where " );
            }
            else {
                builder.append( " and " );
            }

            builder.append( "v.module like :module" );
            hasModule = true;
            isFirst = false;
        }

        if ( !includeDisabled ) {
            if ( isFirst ) {
                builder.append( " where " );
            }
            else {
                builder.append( " and " );
            }
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );

        if ( pageNum > 0 ) {
            int index = pageSize * ( pageNum - 1 );
            query.setFirstResult( index );
            query.setMaxResults( pageSize );
        }

        if ( hasNRIC ) {
            query.setParameter( "nric", nric );
        }

        if ( hasModule ) {
            query.setParameter( "module", module );
        }

        List< Blacklistee > blacklistees = query.list();

        // Filter data based on what caller can see. 
        return DataFilter.filterBlacklistees( SecurityInfo.createInstance(), blacklistees );
    }

    public int getSearchBlacklistCount( String nric, String module, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM Blacklistee v" );

        boolean hasNRIC = false;
        boolean hasModule = false;
        boolean isFirst = true;

        if ( nric != null && !nric.isEmpty() ) {
            builder.append( " where v.nric like :nric" );
            hasNRIC = true;
            isFirst = false;
        }

        if ( module != null && !module.isEmpty() ) {
            if ( isFirst ) {
                builder.append( " where " );
            }
            else {
                builder.append( " and " );
            }

            builder.append( "v.module like :module" );
            hasModule = true;
            isFirst = false;
        }

        if ( !includeDisabled ) {
            if ( isFirst ) {
                builder.append( " where " );
            }
            else {
                builder.append( " and " );
            }
            builder.append( SELECT_ONLY_ENABLED );
        }

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );

        if ( hasNRIC ) {
            query.setParameter( "nric", nric );
        }

        if ( hasModule ) {
            query.setParameter( "module", module );
        }

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    @SuppressWarnings( "unchecked" )
    public List< BlacklistModule > getBlacklistModules() {

        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "from BlacklistModule" );

        return query.list();
    }

    public boolean isBlacklisted( String nric, String module ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select count(v) from Blacklistee v " );
        builder.append( "where v.nric = :nric and v.module = :module and " );
        builder.append( SELECT_ONLY_ENABLED );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "nric", nric );
        query.setParameter( "module", module );

        long count = ( ( Number ) query.uniqueResult() ).longValue();

        return ( count > 0 );
    }

    public boolean isBlacklisted( String nric, String module, Date startDate, Date endDate ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select count(v) from Blacklistee v where 1=1 " );
        if ( nric != null ) {
            builder.append( "and v.nric = :nric " );
        }
        if ( module != null ) {
            builder.append( "and v.module = :module " );
        }
        if ( startDate != null ) {
            builder.append( "and ( v.effectiveDate is not null and v.effectiveDate <= :startDate ) " );
            builder.append( "and ( v.obsoleteDate is null or v.obsoleteDate >= :startDate ) " );
        }
        if ( endDate != null ) {
            builder.append( "and ( v.effectiveDate is not null and v.effectiveDate <= :endDate ) " );
            builder.append( "and ( v.obsoleteDate is null or v.obsoleteDate >= :endDate ) " );
        }
        Query query = session.createQuery( builder.toString() );
        if ( nric != null ) {
            query.setParameter( "nric", nric );
        }
        if ( module != null ) {
            query.setParameter( "module", module );
        }
        if ( startDate != null ) {
            query.setParameter( "startDate", startDate );
        }
        if ( endDate != null ) {
            query.setParameter( "endDate", endDate );
        }

        long count = ( ( Number ) query.uniqueResult() ).longValue();

        return ( count > 0 );
    }

    @SuppressWarnings( "unchecked" )
    public PersonalNameDepartment getPersonalNameDepartment( String nric ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select p.name as name, c.description as department " );
        builder.append( "from PersonalDetail p, Code c where p.nric = :nric " );
        builder.append( "and c.id = p.employment.organisationOrDepartment and c.type = :type" );

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "nric", nric );
        query.setParameter( "type", CodeType.UNIT_DEPARTMENT );
        query.setMaxResults( 1 );

        query.setResultTransformer( Transformers.aliasToBean( PersonalNameDepartment.class ) );

        List< PersonalNameDepartment > list = query.list();

        if ( !list.isEmpty() ) {
            return list.get( 0 );
        }

        return null;
    }
    
    @SuppressWarnings("unchecked")
	public List<Blacklistee> getBlacklisteeByUnit (String module, String unit, String nric, boolean includeDisabled) {
    	Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    	
    	StringBuilder queryStr = new StringBuilder();
    	
    	queryStr.append("Select v from Blacklistee v, PersonalDetail p ");
    	queryStr.append("where v.nric = p.nric ");
    	
    	if (module != null) {
    		queryStr.append("AND v.module = :module ");
    	}
    	if (nric != null) {
    		queryStr.append("AND v.nric = :nric ");
    	}
    	if (unit != null) {
    		queryStr.append("AND p.employment.organisationOrDepartment = :unit ");
    	}
    	if (!includeDisabled) {
    		queryStr.append("AND ");
    		queryStr.append(SELECT_ONLY_ENABLED);
    	}
    	
    	Query sqlQuery = session.createQuery(queryStr.toString());
    
    	if (module != null) {
    		sqlQuery.setParameter("module", module);
    	}
    	if (nric != null) {
    		sqlQuery.setParameter("nric", nric);
    	}
    	if (unit != null) {
    		sqlQuery.setParameter("unit", unit);
    	}
    	
    	return (List<Blacklistee>)sqlQuery.list();
    }
    
    @SuppressWarnings("unchecked")
	public PersonalNameDepartment getPersonalNameDepartment( String nric , String unit ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "select p.name as name, c.description as department " );
        builder.append( "from PersonalDetail p, Code c where p.nric = :nric " );
        builder.append( "and c.id = p.employment.organisationOrDepartment and c.type = :type " );
        if (unit != null) {
        	builder.append( "and p.employment.organisationOrDepartment = :unit");
        }
        
        Query query = session.createQuery( builder.toString() );
        query.setParameter( "nric", nric );
        query.setParameter( "type", CodeType.UNIT_DEPARTMENT );
        if (unit != null) {
        	query.setParameter( "unit", unit);
        }
        
        query.setMaxResults( 1 );

        query.setResultTransformer( Transformers.aliasToBean( PersonalNameDepartment.class ) );

        List< PersonalNameDepartment > list = query.list();

        if ( !list.isEmpty() ) {
            return list.get( 0 );
        }

        return null;
    }
}
