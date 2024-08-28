package com.stee.spfcore.service.genericEvent.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.GenericEventDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.genericEvent.GETicketingChoice;
import com.stee.spfcore.model.genericEvent.GETicketingOption;
import com.stee.spfcore.model.genericEvent.GETicketingSection;
import com.stee.spfcore.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.service.genericEvent.GenericEventServiceException;
import com.stee.spfcore.service.genericEvent.IGenericEventService;
import com.stee.spfcore.vo.genericEvent.UserChoice;
import com.stee.spfcore.vo.genericEvent.UserEventApplicationDetail;

public abstract class AbstractGenericEventService implements IGenericEventService {

	protected static final Logger logger = Logger.getLogger(AbstractGenericEventService.class.getName());
	
	protected GenericEventDAO dao;
	
	protected AbstractGenericEventService () {
		dao = new GenericEventDAO();
	}

	@Override
	public GenericEventDetail getEventDetail(String id) throws GenericEventServiceException {
		GenericEventDetail result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getGenericEventDetail(id);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventDetail", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<GenericEventDetail> getEventDetails() throws GenericEventServiceException {
		List<GenericEventDetail> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getGenericEventDetails ();
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventDetails", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<GenericEventApplication> getEventApplications(String nric, Date startDate, Date endDate)
			throws GenericEventServiceException {
		
		List<GenericEventApplication> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getGenericEventApplications (nric, startDate, endDate);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventApplication", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<GenericEventDetail> getEventPendingRegistration(String nric) throws GenericEventServiceException {
		
		List<GenericEventDetail> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getGenericEventPendingRegistration(nric);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEvent pending registration", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public GenericEventApplication getEventApplication(String eventId, String nric) throws GenericEventServiceException {
		
		GenericEventApplication result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getGenericEventApplication (eventId, nric);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventApplication", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public boolean isTargetedUser(String eventId, String nric) throws GenericEventServiceException {
		
		boolean result = false;

		try {
			SessionFactoryUtil.beginTransaction();
			
			result = dao.isTargetedUser (eventId, nric);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to check if user is targeted user", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<GenericEventApplication> getEventApplications(String eventId) throws GenericEventServiceException {
		
		List<GenericEventApplication> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getGenericEventApplications(eventId);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventApplication", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public List<UserEventApplicationDetail> getUserEventApplications(String eventId) throws GenericEventServiceException {
		
		List<UserEventApplicationDetail> result = new ArrayList<>();
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			GenericEventDetail eventDetail = dao.getGenericEventDetail(eventId);
			List<GenericEventApplication> eventApplications = dao.getGenericEventApplications(eventId);
			
			for (GenericEventApplication application : eventApplications) {
				
				UserEventApplicationDetail applicationDetail = new UserEventApplicationDetail();
				
				applicationDetail.setNric(application.getNric());
				applicationDetail.setDepartment(application.getDepartment());
				applicationDetail.setName(application.getName());
				applicationDetail.setServiceType(application.getServiceType());
				applicationDetail.setStatus(application.getStatus().toString());
				applicationDetail.setRegisteredOn(application.getRegisteredOn());
				
				List<UserChoice> userChoices = new ArrayList<>();
				applicationDetail.setUserChoices(userChoices);
				
				Map<String, Integer> choiceMap = getChoiceMap (application.getChoices());
				for (GETicketingSection section : eventDetail.getTicketingSections()) {
					for (GETicketingOption option : section.getTicketingOption()) {
						UserChoice choice = new UserChoice();
						choice.setOptionId(option.getId());
						choice.setTitle(section.getName()+" - "+option.getTitle());
						
						double unitCost = option.getUnitCost();
						int quantity = 0;
						if (choiceMap.containsKey(option.getId())) {
							quantity = choiceMap.get(option.getId());
						}
						
						choice.setQuantity(quantity);
						choice.setCost(quantity * unitCost);
						
						userChoices.add(choice);
					}
					
					
				}
				
				result.add(applicationDetail);
			}
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventApplication", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
		
	}
	
	
	private Map<String,Integer> getChoiceMap (List<GETicketingChoice> ticketingChoices) {
		
		Map<String,Integer> result = new HashMap<>();
		
		for (GETicketingChoice choice : ticketingChoices) {
			result.put(choice.getOptionId(), choice.getValue());
		}
		
		return result;
	}
	@Override
	public List<GenericEventDetail> getUnitGenericEventDetail (String isMainDepartment, String unit) throws GenericEventServiceException {
		
		List<GenericEventDetail> result = null;
		logger.info("getUnitGenericEventDetail ");
		try {
			logger.info("try getUnitGenericEventDetail ");
			SessionFactoryUtil.beginTransaction();
			result = dao.getUnitGenericEventDetails(isMainDepartment, unit);
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get GenericEventDetail", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}
	
	@Override
	public List<GenericEventDetail> getPcwfEventDetails() throws GenericEventServiceException {
		
		List<GenericEventDetail> result = null;
		try {
			
			SessionFactoryUtil.beginTransaction();
			result = dao.getPcwfEventDetails();
			
			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get PCWFGenericEventDetail", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}
	
	
	
	
}
