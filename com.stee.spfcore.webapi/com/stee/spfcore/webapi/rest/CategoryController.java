package com.stee.spfcore.webapi.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stee.spfcore.webapi.model.UserSessionUtil;
import com.stee.spfcore.webapi.model.category.Category;
import com.stee.spfcore.webapi.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	private CategoryService service;
	
	@Autowired
	public CategoryController(CategoryService service) {
		this.service = service;
	}
	
	@GetMapping("/getCategories")
	public List<Category> getCategories(@RequestParam boolean includeDisabled) {
		
		List<Category> result = service.getCategories(includeDisabled); 
		return result;	
	}
	
	@PostMapping("/addCategory")
	public void addCategory(@RequestHeader("requester") String requester, @RequestBody Category category) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.addCategory(category);
	}
	
	@PostMapping("/updateCategory")
	public void updateCategory(@RequestHeader("requester") String requester, @RequestBody Category category) {
		requester = (requester != null) ? requester : "";
		UserSessionUtil.setUser(requester);
		service.updateCategory(category);
	}
	
}
