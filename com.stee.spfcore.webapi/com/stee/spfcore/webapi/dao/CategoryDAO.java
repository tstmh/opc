package com.stee.spfcore.webapi.dao;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.category.Category;

@Repository
public class CategoryDAO {
	private static final Logger logger = LoggerFactory.getLogger(CalendarDAO.class);
	private EntityManager entityManager;
	
	private static final String SELECT_ONLY_ENABLED = "(v.effectiveDate is not null and v.effectiveDate <= current_date()) "
			+ " and (v.obsoleteDate is null or v.obsoleteDate > current_date())";

	@Autowired
	public CategoryDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<Category> getCategories(boolean includeDisabled) {
		logger.info("Get Categories");
		Session session = entityManager.unwrap(Session.class);

		StringBuilder builder = new StringBuilder("FROM Category v");

		if (!includeDisabled) {
			builder.append(" where ");
			builder.append(SELECT_ONLY_ENABLED);
			
			Filter filter = session.enableFilter("subcategoryFilter");
			filter.setParameter("subcategoryFilterParam", new Date ());
		}

		Query query = session.createQuery(builder.toString());

		return query.list();
	}
	
	public void addCategory (Category category) {
		logger.info("Add Category");
		Session session = entityManager.unwrap(Session.class);

		session.save(category);

		session.flush();
	}

	public void updateCategory(Category category) {
		logger.info("Update Category");
		Session session = entityManager.unwrap(Session.class);
		
		session.merge(category);

		session.flush();
	}

}
