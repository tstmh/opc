package com.stee.spfcore.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.survey.Response;
import com.stee.spfcore.model.survey.Survey;
import com.stee.spfcore.model.survey.SurveyStatus;
import com.stee.spfcore.model.survey.SurveyTask;
import com.stee.spfcore.model.survey.internal.SurveyBroadcastLog;

public class SurveyDAO {

	private static final Logger logger = Logger.getLogger( SurveyDAO.class.getName() );
	
	public String addSurvey (Survey survey, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(survey);

		session.flush();
		
		return survey.getId();
	}

	
	public void updateSurvey (Survey survey, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(survey);

		session.flush();
	}
	
	public void deleteSurvey (Survey survey, String requester) {
		
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.delete(survey);
		
		session.flush();
	}
	
	
	public int getSurveyCount () {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("SELECT count(v) FROM Survey v");

		Query query = session.createQuery(builder.toString());

		return ((Number) query.uniqueResult()).intValue();
	}

	public int getSurveyCount (String module) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("SELECT count(v) FROM Survey v where v.module like :module");

		Query query = session.createQuery(builder.toString());
		query.setParameter("module", module);

		return ((Number) query.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> getSurveys () {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Survey v");

		Query query = session.createQuery(builder.toString());

		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> getSurveys (String module) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Survey v where v.module like :module");

		Query query = session.createQuery(builder.toString());
		query.setParameter("module", module);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> getSurveys (int pageNum, int pageSize) {
		
		int index = pageSize * (pageNum - 1);
		
		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Survey v");

		Query query = session.createQuery(builder.toString());
		query.setFirstResult(index);
		query.setMaxResults(pageSize);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Survey> getSurveys (int pageNum, int pageSize, String module) {
		
		int index = pageSize * (pageNum - 1);
		
		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Survey v where v.module like :module");

		Query query = session.createQuery(builder.toString());
		query.setParameter("module", module);
		query.setFirstResult(index);
		query.setMaxResults(pageSize);
		
		return query.list();
	}
	
	public Survey getSurvey (String id) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (Survey) session.get(Survey.class, id);
	}
	
	public String addResponse (Response response, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(response);

		session.flush();
		
		return response.getId();
	}

	
	public void updateResponse (Response response, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(response);

		session.flush();
	}
	
	public int getResponseCount (String surveyId) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("SELECT count(v) FROM Response v where v.surveyId = :surveyId");

		Query query = session.createQuery(builder.toString());
		query.setParameter("surveyId", surveyId);
		
		return ((Number) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Response> getResponses (String surveyId) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Response v where v.surveyId = :surveyId");

		Query query = session.createQuery(builder.toString());
		query.setParameter("surveyId", surveyId);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Response> getResponses (int pageNum, int pageSize, String surveyId) {
		
		int index = pageSize * (pageNum - 1);
		
		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Response v where v.surveyId = :surveyId");

		Query query = session.createQuery(builder.toString());
		query.setParameter("surveyId", surveyId);
		
		query.setFirstResult(index);
		query.setMaxResults(pageSize);
		
		return query.list();
	}
	
	public Response getResponse (String id) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (Response) session.get(Response.class, id);
	}
	
	public Response getResponse (String nric, String surveyId) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder builder = new StringBuilder("FROM Response v where v.surveyId = :surveyId and v.nric = :nric");
		Query query = session.createQuery(builder.toString());
		query.setMaxResults(1);
		
		query.setParameter("surveyId", surveyId);
		query.setParameter("nric", nric);
		
		List<?> list = query.list();
		if ( !list.isEmpty() ) {
			return (Response) list.get( 0 );
		} else {
			return null;
		}
	}
	
	
	public void addSurveyTask (SurveyTask task, String requester) {
		
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(task);

		session.flush();
	}
	
	
	public void addSurveyTasks (List<SurveyTask> tasks, String requester) {
		
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		for (int i = 0; i < tasks.size() ; i++) {
			SurveyTask task = tasks.get(i);
			session.save(task);
			
			if ( i % 20 == 0 ) { //20, same as the JDBC batch size
        //flush a batch of inserts and release memory:
        session.flush();
        session.clear();
			}
		}
		
		session.flush();
	}

	
	public void updateSurveyTask (SurveyTask task, String requester) {
		
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(task);

		session.flush();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> getSurveyTaskUsers (String surveyId) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder builder = new StringBuilder("select t.nric from SurveyTask t where t.surveyId = :surveyId");
		
		Query query = session.createQuery(builder.toString());
		query.setParameter("surveyId", surveyId);
		
		return query.list();
	} 
	
	
	@SuppressWarnings("unchecked")
	public List<SurveyTask> getPendingSurveyTasks (String nric) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder builder = new StringBuilder("FROM SurveyTask v where v.nric = :nric and v.completed = false");
		builder.append(" and v.surveyId in (select s.id from Survey s where s.status = :status and s.publishDate < :dateTime and s.endDate > :dateTime)");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("getPendingSurveyTasks: %s", builder));
		}
		Query query = session.createQuery(builder.toString());
		query.setParameter("nric", nric);
		query.setParameter("status", SurveyStatus.ACTIVATED);
		query.setParameter("dateTime", new Date ());
	
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonalDetail> getPendingSurveyUsers (String surveyId) {
	
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder builder = new StringBuilder("select p FROM PersonalDetail p ");
		builder.append("where p.nric in (select t.nric from SurveyTask t where t.surveyId = :surveyId and t.completed = false)");
		
		Query query = session.createQuery(builder.toString());
		query.setParameter("surveyId", surveyId);
		
		return query.list();
	}
	
	
	public SurveyTask getUserSurveyTask (String surveyId, String nric) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		StringBuilder builder = new StringBuilder("FROM SurveyTask v where v.nric = :nric and v.surveyId = :surveyId");
		
		Query query = session.createQuery(builder.toString());
		query.setParameter("nric", nric);
		query.setParameter("surveyId", surveyId);
		
		List<?> list = query.list();
		if ( !list.isEmpty() ) {
			return (SurveyTask) list.get( 0 );
		} else {
			return null;
		}
	}
	
	
	public void addSurveyBroadcastLog (SurveyBroadcastLog log) {
		
		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(log);

		session.flush();
	}
	
	public void updateSurveyBroadcastLog (SurveyBroadcastLog log) {
		
		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(log);

		session.flush();
	}
	
	
	public SurveyBroadcastLog getSurveyBroadcastLog (String broadcastId) {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		return (SurveyBroadcastLog) session.get(SurveyBroadcastLog.class, broadcastId);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Survey> getPendingBroadcastSurveys () {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		StringBuilder builder = new StringBuilder("select distinct s from Survey as s inner join s.broadcasts as b ");
		builder.append("where s.status = :status and s.publishDate < :dateTime and b.id not in ");
		builder.append("(select l.broadcastId from SurveyBroadcastLog l where l.broadcastId = b.id)");
		
		Query query = session.createQuery(builder.toString());
		query.setParameter("status", SurveyStatus.ACTIVATED);
		query.setParameter("dateTime", new Date ());
		
		return query.list();
	}
	
	
	public int getSurveyTaskCount (String surveyId) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("SELECT count(v) FROM SurveyTask v where v.surveyId = :surveyId");

		Query query = session.createQuery(builder.toString());
		query.setParameter("surveyId", surveyId);
		
		return ((Number) query.uniqueResult()).intValue();
	}
}
