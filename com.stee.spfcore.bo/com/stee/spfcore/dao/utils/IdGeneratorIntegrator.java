package com.stee.spfcore.dao.utils;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class IdGeneratorIntegrator implements Integrator {

	@Override
	public void disintegrate(SessionFactoryImplementor arg0, SessionFactoryServiceRegistry arg1) {
		//Do nothing
	}

	@Override
	public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

		
		final EventListenerRegistry eventListenerRegistry = sessionFactoryServiceRegistry.getService( EventListenerRegistry.class );
		
		GenerateIdEventListener listener = new GenerateIdEventListener();
		
    eventListenerRegistry.prependListeners(EventType.SAVE_UPDATE, listener );
    eventListenerRegistry.prependListeners(EventType.SAVE, listener );
    eventListenerRegistry.prependListeners(EventType.MERGE, listener );
    eventListenerRegistry.prependListeners(EventType.PERSIST_ONFLUSH, listener );
    eventListenerRegistry.prependListeners(EventType.PERSIST, listener );
  }

	@Override
	public void integrate(MetadataImplementor arg0, SessionFactoryImplementor arg1, SessionFactoryServiceRegistry arg2) {
		//Do nothing
	}

}
