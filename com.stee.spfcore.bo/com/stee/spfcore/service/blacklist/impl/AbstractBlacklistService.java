package com.stee.spfcore.service.blacklist.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.BlacklistDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.model.blacklist.BlacklistModule;
import com.stee.spfcore.model.blacklist.Blacklistee;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.service.blacklist.BlacklistServiceException;
import com.stee.spfcore.service.blacklist.IBlacklistService;
import com.stee.spfcore.vo.blacklist.PersonalNameDepartment;

public abstract class AbstractBlacklistService implements IBlacklistService {

	protected static final Logger logger = Logger.getLogger(AbstractBlacklistService.class.getName());
	
	protected BlacklistDAO dao;
	
	protected AbstractBlacklistService() {
		dao = new BlacklistDAO();
	}
	
	@Override
	public int getBlacklistCount(boolean includeDisabled) throws BlacklistServiceException {
		
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklistCount(includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklist count", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public int getBlacklistCount(String module, boolean includeDisabled) throws BlacklistServiceException {
		
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklistCount(module, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklist count", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public int getUserBlacklistCount(String nric, boolean includeDisabled) throws BlacklistServiceException {
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getUserBlacklistCount(nric, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get user blacklist count", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> getBlacklist(boolean includeDisabled) throws BlacklistServiceException {
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklist(includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> getBlacklist(String module, boolean includeDisabled) throws BlacklistServiceException {
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklist(module, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> getUserBlacklist(String nric, boolean includeDisabled) throws BlacklistServiceException {
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getUserBlacklist(nric, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get user blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> getBlacklist(int pageNum, int pageSize, boolean includeDisabled)
			throws BlacklistServiceException {
		
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklist(pageNum, pageSize, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> getBlacklist(int pageNum, int pageSize, String module, boolean includeDisabled)
			throws BlacklistServiceException {
		
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklist(pageNum, pageSize, module, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> getUserBlacklist(int pageNum, int pageSize, String nric, boolean includeDisabled)
			throws BlacklistServiceException {
		
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getUserBlacklist(pageNum, pageSize, nric, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get user blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public Blacklistee getBlacklistee(String nric, String module)
			throws BlacklistServiceException {

		Blacklistee result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBlacklistee (nric, module);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklistee", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Blacklistee> searchBlacklist(int pageNum, int pageSize, String nric, String module,
			boolean includeDisabled) throws BlacklistServiceException {
		
		List<Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.searchBlacklist(pageNum, pageSize, nric, module, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to search blacklist", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public int getSearchBlacklistCount(String nric, String module, boolean includeDisabled)
			throws BlacklistServiceException {
		
		int result = 0;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getSearchBlacklistCount(nric, module, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get search blacklist count", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<BlacklistModule> getBlacklistModules() throws BlacklistServiceException {
		
		List<BlacklistModule> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			result = dao.getBlacklistModules();
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Blacklist Modules", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public boolean canAccess(String nric) throws BlacklistServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			result = DataAccessCheck.canAccess(SecurityInfo.createInstance(), nric);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to check on accessibility", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public boolean isBlacklisted(String nric, String module) throws BlacklistServiceException {
		
		boolean result = false;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			result = dao.isBlacklisted (nric, module);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to check if user is blacklisted", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public PersonalNameDepartment getPersonalNameDepartment(String nric) throws BlacklistServiceException {
		
		PersonalNameDepartment result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			result = dao.getPersonalNameDepartment (nric);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get personal name and department", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}
	
	@Override
	public List<Blacklistee> getBlacklisteeByUnit (String module, String unit, String nric, boolean includeDisabled) throws BlacklistServiceException {
		List <Blacklistee> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			
			result = dao.getBlacklisteeByUnit(module, unit, nric, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get blacklistee by Unit");
			SessionFactoryUtil.rollbackTransaction();
			throw new BlacklistServiceException("Fail to get blacklistee by Unit", e);
		}
		
		return result;
	}
	
	@Override
	public PersonalNameDepartment getPersonalNameDepartmentByNRICUnit (String nric, String unit) throws BlacklistServiceException {
		PersonalNameDepartment result = null;
		
		try {
		SessionFactoryUtil.beginTransaction();
		
		result = dao.getPersonalNameDepartment(nric, unit);
		
		SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Personal Name Department by NRIC and Unit");
			SessionFactoryUtil.rollbackTransaction();
			throw new BlacklistServiceException("Fail to get Personal Name Department by NRIC and Unit",e);
		}
		return result;
	}
	
}
