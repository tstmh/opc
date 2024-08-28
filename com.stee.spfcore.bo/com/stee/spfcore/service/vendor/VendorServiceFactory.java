package com.stee.spfcore.service.vendor;

import com.stee.spfcore.service.vendor.impl.VendorService;

public class VendorServiceFactory {

	private VendorServiceFactory(){}
	private static IVendorService instance;
	
	public static synchronized IVendorService getInstance () {
		if (instance == null) {
			instance = createVendorService ();
		}
		
		return instance;
	}
	
	private static IVendorService createVendorService () {

		return new VendorService();
		
	}
}
