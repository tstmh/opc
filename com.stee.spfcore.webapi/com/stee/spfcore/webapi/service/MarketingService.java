package com.stee.spfcore.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.MarketingDAO;


@Service
public class MarketingService {

	private MarketingDAO marketingDAO;
	
	@Autowired
	public MarketingService (MarketingDAO marketingDAO) {
		this.marketingDAO = marketingDAO;
	}
	
	@Transactional
	public List<String> getMemberInGroup(String groupId) {
		return marketingDAO.getMemberInGroup(groupId);
	}
	
	
	

	
}
