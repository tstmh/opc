package com.stee.spfcore.webapi.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.SurveyDAO;
import com.stee.spfcore.webapi.model.survey.Response;
import com.stee.spfcore.webapi.model.survey.Survey;
import com.stee.spfcore.webapi.model.survey.SurveyTask;

@Service
public class SurveyService {

	private SurveyDAO surveyDAO;
	
	@Autowired
	public SurveyService (SurveyDAO surveyDAO) {
		this.surveyDAO = surveyDAO;
	}
	
	@Transactional
	public Survey getSurvey (String id){
		return surveyDAO.getSurvey(id);
	}
	
	@Transactional
	public List<SurveyTask> getPendingSurveyTasks (String nric){
		return surveyDAO.getPendingSurveyTasks(nric);
	}

	@Transactional
	public List<SurveyTask> getPendingSurveyTasks (String nric, Date date){
		return surveyDAO.getPendingSurveyTasks(nric, date);
	}
	
	@Transactional
	public SurveyTask getUserSurveyTask (String surveyId, String nric) {
		return surveyDAO.getUserSurveyTask(surveyId, nric);
	}
	
	@Transactional
	public void updateSurveyTask (SurveyTask task) {
		surveyDAO.updateSurveyTask(task);
	}
	
	@Transactional
	public String addResponse (Response response){
		return surveyDAO.addResponse(response);
	}
	
	
}
