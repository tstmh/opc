package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.stee.spfcore.model.system.SystemStatus;
import com.stee.spfcore.service.system.SystemServiceException;

public class SystemDAO {

	/**
	 * Get the system status
	 * 
	 * @param null
	 * @return SystemStatus or null
	 * @throws SystemServiceException
	 */
	public SystemStatus getSystemStatus(String systemName) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (SystemStatus) session.get(SystemStatus.class, systemName);
	}

	/**
	 * Add new system status.
	 * 
	 * @param SystemStatus
	 *            the system status detail to be added.
	 * @throws SystemServiceException
	 *             Exception while adding the system status.
	 */
	public void addSystemStatus(SystemStatus systemStatus) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		session.save(systemStatus);

		session.flush();
	}

	/**
	 * Updating system status detail
	 * 
	 * @param SystemStatus
	 *            the system status detail to be updated.
	 * @throws SystemServiceException
	 *             Exception while updating the system status detail.
	 */
	public void updateSystemStatus(SystemStatus systemStatus) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		session.merge(systemStatus);

		session.flush();
	}
    
	/**
	 * Get system status detail list 
	 * 
	 * @param null
	 * @throws SystemServiceException
	 *             Exception while getting the system status detail.
	 */
    @SuppressWarnings("unchecked")
    public List<SystemStatus> getSystemStatusList() {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Criteria criteria = session.createCriteria(SystemStatus.class);

        return (List<SystemStatus>) criteria.list();
    }
}
