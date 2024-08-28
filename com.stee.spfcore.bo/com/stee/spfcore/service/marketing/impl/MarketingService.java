package com.stee.spfcore.service.marketing.impl;

import java.util.List;
import java.util.logging.Level;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.service.marketing.MarketingServiceException;

public class MarketingService extends AbstractMarketingService {

	@Override
	public String addMemberGroup(MemberGroup group, String requester) throws MarketingServiceException {
		
		String id;
		try {
			SessionFactoryUtil.beginTransaction();
			id = dao.addMemberGroup (group, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add member goup", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingServiceException("Fail to add member goup", e);
		}
		
		return id;
	}

	@Override
	public void updateMemberGroup(MemberGroup group, String requester) throws MarketingServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateMemberGroup (group, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to update member group", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingServiceException("Fail to update member group", e);
		}
		
	}

	@Override
	public List<MemberGroup> getMemberGroupsOfAnnouncement(String module, boolean template, List<String> announcementIds) throws MarketingServiceException {
		List<MemberGroup> groups;
		try {
			SessionFactoryUtil.beginTransaction();
			groups = dao.getMemberGroupsOfAnnouncement(module, template, announcementIds);
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get unit member goup", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingServiceException("Fail to get unit member goup", e);
		}
		
		return groups;
	}
	
	@Override
	public List<MemberGroup> getMemberGroupsByAnnoucementIds (boolean includeDisabled, List<String> announcementIds) throws MarketingServiceException {
		List<MemberGroup> groups;
		try {
			SessionFactoryUtil.beginTransaction();
			groups = dao.getMemberGroupsByAnnoucementIds(includeDisabled, announcementIds);
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get unit member goup", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingServiceException("Fail to get unit member goup", e);
		}
		
		return groups;
	}
	@Override
	public List<MemberGroup> getMemberGroupByIds (List<String> memberGroupIds, boolean includeDisabled) throws MarketingServiceException {
		List<MemberGroup> groups;
		
		try {
			SessionFactoryUtil.beginTransaction();
			groups = dao.getMemberGroupsByIds(memberGroupIds, includeDisabled);
			SessionFactoryUtil.commitTransaction();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get unit member group by Ids", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingServiceException("Fail to get unit member group by Ids", e);
		}
		return groups;
	}
}
