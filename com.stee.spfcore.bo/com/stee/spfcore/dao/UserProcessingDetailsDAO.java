package com.stee.spfcore.dao;

import org.hibernate.Session;

import com.stee.spfcore.model.UserProcessingDetails;


public class UserProcessingDetailsDAO {

	public UserProcessingDetails getUserProcessingDetails (String id) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		return (UserProcessingDetails) session.get(UserProcessingDetails.class, id);
	}
	
	public void saveUserProcessingDetails (UserProcessingDetails details) {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		session.saveOrUpdate( details );

	}	
	
}


