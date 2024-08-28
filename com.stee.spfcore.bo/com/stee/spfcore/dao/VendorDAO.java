package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.stee.spfcore.model.vendor.CategoryInfo;
import com.stee.spfcore.model.vendor.Vendor;

public class VendorDAO {

    private static final String SELECT_ONLY_ENABLED = "(v.effectiveDate is not null and v.effectiveDate <= current_date()) " + " and (v.obsoleteDate is null or v.obsoleteDate > current_date())";

    public String addVendor( Vendor vendor, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.save( vendor );

        session.flush();

        return vendor.getId();
    }

    public void updateVendor( Vendor vendor, String requester ) {

        SessionFactoryUtil.setUser( requester );

        Session session = SessionFactoryUtil.getCurrentSession();

        session.merge( vendor );

        session.flush();
    }

    public Integer getVendorCount( boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "SELECT count(v) FROM Vendor v" );

        if ( !includeDisabled ) {
            builder.append( " where " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );

        return ( ( Number ) query.uniqueResult() ).intValue();
    }

    @SuppressWarnings( "unchecked" )
    public List< Vendor > getVendors( boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Vendor v" );

        if ( !includeDisabled ) {
            builder.append( " where " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );

        return query.list();
    }

    public Vendor getVendor( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        return ( Vendor ) session.get( Vendor.class, id );
    }

    public Vendor getVendorByName( String name ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Query query = session.createQuery( "FROM Vendor v where v.name like :name" );
        query.setMaxResults( 1 );

        query.setParameter( "name", name );

        List< ? > list = query.list();
        if ( !list.isEmpty() ) {
            return ( Vendor ) list.get( 0 );
        }
        else {
            return null;
        }
    }

    @SuppressWarnings( "unchecked" )
    public Vendor getVendorByNameAndCategoryNames( String vendorName, List< String > categoryNames ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Vendor result = null;

        // query category id list based on category name
        StringBuilder query1Builder = new StringBuilder();
        query1Builder.append( "select c.id from Category c where 1=1 " );
        if ( categoryNames != null && !categoryNames.isEmpty() ) {
            query1Builder.append( "and c.name in :categoryNames " );
        }
        Query query1 = session.createQuery( query1Builder.toString() );
        if ( categoryNames != null && !categoryNames.isEmpty() ) {
            query1.setParameterList( "categoryNames", categoryNames );
        }
        List< String > catIdList = query1.list();

        if ( catIdList != null ) {
            // query vendor list based on vendor name
            StringBuilder query2Builder = new StringBuilder();
            query2Builder.append( "select v from Vendor v where 1=1 " );
            if ( vendorName != null ) {
                query2Builder.append( "and v.name = :vendorName " );
            }
            Query query2 = session.createQuery( query2Builder.toString() );
            if ( vendorName != null ) {
                query2.setParameter( "vendorName", vendorName );
            }
            List< Vendor > vendorList = ( List< Vendor > ) query2.list();

            if ( vendorList != null ) {
                // filter vendor list based on category id        
                for ( Vendor vendor : vendorList ) {
                    if ( result != null ) {
                        break;
                    }
                    List< CategoryInfo > categoryInfoList = vendor.getCategoryIds();
                    if ( categoryInfoList != null ) {
                        for ( CategoryInfo categoryInfo : categoryInfoList ) {
                            String catId = categoryInfo.getCategoryId();
                            if ( catId != null && catIdList.contains( catId)) {
                                result = vendor;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings( "unchecked" )
    public List< Vendor > getVendors( int pageNum, int pageSize, boolean includeDisabled ) {

        int index = pageSize * ( pageNum - 1 );

        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder( "FROM Vendor v" );

        if ( !includeDisabled ) {
            builder.append( " where " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setFirstResult( index );
        query.setMaxResults( pageSize );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Vendor > getVendors( String categoryId, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder builder = new StringBuilder( "Select distinct v FROM Vendor as v inner join v.categoryInfos as cat where cat.categoryId = :categoryId" );

        if ( !includeDisabled ) {
            builder.append( " and " );
            builder.append( SELECT_ONLY_ENABLED );
        }

        Query query = session.createQuery( builder.toString() );
        query.setParameter( "categoryId", categoryId );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< CategoryInfo > getCategoryInfos() {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Query query = session.createQuery( "select c.id as categoryId, c.name as name from Category c order by c.name asc" );
        query.setResultTransformer( new AliasToBeanResultTransformer( CategoryInfo.class ) );

        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Vendor > getVendors( List< String > vendorIds ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder builder = new StringBuilder( "Select v FROM Vendor as v where 1=1 " );
        if ( vendorIds != null ) {
            builder.append( "and v.id in :vendorIds" );
        }
        Query query = session.createQuery( builder.toString() );
        if ( vendorIds != null ) {
            query.setParameterList( "vendorIds", vendorIds );
        }
        return ( List< Vendor > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Vendor > getVendors( List< String > categoryIds, String vendorCode, String name, boolean includeDisabled ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        boolean hasCatId = false;
        boolean hasVendorCode = false;
        boolean hasName = false;

        StringBuilder builder = new StringBuilder( "Select distinct v FROM Vendor as v inner join v.categoryInfos as cat" );

        if ( categoryIds != null && !categoryIds.isEmpty() ) {
            hasCatId = true;
            builder.append( " where cat.categoryId in (:categoryId)" );
        }

        if ( vendorCode != null && !vendorCode.trim().isEmpty() ) {
            if ( hasCatId ) {
                builder.append( " and " );
            }
            else {
                builder.append( " where " );
            }
            hasVendorCode = true;
            builder.append( "v.vendorCode like :vendorCode" );
        }

        if ( name != null && !name.trim().isEmpty() ) {
            if ( hasCatId || hasVendorCode ) {
                builder.append( " and " );
            }
            else {
                builder.append( " where " );
            }
            hasName = true;
            builder.append( "v.name like :name" );
        }

        if ( !includeDisabled ) {
            if ( hasCatId || hasVendorCode || hasName ) {
                builder.append( " and " );
            }
            else {
                builder.append( " where " );
            }
            builder.append( SELECT_ONLY_ENABLED );
        }

        builder.append( " order by v.name asc" );

        Query query = session.createQuery( builder.toString() );

        if ( hasCatId ) {
            query.setParameterList( "categoryId", categoryIds );
        }

        if ( hasVendorCode ) {
            query.setParameter( "vendorCode", vendorCode );
        }

        if ( hasName ) {
            query.setParameter( "name", name );
        }

        return query.list();
    }

}
