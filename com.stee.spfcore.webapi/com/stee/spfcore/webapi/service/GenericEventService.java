package com.stee.spfcore.webapi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.GenericEventDAO;
import com.stee.spfcore.webapi.model.genericEvent.GETargetedUser;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventDetail;

@Service
public class GenericEventService {

	private GenericEventDAO genericEventDAO;
	
	@Autowired
	public GenericEventService (GenericEventDAO genericEventDAO) {
		this.genericEventDAO = genericEventDAO;
	}
	
	@Transactional
	public GenericEventDetail getEventDetail(String eventId) {
		return genericEventDAO.getEventDetail(eventId);
	}
	
	@Transactional
	public List<GenericEventApplication> getGenericEventApplications(String nric, Date startDate, Date endDate) {
		return genericEventDAO.getGenericEventApplications(nric, startDate, endDate);
	}
	
	@Transactional
	public List<GenericEventDetail> getGenericEventPendingRegistration(String nric) {
		return genericEventDAO.getGenericEventPendingRegistration(nric);
	}
	
	@Transactional
	public GenericEventApplication getGenericEventApplication(String eventId, String nric) {
		return genericEventDAO.getGenericEventApplication(eventId, nric);
	}
	
	@Transactional
	public String addGenericEventApplication (GenericEventApplication application) {
		return genericEventDAO.addGenericEventApplication(application);
	}
	
	@Transactional
	public void updateGenericEventApplication (GenericEventApplication application) {
		genericEventDAO.updateGenericEventApplication(application);
	}
	
	@Transactional
	public String addGenericEventDetail (GenericEventDetail eventDetail) {
		return genericEventDAO.addGenericEventDetail(eventDetail);
	}
	
	@Transactional
	public void updateGenericEventDetail (GenericEventDetail eventDetail) {
		genericEventDAO.updateGenericEventDetail(eventDetail);
	}
	
	@Transactional
	public void deleteGenericEventDetail (GenericEventDetail eventDetail) {
		genericEventDAO.deleteGenericEventDetail(eventDetail);
	}
	
	@Transactional
	public String addGenericEventTargetedUser (GETargetedUser targetedUser) {
		return genericEventDAO.addGenericEventTargetedUser(targetedUser);
	}

	@Transactional
	public boolean isTargetedUser(String eventId, String nric) {
		return genericEventDAO.isTargetedUser(eventId, nric);
	}
}
