package com.stee.spfcore.service.survey.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.survey.Response;
import com.stee.spfcore.model.survey.Survey;
import com.stee.spfcore.model.survey.SurveyStatus;
import com.stee.spfcore.model.survey.SurveyTask;
import com.stee.spfcore.service.survey.SurveyServiceException;

public class SurveyService extends AbstractSurveyService {

	private BroadcastHelper broadcastHelper;
	
	public SurveyService () {
		super ();
		
		broadcastHelper = new BroadcastHelper();
	}
	
	
	@Override
	public String addSurvey(Survey survey, String requester) throws SurveyServiceException {
		String id = null;
		try {
			SessionFactoryUtil.beginTransaction();
			id = dao.addSurvey(survey, requester);
			
			if (survey.getStatus() == SurveyStatus.ACTIVATED) {
				dateCheck (survey);
			}
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add survey", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to add survey", e);
		}
		
		return id;
	}

	@Override
	public void updateSurvey(Survey survey, String requester) throws SurveyServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateSurvey(survey, requester);
			
			if (survey.getStatus() == SurveyStatus.ACTIVATED) {
				dateCheck (survey);
			}
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update Survey", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to update Survey", e);
		}
	}

	@Override
	public void deleteSurvey(String surveyId, String requester) throws SurveyServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			
			Survey survey = dao.getSurvey(surveyId);
			
			if (survey.getStatus() != SurveyStatus.DRAFT) {
				throw new SurveyServiceException ("Only draft survey can be deleted.");
			}
			
			dao.deleteSurvey(survey, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to delete Survey", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to delete Survey", e);
		}
	}


	private void dateCheck (Survey survey) throws SurveyServiceException {
		Date publishDate = survey.getPublishDate();
		
		if (publishDate == null) {
			throw new SurveyServiceException("Publish Date for survey cannot be null.");
		}
		
		Date now = new Date ();
		
		if (now.after(publishDate)) {
			throw new SurveyServiceException("Publish Date cannot be past date.");
		}
	}
	
	
	@Override
	public String addResponse(Response response, String requester) throws SurveyServiceException {
		throw new UnsupportedOperationException ("Only Internet side is allow to add Survey Response.");
	}

	@Override
	public void updateResponse(Response response, String requester) throws SurveyServiceException {
		throw new UnsupportedOperationException ("Only Internet side is allow to update Survey Response.");
	}

	@Override
	public void addSurveyTask(SurveyTask task, String requester) throws SurveyServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.addSurveyTask(task, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add survey task", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to add survey task", e);
		}
	}

	@Override
	public void updateSurveyTask(SurveyTask task, String requester) throws SurveyServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateSurveyTask (task, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update Survey task", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to update Survey task", e);
		}
	}

	
	@Override
	public void processTask() throws SurveyServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			
			List<Survey> surveys = dao.getPendingBroadcastSurveys ();
			
			for (Survey survey : surveys) {
				broadcastHelper.broadcast(survey);
			}
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to broadcast Survey", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to broadcast Survey", e);
		}
	}


	@Override
	public void addSurveyTasks(String surveyId, List<String> nricList, String requester) throws SurveyServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			List<String> existingNricList = dao.getSurveyTaskUsers(surveyId);
			
			// Remove existing nric from the list
			nricList.removeAll(existingNricList);
			
			List<SurveyTask> tasks = new ArrayList<>();
			
			for (String nric : nricList) {
				SurveyTask task = new SurveyTask(null, nric, surveyId, false, new Date ());
				tasks.add(task);
			}
			
			dao.addSurveyTasks (tasks, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to broadcast Survey", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to broadcast Survey", e);
		}
	}


	@Override
	public boolean activateSurvey (String surveyId, String requester) throws SurveyServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Survey survey = dao.getSurvey(surveyId);
			
			if (survey.getStatus() != SurveyStatus.DRAFT) {
				throw new SurveyServiceException("Only draft survey can be activated.");
			}
			
			dateCheck (survey);
			
			survey.setStatus(SurveyStatus.ACTIVATED);
			
			dao.updateSurvey(survey, requester);
			
			SessionFactoryUtil.commitTransaction();
			
			result = true;
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to activate Survey", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}


	@Override
	public boolean cancelSurvey (String surveyId, String requester) throws SurveyServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			Survey survey = dao.getSurvey(surveyId);
			
			if (survey.getStatus() != SurveyStatus.ACTIVATED) {
				throw new SurveyServiceException("Only activated survey can be cancel.");
			}
			
			survey.setStatus(SurveyStatus.CANCELLED);
			
			dao.updateSurvey(survey, requester);
			
			SessionFactoryUtil.commitTransaction();
			
			result = true;
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to cancel Survey", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	
	
}
