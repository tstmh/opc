package com.stee.spfcore.service.announcement.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stee.spfcore.dao.AnnouncementDAO;
import com.stee.spfcore.dao.MarketingDAO;
import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;


public class AnnouncementUtil {
	
	private AnnouncementDAO dao;
	private MarketingDAO marketingDAO;
	
	public AnnouncementUtil () {
		dao = new AnnouncementDAO();
		this.marketingDAO = new MarketingDAO();
	}
	
	public Set<PersonalDetail> getTargetedUsers (Announcement announcement) {
		
		List<String> blacklist = dao.getBlacklistedUser(announcement.getExemptedModules());
		List<String> exclusionIds = announcement.getExclusionIds();
		
		Set<PersonalDetail> targetedList = new HashSet<>();
		
		for (String memberGroupId : announcement.getMemberGroupIds()) {
			List<PersonalDetail> personalDetails = marketingDAO.getPersonnelInGroup(memberGroupId);
			for (PersonalDetail personalDetail : personalDetails) {
				// Exclude those in blacklist or exclusion list
				if (blacklist.contains(personalDetail.getNric()) || exclusionIds.contains(personalDetail.getNric())) {
					continue;
				}
				targetedList.add(personalDetail);
			}
		}
		
		return targetedList;
	}
	
	
	public void retrieveTargetedUserContacts (Announcement announcement, Set<PersonalDetail> personalDetails, Set<String> targetedEmails,
			Set<String> targetedMobiles) {
	
		for (PersonalDetail personalDetail : personalDetails) {
			retrieveTargetedUserContacts(announcement, targetedEmails, targetedMobiles, personalDetail);
		}
		
	}

	public void retrieveTargetedUserContacts(Announcement announcement, Set<String> targetedEmails,
			Set<String> targetedMobiles, PersonalDetail personalDetail) {
		
		// Retrieve office contacts
		if (announcement.isSendByOfficeEmail()) {
			
			// If more than one office contacts, then first one will be selected.
			for (Email email : personalDetail.getEmailContacts()) {
				if (email.getLabel() == ContactLabel.WORK && email.getAddress() != null) {
					targetedEmails.add(email.getAddress().trim().toLowerCase());
					break;
				}
			}
		}
		
		// If send by preferred contact is not selected, don't need to 
		// further process email and sms.
		if (!announcement.isSendByPreferredContact()) {
			return;
		}
		
		// if personal preferred mode of contact is email, select the preferred email
		if (personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
			
			String preferredEmail = null;
			
			// Only interested in email other than office that is
			// configured as preferred.
			for (Email email : personalDetail.getEmailContacts()) {
				if (email.isPrefer() && email.getAddress() != null) {
					preferredEmail = email.getAddress().trim().toLowerCase();
					break;
				}
			}
			
			// Since targetedEmails is a Set, will ensure no duplicate
			if (preferredEmail != null) {
				targetedEmails.add(preferredEmail);
			}
		}
		else if (personalDetail.getPreferredContactMode() == ContactMode.SMS){
			
			// Select first phone number that is set to preferred.
			for (Phone phone : personalDetail.getPhoneContacts()) {
				if (phone.isPrefer()) {
					targetedMobiles.add(phone.getNumber());
					break;
				}
			}
		}
	}
}
