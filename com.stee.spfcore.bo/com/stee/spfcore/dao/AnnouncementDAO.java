package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.announcement.AnnouncementSender;
import com.stee.spfcore.vo.announcement.AnnouncementStatistic;

public class AnnouncementDAO {
	public String addAnnouncement (Announcement announcement, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(announcement);

		session.flush();

		return announcement.getId();
	}

	public void updateAnnouncement (Announcement announcement, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(announcement);

		session.flush();
	}

	public void deleteAnnouncement (Announcement announcement, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.delete(announcement);
	}


	public Announcement getAnnouncement (String id) {

		Session session = SessionFactoryUtil.getCurrentSession();

		return (Announcement) session.get(Announcement.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Announcement> getAnnouncements (String module) {

		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("From Announcement a where a.module = :module");
		query.setParameter("module", module);

		return  (List<Announcement>) query.list();
	}


	@SuppressWarnings("unchecked")
	public List<String> getBlacklistedUser (List<String> exemptedModules) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder queryBuilder = new StringBuilder("select distinct bl.nric from Blacklistee bl where ");
		queryBuilder.append(" (bl.effectiveDate is not null and bl.effectiveDate <= current_date()) ");
		queryBuilder.append(" and (bl.obsoleteDate is null or bl.obsoleteDate > current_date()) ");

		if (exemptedModules != null && !exemptedModules.isEmpty()) {
			queryBuilder.append(" and bl.nric not in (select innerBl.nric from Blacklistee innerBl where innerBl.module in (:exemptedModules) ");
			queryBuilder.append(" and (innerBl.effectiveDate is not null and innerBl.effectiveDate <= current_date()) ");
			queryBuilder.append(" and (innerBl.obsoleteDate is null or innerBl.obsoleteDate > current_date())) ");
		}

		Query query = session.createQuery(queryBuilder.toString());

		if (exemptedModules != null && !exemptedModules.isEmpty()) {
			query.setParameterList("exemptedModules", exemptedModules);
		}

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAnnouncementSenderEmails () {

		Session session = SessionFactoryUtil.getCurrentSession();

		Query query = session.createQuery("select v.senderEmail from AnnouncementSender v");

		return query.list();
	}

	public AnnouncementSender getAnnouncementSender (String email) {

		Session session = SessionFactoryUtil.getCurrentSession();

		return (AnnouncementSender) session.get(AnnouncementSender.class, email);
	}


	public AnnouncementStatistic getAnnouncementStatistic (String id) {

		AnnouncementStatistic statistic = new AnnouncementStatistic();

		Session session = SessionFactoryUtil.getCurrentSession();

		Announcement announcement = (Announcement) session.get(Announcement.class, id);
		if (announcement == null) {
			return statistic;
		}

		// Get total number of notification (UserAnnouncement)
		StringBuilder queryBuilder = new StringBuilder("select count(*) from UserAnnouncement ua where ua.contentId in ");
		queryBuilder.append("(select distinct mc.id from MarketingContentSet as ms inner join ms.contents as mc where ms.id = :contentSetId)");

		Query query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentSetId", announcement.getContentSetId());

		statistic.setTotalNotification (((Number)query.uniqueResult()).intValue());

		// Get total number of notification opened (UserContentViewRecord)
		queryBuilder = new StringBuilder("select count(*) from UserContentViewRecord ua where ua.contentId in ");
		queryBuilder.append("(select distinct mc.id from MarketingContentSet as ms inner join ms.contents as mc where ms.id = :contentSetId)");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentSetId", announcement.getContentSetId());

		statistic.setNotificationOpened(((Number)query.uniqueResult()).intValue());

		// Total number of email sent so far (EmailLog)
		queryBuilder = new StringBuilder("select count(*) from EmailLog ua where ua.contentId in ");
		queryBuilder.append("(select distinct mc.id from MarketingContentSet as ms inner join ms.contents as mc where ms.id = :contentSetId)");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentSetId", announcement.getContentSetId());

		statistic.setEmailSent(((Number)query.uniqueResult()).intValue());

		// Total sms sent so far (SmsLog)
		queryBuilder = new StringBuilder("select count(*) from SmsLog ua where ua.contentId in ");
		queryBuilder.append("(select distinct mc.id from MarketingContentSet as ms inner join ms.contents as mc where ms.id = :contentSetId)");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentSetId", announcement.getContentSetId());

		statistic.setSmsSent(((Number)query.uniqueResult()).intValue());

		// Targeted email count (PublishingContent.emails.size)
		queryBuilder = new StringBuilder("select count(email) from PublishingContent as content inner join content.emails as email where content.contentId in ");
		queryBuilder.append("(select distinct mc.id from MarketingContentSet as ms inner join ms.contents as mc where ms.id = :contentSetId)");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentSetId", announcement.getContentSetId());

		statistic.setTargetedEmailCount(((Number)query.uniqueResult()).intValue());

		// Targeted SMS count (PublishingContent.phones.size)
		queryBuilder = new StringBuilder("select count(phone) from PublishingContent as content inner join content.phones as phone where content.contentId in ");
		queryBuilder.append("(select distinct mc.id from MarketingContentSet as ms inner join ms.contents as mc where ms.id = :contentSetId)");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentSetId", announcement.getContentSetId());

		statistic.setTargetedSmsCount(((Number)query.uniqueResult()).intValue());

		return statistic;
	}


	public AnnouncementStatistic getAnnouncementContentStatistic (String contentId) {

		AnnouncementStatistic statistic = new AnnouncementStatistic();

		Session session = SessionFactoryUtil.getCurrentSession();

		// Get total number of notification (UserAnnouncement)
		StringBuilder queryBuilder = new StringBuilder("select count(*) from UserAnnouncement ua where ua.contentId = :contentId");

		Query query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentId", contentId);

		statistic.setTotalNotification (((Number)query.uniqueResult()).intValue());

		// Get total number of notification opened (UserContentViewRecord)
		queryBuilder = new StringBuilder("select count(*) from UserContentViewRecord ua where ua.contentId = :contentId");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentId", contentId);

		statistic.setNotificationOpened(((Number)query.uniqueResult()).intValue());

		// Total number of email sent so far (EmailLog)
		queryBuilder = new StringBuilder("select count(*) from EmailLog ua where ua.contentId = :contentId");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentId", contentId);

		statistic.setEmailSent(((Number)query.uniqueResult()).intValue());

		// Total sms sent so far (SmsLog)
		queryBuilder = new StringBuilder("select count(*) from SmsLog ua where ua.contentId = :contentId");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentId", contentId);

		statistic.setSmsSent(((Number)query.uniqueResult()).intValue());

		// Targeted email count (PublishingContent.emails.size)
		queryBuilder = new StringBuilder("select count(email) from PublishingContent as content inner join content.emails as email where content.contentId = :contentId");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentId", contentId);

		statistic.setTargetedEmailCount(((Number)query.uniqueResult()).intValue());

		// Targeted SMS count (PublishingContent.phones.size)
		queryBuilder = new StringBuilder("select count(phone) from PublishingContent as content inner join content.phones as phone where content.contentId = :contentId");

		query = session.createQuery(queryBuilder.toString());
		query.setParameter("contentId", contentId);

		statistic.setTargetedSmsCount(((Number)query.uniqueResult()).intValue());

		return statistic;
	}

}
