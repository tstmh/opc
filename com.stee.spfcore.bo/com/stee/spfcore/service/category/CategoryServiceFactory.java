package com.stee.spfcore.service.category;

import com.stee.spfcore.service.category.impl.CategoryService;

public class CategoryServiceFactory {

	private CategoryServiceFactory(){}
	private static ICategoryService instance;
	
	public static synchronized ICategoryService getInstance () {
		
		if (instance == null) {
			instance = createCategoryService ();
		}
		return instance;
	}
	
	
	private static ICategoryService createCategoryService () {

		return new CategoryService();
	}
	
}
