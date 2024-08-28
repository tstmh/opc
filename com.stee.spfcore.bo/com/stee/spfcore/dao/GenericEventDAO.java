package com.stee.spfcore.dao;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.model.course.CourseStatus;
import com.stee.spfcore.model.genericEvent.GEApplicationStatus;
import com.stee.spfcore.model.genericEvent.GETicketingSection;
import com.stee.spfcore.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.model.genericEvent.GenericEventDepartment;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.model.genericEvent.GenericEventStatus;
import com.stee.spfcore.model.genericEvent.internal.GETargetedUser;
import com.stee.spfcore.model.genericEvent.internal.NotifyApplicationResultTask;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;

public class GenericEventDAO {

	private static final Logger logger = Logger.getLogger( GenericEventDAO.class.getName() );
	
	public String addGenericEventDetail (GenericEventDetail eventDetail, String requester) {

		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.save(eventDetail);
		session.flush();
		
		return eventDetail.getId();
	}

	public void updateGenericEventDetail (GenericEventDetail eventDetail, String requester) {

		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.merge(eventDetail);
		session.flush();
	}

	public void deleteGenericEventDetail (GenericEventDetail eventDetail, String requester) {
		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.delete(eventDetail);
	}
	
	public GenericEventDetail getGenericEventDetail (String id) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		return (GenericEventDetail) session.get(GenericEventDetail.class, id);		
	}
	
	@SuppressWarnings("unchecked")
	public List<GenericEventDetail> getGenericEventDetails () {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		Query query = session.createQuery("FROM GenericEventDetail event order by event.registrationStartDate desc");

		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<GenericEventApplication> getGenericEventApplications (String nric, Date startDate, Date endDate) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
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

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("getGenericEventApplications: %s", queryBuilder));
		}
		Query query = session.createQuery(queryBuilder.toString());
		
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
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<GenericEventDetail> getGenericEventPendingRegistration (String nric) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		StringBuilder queryBuilder = new StringBuilder("select c from GenericEventDetail c where c.registrationStartDate <= current_date() ");
		queryBuilder.append("and c.registrationEndDate >= current_date() ");
		queryBuilder.append("and c.status = :status ");
		queryBuilder.append("and c.id not in (select distinct p.eventId from GenericEventApplication p where p.nric = :nric and p.status != :applicationStatus) ");
		queryBuilder.append("and c.id in (select distinct a.eventId from GETargetedUser a where a.nric = :nric) ");

		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("getGenericEventPendingRegistration: %s", queryBuilder));
		}
		Query query = session.createQuery(queryBuilder.toString());
		
		query.setParameter("nric", nric);
		query.setParameter("status", CourseStatus.ACTIVATED);
		query.setParameter("applicationStatus", GEApplicationStatus.WITHDRAWN);
		
		return query.list();
	}
	
	
	public GenericEventApplication getGenericEventApplication (String eventId, String nric) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		Query query = session.createQuery("Select c from GenericEventApplication c where c.nric = :nric and c.eventId = :eventId");
		query.setParameter("nric", nric);
		query.setParameter("eventId", eventId);
		
		List<?> list = query.list();
		
		if (!list.isEmpty()) {
			return (GenericEventApplication) list.get(0);
		}
		else {
			return null;
		}
	}
	
	public void addGenericEventApplication (GenericEventApplication application, String requester) {

		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.save(application);
		session.flush();
	}
	
	public void updateGenericEventApplication (GenericEventApplication application, String requester) {

		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.merge(application);
		session.flush();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GenericEventApplication> getGenericEventApplications (String eventId) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		StringBuilder queryBuilder = new StringBuilder("select c from GenericEventApplication c where c.eventId = :eventId ");
		
		Query query = session.createQuery(queryBuilder.toString());
		
		query.setParameter("eventId", eventId);
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GenericEventApplication> getGenericEventApplications (String eventId, GEApplicationStatus status) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		StringBuilder queryBuilder = new StringBuilder("select c from GenericEventApplication c where c.eventId = :eventId ");
		queryBuilder.append("and c.status = :status");
		
		Query query = session.createQuery(queryBuilder.toString());
		
		query.setParameter("eventId", eventId);
		query.setParameter("status", status);
		
		return query.list();
	}
	
	public void addNotifyApplicationResultTask (NotifyApplicationResultTask task, String requester) {

		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.save(task);
		session.flush();
	}

	public void updateNotifyApplicationResultTask (NotifyApplicationResultTask task, String requester) {

		SessionFactoryUtil.setUser(requester);
		Session session = SessionFactoryUtil.getCurrentSession();
		session.merge(task);
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public List<NotifyApplicationResultTask> getPendingNotifyApplicationResultTasks () {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		StringBuilder queryBuilder = new StringBuilder("select c from NotifyApplicationResultTask c where c.completed = false");
		
		Query query = session.createQuery(queryBuilder.toString());
		
		return query.list();
	}
	
	public void saveTargetedUsers (List<GETargetedUser> targetedUsers, String requester) {
		
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		for (int i = 0; i < targetedUsers.size() ; i++) {
			GETargetedUser targetedUser = targetedUsers.get(i);
			session.save(targetedUser);
			
			if ( i % 20 == 0 ) { //20, same as the JDBC batch size
        //flush a batch of inserts and release memory:
        session.flush();
        session.clear();
			}
		}
		
		session.flush();
		
	}
	
	public void saveTargetedUser (GETargetedUser targetedUser, String requester) {
		
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.saveOrUpdate(targetedUser);
		
		session.flush();
	}
	
	
	public boolean isTargetedUser (String eventId, String nric) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		String queryStr = "select c from GETargetedUser c where c.eventId = :eventId and c.nric = :nric";
		
		Query query = session.createQuery(queryStr);
		
		query.setParameter("eventId", eventId);
		query.setParameter("nric", nric);
		
		return !query.list().isEmpty();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAnnouncementIdsOfUnit(List<String> units)
	{
		if(units != null) {
			if (logger.isLoggable(Level.INFO)) {
				logger.fine(String.format("getAnnouncementIdsOfUnit() units size is %s", units.size()));
			}
		}
		else
			logger.log(Level.FINE,"getAnnouncementIdsOfUnit() units size is null");
		
		HashMap<String,String> announcementIds = new HashMap<>();
		List<GenericEventDetail> eventDetails =  getGenericEventDetails();
		
		for(String unit : units)
		{
			if(eventDetails != null)
			{
				for(GenericEventDetail ge : eventDetails)
				{
					List<GenericEventDepartment> mainAndCoshare = ge.getDepartments();
					if(mainAndCoshare != null)
					{
						for(GenericEventDepartment ged : mainAndCoshare)
						{
							if(unit.equals(ged.getDepartmentId()))
								announcementIds.put(ge.getAnnouncementId(),ge.getAnnouncementId());
						}
					}
				}
			}
		}
		if(announcementIds != null)
			return new ArrayList<>(announcementIds.values());
		else
			return Collections.emptyList();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUnitAnnoucementIds(String unit) {
		
		List<String> result;
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		String queryStr = "select e.announcementId FROM GenericEventDetail e join e.departments d where d.departmentId = :unit and d.isMainDepartment = 1";
		
		Query query = session.createQuery(queryStr);
		query.setParameter("unit", unit);
		
		result = query.list();
			
		return result;
	}
	
	public List<GenericEventDetail> getUnitGenericEventDetails (String isMainDepartment, String unit) {
		
		logger.info("inside getUnitGenericEventDetails");
		if ( logger.isLoggable( Level.INFO ) ) {
			logger.info(String.format("isMainDepartment: %s", isMainDepartment));
			logger.info(String.format("unit: %s", unit));
		}
		Session session = SessionFactoryUtil.getCurrentSession();
		String queryStr = "";

		if(isMainDepartment.toLowerCase(Locale.ENGLISH).equals("yes"))
		{
			logger.info("inside yes");
			queryStr = "select distinct(e) from GenericEventDetail e join e.departments d where d.departmentId = :unit and d.isMainDepartment = 1";
		
		}
		else if(isMainDepartment.toLowerCase(Locale.ENGLISH).equals("no"))
		{
			logger.info("inside no");
			queryStr = "select distinct(e) from GenericEventDetail e join e.departments d where d.departmentId = :unit and d.isMainDepartment = 0";
		}
		else
		{
			logger.info("inside empty");
			queryStr = "select distinct(e) from GenericEventDetail e left join e.departments d where d.departmentId is null";
		}
		Query query = session.createQuery(queryStr);
		if(!isMainDepartment.equals(""))
		{
			
			logger.info("inside set parameter");
			query.setParameter("unit", unit);
		}
		logger.info("end inside getUnitGenericEventDetails");
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<GenericEventDetail> getPcwfEventDetails() {
		
		List<GenericEventDetail> result;
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		String queryStr = "select distinct(e) from GenericEventDetail e left join e.departments d where d.departmentId is null";
		
		Query query = session.createQuery(queryStr);
		
		result = query.list();
			
		return result;
	}
	
	 public PersonalDetail getPersonal( String nric ){

	        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

	        return ( PersonalDetail ) session.get( PersonalDetail.class, nric, new LockOptions( LockMode.PESSIMISTIC_READ ) );
	    }
	 
	public GenericEventApplication getGenericEventApplicationById (String id) {
			
		Session session = SessionFactoryUtil.getCurrentSession();
		return (GenericEventApplication) session.get(GenericEventApplication.class, id);		
			
		
	}

}
