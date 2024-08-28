package com.stee.spfcore.utils;

import java.security.SecureRandom;


/**
 * Generate random string for use in Portal User creation.
 * User will login using Singpass password instead of this string in production.   
 */
public class PasswordGenerator {

	private PasswordGenerator(){}
	/**
	 * Specify the length
	 */
	private static final int PASSWORD_LENGTH = 12; 
	
	/**
	 * List of valid characters to use for generation.
	 */
	private static final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
	
	private static final SecureRandom random = new SecureRandom();
	
	
	public static String generate () {
		
		StringBuilder sb = new StringBuilder (PASSWORD_LENGTH);

		for (int i = 0; i < PASSWORD_LENGTH; i++){
			sb.append(VALID_CHARACTERS.charAt(random.nextInt(VALID_CHARACTERS.length())));
		}

		return(sb.toString());
		
	}
}
