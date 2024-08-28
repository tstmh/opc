package com.stee.spfcore.vo.personnel;

import java.util.List;

import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;

public class PersonalNricEmailPhone {
	private String nric;
	private ContactMode preferredContactMode;
	private List< Email > emails;
	private List< Phone > phones;
	
	public PersonalNricEmailPhone() {
		
	}
	public PersonalNricEmailPhone(String nric, List<Email> emails,
			List<Phone> phones) {
		this.nric = nric;
		this.emails = emails;
		this.phones = phones;
	}
	
	public PersonalNricEmailPhone(String nric, List<Email> emails,
			List<Phone> phones, ContactMode preferredContactMode) {
		this.nric = nric;
		this.emails = emails;
		this.phones = phones;
		this.preferredContactMode = preferredContactMode;
	}
	
	public PersonalNricEmailPhone(PersonalDetail personalDetails) {
		this.nric = personalDetails.getNric();
		this.emails = personalDetails.getEmailContacts();
		this.phones = personalDetails.getPhoneContacts();
		this.preferredContactMode = personalDetails.getPreferredContactMode();
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public List<Email> getEmails() {
		return emails;
	}
	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	public ContactMode getPreferredContactMode() {
		return preferredContactMode;
	}
	public void setPreferredContactMode(ContactMode preferredContactMode) {
		this.preferredContactMode = preferredContactMode;
	}
	
}
