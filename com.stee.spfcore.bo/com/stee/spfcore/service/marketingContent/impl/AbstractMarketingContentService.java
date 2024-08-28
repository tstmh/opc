package com.stee.spfcore.service.marketingContent.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.MarketingContentDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.marketingContent.internal.HtmlFile;
import com.stee.spfcore.service.marketingContent.IMarketingContentService;
import com.stee.spfcore.service.marketingContent.MarketingContentException;

public abstract class AbstractMarketingContentService implements IMarketingContentService {

	protected static final Logger logger = Logger.getLogger(AbstractMarketingContentService.class.getName());

	protected MarketingContentDAO dao;

	protected AbstractMarketingContentService() {
		dao = new MarketingContentDAO();
	}


	@Override
	public MarketingContentSet getMarketingContentSet(String id) throws MarketingContentException {
		MarketingContentSet result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingContentSet(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Marketing Content Set", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get Marketing Content Set", e);
		}

		return result;
	}

	@Override
	public MarketingContent getMarketingContent(String id) throws MarketingContentException {

		MarketingContent result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingContent(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Marketing Content Set", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get Marketing Content Set", e);
		}

		return result;
	}

	@Override
	public List<MarketingContent> getMarketingContents(String module) throws MarketingContentException {

		List<MarketingContent> result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingContents(module);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Marketing Contents by module", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get Marketing Contents by module", e);
		}

		return result;
	}


	@Override
	public List<MarketingContent> getMarketingContents(String module, boolean templateBased) throws MarketingContentException {

		List<MarketingContent> result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getMarketingContents(module, templateBased);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get Marketing Contents by module and whether is template", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get Marketing Contents by module and whether is template", e);
		}

		return result;
	}

	@Override
	public String generateId() throws MarketingContentException {

		String result;

		try {
			SessionFactoryUtil.beginTransaction();
			logger.log(Level.INFO, "inside generateid");
			result = dao.generateId();

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to generate id for Marketing Content", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to generate id for Marketing Content", e);
		}

		return result;
	}



	@Override
	public BinaryFile getBinaryFile(String id) throws MarketingContentException {
		BinaryFile result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBinaryFile(id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get binary file", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get binary file", e);
		}

		return result;
	}


	@Override
	public BinaryFile getBinaryFile(String nric, String id) throws MarketingContentException {
		BinaryFile result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getBinaryFile(nric, id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get binary file", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get binary file", e);
		}

		return result;
	}


	@Override
	public HtmlFile getHtmlFile(String nric, String id) throws MarketingContentException {
		HtmlFile result;

		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getHtmlFile(nric, id);

			SessionFactoryUtil.commitTransaction();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get html file", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new MarketingContentException ("Fail to get html file", e);
		}

		return result;
	}


}
