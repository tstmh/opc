package com.stee.spfcore.webapi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.model.survey.Response;
import com.stee.spfcore.webapi.model.survey.Survey;
import com.stee.spfcore.webapi.model.survey.SurveyStatus;
import com.stee.spfcore.webapi.model.survey.SurveyTask;

@Repository
public class SurveyDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(SurveyDAO.class);
	
	private EntityManager entityManager;
	
	@Autowired
	public SurveyDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Survey getSurvey (String id) {
		logger.info("get Survey");
		Session session = entityManager.unwrap(Session.class);

		return (Survey) session.get(Survey.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<SurveyTask> getPendingSurveyTasks (String nric) {
		logger.info("get Pending Survey Task");
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder builder = new StringBuilder("FROM SurveyTask v where v.nric = :nric and v.completed = false");
		builder.append(" and v.surveyId in (select s.id from Survey s where s.status = :status and s.publishDate < :dateTime and s.endDate > :dateTime)");
		
		logger.info("getPendingSurveyTasks: "+builder.toString());
		
		Query query = session.createQuery(builder.toString());
		query.setParameter("nric", nric);
		query.setParameter("status", SurveyStatus.ACTIVATED);
		query.setParameter("dateTime", new Date ());
	
		return query.list();
	}
	@SuppressWarnings("unchecked")
	public List<SurveyTask> getPendingSurveyTasks (String nric, Date date) {
		logger.info("get Pending Survey Task - 1 year");
		Session session = entityManager.unwrap(Session.class);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
		Date oneYearBefore = calendar.getTime();

		StringBuilder builder = new StringBuilder("FROM SurveyTask v where v.nric = :nric and v.completed = false");
		builder.append(" and v.createdOn >= :oneYearBefore and v.createdOn <= :date");
		builder.append(" and v.surveyId in (select s.id from Survey s where s.status = :status and s.publishDate < :dateTime and s.endDate > :dateTime)");

		logger.info("getPendingSurveyTasks - 1year : "+builder.toString());

		Query query = session.createQuery(builder.toString());
		query.setParameter("nric", nric);
		query.setParameter("oneYearBefore", oneYearBefore);
		query.setParameter("date", date);
		query.setParameter("status", SurveyStatus.ACTIVATED);
		query.setParameter("dateTime", new Date ());

		return query.list();
	}
	
	public SurveyTask getUserSurveyTask (String surveyId, String nric) {
		logger.info("get User Survey Task");
		Session session = entityManager.unwrap(Session.class);
		
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
	
	public void updateSurveyTask (SurveyTask task) {
		logger.info("Update Survey Task");
		Session session = entityManager.unwrap(Session.class);

		session.merge(task);

		session.flush();
	}
	
	public String addResponse (Response response) {
		logger.info("add Response.");
		Session session = entityManager.unwrap(Session.class);

		session.save(response);

		session.flush();
		
		return response.getId();
	}
		
}
