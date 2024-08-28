package com.stee.spfcore.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.BlacklistDAO;

@Service
public class BlacklistService {

	private BlacklistDAO blacklistDAO;
	
	@Autowired
	public BlacklistService ( BlacklistDAO blacklistDAO ) {
		this.blacklistDAO = blacklistDAO;
	}
	
	@Transactional
	public boolean isBlacklisted (String nric, String module) {
		return blacklistDAO.isBlacklisted(nric, module);
	}

}
