package com.stee.spfcore.webapi.rest;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.calendar.MarketingEvent;
import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.personnel.Leave;
import com.stee.spfcore.webapi.service.CalendarService;
import com.stee.spfcore.webapi.utils.ConvertUtil;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

	private CalendarService service;
	
	@Autowired
	public CalendarController(CalendarService service) {
		this.service = service;
	}
	
	@GetMapping("/getPublicHolidays")
	public List<PublicHoliday> getPublicHolidays(@RequestParam String date) {
		
		Date newDate = null;
		if ( date != null && !date.trim().isEmpty() ) {
			newDate = ConvertUtil.convertFebDateControlStringToDate( date );
		}
		
		List<PublicHoliday> publicHolidays = service.getPublicHolidays(newDate); 
		return publicHolidays;	
	}
	
	@GetMapping("/getPublicHolidaysFromStartEnd")
	public List<PublicHoliday> getPublicHolidays(@RequestParam String startDate, String endDate) {
		
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
		}
		
		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
		}
		
		List<PublicHoliday> publicHolidays = service.getPublicHolidays(newStartDate, newEndDate); 
		return publicHolidays;	
	}
	
	@GetMapping("/getUserLeaves")
	public List < Leave > getUserLeaves(@RequestParam String nric, String startDate, String endDate) {
		Date newStartDate = null;
		if ( startDate != null && !startDate.trim().isEmpty() ) {
			newStartDate = ConvertUtil.convertFebDateControlStringToDate( startDate );
		}
		Date newEndDate = null;
		if ( endDate != null && !endDate.trim().isEmpty() ) {
			newEndDate = ConvertUtil.convertFebDateControlStringToDate( endDate );
		}
		List < Leave > userLeaves = service.getUserLeaves(nric, newStartDate, newEndDate); 
		return userLeaves;	
	}
	
	@GetMapping("/getMarketingEvents")
	public List < MarketingEvent > getMarketingEvents(@RequestParam String nric, int month, int year) {
		List < MarketingEvent > marketingEvents = service.getMarketingEvents(nric, month, year); 
		return marketingEvents;	
	}
	
	
}
