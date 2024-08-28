package com.stee.spfcore.service.genericEvent.impl;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.genericEvent.GEApplicationStatus;
import com.stee.spfcore.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.model.genericEvent.GenericEventStatus;
import com.stee.spfcore.model.genericEvent.internal.GETargetedUser;
import com.stee.spfcore.model.genericEvent.internal.NotifyApplicationResultTask;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.service.announcement.AnnouncementServiceFactory;
import com.stee.spfcore.service.announcement.IAnnouncementService;
import com.stee.spfcore.service.genericEvent.GenericEventServiceException;
import com.stee.spfcore.utils.DateUtils;
import com.stee.spfcore.utils.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class GenericEventService extends AbstractGenericEventService {

	private IAnnouncementService announcementService;
	private EmailSmsHelper emailSmsHelper;
	
	public GenericEventService() {
		super();
		this.announcementService = AnnouncementServiceFactory.getInstance();
		emailSmsHelper = new EmailSmsHelper();
	}

	@Override
	public String addEventDetail(GenericEventDetail detail, String requester) throws GenericEventServiceException {
		String id = null;

		try {
			SessionFactoryUtil.beginTransaction();

			id = dao.addGenericEventDetail(detail, requester);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add GenericEventDetail", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to add GenericEventDetail", e);
		}

		return id;
	}

	
	@Override
	public void updateEventDetail(GenericEventDetail detail, String requester) throws GenericEventServiceException {
		try {
			SessionFactoryUtil.beginTransaction();

			dao.updateGenericEventDetail(detail, requester);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update GenericEventDetail", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to update GenericEventDetail", e);
		}
		
	}

	@Override
	public void deleteEventDetail(String id, String requester) throws GenericEventServiceException {
		try {
			SessionFactoryUtil.beginTransaction();

			GenericEventDetail eventDetail = dao.getGenericEventDetail(id);
			
			if (eventDetail.getStatus() != GenericEventStatus.DRAFT) {
				throw new GenericEventServiceException("Only draft event can be deleted.");
			}
			
			dao.deleteGenericEventDetail(eventDetail, requester);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete GenericEventDetail", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to delete GenericEventDetail", e);
		}
	}

	@Override
	public void register(GenericEventApplication application, String requester) throws GenericEventServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}
	
	@Override
	public void reRegister(GenericEventApplication application, String requester) throws GenericEventServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	
	@Override
	public void withdraw(GenericEventApplication application, String requester) throws GenericEventServiceException {
		throw new UnsupportedOperationException ("Internet side only operation.");
	}

	@Override
	public void addGenericEventApplication(GenericEventApplication application, String requester)
			throws GenericEventServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();

			dao.addGenericEventApplication(application, requester);
			
			announcementService.removeAnnouncementRecipient(application.getEventId(), application.getNric(), requester);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add GenericEventApplication", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to add GenericEventApplication", e);
		}
	}

	@Override
	public void updateGenericEventApplication(GenericEventApplication application, String requester)
			throws GenericEventServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();

			dao.updateGenericEventApplication(application, requester);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update GenericEventApplication", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to update GenericEventApplication", e);
		}
	}

	@Override
	public boolean activate(String eventId, String requester) throws GenericEventServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			
			if (eventDetail.getStatus() == GenericEventStatus.ACTIVATED || eventDetail.getStatus() == GenericEventStatus.CANCELLED) {
				throw new GenericEventServiceException ("Only draft generic event can be activated.");
			}
			
			if (eventDetail.getAnnouncementId() == null || eventDetail.getAnnouncementId().isEmpty()) {
				throw new GenericEventServiceException ("Announcement not specified for generic event.");
			}
			
			// Make sure no past date in the event
			boolean isDateOk = validateEventDate (eventDetail);
			
			if (isDateOk) {
				boolean announcementResult = announcementService.activateAnnouncement(eventDetail.getAnnouncementId(), requester);
				
				if (announcementResult) {
					eventDetail.setStatus(GenericEventStatus.ACTIVATED);
					dao.updateGenericEventDetail(eventDetail, requester);
					
					Set<PersonalDetail> personalDetails = announcementService.getTargetedUsers(eventDetail.getAnnouncementId());
					List<GETargetedUser> targetedUsers = new ArrayList<>();
					
					for (PersonalDetail personalDetail : personalDetails) {
						GETargetedUser targetedUser = new GETargetedUser(null, personalDetail.getNric(), eventId);
						targetedUsers.add(targetedUser);
					}
					
					dao.saveTargetedUsers(targetedUsers, requester);
					
					result = true;
				}
			}		
				
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate generic event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to activate generic event", e);
		}
		
		return result;
	}

	@Override
	public boolean activateNoAnnouncement(String eventId, String requester) throws GenericEventServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			
			if (eventDetail.getStatus() == GenericEventStatus.ACTIVATED || eventDetail.getStatus() == GenericEventStatus.CANCELLED) {
				throw new GenericEventServiceException ("Only draft generic event can be activated.");
			}
			
			// Make sure no past date in the event
			boolean isDateOk = validateEventDate (eventDetail);
			
			if (isDateOk) {
				boolean announcementResult = true;

                eventDetail.setStatus(GenericEventStatus.ACTIVATED);
                dao.updateGenericEventDetail(eventDetail, requester);

                result = true;
            }
				
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate generic event (No Announcement)", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to activate generic event (No Announcement)", e);
		}
		
		return result;
	}
	
	private boolean validateEventDate (GenericEventDetail eventDetail) {
		
		Date now = new Date ();
		
		Date startDate = eventDetail.getEventStartDate();
		Date endDate = eventDetail.getEventEndDate();
		Date registrationStart = eventDetail.getRegistrationStartDate();
		Date registrationEnd = eventDetail.getRegistrationEndDate();
		
		if (startDate == null) {
			logger.log(Level.SEVERE, "Missing event start date");
			return false;
		}
		else if (endDate == null) {
			logger.log(Level.SEVERE, "Missing event end date");
			return false;
		}
		else if (registrationStart == null) {
			logger.log(Level.SEVERE, "Missing event registration start date");
			return false;
		}
		else if (registrationEnd == null) {
			logger.log(Level.SEVERE, "Missing event registration end date");
			return false;
		}
		else if (DateUtils.isBeforeDay(startDate, now)) {
			logger.log(Level.SEVERE, "Event start date is a past date");
			return false;
		}
		else if (DateUtils.isBeforeDay(endDate, now)) {
			logger.log(Level.SEVERE, "Event end date is a past date");
			return false;
		}
		else if (DateUtils.isBeforeDay(registrationStart, now)) {
			logger.log(Level.SEVERE, "Registration start date is a past date");
			return false;
		}
		else if (DateUtils.isBeforeDay(registrationEnd, now)) {
			logger.log(Level.SEVERE, "Registration end date is a past date");
			return false;
		}
		else if (DateUtils.isBeforeDay(registrationEnd, registrationStart)) {
			logger.log(Level.SEVERE, "Registration end date is earlier than registration start date");
			return false;
		}
		else if (DateUtils.isBeforeDay(endDate, startDate)) {
			logger.log(Level.SEVERE, "Event end date is earlier than event start date");
			return false;
		}
		else if (DateUtils.isBeforeDay(startDate, registrationEnd)) {
			logger.log(Level.SEVERE, "Event start date is earlier than registration end date");
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public void cancel(String eventId, String requester) throws GenericEventServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			if (eventDetail.getStatus() != GenericEventStatus.ACTIVATED) {
				throw new GenericEventServiceException ("Only activated generic event can be deactivated.");
			}
			
			announcementService.deactivateAnnouncement(eventDetail.getAnnouncementId(), requester);
			
			eventDetail.setStatus(GenericEventStatus.CANCELLED);
			dao.updateGenericEventDetail(eventDetail, requester);
			
			emailSmsHelper.informOfCancellation(eventDetail);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to cancel generic event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to cancel generic event", e);
		}
	}

	@Override
	public void notifyApplicationResult(String eventId, String requester) throws GenericEventServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			NotifyApplicationResultTask task = new NotifyApplicationResultTask(eventId, false, null, new Date (), requester);
			
			dao.addNotifyApplicationResultTask(task, requester);
					
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add notify application result task", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to add notify application result task", e);
		}
	}
	

	@Override
	public void processTask() throws GenericEventServiceException {
		
		List<NotifyApplicationResultTask> tasks = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			tasks = dao.getPendingNotifyApplicationResultTasks();
					
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get pending notify application result task", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to get pending notify application result task", e);
		}
		
		for (NotifyApplicationResultTask task : tasks) {
			
			// Only try once.
			try {
				SessionFactoryUtil.beginTransaction();
				
				task.setCompleted(true);
				task.setPerformedOn(new Date ());
				
				dao.updateNotifyApplicationResultTask(task, "INTERNET");
						
				SessionFactoryUtil.commitTransaction();
			} 
			catch (Exception e) {
				logger.log(Level.SEVERE, "Fail to update notify application result task", e);
				SessionFactoryUtil.rollbackTransaction();
				continue;
			}
			
			try {
				emailSmsHelper.notifyApplicationResult(task.getEventId());
			}
			catch (GenericEventServiceException e) {
				logger.log(Level.SEVERE, "Fail to send application result notification", e);
			}
		}
	}

	@Override
	public void updateApplicationResult(String eventId, List<String> successApplications,
			List<String> unsuccessApplications, String requester) throws GenericEventServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			List<GenericEventApplication> applications = new ArrayList<>();
			
			for (String nric : successApplications) {
				GenericEventApplication application = dao.getGenericEventApplication(eventId, nric);
				
				//If application doesn't exist, log and skip
				if (application == null) {
					logger.log(Level.WARNING, "User " + Util.replaceNewLine(nric) + " has not register for event " + Util.replaceNewLine(eventId));
					continue;
				}
				
				application.setStatus(GEApplicationStatus.SUCCESSFUL);
				
				dao.updateGenericEventApplication(application, requester);
				applications.add(application);
			}
			
			for (String nric : unsuccessApplications) {
				GenericEventApplication application = dao.getGenericEventApplication(eventId, nric);
				
				//If application doesn't exist, log and skip
				if (application == null) {
					logger.log(Level.WARNING, "User " + Util.replaceNewLine(nric) + " has not register for event " + Util.replaceNewLine(eventId));
					continue;
				}
				
				application.setStatus(GEApplicationStatus.UNSUCCESSFUL);
				
				dao.updateGenericEventApplication(application, requester);
				applications.add(application);
			}		
					
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update application result task", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to update application result task", e);
		}
	}
	
	@Override
	public boolean activateEventWithAnnouncement(String eventId, String announcementId, String requester) throws GenericEventServiceException
	{
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			
			if (eventDetail.getStatus() == GenericEventStatus.ACTIVATED || eventDetail.getStatus() == GenericEventStatus.CANCELLED) {
				throw new GenericEventServiceException ("Only draft generic event can be activated.");
			}
				announcementService.setModule(announcementId, "GE", requester);
				
				boolean announcementResult = announcementService.activateAnnouncement(announcementId, requester);
				logger.log(Level.INFO, "activateEventWithAnnouncement, announcementResult >> ", announcementResult);
				if (announcementResult) {
					eventDetail.setStatus(GenericEventStatus.ACTIVATED);
					eventDetail.setAnnouncementId(announcementId);
					dao.updateGenericEventDetail(eventDetail, requester);
					
					Set<PersonalDetail> personalDetails = announcementService.getTargetedUsers(announcementId);
					List<GETargetedUser> targetedUsers = new ArrayList<>();
					
					for (PersonalDetail personalDetail : personalDetails) {
						GETargetedUser targetedUser = new GETargetedUser(null, personalDetail.getNric(), eventId);
						targetedUsers.add(targetedUser);
					}
					
					dao.saveTargetedUsers(targetedUsers, requester);
										
					result = true;
				}
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate generic + announcement event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to activate generic + announcement event", e);
		}
		return result;
	}
	
	@Override
	public List<GenericEventDetail> getEventDetailsForLinking(String status) throws GenericEventServiceException {
		logger.info("Calling get event details with status "+status);
		
		List<GenericEventDetail> geList = dao.getGenericEventDetails();
		
		List<GenericEventDetail> temp = new ArrayList<>();
		GenericEventDetail temp2 = null;
		for(int i = 0; i < geList.size(); i++)
		{
			temp2 = geList.get(i);
			if( status.equals(temp2.getStatus()) 
					&& temp2.getAnnouncementId() == "" 
					&& temp2.getRegistrationStartDate().compareTo(new Date()) >= 0
			)
			{
				temp.add(temp2);
				logger.info(" > Event "+temp2.getId()+" available for linking ");
			}
		}
		
		logger.info("get event details list size is " + temp.size());
		
		return temp;
	}


	@Override
	public List<String> getAnnouncementIdsOfUnit(List<String> units) throws GenericEventServiceException 
	{
		List<String> announcementIds = null;
		try
		{
			SessionFactoryUtil.beginTransaction();
			announcementIds = dao.getAnnouncementIdsOfUnit(units);
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to getAnnouncementIdsOfUnit() ", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to getAnnouncementIdsOfUnit() ", e);
		}
		
		return announcementIds;
	}


	@Override
	public void postpone(String eventId, Date eventStartDate,
			Date eventEndDate, String requester)
			throws GenericEventServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			if (eventDetail.getStatus() != GenericEventStatus.ACTIVATED) {
				throw new GenericEventServiceException ("Only activated generic event can be postponed.");
			}
			eventDetail.setEventStartDate(eventStartDate);
			eventDetail.setEventEndDate(eventEndDate);
			dao.updateGenericEventDetail(eventDetail, requester);
			
			emailSmsHelper.informOfPostpone(eventDetail);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to cancel generic event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to cancel generic event", e);
		}
		
		
	}

	
	public List<String> getUnitAnnouncementIds(String units) throws GenericEventServiceException {
		
		List<String> annoucementsIds = null;
		try{
			SessionFactoryUtil.beginTransaction();
			annoucementsIds = dao.getUnitAnnoucementIds(units);
			SessionFactoryUtil.commitTransaction();
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to getUnitAnnouncementIds ", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to getUnitAnnouncementIds ", e);
		}
		return annoucementsIds;
	}
	
	@Override
	public boolean activateEventForUnit(String eventId, String announcementId, String requester) throws GenericEventServiceException
	{
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			
			if (eventDetail.getStatus() == GenericEventStatus.ACTIVATED || eventDetail.getStatus() == GenericEventStatus.CANCELLED) {
				throw new GenericEventServiceException ("Only draft generic event can be activated.");
			}

				
			boolean announcementResult = announcementService.activateAnnouncementForUnit(announcementId, eventDetail.getSenderEmailAddress(), eventDetail.getSenderEmailPassword(), requester);
				
			if (announcementResult) {
				eventDetail.setStatus(GenericEventStatus.ACTIVATED);
				eventDetail.setAnnouncementId(announcementId);
				dao.updateGenericEventDetail(eventDetail, requester);
					
				Set<PersonalDetail> personalDetails = announcementService.getTargetedUsers(announcementId);
				List<GETargetedUser> targetedUsers = new ArrayList<>();
					
				for (PersonalDetail personalDetail : personalDetails) {
					GETargetedUser targetedUser = new GETargetedUser(null, personalDetail.getNric(), eventId);
					targetedUsers.add(targetedUser);
				}
					
				dao.saveTargetedUsers(targetedUsers, requester);
					
				result = true;
			}
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate generic + announcement event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new GenericEventServiceException ("Fail to activate generic + announcement event", e);
		}
		return result;
	}

}
