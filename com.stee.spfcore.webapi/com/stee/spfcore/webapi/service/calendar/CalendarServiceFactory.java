//package com.stee.spfcore.webapi.service.calendar;
//
//import com.stee.spfcore.webapi.service.calendar.impl.BPMCalendarService;
//import com.stee.spfcore.webapi.service.calendar.impl.FEBCalendarService;
//import com.stee.spfcore.webapi.utils.EnvironmentUtils;
//
//public class CalendarServiceFactory {
//	
//	private static ICalendarService instance;
//	
//	public static synchronized ICalendarService getInstance () {
//		
//		if (instance == null) {
//			instance = createInstance ();
//		}
//		return instance;
//	}
//	
//	private static ICalendarService createInstance () {
//		
//		if (EnvironmentUtils.isInternet()) {
//			return new FEBCalendarService();
//		}
//		else {
//			return new BPMCalendarService();
//		}
//	} 
//}
