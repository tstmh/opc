package com.stee.spfcore.service.vendor.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.VendorDAO;
import com.stee.spfcore.model.vendor.CategoryInfo;
import com.stee.spfcore.model.vendor.Vendor;
import com.stee.spfcore.service.vendor.IVendorService;
import com.stee.spfcore.service.vendor.VendorServiceException;

public abstract class AbstractVendorService implements IVendorService {

    protected static final Logger logger = Logger.getLogger( AbstractVendorService.class.getName() );

    protected VendorDAO dao;

    protected AbstractVendorService() {
        dao = new VendorDAO();
    }

    @Override
    public Integer getVendorCount( boolean includeDisabled ) throws VendorServiceException {
        Integer result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendorCount( includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendor count", e );
        }

        return result;
    }

    @Override
    public List< Vendor > getVendors( boolean includeDisabled ) throws VendorServiceException {
        List< Vendor > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendors( includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendors", e );
        }

        return result;
    }

    @Override
    public Vendor getVendor( String id ) throws VendorServiceException {
        Vendor result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendor( id );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendor", e );
        }

        return result;
    }

    @Override
    public Vendor getVendorByName( String name ) throws VendorServiceException {
        Vendor result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendorByName( name );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendor by name", e );
        }

        return result;
    }

    public Vendor getVendorByNameAndCategoryNames( String vendorName, List< String > categoryNames ) throws VendorServiceException {
        Vendor result = null;

        if ( logger.isLoggable( Level.INFO ) ) {
            logger.info(String.format("getVendorByNameAndCategoryNames() start, vendorName=%s, categoryNames=%s", vendorName, categoryNames));
        }
        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendorByNameAndCategoryNames( vendorName, categoryNames );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendor by vendor name and category names", e );
        }

        logger.info( String.format( "getVendorByNameAndCategoryNames() end, vendorId=%s", ( result == null ) ? null : result.getId() ) );

        return result;
    }

    @Override
    public List< Vendor > getVendors( int pageNum, int pageSize, boolean includeDisabled ) throws VendorServiceException {
        List< Vendor > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendors( pageNum, pageSize, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendors", e );
        }

        return result;
    }

    @Override
    public List< Vendor > getVendors( String categoryId, boolean includeDisabled ) throws VendorServiceException {
        List< Vendor > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getVendors( categoryId, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendors", e );
        }

        return result;
    }

    @Override
    public List< CategoryInfo > getCategoryInfos() throws VendorServiceException {

        List< CategoryInfo > result = null;

        try {
            SessionFactoryUtil.beginTransaction();
            result = dao.getCategoryInfos();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get CategoryInfos", e );
        }

        return result;
    }

    @Override
    public List< Vendor > getVendors( List< String > vendorIds ) throws VendorServiceException {
        List< Vendor > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getVendors( vendorIds );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendors", e );
        }

        return result;
    }

    @Override
    public List< Vendor > getVendors( List< String > categoryIds, String vendorCode, String name, boolean includeDisabled ) throws VendorServiceException {
        List< Vendor > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getVendors( categoryIds, vendorCode, name, includeDisabled );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new VendorServiceException( "Fail to get vendors", e );
        }

        return result;
    }

}
