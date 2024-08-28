package com.stee.spfcore.webapi.service.calendar;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.webapi.model.Module;
import com.stee.spfcore.webapi.model.calendar.MarketingEvent;
import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.personnel.Leave;

public interface ICalendarService {
	
	public String addPublicHoliday (PublicHoliday holiday, String requester) throws CalendarServiceException;
	
	public void addPublicHolidays (List<PublicHoliday> holidays, String requester) throws CalendarServiceException;
	
	public void updatePublicHoliday(PublicHoliday holiday, String requester) throws CalendarServiceException;
	
	public void deletePublicHoliday(PublicHoliday holiday, String requester) throws CalendarServiceException;
	
	public void deletePublicHoliday(String id, String requester) throws CalendarServiceException;
	
	public PublicHoliday getPublicHoliday (String id) throws CalendarServiceException;
	
	public List<PublicHoliday> getPublicHolidays(int month, int year) throws CalendarServiceException;
	
	public List<PublicHoliday> getPublicHolidays(int year) throws CalendarServiceException;
	
	public List<PublicHoliday> getPublicHolidays(Date startDate, Date endDate) throws CalendarServiceException;
	
	public List<PublicHoliday> getPublicHolidays(Date date) throws CalendarServiceException;
	
	public List<Leave> getUserLeaves (String nric, int month, int year) throws CalendarServiceException;
	
	public List<Leave> getUserLeaves (String nric, Date startDate, Date endDate) throws CalendarServiceException;
	
	public String addMarketingEvent (MarketingEvent event, String requester) throws CalendarServiceException;
	
	public void updateMarketingEvent (MarketingEvent event, String requester) throws CalendarServiceException;
	
	public void deleteMarketingEvent (MarketingEvent event, String requester) throws CalendarServiceException;
	
	public MarketingEvent getMarketingEvent (String nric, Module module, String referenceId) throws CalendarServiceException;
	
	public MarketingEvent getMarketingEvent (String id) throws CalendarServiceException;
	
	public List<MarketingEvent> getMarketingEvents (String nric, int month, int year) throws CalendarServiceException;

	public List<PublicHoliday> getPublicHolidaysForCorporateCard(Date date) throws CalendarServiceException;

	
	
}
