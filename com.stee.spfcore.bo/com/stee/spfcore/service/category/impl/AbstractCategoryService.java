package com.stee.spfcore.service.category.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.CategoryDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.category.SubCategory;
import com.stee.spfcore.service.category.CategoryServiceException;
import com.stee.spfcore.service.category.ICategoryService;

public abstract class AbstractCategoryService implements ICategoryService {

	protected static final Logger logger = Logger.getLogger(AbstractCategoryService.class.getName());
			
	protected CategoryDAO dao;
	
	protected AbstractCategoryService() {
		dao = new CategoryDAO();
	}
	
	@Override
	public Integer getCategoryCount(boolean includeDisabled) throws CategoryServiceException {
		Integer result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCategoryCount(includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get category count", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Category> getCategories(boolean includeDisabled) throws CategoryServiceException {
		List<Category> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCategories (includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get category", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public Category getCategory(String id) throws CategoryServiceException {
		Category result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCategory (id);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get category", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public Category getCategoryByName(String name) throws CategoryServiceException {
		Category result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCategoryByName(name);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get category by name", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public List<Category> getCategories(int pageNum, int pageSize, boolean includeDisabled)
			throws CategoryServiceException {
		
		List<Category> result = null;
		
		try {
			SessionFactoryUtil.beginTransaction();
			result = dao.getCategories (pageNum, pageSize, includeDisabled);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get categories", e);
			SessionFactoryUtil.rollbackTransaction();
		}
		
		return result;
	}

	@Override
	public SubCategory getSubCategory(String id) throws CategoryServiceException {
		
		// Is nested transaction if there is already an active transaction
		boolean isNestedTx = SessionFactoryUtil.isTransactionActive();

		SubCategory result = null;
		
		try {
			beginTransaction(isNestedTx);
			
			result = dao.getSubCategory(id);
			
			commitTransaction(isNestedTx);
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to get subcategory", e);
			rollbackTransaction(isNestedTx);
		}
		
		return result;
	}
	
	private void beginTransaction(boolean isNestedTx) {
		// Don't started new transaction if transaction already started.
		// Nested transaction not supported.
		if (!isNestedTx) {
			SessionFactoryUtil.beginTransaction();
		}
	}

	private void commitTransaction(boolean isNestedTx) {
		// Can not commit a nested transaction.
		// Let outer transaction commit transaction.
		if (!isNestedTx) {
			SessionFactoryUtil.commitTransaction();
		}
	}

	private void rollbackTransaction(boolean isNestedTx) {
		// Can not rollback nested transaction.
		// Let outer transaction rollback transaction.
		if (!isNestedTx) {
			SessionFactoryUtil.rollbackTransaction();
		}
	}
	
}
