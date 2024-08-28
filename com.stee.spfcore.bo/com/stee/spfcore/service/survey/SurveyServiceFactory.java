package com.stee.spfcore.service.survey;

import com.stee.spfcore.service.survey.impl.SurveyService;

public class SurveyServiceFactory {

	private SurveyServiceFactory(){}
	private static ISurveyService service;
	
	public static synchronized ISurveyService getSurveyService () {
		if (service == null) {
			service = createSurveyService ();
		}
		return service;
	}
	
	private static ISurveyService createSurveyService () {
	
		return new SurveyService();
		
	}
	
}
