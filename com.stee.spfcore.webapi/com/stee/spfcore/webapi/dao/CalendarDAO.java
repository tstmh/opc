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

import com.stee.spfcore.webapi.model.calendar.MarketingEvent;
import com.stee.spfcore.webapi.model.calendar.PublicHoliday;
import com.stee.spfcore.webapi.model.personnel.Leave;

@Repository
public class CalendarDAO {
	private static final Logger logger = LoggerFactory.getLogger(CalendarDAO.class);
	
	private EntityManager entityManager;
	
	@Autowired
	public CalendarDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings( "unchecked" )
    public List<PublicHoliday> getPublicHolidays( Date date ) {
		logger.info("Get Public Holidays");
		Session session = entityManager.unwrap(Session.class);

        StringBuilder builder = new StringBuilder("select rec from PublicHoliday rec where rec.startDate <= :date and :date <= rec.endDate");
        Query query = session.createQuery(builder.toString());
        query.setParameter("date", date);
        return (List<PublicHoliday>) query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PublicHoliday> getPublicHolidays(Date startDate, Date endDate) {
		logger.info("Get Public Holidays");
		Session session = entityManager.unwrap(Session.class);
		
		StringBuilder builder = new StringBuilder("from PublicHoliday rec where ");
		builder.append("(");
		builder.append("(year(rec.endDate) > :startYear) or ");
		builder.append("(year(rec.endDate) = :startYear and month(rec.endDate) > :startMonth) or ");
		builder.append("(year(rec.endDate) = :startYear and month(rec.endDate) = :startMonth and day(rec.endDate) >= :startDay)");
		builder.append(") and (");
		builder.append("(year(rec.startDate) < :endYear) or ");
		builder.append("(year(rec.startDate) = :endYear and month(rec.startDate) < :endMonth) or ");
		builder.append("(year(rec.startDate) = :endYear and month(rec.startDate) = :endMonth and day(rec.startDate) <= :endDay)");
		builder.append(")");
		
		Query query = session.createQuery(builder.toString());

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(startDate);
		
		query.setParameter("startYear", cal.get(Calendar.YEAR));
		query.setParameter("startMonth", cal.get(Calendar.MONTH) + 1);
		query.setParameter("startDay", cal.get(Calendar.DATE));
		
		cal.setTime(endDate);
		
		query.setParameter("endYear", cal.get(Calendar.YEAR));
		query.setParameter("endMonth", cal.get(Calendar.MONTH) + 1);
		query.setParameter("endDay", cal.get(Calendar.DATE));

		return (List<PublicHoliday>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Leave> getUserLeaves(String nric, Date startDate, Date endDate) {
		logger.info("Get User Leaves");
		Session session = entityManager.unwrap(Session.class);
		StringBuffer queryString = new StringBuffer();
		queryString.append( "select l from PersonalDetail p inner join p.leaves l " );
		queryString.append( "where p.nric = :nric and " );
		queryString.append("(");
		queryString.append("(year(l.endDate) > :startYear) or ");
		queryString.append("(year(l.endDate) = :startYear and month(l.endDate) > :startMonth) or ");
		queryString.append("(year(l.endDate) = :startYear and month(l.endDate) = :startMonth and day(l.endDate) >= :startDay)");
		queryString.append(") and (");
		queryString.append("(year(l.startDate) < :endYear) or ");
		queryString.append("(year(l.startDate) = :endYear and month(l.startDate) < :endMonth) or ");
		queryString.append("(year(l.startDate) = :endYear and month(l.startDate) = :endMonth and day(l.startDate) <= :endDay)");
		queryString.append(")");
		
		Query query = session.createQuery( queryString.toString() );

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(startDate);
		
		query.setParameter("startYear", cal.get(Calendar.YEAR));
		query.setParameter("startMonth", cal.get(Calendar.MONTH) + 1);
		query.setParameter("startDay", cal.get(Calendar.DATE));
		
		cal.setTime(endDate);
		
		query.setParameter("endYear", cal.get(Calendar.YEAR));
		query.setParameter("endMonth", cal.get(Calendar.MONTH) + 1);
		query.setParameter("endDay", cal.get(Calendar.DATE));
		
		query.setParameter("nric", nric);
		
		return (List<Leave>) query.list();
		
	}

	@SuppressWarnings("unchecked")
	public List<MarketingEvent> getMarketingEvents(String nric, int month, int year) {
		logger.info("Get Marketing Events");
		Session session = entityManager.unwrap(Session.class);
		StringBuffer queryString = new StringBuffer();
		queryString.append("from MarketingEvent rec where "
				+ " rec.nric = :nric and  "
				+ " ( (year(rec.startDate) < :year) or (year(rec.startDate) = :year and month(rec.startDate) <= :month )) " 
				+ " and ( (year(rec.endDate) > :year) or (year(rec.endDate) = :year and month(rec.endDate) >= :month ))) ");
		Query query = session.createQuery( queryString.toString() );
		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("nric", nric);		
		return (List < MarketingEvent >) query.list();
	}

	public boolean isPublicHoliday (Date date) {
		logger.info("Is Public Holiday");
		Session session = entityManager.unwrap(Session.class);
	
	    StringBuilder builder = new StringBuilder("select count(rec) from PublicHoliday rec where rec.startDate <= :date and :date < rec.endDate");
	    Query query = session.createQuery(builder.toString());
	    query.setParameter("date", date);
	    
	    return ((Long)query.uniqueResult()) > 0;
	}
	
}
