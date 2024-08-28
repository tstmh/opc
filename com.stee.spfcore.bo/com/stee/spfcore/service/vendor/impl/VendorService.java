package com.stee.spfcore.service.vendor.impl;

import java.util.logging.Level;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.vendor.Vendor;
import com.stee.spfcore.service.vendor.VendorServiceException;

public class VendorService extends AbstractVendorService {

	
	@Override
	public String addVendor(Vendor vendor, String requester) throws VendorServiceException {
		
		String id = null;
		try {
			SessionFactoryUtil.beginTransaction();
			id = dao.addVendor(vendor, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new VendorServiceException ("Fail to add vendor", e);
		}
		
		return id;
	}

	@Override
	public void updateVendor(Vendor vendor, String requester) throws VendorServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateVendor (vendor, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new VendorServiceException ("Fail to update vendor", e);
		}
	}


	

}
