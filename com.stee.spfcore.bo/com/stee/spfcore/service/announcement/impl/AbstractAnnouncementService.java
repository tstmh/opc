package com.stee.spfcore.service.announcement.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.AnnouncementDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.service.announcement.AnnouncementServiceException;
import com.stee.spfcore.service.announcement.IAnnouncementService;

public abstract class AbstractAnnouncementService implements IAnnouncementService {

	protected static final Logger logger = Logger.getLogger(AbstractAnnouncementService.class.getName());

	protected AnnouncementDAO dao;
	
	protected AbstractAnnouncementService() {
		dao = new AnnouncementDAO();
		
	}

	@Override
	public Announcement getAnnouncement(String id) throws AnnouncementServiceException {
		Announcement result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getAnnouncement(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get announcement", e);
			SessionFactoryUtil.rollbackTransaction();
		}

        return result;
	}

	@Override
	public List<Announcement> getAnnouncements(String module) throws AnnouncementServiceException {

		List<Announcement> result = null;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getAnnouncements(module);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get announcements", e);
			SessionFactoryUtil.rollbackTransaction();
		}

		return result;
	}

	@Override
	public void setModule(String id, String modName, String requester) throws AnnouncementServiceException
	{		
		boolean isNewTrx = false;
		try {
			if(!SessionFactoryUtil.isTransactionActive())
			{
				SessionFactoryUtil.beginTransaction();
				isNewTrx = true;
			}
			Announcement result = dao.getAnnouncement(id);
			result.setModule(modName);
			dao.updateAnnouncement(result, requester);
			if(isNewTrx)
				SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get announcements", e);
			SessionFactoryUtil.rollbackTransaction();
		}

	}

}
