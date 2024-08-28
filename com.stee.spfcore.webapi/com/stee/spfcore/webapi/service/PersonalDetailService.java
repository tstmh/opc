package com.stee.spfcore.webapi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.PersonalDetailDAO;
import com.stee.spfcore.webapi.model.personnel.ChangeRecord;
import com.stee.spfcore.webapi.model.personnel.Employment;
import com.stee.spfcore.webapi.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;

@Service
public class PersonalDetailService {

	private PersonalDetailDAO personalDetailDAO;
	
	@Autowired
	public PersonalDetailService (PersonalDetailDAO personalDetailDAO) {
		this.personalDetailDAO = personalDetailDAO;
	}
	
	@Transactional
	public PersonalDetail getPersonalDetail(String nric) {
		return personalDetailDAO.getPersonalDetail(nric);
	}
	
	@Transactional
	public void updatePersonalDetail(PersonalDetail personal) {
		personalDetailDAO.updatePersonalDetail(personal);
	}
	
	@Transactional
	public List<PersonalDetail> getPersonalDetails() {
		return personalDetailDAO.getPersonalDetails();
	}
	
	@Transactional
	public void addPersonalDetail(PersonalDetail personal) {
		personalDetailDAO.addPersonalDetail(personal);
	}
	
	@Transactional
	public ChangeRecord getChangeRecord(String nric) {
		return personalDetailDAO.getChangeRecord(nric);
	}
	
	@Transactional
	public void addChangeRecord(ChangeRecord record) {
		personalDetailDAO.addChangeRecord(record);
	}
	
	@Transactional
	public void updateChangeRecord(ChangeRecord record) {
		personalDetailDAO.updateChangeRecord(record);
	}
	
	@Transactional
	public void saveExtraEmploymentInfo(ExtraEmploymentInfo info) {
		personalDetailDAO.saveExtraEmploymentInfo(info);
	}
	
	@Transactional
	public String getPersonalName(String nric) {
		return personalDetailDAO.getPersonalName(nric);
	}
	
	@Transactional
	public Employment getEmployment ( String nric ) {
		return personalDetailDAO.getEmployment(nric);
	}
	
	@Transactional
	public boolean isOnFullMonthNoPayLeave ( String nric, Date date ) {
		return personalDetailDAO.isOnFullMonthNoPayLeave(nric, date);
	}
	
}
