package com.stee.spfcore.utils;

import com.stee.spfcore.service.configuration.ICodeConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;

public class PersonnelUtils {

	private PersonnelUtils(){}
	/**
	 * Check if the service type code refer to non-SPF officer.
	 * @param serviceType
	 * @return
	 */
	public static boolean isNonSPFOfficer (String serviceType) {
		
		ICodeConfig codeConfig = ServiceConfig.getInstance().getCodeConfig();
		
		return (codeConfig.nonSPFCivServType().equals(serviceType) || codeConfig.nonSPFUniServType().equals(serviceType));
	}
	
	/**
	 * Check if the employment status code refer to active employment.
	 * @param employmentStatus
	 * @return
	 */
	public static boolean isActiveEmployment (String employmentStatus) {
		ICodeConfig codeConfig = ServiceConfig.getInstance().getCodeConfig();
		return codeConfig.activeEmploymentStatus().equals(employmentStatus);
	}
	
	/**
	 * Check if the service type code refer to Police Civilian (HQ) Personnel
	 * @param serviceType
	 * @return
	 */
	public static boolean isPoliceCivilianHQPersonnel (String serviceType) {
		ICodeConfig codeConfig = ServiceConfig.getInstance().getCodeConfig();
		
		return codeConfig.policeCivHQServType().equals(serviceType);
	}
	
	/**
	 * Check if the service type code refer to NSPAM PNSMEN
	 * @param serviceType
	 * @return
	 */
	public static boolean isPNSMEN (String serviceType) {
		ICodeConfig codeConfig = ServiceConfig.getInstance().getCodeConfig();
		
		return codeConfig.pnsmen().equals(serviceType);
	}
	
}
