package com.stee.spfcore.service.survey.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.SurveyDAO;
import com.stee.spfcore.model.survey.Response;
import com.stee.spfcore.model.survey.Survey;
import com.stee.spfcore.model.survey.SurveyTask;
import com.stee.spfcore.service.survey.ISurveyService;
import com.stee.spfcore.service.survey.SurveyServiceException;
import com.stee.spfcore.utils.Util;

public abstract class AbstractSurveyService implements ISurveyService {

	protected static final Logger logger = Logger.getLogger(AbstractSurveyService.class.getName());
	
	protected SurveyDAO dao;
	
	protected AbstractSurveyService() {
		dao = new SurveyDAO();
	}
	
	@Override
	public int getSurveyCount() throws SurveyServiceException {
		
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveyCount();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to get survey count", e);
		}
		
		return result;
	}

	@Override
	public int getSurveyCount(String module) throws SurveyServiceException {
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveyCount(module);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to get survey count", e);
		}
		
		return result;
	}

	@Override
	public List<Survey> getSurveys() throws SurveyServiceException {
		List<Survey> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveys();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to get surveys", e);
		}
		
		return result;
	}

	@Override
	public List<Survey> getSurveys(String module) throws SurveyServiceException {
		List<Survey> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveys(module);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to get surveys", e);
		}
		
		return result;
	}

	@Override
	public List<Survey> getSurveys(int pageNum, int pageSize) throws SurveyServiceException {
		List<Survey> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveys(pageNum, pageSize);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to get surveys", e);
		}
		
		return result;
	}

	@Override
	public List<Survey> getSurveys(int pageNum, int pageSize, String module) throws SurveyServiceException {
		List<Survey> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveys(pageNum, pageSize, module);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			SessionFactoryUtil.rollbackTransaction();
			throw new SurveyServiceException ("Fail to get surveys", e);
		}
		
		return result;
	}

	@Override
	public Survey getSurvey(String id) throws SurveyServiceException {
		Survey result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurvey (id);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get survey %s", e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public int getResponseCount(String surveyId) throws SurveyServiceException {
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getResponseCount(surveyId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get response count %s", e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Response> getResponses(String surveyId) throws SurveyServiceException {
		List<Response> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getResponses(surveyId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get responses %s", e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Response> getResponses(int pageNum, int pageSize, String surveyId) throws SurveyServiceException {
		List<Response> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getResponses(pageNum, pageSize, surveyId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get responses %s", e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public Response getResponse(String id) throws SurveyServiceException {
		Response result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getResponse (id);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get response %s", e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public Response getResponse(String nric, String surveyId) throws SurveyServiceException {
		Response result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getResponse (nric, surveyId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get response %s", e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<SurveyTask> getPendingSurveyTask(String nric) throws SurveyServiceException {
		
		List<SurveyTask> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getPendingSurveyTasks(nric);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get pending survey task for user: %s %s",nric, e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public int getSurveyTaskCount(String surveyId) throws SurveyServiceException {
		
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSurveyTaskCount (surveyId);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to get survey task count:%s %s", Util.replaceNewLine(surveyId), e));
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	
}
