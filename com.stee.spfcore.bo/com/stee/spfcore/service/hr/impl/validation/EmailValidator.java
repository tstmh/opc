package com.stee.spfcore.service.hr.impl.validation;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.logging.Logger;

public class EmailValidator {

	private EmailValidator(){}
	private static final Logger LOGGER = Logger.getLogger(EmailValidator.class.getName());
	
	public static boolean validateEmail(String email) {

		if (email == null || email.trim().isEmpty()) {
			return false;
		}

		boolean isValid = false;
		try {
			// Create InternetAddress object and validated the supplied
			// address which is this case is an email address.
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
		} 
		catch (AddressException e) {
			LOGGER.finest(String.format("Not valid email address: %s %s", email, e));
		}

		return isValid;
	}

}
