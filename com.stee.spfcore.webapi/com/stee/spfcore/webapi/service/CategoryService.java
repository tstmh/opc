package com.stee.spfcore.webapi.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stee.spfcore.webapi.dao.CategoryDAO;
import com.stee.spfcore.webapi.model.category.Category;
@Service
public class CategoryService {

	private CategoryDAO categoryDAO;
	
	private static final Logger logger = Logger.getLogger(CategoryService.class.getName());
	
	@Autowired
	public CategoryService ( CategoryDAO categoryDAO ) {
		this.categoryDAO = categoryDAO;
	}
	
	@Transactional
	public List<Category> getCategories ( boolean includeDisabled ) {
		return categoryDAO.getCategories (includeDisabled);
	}
	
	@Transactional
	public void addCategory ( Category category ) {
		categoryDAO.addCategory (category);
	}
	
	@Transactional
	public void updateCategory ( Category category ) {
		categoryDAO.updateCategory (category);
	}
	
}
