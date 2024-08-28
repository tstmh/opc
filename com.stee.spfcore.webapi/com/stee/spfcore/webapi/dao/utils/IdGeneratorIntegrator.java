package com.stee.spfcore.webapi.dao.utils;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorIntegrator implements Integrator {

	@Override
	public void disintegrate(SessionFactoryImplementor arg0, SessionFactoryServiceRegistry arg1) {
		//Do nothing
	}

	@Override
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
		
		final EventListenerRegistry eventListenerRegistry = sessionFactoryServiceRegistry.getService( EventListenerRegistry.class );
		
		GenerateIdEventListener listener = new GenerateIdEventListener();
		
	    eventListenerRegistry.prependListeners(EventType.SAVE_UPDATE, listener );
	    eventListenerRegistry.prependListeners(EventType.SAVE, listener );
	    eventListenerRegistry.prependListeners(EventType.MERGE, listener );
	    eventListenerRegistry.prependListeners(EventType.PERSIST_ONFLUSH, listener );
	    eventListenerRegistry.prependListeners(EventType.PERSIST, listener );
		
	}

}
