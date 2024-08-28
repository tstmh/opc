package com.stee.spfcore.service.genericEvent;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.vo.genericEvent.UserEventApplicationDetail;

public interface IGenericEventService {

	
	public String addEventDetail (GenericEventDetail detail, String requester) throws GenericEventServiceException;
	
	public void updateEventDetail (GenericEventDetail detail, String requester) throws GenericEventServiceException;
	
	public void deleteEventDetail (String id, String requester) throws GenericEventServiceException;
	
	public GenericEventDetail getEventDetail (String id) throws GenericEventServiceException;
	
	public List<GenericEventDetail> getEventDetails () throws GenericEventServiceException;
	
	public List<GenericEventApplication> getEventApplications (String nric, Date startDate, Date endDate) throws GenericEventServiceException;
	
	public List<GenericEventDetail> getEventPendingRegistration (String nric) throws GenericEventServiceException;
	
	public GenericEventApplication getEventApplication (String eventId, String nric) throws GenericEventServiceException;
	
	public boolean isTargetedUser (String eventId, String nric) throws GenericEventServiceException;
	
	public void register (GenericEventApplication application, String requester) throws GenericEventServiceException;
	
	public void reRegister (GenericEventApplication application, String requester) throws GenericEventServiceException;
	
	public void addGenericEventApplication (GenericEventApplication application, String requester) throws GenericEventServiceException;
	
	public void updateGenericEventApplication (GenericEventApplication application, String requester) throws GenericEventServiceException;
	
	public boolean activate (String eventId, String requester) throws GenericEventServiceException;
	
	public boolean activateNoAnnouncement (String eventId, String requester) throws GenericEventServiceException;
	
	public void cancel (String eventId, String requester) throws GenericEventServiceException;
	
	public List<GenericEventApplication> getEventApplications (String eventId) throws GenericEventServiceException;
	
	public void withdraw (GenericEventApplication application, String requester) throws GenericEventServiceException;
	
	public List<UserEventApplicationDetail> getUserEventApplications (String eventId) throws GenericEventServiceException;
	
	public void notifyApplicationResult (String eventId, String requester) throws GenericEventServiceException;
	
	public void processTask () throws GenericEventServiceException;
	
	public void updateApplicationResult (String eventId, List<String> successApplications, List<String> unsuccessApplications, 
			String requester) throws GenericEventServiceException;
	
	public boolean activateEventWithAnnouncement(String eventId, String announcementId, String requester) throws GenericEventServiceException;
	
	public List<GenericEventDetail> getEventDetailsForLinking(String status) throws GenericEventServiceException;
	public List<GenericEventDetail> getUnitGenericEventDetail(String isMainDepartment, String unit) throws GenericEventServiceException;
	
	public List<String> getAnnouncementIdsOfUnit(List<String> units) throws GenericEventServiceException;
	
	public void postpone (String eventId, Date eventStartDate, Date eventEndDate, String requester) throws GenericEventServiceException;

	public List<String> getUnitAnnouncementIds(String units) throws GenericEventServiceException;
	
	public boolean activateEventForUnit(String eventId, String announcementId, String requester) throws GenericEventServiceException;

	public List<GenericEventDetail> getPcwfEventDetails() throws GenericEventServiceException;

} 
