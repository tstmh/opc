package com.stee.spfcore.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.MarketingContentDAO;
import com.stee.spfcore.webapi.model.marketingContent.BinaryAttachment;
import com.stee.spfcore.webapi.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.webapi.model.marketingContent.internal.HtmlFile;
import com.stee.spfcore.webapi.model.marketingContent.internal.UserContentViewRecord;

@Service
public class MarketingContentService {

	private MarketingContentDAO marketingContentDAO;
	
	@Autowired
	public MarketingContentService (MarketingContentDAO marketingContentDAO) {
		this.marketingContentDAO = marketingContentDAO;
	}
	
	@Transactional
	public void saveUserContentViewRecord(UserContentViewRecord record) {
		marketingContentDAO.saveUserContentViewRecord(record);
	}
	
	@Transactional
	public BinaryFile getBinaryFile (String nric, String contentId) {
		return marketingContentDAO.getBinaryFile(nric, contentId);
	}
	
	@Transactional
	public HtmlFile getHtmlFile (String nric, String contentId) {
		return marketingContentDAO.getHtmlFile(nric, contentId);
	}

	@Transactional
	public List<BinaryAttachment> getAttachments(String contentId) {
		return marketingContentDAO.getAttachments(contentId);
	}

	@Transactional
	public UserContentViewRecord getUserContentViewRecord(String id) {
		return marketingContentDAO.getUserContentViewRecord(id);
	}
	
	
}
