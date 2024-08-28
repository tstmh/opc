package com.stee.spfcore.webapi.rest;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.webapi.utils.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.survey.Response;
import com.stee.spfcore.webapi.model.survey.Survey;
import com.stee.spfcore.webapi.model.survey.SurveyTask;
import com.stee.spfcore.webapi.service.SurveyService;

@RestController
@RequestMapping("/survey")
public class SurveyController {

	private SurveyService service;
	
	@Autowired
	public SurveyController(SurveyService service) {
		this.service = service;
	}
	
	@GetMapping("/getSurvey")
	public Survey getSurvey (@RequestParam String id){
		Survey survey = service.getSurvey(id);
		return survey;
	}
	
//	@GetMapping("/getPendingSurveyTasks")
//	public List<SurveyTask> getPendingSurveyTasks (@RequestParam String nric){
//		List<SurveyTask> surveyTasks = service.getPendingSurveyTasks(nric);
//		return surveyTasks;
//	}
@GetMapping("/getPendingSurveyTasks")
public List<SurveyTask> getPendingSurveyTasks (@RequestParam String nric, @RequestParam(required = false) String date){
	if ( date != null && !date.trim().isEmpty() ) {
		Date newDate = null;
		newDate = ConvertUtil.convertFebDateControlStringToDate( date );
		List<SurveyTask> surveyTasks = service.getPendingSurveyTasks(nric, newDate);
		return surveyTasks;
	}
	List<SurveyTask> surveyTasks = service.getPendingSurveyTasks(nric);
	return surveyTasks;
}

	@GetMapping("/getUserSurveyTask")
	public SurveyTask getUserSurveyTask (@RequestParam String surveyId, String nric) {
		SurveyTask surveyTask = service.getUserSurveyTask(surveyId, nric);
		return surveyTask;
	}
	
	@PostMapping("/updateSurveyTask")
	public void updateSurveyTask (@RequestHeader("requester") String requester, @RequestBody SurveyTask task) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateSurveyTask(task);
	}
	
	@PostMapping("/addResponse")
	public String addResponse (@RequestHeader("requester") String requester, @RequestBody Response response) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		String output = service.addResponse(response);
		return output;
	}
	
}
