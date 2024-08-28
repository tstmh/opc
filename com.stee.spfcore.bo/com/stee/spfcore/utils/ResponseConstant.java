package com.stee.spfcore.utils;

/**
 * 
 * @ClassName: ResponseConstant 
 * @Description: Response Constant
 * @author yu.yunxia
 * @date Jul 26, 2016 1:32:14 PM 
 *
 */
public interface ResponseConstant {
	
	/**
	 * responseStatus of success
	 */
	static final String SUCCESS_CODE = "OK";
	
	/**
	 * responseMsg of success
	 */
	static final String SUCCESS_MSG = "OK";
	
	/**
	 * responseStatus of fail
	 */
	static final String FAIL_CODE = "FAIL";
	
	/**
	 * responseMsg of fail
	 */
	static final String FAIL_MSG =
			"Unexpected service exception. Please contact administrator for assistance.";
}
