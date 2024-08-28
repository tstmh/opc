package com.stee.spfcore.service.course.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.utils.DateUtils;

public class CourseUtil {

	private CourseUtil(){}
	public static boolean isRegistrationStarted (Course course) {
		Date now = new Date ();
		Date startDate = course.getRegistrationConfig().getOpenDate();
		
		return	!DateUtils.isBeforeDay(now, startDate);
	}
	
	public static boolean isRegistrationClosed (Course course) {
		Date now = new Date ();
		Date endDate = course.getRegistrationConfig().getCloseDate();
		
		return DateUtils.isAfterDay(now, endDate);
	}
	
	public static boolean withinCoolingPeriod(Course course) {

		Date now = new Date();
		Date closeDate = course.getRegistrationConfig().getCloseDate();
		Date endDate = course.getCoolingPeriodEndDate();
		
		return (!DateUtils.isBeforeDay(now, closeDate)) && (!DateUtils.isAfterDay(now, endDate));
	}
	
	
	public static void retrieveParticipantContacts (boolean includeOffice, PersonalDetail personalDetail, Set<String> emails, Set<String> phones) throws AccessDeniedException {
		
		List<Email> emailList = personalDetail.getEmailContacts();
		List<Phone> phoneList = personalDetail.getPhoneContacts();
		
		// Get office email
		if (includeOffice) {
			for (Email email : emailList) {
				if ((email.getLabel() == ContactLabel.WORK) && (email.getAddress() != null)){
					emails.add(email.getAddress().trim().toLowerCase());
				}
			}
		}
		
		// Get preferred email
		if (personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
			for (Email email : emailList) {
				if (email.isPrefer() && email.getAddress() != null) {
					// As emails is a Set, duplicate will be removed.
					emails.add(email.getAddress().trim().toLowerCase());
					break;
				}
			}
		}
		else if (personalDetail.getPreferredContactMode() == ContactMode.SMS) {
			for (Phone phone : phoneList) {
				if (phone.isPrefer()) {
					phones.add(phone.getNumber());
					break;
				}
			}
		}
	}
	
	
}
