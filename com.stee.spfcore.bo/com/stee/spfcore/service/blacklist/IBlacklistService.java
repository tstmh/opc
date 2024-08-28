package com.stee.spfcore.service.blacklist;

import java.util.List;

import com.stee.spfcore.model.blacklist.BlacklistModule;
import com.stee.spfcore.model.blacklist.Blacklistee;
import com.stee.spfcore.vo.blacklist.PersonalNameDepartment;

public interface IBlacklistService {

	
	public void addBlacklistee (Blacklistee blacklistee, String requester) throws BlacklistServiceException;
	
	public void updateBlacklistee (Blacklistee blacklistee, String requester) throws BlacklistServiceException;
	
	public int getBlacklistCount(boolean includeDisabled) throws BlacklistServiceException;
		
	public int getBlacklistCount(String module, boolean includeDisabled) throws BlacklistServiceException;
		
	public int getUserBlacklistCount (String nric, boolean includeDisabled) throws BlacklistServiceException;
		
	public List<Blacklistee> getBlacklist (boolean includeDisabled) throws BlacklistServiceException;
		
	public List<Blacklistee> getBlacklist (String module, boolean includeDisabled) throws BlacklistServiceException;
		
	public List<Blacklistee> getUserBlacklist (String nric, boolean includeDisabled) throws BlacklistServiceException;
		
	public List<Blacklistee> getBlacklist (int pageNum, int pageSize, boolean includeDisabled) throws BlacklistServiceException;
		
	public List<Blacklistee> getBlacklist (int pageNum, int pageSize, String module, boolean includeDisabled) throws BlacklistServiceException;
	
	public List<Blacklistee> getUserBlacklist (int pageNum, int pageSize, String nric, boolean includeDisabled) throws BlacklistServiceException;
	
	public Blacklistee getBlacklistee (String nric, String module) throws BlacklistServiceException;

	public List<Blacklistee> searchBlacklist (int pageNum, int pageSize, String nric, String module, boolean includeDisabled) throws BlacklistServiceException;
	
	public int getSearchBlacklistCount (String nric, String module, boolean includeDisabled) throws BlacklistServiceException;
	
	public List<BlacklistModule> getBlacklistModules () throws BlacklistServiceException;
	
	public boolean canAccess (String nric) throws BlacklistServiceException;
	
	public boolean isBlacklisted (String nric, String module) throws BlacklistServiceException;
	
	public PersonalNameDepartment getPersonalNameDepartment (String nric) throws BlacklistServiceException;
	
	public List<Blacklistee> getBlacklisteeByUnit (String module, String unit, String nric, boolean includeDisabled) throws BlacklistServiceException;

	public PersonalNameDepartment getPersonalNameDepartmentByNRICUnit(String nric, String unit) throws BlacklistServiceException;
}
