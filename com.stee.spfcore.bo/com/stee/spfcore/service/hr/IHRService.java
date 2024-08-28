package com.stee.spfcore.service.hr;

public interface IHRService {

	public static final String HRHUB = "hrhub";
	public static final String NSPAM = "nspam";
	public static final String HTVMS = "htvms";
	
	
	public void processHRHUBFile ();
	
	
	public void processNSPAMFile ();
	
	
	public void processHTVMSFile ();
	
}
