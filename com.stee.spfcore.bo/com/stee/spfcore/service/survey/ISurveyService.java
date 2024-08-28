package com.stee.spfcore.service.survey;

import java.util.List;

import com.stee.spfcore.model.survey.Response;
import com.stee.spfcore.model.survey.Survey;
import com.stee.spfcore.model.survey.SurveyTask;

public interface ISurveyService {
	
	public String addSurvey (Survey survey, String requester) throws SurveyServiceException;
	
	public void updateSurvey (Survey survey, String requester) throws SurveyServiceException;
	
	public void deleteSurvey (String surveyId, String requester) throws SurveyServiceException;
	
	public int getSurveyCount () throws SurveyServiceException;
	
	public int getSurveyCount (String module) throws SurveyServiceException;
		
	public List<Survey> getSurveys () throws SurveyServiceException;
		
	public List<Survey> getSurveys (String module) throws SurveyServiceException;
	
	public List<Survey> getSurveys (int pageNum, int pageSize) throws SurveyServiceException;
		
	public List<Survey> getSurveys (int pageNum, int pageSize, String module) throws SurveyServiceException;
	
	public Survey getSurvey (String id) throws SurveyServiceException;
		
	public String addResponse (Response response, String requester) throws SurveyServiceException;
	
	public void updateResponse (Response response, String requester) throws SurveyServiceException;
		
	public int getResponseCount (String surveyId) throws SurveyServiceException;
	
	public List<Response> getResponses (String surveyId) throws SurveyServiceException;
		
	public List<Response> getResponses (int pageNum, int pageSize, String surveyId) throws SurveyServiceException;
		
	public Response getResponse (String id) throws SurveyServiceException;
		
	public Response getResponse (String nric, String surveyId) throws SurveyServiceException;
	
	public void addSurveyTask (SurveyTask task, String requester) throws SurveyServiceException;
	
	public void updateSurveyTask (SurveyTask task, String requester) throws SurveyServiceException;
	
	public List<SurveyTask> getPendingSurveyTask (String nric) throws SurveyServiceException;
	
	public void processTask () throws SurveyServiceException;
	
	public void addSurveyTasks (String surveyId, List<String> nricList, String requester) throws SurveyServiceException;
	
	public int getSurveyTaskCount (String surveyId) throws SurveyServiceException;
	
	public boolean activateSurvey (String surveyId, String requester) throws SurveyServiceException;
	
	public boolean cancelSurvey (String surveyId, String requester) throws SurveyServiceException;
}
