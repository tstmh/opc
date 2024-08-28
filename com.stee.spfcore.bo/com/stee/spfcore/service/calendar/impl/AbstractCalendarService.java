package com.stee.spfcore.service.calendar.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.CalendarDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.Module;
import com.stee.spfcore.model.calendar.MarketingEvent;
import com.stee.spfcore.model.calendar.PublicHoliday;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.service.calendar.CalendarServiceException;
import com.stee.spfcore.service.calendar.ICalendarService;

public abstract class AbstractCalendarService implements ICalendarService {

	protected static final Logger logger = Logger.getLogger(AbstractCalendarService.class.getName());
	
	protected CalendarDAO dao;
	
	protected AbstractCalendarService() {
		dao = new CalendarDAO();
	}
			
			
	@Override
	public List<PublicHoliday> getPublicHolidays(int month, int year) throws CalendarServiceException {
		List<PublicHoliday> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getPublicHolidays(month, year);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get public holidays", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	@Override
	public List<PublicHoliday> getPublicHolidays(int year) throws CalendarServiceException {
		List<PublicHoliday> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getPublicHolidays(year);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get public holidays", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	public PublicHoliday getPublicHoliday (String id) throws CalendarServiceException {
		
		PublicHoliday result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getPublicHoliday(id);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get public holidays", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	@Override
	public List<PublicHoliday> getPublicHolidays(Date startDate, Date endDate) throws CalendarServiceException {
		List<PublicHoliday> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getPublicHolidays(startDate, endDate);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get public holidays", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	   @Override
	    public List<PublicHoliday> getPublicHolidays(Date date) throws CalendarServiceException {
	        List<PublicHoliday> result = null;
	        
	        try {
	            SessionFactoryUtil.beginTransaction();
	            result = dao.getPublicHolidays(date);
	            
	            SessionFactoryUtil.commitTransaction();
	        } 
	        catch (Exception e) {
	            logger.log(Level.SEVERE, "Fail to get public holidays", e);
	            SessionFactoryUtil.rollbackTransaction();
	        }
	        
	        return result;
	    }

	@Override
	public List<Leave> getUserLeaves(String nric, int month, int year) throws CalendarServiceException {
		List<Leave> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getUserLeaves(nric, month, year);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get user leaves", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	@Override
	public List<Leave> getUserLeaves(String nric, Date startDate, Date endDate) throws CalendarServiceException {
		List<Leave> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getUserLeaves(nric, startDate, endDate);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get user leaves", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	public MarketingEvent getMarketingEvent (String nric, Module module, String referenceId) throws CalendarServiceException {
		
		MarketingEvent result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingEvent(nric, module, referenceId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get marketing event", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	public MarketingEvent getMarketingEvent (String id) throws CalendarServiceException {
		
		MarketingEvent result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingEvent(id);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get marketing event", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	@Override
	public List<MarketingEvent> getMarketingEvents(String nric, int month, int year) throws CalendarServiceException {
		
		List<MarketingEvent> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingEvents(nric, month, year);
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get marketing events", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	

}
