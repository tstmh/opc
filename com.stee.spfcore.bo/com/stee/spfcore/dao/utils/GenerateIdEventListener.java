package com.stee.spfcore.dao.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

import com.stee.spfcore.model.annotation.GeneratedId;

public class GenerateIdEventListener implements SaveOrUpdateEventListener, MergeEventListener, PersistEventListener {

	private static final Logger logger = Logger.getLogger(GenerateIdEventListener.class.getName());

	private static final long serialVersionUID = -4869893100272379414L;

	private static final String MODEL_CLASS_NAME = "com.stee.spfcore.model.";

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("onSaveOrUpdate for entity:" + event.getEntityName());
		}

		setIdIfEmpty(event.getObject());
	}

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		// Do nothing
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("onMerge for entity:" + event.getEntityName());
		}
		setIdIfEmpty(event.getOriginal());
	}

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		// Do nothing
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("onPersist for entity:" + event.getEntityName());
		}
		setIdIfEmpty(event.getObject());
	}


	private void setIdIfEmpty(Object entity) {
		try {
			String className = entity.getClass().getName();

			if (className.startsWith(MODEL_CLASS_NAME)) {
				IdField idField = getIdField(entity);

				if (idField != null) {
					if (!idField.containsId()) {
						Class<?> seqClass = idField.getSequenceClass();
						if (seqClass == void.class) {
							seqClass = entity.getClass();
						}

						String id = SequenceUtil.getInstance().getNextSequenceValue(seqClass);

						FieldUtils.writeField (idField.getField(), entity, id, true );

						if (logger.isLoggable(Level.INFO)) {
							logger.info("Generated id for entity (" + className + "):" + id);
						}
					}
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new HibernateException("Unable to generate id", e);
		}
	}

	private IdField getIdField(Object entity) throws IllegalArgumentException, IllegalAccessException {

		Class<?> currentClass = entity.getClass();
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			for (final Field field : declaredFields) {
				GeneratedId generatedId = field.getAnnotation(GeneratedId.class);
				if (generatedId != null) {
					String value = (String) FieldUtils.readField (field, entity, true);

					if (logger.isLoggable(Level.FINEST)) {
						logger.finest("Id value for entity (" + currentClass.getName() + "):" + value);
					}

					return new IdField(field, generatedId, value);
				}
			}
			currentClass = currentClass.getSuperclass();
		}

		return null;
	}

	private class IdField {

		private Field field;
		private GeneratedId generatedId;
		private String value;

		public IdField(Field field, GeneratedId generatedId, String value) {
			super();
			this.field = field;
			this.generatedId = generatedId;
			this.value = value;

			if (!field.getType().isAssignableFrom(String.class)) {
				throw new IllegalArgumentException("Only String ID field is supported.");
			}
		}

		public boolean containsId() {
			return (value != null) && !value.trim().isEmpty();
		}

		public Field getField() {
			return field;
		}

		public Class<?> getSequenceClass() {
			return generatedId.sequenceDefinitionClass();
		}
	}


}
