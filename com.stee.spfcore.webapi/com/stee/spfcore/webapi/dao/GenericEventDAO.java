package com.stee.spfcore.webapi.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.genericEvent.GEApplicationStatus;
import com.stee.spfcore.webapi.model.genericEvent.GETargetedUser;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventStatus;

@Repository
public class GenericEventDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(GenericEventDAO.class);
	
	@Autowired
	public GenericEventDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public GenericEventDetail getEventDetail ( String eventId ){
		logger.info("Get Event Detail");
		Session session = entityManager.unwrap(Session.class);
		
		return session.get(GenericEventDetail.class, eventId);
	}

	@SuppressWarnings("unchecked")
	public List<GenericEventApplication> getGenericEventApplications ( String nric, Date startDate, Date endDate ){
		logger.info("Get Generic Event Applications");
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder queryBuilder = new StringBuilder("select c from GenericEventApplication c where c.nric = :nric ");
		queryBuilder.append("and c.eventId in (select s.id from GenericEventDetail s where ");
		queryBuilder.append("(( (year(s.eventStartDate) < :endYear) ");
		queryBuilder.append("  or (year(s.eventStartDate) = :endYear and month(s.eventStartDate) < :endMonth) ");
		queryBuilder.append("  or (year(s.eventStartDate) = :endYear and month(s.eventStartDate) = :endMonth and day(s.eventStartDate) <= :endDay) ) ");
		queryBuilder.append("and ( (year(s.eventEndDate) > :startYear) ");
		queryBuilder.append("  or (year(s.eventEndDate) = :startYear and month(s.eventEndDate) > :startMonth) ");
		queryBuilder.append("  or (year(s.eventEndDate) = :startYear and month(s.eventEndDate) = :startMonth and day(s.eventEndDate) >= :startDay) ) ) ");
		
		queryBuilder.append("or (( (year(s.registrationStartDate) < :endYear) ");
		queryBuilder.append("  or (year(s.registrationStartDate) = :endYear and month(s.registrationStartDate) < :endMonth) ");
		queryBuilder.append("  or (year(s.registrationStartDate) = :endYear and month(s.registrationStartDate) = :endMonth and day(s.registrationStartDate) <= :endDay) ) ");
		queryBuilder.append("and ( (year(s.registrationEndDate) > :startYear) ");
		queryBuilder.append("  or (year(s.registrationEndDate) = :startYear and month(s.registrationEndDate) > :startMonth) ");
		queryBuilder.append("  or (year(s.registrationEndDate) = :startYear and month(s.registrationEndDate) = :startMonth and day(s.registrationEndDate) >= :startDay) ) ))");

		logger.info("getGenericEventApplicationsByDate: "+queryBuilder.toString());
		Query<GenericEventApplication> query = session.createQuery(queryBuilder.toString());
		
		query.setParameter("nric", nric);
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(startDate);
		query.setParameter("startYear", cal.get(Calendar.YEAR));
		query.setParameter("startMonth", cal.get(Calendar.MONTH) + 1);
		query.setParameter("startDay", cal.get(Calendar.DATE));
		
		cal.setTime(endDate);
		query.setParameter("endYear", cal.get(Calendar.YEAR));
		query.setParameter("endMonth", cal.get(Calendar.MONTH) + 1);
		query.setParameter("endDay", cal.get(Calendar.DATE));
		
		return (List<GenericEventApplication>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<GenericEventDetail> getGenericEventPendingRegistration ( String nric ){
		logger.info("Get Generic Event Pending Registration");
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder queryBuilder = new StringBuilder("select c from GenericEventDetail c where c.registrationStartDate <= current_date() ");
		queryBuilder.append("and c.registrationEndDate >= current_date() ");
		queryBuilder.append("and c.status = :status ");
		queryBuilder.append("and c.id not in (select distinct p.eventId from GenericEventApplication p where p.nric = :nric and p.status != :applicationStatus) ");
		queryBuilder.append("and c.id in (select distinct a.eventId from GETargetedUser a where a.nric = :nric) ");
		
		logger.info("getGenericEventPendingRegistration: "+queryBuilder.toString());
		Query<GenericEventDetail> query = session.createQuery(queryBuilder.toString());
		
		query.setParameter("nric", nric);
		query.setParameter("status", GenericEventStatus.ACTIVATED);
		query.setParameter("applicationStatus", GEApplicationStatus.WITHDRAWN);
		
		return (List<GenericEventDetail>) query.list();
	}
	
	@SuppressWarnings("unchecked")
	public GenericEventApplication getGenericEventApplication ( String eventId, String nric ){
		logger.info("Get Generic Event Application");
		Session session = entityManager.unwrap(Session.class);
		
		Query<GenericEventApplication> query = session.createQuery("Select c from GenericEventApplication c where c.nric = :nric and c.eventId = :eventId");
		query.setParameter("nric", nric);
		query.setParameter("eventId", eventId);
		
		List<GenericEventApplication> list = query.list();
		
		if (!list.isEmpty()) {
			return (GenericEventApplication) list.get(0);
		} else {
			return null;
		}
		
	}
	
	public String addGenericEventApplication ( GenericEventApplication application  ) {
		logger.info("Add Generic Event Application");
		Session session = entityManager.unwrap(Session.class);
		
	    session.save( application );
	    session.flush();
	    
	    return application.getId();
	}
	
	public void updateGenericEventApplication ( GenericEventApplication application  ) {
		logger.info("Update Generic Event Application");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( application );
	    session.flush();
	}
	
	public String addGenericEventDetail ( GenericEventDetail eventDetail  ) {
		logger.info("Add Generic Event Detail");
		Session session = entityManager.unwrap(Session.class);
		
	    session.save( eventDetail );
	    session.flush();
	    
	    return eventDetail.getId();
	}

	public void updateGenericEventDetail ( GenericEventDetail eventDetail  ) {
		logger.info("Update Generic Event Detail");
		Session session = entityManager.unwrap(Session.class);
		
	    session.merge( eventDetail );
	    session.flush();
	}
	
	public void deleteGenericEventDetail ( GenericEventDetail eventDetail  ) {
		logger.info("Delete Generic Event Detail");
		Session session = entityManager.unwrap(Session.class);

		session.delete(eventDetail);
	}
	
	public String addGenericEventTargetedUser ( GETargetedUser targetedUser  ) {
		logger.info("Save Targeted User");
		Session session = entityManager.unwrap(Session.class);
		
	    session.saveOrUpdate( targetedUser );
	    session.flush();
	    
	    return targetedUser.getId();
	}
	
	@SuppressWarnings("unchecked")
	public boolean isTargetedUser(String eventId, String nric) {
		logger.info("Is Targeted User");
		Session session = entityManager.unwrap(Session.class);
		
		String queryStr = "select c from GETargetedUser c where c.eventId = :eventId and c.nric = :nric";
		
		Query<GETargetedUser> query = session.createQuery(queryStr);

		query.setParameter("nric", nric);
		query.setParameter("eventId", eventId);

		return !query.list().isEmpty();
	}
	
}
