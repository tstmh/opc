package com.stee.spfcore.webapi.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stee.spfcore.webapi.model.internal.ProcessingInfo;

@Repository
public class ProcessingInfoDAO {
	
	private EntityManager entityManager;
	
	@Autowired
	public ProcessingInfoDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void saveProcessingInfo ( ProcessingInfo info ) {
		Session session = entityManager.unwrap(Session.class);
		
		session.saveOrUpdate(info);
	}
	
}
