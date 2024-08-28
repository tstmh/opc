package com.stee.spfcore.webapi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.CalendarDAO;
import com.stee.spfcore.webapi.model.calendar.MarketingEvent;
import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.personnel.Leave;

@Service
public class CalendarService {

	private CalendarDAO calendarDAO;
	
	@Autowired
	public CalendarService ( CalendarDAO calendarDAO ) {
		this.calendarDAO = calendarDAO;
	}
	
	@Transactional
	public List<PublicHoliday> getPublicHolidays (Date date) {
		return calendarDAO.getPublicHolidays(date);
	}
	
	@Transactional
	public List<PublicHoliday> getPublicHolidays(Date startDate, Date endDate) {
		return calendarDAO.getPublicHolidays(startDate, endDate);
	}
	
	@Transactional
	public List<Leave> getUserLeaves(String nric, Date startDate, Date endDate) {
		return calendarDAO.getUserLeaves(nric, startDate, endDate);
	}

	@Transactional
	public List<MarketingEvent> getMarketingEvents(String nric, int month, int year) {
		return calendarDAO.getMarketingEvents(nric, month, year);
	}



}
