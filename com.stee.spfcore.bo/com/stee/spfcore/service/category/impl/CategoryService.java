package com.stee.spfcore.service.category.impl;

import java.util.logging.Level;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.service.category.CategoryServiceException;

public class CategoryService extends AbstractCategoryService {

	public CategoryService() {
		super();
	}

	@Override
	public void addCategory(Category category, String requester) throws CategoryServiceException {
		
		try {
			SessionFactoryUtil.beginTransaction();
			dao.addCategory (category, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to add category", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CategoryServiceException ("Fail to add category", e);
		}
		
	}

	@Override
	public void updateCategory(Category category, String requester) throws CategoryServiceException {
		try {
			SessionFactoryUtil.beginTransaction();
			dao.updateCategory(category, requester);
			
			SessionFactoryUtil.commitTransaction();
		} 
		catch (Exception e) {
			logger.log(Level.SEVERE, "Fail to save category", e);
			SessionFactoryUtil.rollbackTransaction();
			throw new CategoryServiceException ("Fail to save category", e);
		}
	}

}
