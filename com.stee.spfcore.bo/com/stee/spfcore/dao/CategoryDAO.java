package com.stee.spfcore.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.category.SubCategory;

public class CategoryDAO {
	
	private static final String SELECT_ONLY_ENABLED = "(v.effectiveDate is not null and v.effectiveDate <= current_date()) "
			+ " and (v.obsoleteDate is null or v.obsoleteDate > current_date())";
	private static final String WHERE_CLAUSE = " where ";

	
	public void addCategory (Category category, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.save(category);

		session.flush();
	}

	public void updateCategory(Category category, String requester) {

		SessionFactoryUtil.setUser(requester);

		Session session = SessionFactoryUtil.getCurrentSession();

		session.merge(category);

		session.flush();
	}

	public Integer getCategoryCount(boolean includeDisabled) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("SELECT count(v) FROM Category v");

		if (!includeDisabled) {
			builder.append(WHERE_CLAUSE);
			builder.append(SELECT_ONLY_ENABLED);
		}

		Query query = session.createQuery(builder.toString());

		return ((Number) query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Category> getCategories(boolean includeDisabled) {

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Category v");

		if (!includeDisabled) {
			builder.append(WHERE_CLAUSE);
			builder.append(SELECT_ONLY_ENABLED);
			
			Filter filter = session.enableFilter("subcategoryFilter");
			filter.setParameter("subcategoryFilterParam", new Date ());
		}

		Query query = session.createQuery(builder.toString());

		return query.list();
	}

	public Category getCategory(String id) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (Category) session.get(Category.class, id);
	}

	public Category getCategoryByName(String name) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Query query = session.createQuery("FROM Category v where v.name like :name");
		query.setMaxResults(1);
		
		query.setParameter("name", name);

		List<?> list = query.list();
		if (!list.isEmpty()) {
			return (Category) list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Category> getCategories(int pageNum, int pageSize, boolean includeDisabled) {

		int index = pageSize * (pageNum - 1);

		Session session = SessionFactoryUtil.getCurrentSession();

		StringBuilder builder = new StringBuilder("FROM Category v");

		if (!includeDisabled) {
			builder.append(WHERE_CLAUSE);
			builder.append(SELECT_ONLY_ENABLED);
			
			Filter filter = session.enableFilter("subcategoryFilter");
			filter.setParameter("subcategoryFilterParam", new Date ());
		}

		Query query = session.createQuery(builder.toString());
		query.setFirstResult(index);
		query.setMaxResults(pageSize);

		return query.list();
	}
	
	
	public SubCategory getSubCategory(String id) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (SubCategory) session.get(SubCategory.class, id);
	}
	
	
}
