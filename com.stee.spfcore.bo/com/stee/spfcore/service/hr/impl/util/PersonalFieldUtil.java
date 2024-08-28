package com.stee.spfcore.service.hr.impl.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.model.personnel.Child;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.personnel.ContactMode;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;
import com.stee.spfcore.model.personnel.Spouse;
import com.stee.spfcore.service.hr.impl.input.IChild;
import com.stee.spfcore.service.hr.impl.input.ILeave;
import com.stee.spfcore.service.hr.impl.input.IRecord;
import com.stee.spfcore.service.hr.impl.input.ISpouse;
import com.stee.spfcore.utils.DateUtils;

public class PersonalFieldUtil {

	private PersonalFieldUtil(){}
	private static final Logger LOGGER = Logger.getLogger(PersonalFieldUtil.class.getName());
	
	private static final String DELETE_LEAVE_INDICATOR = "Y";


	public static void populate (PersonalDetail personalDetail, IRecord record)  {
		
		personalDetail.setName(record.getName());
		
		try {
			personalDetail.setDateOfBirth(record.getDateOfBirth());
		} 
		catch (ParseException e) {
			// Shouldn't happen.
			personalDetail.setDateOfBirth (null);
			LOGGER.log(Level.WARNING, "Error while processing date of birth", e);
		}
		
		if (record.getCitizenship() != null) {
			personalDetail.setCitizenship(record.getCitizenship().getValue());
		}
		else {
			personalDetail.setCitizenship(null);
		}
		
		if (record.getNationality() != null) {
			personalDetail.setNationality(record.getNationality().getValue());
		}
		else {
			personalDetail.setNationality(null);
		}
		
		
		if (record.getRace() != null) {
			personalDetail.setRace(record.getRace().getValue());
		}
		else {
			personalDetail.setRace(null);
		}
		
		if (record.getGender () != null) {
			personalDetail.setGender(record.getGender().getValue());
		}
		else {
			personalDetail.setGender(null);
		}
		
		if (record.getAddressType() != null) {
			personalDetail.setAddressType(record.getAddressType().getValue());
		}
		else {
			personalDetail.setAddressType(null);
		}
		
		personalDetail.setBlockNumber(record.getBlockNumber());
		
		personalDetail.setStreetName(record.getStreetName());
		
		personalDetail.setFloorNumber(record.getFloorNumber ());
		
		personalDetail.setUnitNumber(record.getUnitNumber());
		
		personalDetail.setBuildingName(record.getBuildingName());
		
		personalDetail.setPostalCode(record.getPostalCode());
		
		if (record.getMaritalStatus() != null) {
			personalDetail.setMaritalStatus(record.getMaritalStatus().getValue());
		}
		else {
			personalDetail.setMaritalStatus(null);
		}
		
		if (record.getEduationLevel() != null) {
			personalDetail.setEduationLevel(record.getEduationLevel().getValue());
		}
		else {
			personalDetail.setEduationLevel(null);
		}
		
		// Employment detail
		Employment employment = personalDetail.getEmployment();
		if (employment == null) {
			employment = new Employment();
			personalDetail.setEmployment(employment);
		}
		
		if (record.getDepartment() != null) {
			String departmentId = record.getDepartment().getValue();
			
			// Don't make changes to department if the value from HR System is the same. 
			if (departmentId == null || !departmentId.equals(employment.getOrganisationOrDepartment())) {
				employment.setOrganisationOrDepartment(departmentId);
				employment.setSubunit(null);
			}
		}
		else {
			employment.setOrganisationOrDepartment(null);
			employment.setSubunit(null);
		}
		
		try {
			employment.setDateOfEnlistment(record.getDateOfEnlistment());
		} 
		catch (ParseException e) {
			// Shouldn't happen
			employment.setDateOfEnlistment(null);
			LOGGER.log(Level.WARNING, "Error while processing date of enlistment", e);
		}
		
		try {
			employment.setDateOfAppointment(record.getDateOfAppointment());
		} 
		catch (ParseException e) {
			// Shouldn't happen
			employment.setDateOfAppointment(null);
			LOGGER.log(Level.WARNING, "Error while processing date of appointment", e);
		}
		
		try {
			employment.setDateOfRetirement(record.getDateOfRetirement());
		} 
		catch (ParseException e) {
			// Shouldn't happen
			employment.setDateOfRetirement(null);
		}
		
		if (record.getDesignation() != null) {
			employment.setDesignation(record.getDesignation().getValue());
		}
		else {
			employment.setDesignation(null);
		}
		
		if (record.getRankOrGrade() != null) {
			employment.setRankOrGrade(record.getRankOrGrade().getValue());
		}
		else {
			employment.setRankOrGrade(null);
		}
		
		if (record.getServiceType() != null) {
			employment.setServiceType(record.getServiceType().getValue());
		}
		else {
			employment.setServiceType(null);
		}
		
		
		if (record.getTenureOfService() != null) {
			employment.setTenureOfService(record.getTenureOfService().getValue());
		}
		else {
			employment.setTenureOfService(null);
		}
		
		if (record.getMedicalScheme() != null) {
			employment.setMedicalScheme(record.getMedicalScheme().getValue());
		}
		else {
			employment.setMedicalScheme(null);
		}
		
		if (record.getDivisionStatus() != null) {
			employment.setDivisionStatus(record.getDivisionStatus().getValue());
		}
		else {
			employment.setDivisionStatus(null);
		}
		
		if (record.getSchemeOfService() != null) {
			employment.setSchemeOfService(record.getSchemeOfService().getValue());
		}
		else {
			employment.setSchemeOfService(null);
		}
		
		if (record.getEmploymentStatus() != null) {
			employment.setEmploymentStatus(record.getEmploymentStatus().getValue());
		}
		else {
			employment.setEmploymentStatus(null);
		}
		
		try {
			employment.setLeavingServiceDate(record.getLeavingServiceDate());
		} 
		catch (ParseException e) {
			// Shouldn't happen
			employment.setLeavingServiceDate(null);
			LOGGER.log(Level.WARNING, "Error while processing leaving service date", e);
		}
		
		employment.setSalary(record.getSalary());
		
		
		populatePhones (personalDetail, record);
		
		populateEmails (personalDetail, record);
		
		setDefaultContact (personalDetail);
		
		try {
			populateLeaves (personalDetail, record);
		} 
		catch (ParseException e) {
			// Shouldn't happen as already validated.
			LOGGER.log(Level.WARNING, "Error while processing leaves", e);
		}
		
		try {
			populateSpouses (personalDetail, record);
		} 
		catch (ParseException e) {
			// Shouldn't happen as already validated.
			LOGGER.log(Level.WARNING, "Error while processing spouses", e);
		}

		try {
			populateChildren (personalDetail, record);
		} 
		catch (ParseException e) {
			// Shouldn't happen as already validated.
			LOGGER.log(Level.WARNING, "Error while processing children", e);
		}
		
	}
	
	
	private static void setDefaultContact (PersonalDetail personalDetail) {
		if (personalDetail.getPreferredContactMode() == null) {
			// Default the preferred contact mode to Email
			personalDetail.setPreferredContactMode(ContactMode.EMAIL);
		}
		
		// Set the office email to preferred if preferred email is not set
		if (personalDetail.getPreferredContactMode() == ContactMode.EMAIL) {
			boolean hasPreferred = false;
			for (Email email : personalDetail.getEmailContacts()) {
				if (email.isPrefer()) {
					hasPreferred = true;
				}
			}
			//Don't have prefer email. Set the office email to preferred
			if (!hasPreferred) {
				for (Email email : personalDetail.getEmailContacts()) {
					if (email.getLabel() == ContactLabel.WORK) {
						Email officeEmail = email;
                        officeEmail.setPrefer(true);
                    }
				}
			}
		}
	}
	
	private static String removeCountryCodeFromPhone(String phone){
		
		String[] split = phone.split("-");
		
		if (split.length > 1){
			return split[split.length - 1].trim();
		} else {
			return phone;
		}
	}
	
	
	private static void populatePhones (PersonalDetail personalDetail, IRecord record) {
		
		List<Phone> phones = personalDetail.getPhoneContacts();
		if (phones == null) {
			phones = new ArrayList<>();
			personalDetail.setPhoneContacts(phones);
		}
		
		Phone preferPhone = new Phone();
		// Retrieve the preferred phone, which could be type MOBILE or OTHERS
		// Can only have one preferred 
		for (Phone phone : phones) {
			if (phone.isPrefer()) {
				preferPhone = phone;
				break;
			}
		}
		
		// Clear all and update with records from External System.
		phones.clear();
		
		// Add office contact
		for (String num : record.getOfficeContact()) {
			if (num != null && !num.isEmpty()) {
				Phone phone = new Phone ();
				phone.setLabel(ContactLabel.WORK);
				phone.setPrefer(false);
				phone.setNumber(num);
				
				phones.add(phone);
			}
		}
		
		// Add home
		String num = record.getHomeContact();
		if (num != null && !num.isEmpty()) {
			Phone phone = new Phone ();
			phone.setPrefer(false);
			phone.setLabel(ContactLabel.HOME);
			phone.setNumber(num);
			
			phones.add(phone);
		}
		
		// Mobile Conatcts
		for (String hp : record.getMobileContact ()) {
			if (hp != null && !hp.isEmpty()) {
				Phone phone = new Phone();
				phone.setNumber(removeCountryCodeFromPhone(hp));
				phone.setLabel(ContactLabel.MOBILE);
				
				// Preserve the prefer flag
				if ((preferPhone!=null)&&(hp.equals(preferPhone.getNumber()))) {
					phone.setPrefer(true);
					preferPhone = null;
				}
				else {
					phone.setPrefer(false);
				}
				
				phones.add(phone);
			}
		}
		
		// Finally, if the preferred phone from previous record still exists,
		// then need to preserve it if it is of type 'OTHERS'. Else, too bad.
		if ((preferPhone != null) && ((preferPhone.getLabel() != null) && (preferPhone.getLabel() == ContactLabel.OTHERS))){
			phones.add(preferPhone);
		}
	}
	
	private static void populateEmails (PersonalDetail personalDetail, IRecord record) {
		
		List<Email> emails = personalDetail.getEmailContacts();
		
		if (emails == null) {
			emails = new ArrayList<>();
			personalDetail.setEmailContacts(emails);
		}
		
		Email preferEmail = new Email();
	
		for (Email email : emails) {
			if (email.isPrefer()) {
				preferEmail = email;
				break;
			}
		}
		
		emails.clear();
		
		String officeEmail = record.getOfficeEmail ();
		if (officeEmail != null && !officeEmail.isEmpty()) {
			Email email = new Email();
			email.setLabel(ContactLabel.WORK);
			email.setAddress(officeEmail);
			
			if(preferEmail.getAddress() != null) {
				if (officeEmail.equals(preferEmail.getAddress())) {
					email.setPrefer(true);
					preferEmail = null;
				}
				else {
					email.setPrefer(false);
				}
			} else {
				email.setPrefer(false);
			}
			
			emails.add(email);
		}
		
		String personalEmail = record.getPersonalEmail();
		if (personalEmail != null && !personalEmail.isEmpty()) {
			Email email = new Email();
			email.setLabel(ContactLabel.HOME);
			email.setAddress(personalEmail);

			if (preferEmail !=null) {
				if (preferEmail.getAddress() != null) {
					if (personalEmail.equals(preferEmail.getAddress())) {
						email.setPrefer(true);
						preferEmail = null;
					} else {
						email.setPrefer(false);
					}
				} else {
					email.setPrefer(false);
				}
			}
			emails.add(email);
		}
			
		if ((preferEmail != null) &&
		((preferEmail.getLabel() != null) && (preferEmail.getLabel() == ContactLabel.OTHERS))){
			emails.add(preferEmail);
		}
	}
	
	
	private static void populateLeaves (PersonalDetail personalDetail, IRecord record) throws ParseException {
		
		List<Leave> leaves = personalDetail.getLeaves();
		if (leaves == null) {
			leaves = new ArrayList<>();
			personalDetail.setLeaves(leaves);
		}
		
		// Need to add leave first then handle the delete
		// to ensure that if the HRHUB send me leave to add 
		// and delete as separate record. 
		for (ILeave leave : record.getLeaves()) {
			if (!DELETE_LEAVE_INDICATOR.equals(leave.getDeleteIndicator())) {
				int index = findLeave(leaves, leave);
				// Only add when the record not already exists.
				if (index == -1) {
					Leave personLeave = new Leave ();
					personLeave.setStartDate(leave.getStartDate());
					personLeave.setEndDate(leave.getEndDate());
					personLeave.setLeaveType(leave.getLeaveType().getValue());
					
					leaves.add(personLeave);
				}
				else {
					Leave personLeave = (Leave) leaves.get(index);
					String type = leave.getLeaveType().getValue();
					if (!personLeave.getLeaveType().equals(type)) {
						personLeave.setLeaveType(type);
					}
				}
			}
		}
		
		// Now, remove record that is indicated to be deleted.
		for (ILeave leave : record.getLeaves()) {
			if (DELETE_LEAVE_INDICATOR.equals(leave.getDeleteIndicator())) {
				int index = findLeave(leaves, leave);
				
				if (index != -1) {
					leaves.remove(index);
				}
			}
		}
		
	}
	
	
	private static int findLeave (List<Leave> leaves, ILeave leave) throws ParseException {
		
		int index = -1;
		
		for (int i = 0; i < leaves.size(); i++) {
			Leave personLeave = leaves.get(i);
			if (DateUtils.isSameDay(personLeave.getStartDate(), leave.getStartDate()) &&
						DateUtils.isSameDay(personLeave.getEndDate(), leave.getEndDate())) {
									
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	
	private static void populateSpouses (PersonalDetail personalDetail, IRecord record) throws ParseException {
		
		// Only NSPAM and HRHub has spouse info.
		if (record.getSpouses() == null) {
			return;
		}
		
		List<Spouse> spouses = personalDetail.getSpouses();
		if (spouses == null) {
			spouses = new ArrayList<>();
			personalDetail.setSpouses(spouses);
		}
		
		LOGGER.log(Level.INFO, "Spouse size from db: "+ spouses.size());
		
		List<Spouse> recordSpouseList = new ArrayList<>();
		
		for (ISpouse spouse : record.getSpouses()) {
			
			if (validateSpouse(spouse)) {
				
				Spouse personSpouse = new Spouse();
				personSpouse.setNric(spouse.getId());
				personSpouse.setName(spouse.getName());
				personSpouse.setDateOfMarriage(spouse.getDateOfMarriage());
				personSpouse.setCertificateNumber(spouse.getCertificateNumber());
				
				personSpouse.setIdType(spouse.getIdType().getValue());
				
				recordSpouseList.add(personSpouse);
			} else {
				LOGGER.log(Level.WARNING, "Spouse id or idtype is null");
			}
		}
		
		LOGGER.log(Level.INFO, "Spouse size from record: "+ recordSpouseList.size());
		
		//add db spouse to new list if not in new list
		for (Spouse spouse : spouses){
			if(!containsSpouse(recordSpouseList, spouse.getNric())){
				recordSpouseList.add(spouse);
			}
		}
		
		LOGGER.log(Level.INFO, "Spouse final size: "+ recordSpouseList.size());
		personalDetail.setSpouses(recordSpouseList);
	}
	
	private static boolean validateSpouse(ISpouse spouse) throws ParseException{
		
		boolean validate = true;
		
		if (spouse.getIdType() == null){
			validate = false;
		} else {
			if (spouse.getIdType().getValue().isEmpty() || spouse.getIdType().getDescription().isEmpty()){
				validate = false;
			}
		}
		
		if (spouse.getId() == null){
			validate = false;
		} else {
			if (spouse.getId().isEmpty()){
				validate = false;
			}
		}
		
		if (spouse.getName() == null){
			validate = false;
		} else {
			if (spouse.getName().isEmpty()){
				validate = false;
			}
		}
		
		if (spouse.getDateOfMarriage() == null) {
			validate = false;
		}
		
		if (spouse.getCertificateNumber() == null) {
			validate = false;
		} else {
			if (spouse.getCertificateNumber().isEmpty()){
				validate = false;
			}
		}
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("validateSpouse: %s", validate));
		}
		return validate;
	}
	
	private static boolean containsSpouse (List<Spouse> list, String nric){
		for (Spouse spouse : list){
			if (spouse.getNric().equals(nric)){
				return true;
			}
		}
		return false;
	}
	
	private static void populateChildren (PersonalDetail personalDetail, IRecord record) throws ParseException {
		
		// Only NSPAM and HRHub has spouse info.
		if (record.getChildren() == null) {
			return;
		}
		
		List<Child> children = personalDetail.getChildren();
		if (children == null) {
			children = new ArrayList<>();
			personalDetail.setChildren(children);
		}
		
		LOGGER.log(Level.INFO, "Children size from db: "+ children.size());
		
		List<Child> recordChildrenList = new ArrayList<>();
		
		for (IChild child : record.getChildren()) {
			
			if (validateChild(child)) {
				Child personChild = new Child();
				personChild.setNric(child.getId());
				personChild.setName(child.getName());
				personChild.setDateOfBirth(child.getDateOfBirth());
				
				personChild.setIdType(child.getIdType().getValue());
				
				recordChildrenList.add(personChild);
			} else {
				LOGGER.log(Level.WARNING, "Child id or idtype is null");
			}
		}
		
		LOGGER.log(Level.INFO, "Children size from record: "+ recordChildrenList.size());
		
		//add db child to new list if not in new list
		for (Child child : children){
			if(!containsChildren(recordChildrenList, child.getNric())){
				recordChildrenList.add(child);
			}
		}
		
		LOGGER.log(Level.INFO, "Children final size: "+ recordChildrenList.size());
		
		personalDetail.setChildren(recordChildrenList);
	}
	
	private static boolean validateChild(IChild child) throws ParseException{
		
		boolean validate = true;
		
		if (child.getIdType() == null){
			validate = false;
		} else {
			if (child.getIdType().getValue().isEmpty() || child.getIdType().getDescription().isEmpty()){
				validate = false;
			}
		}
		
		if (child.getId() == null){
			validate = false;
		} else {
			if (child.getId().isEmpty()){
				validate = false;
			}
		}
		
		if (child.getName() == null){
			validate = false;
		} else {
			if (child.getName().isEmpty()){
				validate = false;
			}
		}
		
		if (child.getDateOfBirth() == null) {
			validate = false;
		}
		if ( LOGGER.isLoggable( Level.INFO ) ) {
			LOGGER.info(String.format("validateChild: %s", validate));
		}
		return validate;
	}
	
	private static boolean containsChildren (List<Child> list, String nric){
		for (Child child : list){
			if (child.getNric().equals(nric)){
				return true;
			}
		}
		return false;
	}
	
	public static void populate (ExtraEmploymentInfo info, IRecord record)  {
		
		info.setNric(record.getNric());
		
		if (record.getPresentUnit() != null) {
			info.setPresentUnit (record.getPresentUnit().getValue());
		}
		else {
			info.setPresentUnit (null);
		}
		
		if (record.getPresentDesignation() != null) {
			info.setPresentDesignation(record.getPresentDesignation().getValue());
		}
		else {
			info.setPresentDesignation (null);
		}
		
		try {
			info.setCurrentSchemeDate(record.getCurrentSchemeDate());
		} 
		catch (ParseException e) {
			// Shouldn't happen
			info.setCurrentSchemeDate(null);
			LOGGER.log(Level.WARNING, "Error while processing current scheme date", e);
		}
	}
}
