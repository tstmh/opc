package com.stee.spfcore.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;

public class UserCreationDAO {

    @SuppressWarnings("unchecked")
	public List< String > getUserToAdd() {
	
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Query query = session.createQuery( "SELECT nric FROM Employment where (NOT serviceType = 000 and NOT serviceType = 111) and employmentStatus = 3" );

		return query.list();
	}
}
