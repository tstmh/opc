package com.stee.spfcore.service.vendor;

import java.util.List;

import com.stee.spfcore.model.vendor.CategoryInfo;
import com.stee.spfcore.model.vendor.Vendor;

public interface IVendorService {

    /**
     * Add new Vendor.
     * 
     * @param vendor
     * @param requester
     */
    public String addVendor( Vendor vendor, String requester ) throws VendorServiceException;

    /**
     * Update vendor
     * 
     * @param vendor
     * @param requester
     */
    public void updateVendor( Vendor vendor, String requester ) throws VendorServiceException;

    /**
     * Get total vendor count in repository.
     * 
     * @param includeDisabled
     *            if true, will include disabled vendors. False, otherwise.
     * @return
     */
    public Integer getVendorCount( boolean includeDisabled ) throws VendorServiceException;

    /**
     * Retrieve all vendors
     * 
     * @param includeDisabled
     *            if true, will include disabled vendors. False, otherwise.
     * @return
     */
    public List< Vendor > getVendors( boolean includeDisabled ) throws VendorServiceException;

    /**
     * Get vendor by id
     * 
     * @param id
     * @return null if not found.
     */
    public Vendor getVendor( String id ) throws VendorServiceException;

    /**
     * Get vendor by name. If more than one vendor match the name specified, the first one
     * in the list will be returned.
     * 
     * @param name
     * @return null if not found.
     */
    public Vendor getVendorByName( String name ) throws VendorServiceException;

    public Vendor getVendorByNameAndCategoryNames( String vendorName, List< String > categoryNames ) throws VendorServiceException;

    /**
     * Retrieve all vendors within the specified page of the specified size
     * 
     * @param pageNum
     * @param pageSize
     * @param includeDisabled
     *            if true, will include disabled vendors. False, otherwise.
     * @return
     */
    public List< Vendor > getVendors( int pageNum, int pageSize, boolean includeDisabled ) throws VendorServiceException;

    /**
     * Retrieve all vendors from the specified categoryId.
     * 
     * @param categoryId
     * @param includeDisabled
     * @return
     * @throws VendorServiceException
     */
    public List< Vendor > getVendors( String categoryId, boolean includeDisabled ) throws VendorServiceException;

    /**
     * Retrieve all Category as CategoryInfo
     * 
     * @return
     * @throws VendorServiceException
     */
    public List< CategoryInfo > getCategoryInfos() throws VendorServiceException;

    public List< Vendor > getVendors( List< String > vendorIds ) throws VendorServiceException;

    public List< Vendor > getVendors( List< String > categoryIds, String vendorCode, String name, boolean includeDisabled ) throws VendorServiceException;
}
