package com.stee.spfcore.service.category;

import java.util.List;

import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.category.SubCategory;

public interface ICategoryService {
	
	/**
	 * Add Category.
	 * @param category
	 * @param requester
	 */
	public void addCategory (Category category, String requester) throws CategoryServiceException;
	
	/**
	 * Update category
	 * @param category
	 * @param requester
	 */
	public void updateCategory(Category category, String requester) throws CategoryServiceException;
	
	/**
	 * Get category count
	 * @param includeDisabled if true, will include disabled categories. False, otherwise.
	 * @return
	 */
	public Integer getCategoryCount(boolean includeDisabled) throws CategoryServiceException;
	
	/**
	 * Retrieve all categories
	 * @param includeDisabled if true, will include disabled categories. False, otherwise.
	 * @return
	 */
	public List<Category> getCategories(boolean includeDisabled) throws CategoryServiceException;
	
	/**
	 * Retrieve category by id.
	 * @param id
	 * @return null if not found.
	 */
	public Category getCategory(String id) throws CategoryServiceException;
	
	/**
	 * Retrieve category by name. 
	 * @param name
	 * @return null if not found
	 */
	public Category getCategoryByName(String name) throws CategoryServiceException;
	
	/**
	 * Retrieve categories from the specified page of the specified page size.
	 * @param pageNum
	 * @param pageSize
	 * @param includeDisabled if true, will include disabled categories. False, otherwise.
	 * @return
	 */
	public List<Category> getCategories(int pageNum, int pageSize, boolean includeDisabled) throws CategoryServiceException;
	
	
	/**
	 * Retrieve sub category by ID
	 * @param id
	 * @return
	 * @throws CategoryServiceException
	 */
	public SubCategory getSubCategory (String id) throws CategoryServiceException;
	
}
