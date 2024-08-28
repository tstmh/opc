package com.stee.spfcore.service.calendar.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.calendar.MarketingEvent;
import com.stee.spfcore.model.calendar.PublicHoliday;
import com.stee.spfcore.service.calendar.CalendarServiceException;

public class CalendarService extends AbstractCalendarService {

	@Override
	public String addPublicHoliday(PublicHoliday holiday, String requester) throws CalendarServiceException {
		
		String id = null;
		try {
			SessionFactoryUtil.beginTransaction();
			id = dao.addPublicHoliday(holiday, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add public holiday", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to add public holiday", e);
		}
		
		return id;
	}
	
	@Override
	public void addPublicHolidays (List<PublicHoliday> holidays, String requester) throws CalendarServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			for (PublicHoliday holiday : holidays) {
				dao.addPublicHoliday(holiday, requester);
			}
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add public holidays", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to add public holidays", e);
		}
	}

	@Override
	public void updatePublicHoliday(PublicHoliday holiday, String requester) throws CalendarServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updatePublicHoliday(holiday, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update public holiday", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to update public holiday", e);
		}
	}

	@Override
	public void deletePublicHoliday (PublicHoliday holiday, String requester) throws CalendarServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.deletePublicHoliday(holiday, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete public holiday", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to delete public holiday", e);
		}
	}
	
	@Override
	public void deletePublicHoliday (String id, String requester) throws CalendarServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			
			PublicHoliday holiday = dao.getPublicHoliday(id);
			dao.deletePublicHoliday(holiday, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete public holiday", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to delete public holiday", e);
		}
	}

	@Override
	public String addMarketingEvent(MarketingEvent event, String requester) throws CalendarServiceException {
		String id = null;
		try {
			SessionFactoryUtil.beginTransaction();
			id = dao.addMarketingEvent(event, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add marketing event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to add marketing event", e);
		}
		
		return id;
	}

	@Override
	public void updateMarketingEvent(MarketingEvent event, String requester) throws CalendarServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateMarketingEvent(event, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update marketing event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to update marketing event", e);
		}
	}

	@Override
	public void deleteMarketingEvent(MarketingEvent event, String requester) throws CalendarServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.deleteMarketingEvent(event, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete marketing event", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CalendarServiceException ("Fail to delete marketing event", e);
		}
	}
	

	public List<PublicHoliday> getPublicHolidaysForCorporateCard(Date date) throws CalendarServiceException {
        List<PublicHoliday> result = null;
        logger.info("inside getPublicHolidaysForCorporateCard");
        try {
            SessionFactoryUtil.getCurrentSession();
            result = dao.getPublicHolidays(date);
        } 
        catch (Exception e) {
            logger.log(Level.SEVERE, "Fail to get public holidays", e);
            SessionFactoryUtil.rollbackTransaction();
            throw new CalendarServiceException ("Fail to get public holidays", e);
        }
        
        return result;
    }
	
	
	
}
