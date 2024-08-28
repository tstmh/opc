package com.stee.spfcore.service.calendar;

import com.stee.spfcore.service.calendar.impl.CalendarService;

public class CalendarServiceFactory {

	private CalendarServiceFactory(){}
	private static ICalendarService instance;
	
	public static synchronized ICalendarService getInstance () {
		
		if (instance == null) {
			instance = createInstance ();
		}
		return instance;
	}
	
	private static ICalendarService createInstance () {
		
		return new CalendarService();
	} 
}
