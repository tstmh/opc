package com.stee.spfcore.webapi.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.ReferenceGenerator;
import com.stee.spfcore.webapi.model.internal.ApplicationType;

@Repository
public class ReferenceGeneratorDAO {
	
	private EntityManager entityManager;
	
	@Autowired
	public ReferenceGeneratorDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public ReferenceGenerator getReferenceGenerator(ApplicationType applicationType) {
		Session session = entityManager.unwrap(Session.class);
		
		return (ReferenceGenerator) session.get(ReferenceGenerator.class, applicationType.toString());
	}
	
	public void addReferenceGenerator(ReferenceGenerator referenceGenerator) {
		Session session = entityManager.unwrap(Session.class);
		
		session.save(referenceGenerator);
		session.flush();
	}
	
	public void updateReferenceGenerator(ReferenceGenerator referenceGenerator) {
		Session session = entityManager.unwrap(Session.class);
		
		session.merge(referenceGenerator);
		session.flush();
	}
}
