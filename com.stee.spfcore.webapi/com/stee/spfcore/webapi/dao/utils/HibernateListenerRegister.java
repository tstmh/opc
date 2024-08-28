package com.stee.spfcore.webapi.dao.utils;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateListenerRegister {
	
	private static final Logger logger = Logger.getLogger(HibernateListenerRegister.class.getName());
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private GenerateIdEventListener listener;
	
	@PostConstruct
	public void registerListeners() {
		
		logger.info("Registering Hibernate Listener..");
		
		SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
		EventListenerRegistry eventListenerRegistry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class);
	    eventListenerRegistry.prependListeners(EventType.SAVE_UPDATE, listener );
	    eventListenerRegistry.prependListeners(EventType.SAVE, listener );
	    eventListenerRegistry.prependListeners(EventType.MERGE, listener );
	    eventListenerRegistry.prependListeners(EventType.PERSIST_ONFLUSH, listener );
	    eventListenerRegistry.prependListeners(EventType.PERSIST, listener );
	}
	
}
