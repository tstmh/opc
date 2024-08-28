package com.stee.spfcore.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.Module;
import com.stee.spfcore.model.calendar.MarketingEvent;
import com.stee.spfcore.model.calendar.PublicHoliday;
import com.stee.spfcore.model.personnel.Leave;

public class CalendarDAO {

	public String addPublicHoliday(PublicHoliday holiday, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(holiday);

		session.flush();

		return holiday.getId();
	}

	public void updatePublicHoliday(PublicHoliday holiday, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(holiday);

		session.flush();
	}

	public void deletePublicHoliday(PublicHoliday holiday, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();
		
		session.delete(holiday);
	}

	
	public PublicHoliday getPublicHoliday (String id) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		return (PublicHoliday) session.get(PublicHoliday.class, id);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PublicHoliday> getPublicHolidays(int month, int year) {

		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("from PublicHoliday rec where "
				+ " ( (year(rec.startDate) < :year) or (year(rec.startDate) = :year and month(rec.startDate) <= :month )) " 
				+ " and ( (year(rec.endDate) > :year) or (year(rec.endDate) = :year and month(rec.endDate) >= :month ))) order by rec.startDate asc");

		query.setParameter("month", month);
		query.setParameter("year", year);

		return (List<PublicHoliday>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<PublicHoliday> getPublicHolidays(int year) {

		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("from PublicHoliday rec where "
				+ " year(rec.startDate) = :year or year(rec.endDate) = :year order by rec.startDate asc");

		query.setParameter("year", year);

		return (List<PublicHoliday>) query.list();
	}
	
	public boolean isPublicHoliday (Date date) {
		
		Session session = SessionFactoryUtil.getCurrentSession();

    StringBuilder builder = new StringBuilder("select count(rec) from PublicHoliday rec where rec.startDate <= :date and :date < rec.endDate");
    Query query = session.createQuery(builder.toString());
    query.setParameter("date", date);
    
    return ((Long)query.uniqueResult()) > 0;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PublicHoliday> getPublicHolidays(Date startDate, Date endDate) {

		Session session = SessionFactoryUtil.getCurrentSession();

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
	
	@SuppressWarnings( "unchecked" )
    public List<PublicHoliday> getPublicHolidays( Date date ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        StringBuilder builder = new StringBuilder("select rec from PublicHoliday rec where rec.startDate <= :date and :date <= rec.endDate");
        Query query = session.createQuery(builder.toString());
        query.setParameter("date", date);
        return (List<PublicHoliday>) query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Leave> getUserLeaves (String nric, int month, int year) {
		
		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("select l from PersonalDetail p inner join p.leaves l where "
				+ " p.nric = :nric and l.leaveType != 'UAWL' and "
				+ " ( (year(l.startDate) < :year) or (year(l.startDate) = :year and month(l.startDate) <= :month )) " 
				+ " and ( (year(l.endDate) > :year) or (year(l.endDate) = :year and month(l.endDate) >= :month ))) ");

		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("nric", nric);
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Leave> getUserLeaves (String nric, Date startDate, Date endDate) {
		
		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("select l from PersonalDetail p inner join p.leaves l ");
		builder.append("where p.nric = :nric and ");
		builder.append("(");
		builder.append("(year(l.endDate) > :startYear) or ");
		builder.append("(year(l.endDate) = :startYear and month(l.endDate) > :startMonth) or ");
		builder.append("(year(l.endDate) = :startYear and month(l.endDate) = :startMonth and day(l.endDate) >= :startDay)");
		builder.append(") and (");
		builder.append("(year(l.startDate) < :endYear) or ");
		builder.append("(year(l.startDate) = :endYear and month(l.startDate) < :endMonth) or ");
		builder.append("(year(l.startDate) = :endYear and month(l.startDate) = :endMonth and day(l.startDate) <= :endDay)");
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
		
		query.setParameter("nric", nric);
		
		return query.list();
	}
	
	
	public String addMarketingEvent (MarketingEvent event, String requester) {
	
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(event);

		session.flush();

		return event.getId();
	}
	
	
	public void updateMarketingEvent (MarketingEvent event, String requester) {
	
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(event);

		session.flush();
	}
	
	public void deleteMarketingEvent (MarketingEvent event, String requester) {
	
		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.delete(event);

		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	public MarketingEvent getMarketingEvent (String nric, Module module, String referenceId) {
	
		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("from MarketingEvent rec where rec.nric = :nric and " +
																"rec.module = :module and rec.referenceId = :referenceId");
				
		
		query.setParameter("nric", nric);
		query.setParameter("module", module);
		query.setParameter("referenceId", referenceId);
		query.setMaxResults(1);
		
		List<MarketingEvent> list = (List<MarketingEvent>) query.list();
		if (list.isEmpty()) {
			return null;
		}
		else {
			return list.get(0);
		}
	}
	
	
	public MarketingEvent getMarketingEvent (String id) {
		
		Session session = SessionFactoryUtil.getCurrentSession();
		
		return (MarketingEvent) session.get(MarketingEvent.class, id);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MarketingEvent> getMarketingEvents (String nric, int month, int year) {
		
		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("from MarketingEvent rec where "
				+ " rec.nric = :nric and  "
				+ " ( (year(rec.startDate) < :year) or (year(rec.startDate) = :year and month(rec.startDate) <= :month )) " 
				+ " and ( (year(rec.endDate) > :year) or (year(rec.endDate) = :year and month(rec.endDate) >= :month ))) ");

		query.setParameter("month", month);
		query.setParameter("year", year);
		query.setParameter("nric", nric);
		
		return (List<MarketingEvent>) query.list();
	}
	
}
