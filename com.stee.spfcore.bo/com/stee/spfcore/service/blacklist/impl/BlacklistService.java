package com.stee.spfcore.service.blacklist.impl;

import java.util.logging.Level;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.blacklist.Blacklistee;
import com.stee.spfcore.service.blacklist.BlacklistServiceException;

public class BlacklistService extends AbstractBlacklistService {

	@Override
	public void addBlacklistee(Blacklistee blacklistee, String requester) throws BlacklistServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			dao.addBlacklistee (blacklistee, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new BlacklistServiceException ("Fail to add blacklist", e);
		}
	}

	@Override
	public void updateBlacklistee(Blacklistee blacklistee, String requester) throws BlacklistServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateBlacklistee (blacklistee, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new BlacklistServiceException ("Fail to update blacklist", e);
		}
	}

}
