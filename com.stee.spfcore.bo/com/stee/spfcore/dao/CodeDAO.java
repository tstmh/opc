package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeMapping;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.code.ExternalSystemType;
import com.stee.spfcore.vo.code.CodeNamedValuePair;
import com.stee.spfcore.vo.code.CodeTypeIdPair;
import com.stee.spfcore.vo.code.SearchResult;

public class CodeDAO {

    /**
     * Get all enabled code of the specified type.
     * 
     * @param type
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List< Code > getCodes( CodeType type ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        Criteria criteria = session.createCriteria( Code.class );

        criteria.add( Restrictions.eq( "type", type ) );
        criteria.add( Restrictions.eq( "enabled", true ) );
        criteria.addOrder( Order.asc( "order" ) );

        return ( List< Code > ) criteria.list();
    }

    /**
     * Return all code of the specified type. If includeDisabled is true, it
     * will return all code including those that is disabled.
     * 
     * @param type
     * @param includeDisabled
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List< Code > getCodes( CodeType type, boolean includeDisabled ) {

        if ( includeDisabled ) {
            Session session = SessionFactoryUtil.getCurrentSession();

            Query query = session.createQuery( "SELECT c FROM Code as c WHERE c.type = :codeType order by c.order asc" );
            query.setParameter( "codeType", type );

            return ( List< Code > ) query.list();
        }
        else {
            return getCodes( type );
        }
    }

    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type ) {
        return getCodeNamedValuePairs( type, true );
    }

    @SuppressWarnings( "unchecked" )
    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type, boolean includeDisabled ) {
        Session session = SessionFactoryUtil.getCurrentSession();
        Query query;
        if ( includeDisabled ) {
            query = session.createQuery( "SELECT c.id as name, c.description as value FROM Code as c WHERE c.type = :codeType and c.enabled = :enabled order by c.order asc" );
            query.setParameter( "codeType", type );
            query.setParameter( "enabled", includeDisabled);
        }
        else {
            query = session.createQuery( "SELECT c.id as name, c.description as value FROM Code as c WHERE c.type = :codeType order by c.order asc" );
            query.setParameter( "codeType", type );
        }
        query.setResultTransformer( Transformers.aliasToBean( CodeNamedValuePair.class ) );

        return ( List< CodeNamedValuePair > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CodeNamedValuePair > getCodeNamedValuePairs( CodeType type, boolean includeDisabled, boolean sortByValue ) {
        Session session = SessionFactoryUtil.getCurrentSession();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append( "SELECT c.id as name, c.description as value FROM Code as c WHERE c.type = :codeType " );
        if ( includeDisabled ) {
            queryBuilder.append( "and c.enabled = :enabled " );
        }
        if ( sortByValue ) {
            queryBuilder.append( "order by c.description asc " );
        }
        else {
            queryBuilder.append( "order by c.order asc " );
        }
        Query query = session.createQuery( queryBuilder.toString() );
        query.setParameter( "codeType", type );
        if ( includeDisabled ) {
            query.setParameter( "enabled", includeDisabled);
        }
        query.setResultTransformer( Transformers.aliasToBean( CodeNamedValuePair.class ) );

        return ( List< CodeNamedValuePair > ) query.list();
    }

    public Code getCode( CodeType type, String id ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "SELECT c FROM Code as c WHERE c.type = :codeType and c.id = :codeId" );
        query.setParameter( "codeType", type );
        query.setParameter( "codeId", id );
        query.setMaxResults( 1 );

        List< ? > list = query.list();
        if ( !list.isEmpty() ) {
            return ( Code ) list.get( 0 );
        }
        else {
            return null;
        }
    }

    public void addCode( Code code, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( code );

        session.flush();
    }

    public void updateCode( Code code, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( code );

        session.flush();
    }

    public void deleteCode( Code code, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.delete( code );

        session.flush();
    }

    public void updateCodeStatus( boolean toEnable, Date comparisonDate, String requester ) {
        if ( comparisonDate != null ) {

            SessionFactoryUtil.setUser( requester );

            Session session = SessionFactoryUtil.getCurrentSession();
            StringBuilder queryStr = new StringBuilder();
            queryStr.append( "UPDATE Code c SET c.enabled = :enabled " );
            if ( toEnable ) {
                queryStr.append( "WHERE c.effectiveDate = :effectiveDate" );
            }
            else {
                queryStr.append( "WHERE c.obsoleteDate = :obsoleteDate" );
            }

            Query query = session.createQuery( queryStr.toString() );

            query.setBoolean( "enabled", toEnable );
            if ( toEnable ) {
                query.setParameter( "effectiveDate", comparisonDate );
            }
            else {
                query.setParameter( "obsoleteDate", comparisonDate );
            }

            query.executeUpdate();
            session.flush();
        }
    }

    public List< CodeMapping > getCodeMappings( ExternalSystemType sysType, CodeType type ) {
        return getCodeMappings( sysType, type, false );
    }

    @SuppressWarnings( "unchecked" )
    public List< CodeMapping > getCodeMappings( ExternalSystemType sysType, CodeType type, boolean includeDisabled ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Criteria criteria = session.createCriteria( CodeMapping.class );

        criteria.add( Restrictions.eq( "systemType", sysType ) );
        criteria.add( Restrictions.eq( "codeType", type ) );
        if ( !includeDisabled ) {
            criteria.add( Restrictions.eq( "enabled", true ) );
        }

        return ( List< CodeMapping > ) criteria.list();
    }

    public CodeMapping getCodeMapping( ExternalSystemType sysType, CodeType type, String extId ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "SELECT c FROM CodeMapping as c WHERE c.systemType = :sysType and c.codeType = :codeType and c.externalId = :extId" );
        query.setParameter( "sysType", sysType );
        query.setParameter( "codeType", type );
        query.setParameter( "extId", extId );
        query.setMaxResults( 1 );

        List< ? > list = query.list();
        if ( !list.isEmpty() ) {
            return ( CodeMapping ) query.list().get( 0 );
        }
        else {
            return null;
        }
    }

    public void addCodeMapping( CodeMapping code, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( code );

        session.flush();
    }

    public void updateCodeMapping( CodeMapping code, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( code );

        session.flush();
    }

    public void deleteCodeMapping( CodeMapping code, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.delete( code );

        session.flush();
    }

    @SuppressWarnings( "unchecked" )
    public List< Code > searchCodes( CodeType type, String id, String description, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "SELECT c FROM Code c" );

        boolean hasType = false;
        if ( type != null ) {
            queryBuilder.append( " where c.type = :type" );
            hasType = true;
        }

        boolean hasId = false;
        if ( id != null && !id.trim().isEmpty() ) {
            if ( hasType ) {
                queryBuilder.append( " and " );
            }
            else {
                queryBuilder.append( " where " );
            }

            queryBuilder.append( "c.id like :id" );
            hasId = true;
        }

        boolean hasDescription = false;
        if ( description != null && !description.trim().isEmpty() ) {
            if ( hasType || hasId ) {
                queryBuilder.append( " and " );
            }
            else {
                queryBuilder.append( " where " );
            }

            queryBuilder.append( "c.description like :description" );
            hasDescription = true;
        }

        if ( !includeDisabled ) {
            if ( hasType || hasId || hasDescription ) {
                queryBuilder.append( " and " );
            }
            else {
                queryBuilder.append( " where " );
            }

            queryBuilder.append( "c.enabled = true" );
        }

        queryBuilder.append( " order by c.order asc" );

        Query query = session.createQuery( queryBuilder.toString() );

        if ( hasType ) {
            query.setParameter( "type", type );
        }

        if ( hasId ) {
            query.setParameter( "id", id );
        }

        if ( hasDescription ) {
            query.setParameter( "description", description );
        }

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CodeNamedValuePair > getInternalIdList( CodeType type, String keyword, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder queryBuilder = new StringBuilder( "SELECT c.id as name, c.description as value FROM Code c" );

        boolean hasType = false;
        if ( type != null ) {
            queryBuilder.append( " where c.type = :type" );
            hasType = true;
        }

        boolean hasKeyword = false;
        if ( keyword != null ) {
            keyword = keyword.trim();

            if ( !keyword.isEmpty() ) {
                if ( hasType ) {
                    queryBuilder.append( " and c.description like :keyword" );
                }
                else {
                    queryBuilder.append( " where c.description like :keyword" );
                }
                hasKeyword = true;
            }
        }

        if ( !includeDisabled ) {
            if ( hasType || hasKeyword ) {
                queryBuilder.append( " and " );
            }
            else {
                queryBuilder.append( " where " );
            }

            queryBuilder.append( "c.enabled = true" );
        }

        Query query = session.createQuery( queryBuilder.toString() );

        if ( hasType ) {
            query.setParameter( "type", type );
        }

        if ( hasKeyword ) {
            query.setParameter( "keyword", keyword + "%" );
        }

        query.setResultTransformer( Transformers.aliasToBean( CodeNamedValuePair.class ) );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public SearchResult searchCodeMappings( ExternalSystemType systemType, CodeType codeType, String externalId, boolean includeDisabled, int pageNum, int pageSize ) {

        StringBuilder queryCountBuilder = new StringBuilder( "SELECT count(c) FROM CodeMapping c" );

        Query query = buildSearchCodeMappingQuery( queryCountBuilder, systemType, codeType, externalId, includeDisabled );

        int totalCount = ( ( Number ) query.uniqueResult() ).intValue();

        int index = pageSize * ( pageNum - 1 );

        StringBuilder queryMappingBuilder = new StringBuilder( "SELECT c FROM CodeMapping c" );

        query = buildSearchCodeMappingQuery( queryMappingBuilder, systemType, codeType, externalId, includeDisabled );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );

        List< CodeMapping > codeMapping = query.list();

        return new SearchResult( codeMapping, totalCount, pageNum, pageSize );
    }

    private Query buildSearchCodeMappingQuery( StringBuilder queryBuilder, ExternalSystemType systemType, CodeType codeType, String externalId, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        boolean hasSysType = false;
        if ( systemType != null ) {
            queryBuilder.append( " where c.systemType = :systemType" );
            hasSysType = true;
        }

        boolean hasType = false;
        if ( codeType != null ) {
            if ( hasSysType ) {
                queryBuilder.append( " and c.codeType = :codeType" );
            }
            else {
                queryBuilder.append( " where c.codeType = :codeType" );
            }
            hasType = true;
        }

        boolean hasId = false;
        if ( externalId != null ) {
            externalId = externalId.trim();

            if ( !externalId.trim().isEmpty() ) {

                if ( hasType || hasSysType ) {
                    queryBuilder.append( " and c.externalId like :externalId" );
                }
                else {
                    queryBuilder.append( " where c.externalId like :externalId" );
                }

                hasId = true;
            }
        }

        if ( !includeDisabled ) {
            if ( hasType || hasSysType || hasId ) {
                queryBuilder.append( " and " );
            }
            else {
                queryBuilder.append( " where " );
            }

            queryBuilder.append( "c.enabled = true" );
        }

        Query query = session.createQuery( queryBuilder.toString() );

        if ( hasSysType ) {
            query.setParameter( "systemType", systemType );
        }

        if ( hasType ) {
            query.setParameter( "codeType", codeType );
        }

        if ( hasId ) {
            query.setParameter( "externalId", externalId + "%" );
        }

        return query;
    }

    @SuppressWarnings( "unchecked" )
    public List< Code > searchCodes( List< CodeTypeIdPair > codeTypeIdPairs ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        if ( null != codeTypeIdPairs && !codeTypeIdPairs.isEmpty() ) {
            List< CodeType > typeReadList = new ArrayList<>();
            Criteria criteria = session.createCriteria( Code.class );
            Disjunction or = Restrictions.disjunction();
            for ( CodeTypeIdPair eachPair : codeTypeIdPairs ) {
                if ( eachPair.getId() != null && eachPair.getType() != null &&
                (typeReadList.isEmpty() || !typeReadList.contains(eachPair.getType()))) {
                        typeReadList.add( eachPair.getType() );
                        Criterion c1 = Restrictions.eq( "id", eachPair.getId() );
                        Criterion c2 = Restrictions.eq( "type", eachPair.getType() );
                        or.add( Restrictions.and( c1, c2 ) );
                    }
                }
            criteria.add(or);
            return (List<Code>) criteria.list();
        } else {
            return null;
        }
    }

    public Code getCodeByDescription( CodeType type, String description ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "SELECT c FROM Code as c WHERE c.type = :codeType and c.description = :description" );
        query.setParameter( "codeType", type );
        query.setParameter( "description", description );
        query.setMaxResults( 1 );

        List< ? > list = query.list();
        if ( !list.isEmpty() ) {
            return ( Code ) list.get( 0 );
        }
        else {
            return null;
        }
    }
}
