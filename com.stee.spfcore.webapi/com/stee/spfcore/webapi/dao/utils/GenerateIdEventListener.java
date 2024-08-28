package com.stee.spfcore.webapi.dao.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;

@Component
public class GenerateIdEventListener implements SaveOrUpdateEventListener, MergeEventListener, PersistEventListener {

	private static final Logger logger = LoggerFactory.getLogger(GenerateIdEventListener.class);

	private static final long serialVersionUID = -4869893100272379414L;

	private static final String MODEL_CLASS_NAME = "com.stee.spfcore.webapi.model.";
	
	@Autowired
	private SequenceUtil sequenceUtil;
	
	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {

		//logger.info("onSaveOrUpdate for entity:" + event.getEntityName());
		
		
		setIdIfEmpty(event.getObject());
	}

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		// Do nothing
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {

		//logger.info("onMerge for entity:" + event.getEntityName());
		
		setIdIfEmpty(event.getOriginal());
	}

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		// Do nothing
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {

		//logger.info("onPersist for entity:" + event.getEntityName());
		
		setIdIfEmpty(event.getObject());
	}
	
	
	private void setIdIfEmpty(Object entity) {
		try {
			//logger.info("setIdIfEmpty running");
			String className = entity.getClass().getName();
			//logger.info("className>>>" + className);

			if (className.startsWith(MODEL_CLASS_NAME)) {
				IdField idField = getIdField(entity);
				if (idField != null) {
					//logger.info("idField>>>>>>>" + idField.toString());
					if (!idField.containsId()) {
						Class<?> seqClass = idField.getSequenceClass();
						if (seqClass == void.class) {
							seqClass = entity.getClass();
						}
						
						//String id = SequenceUtil.getInstance().getNextSequenceValue(seqClass);
						String id = sequenceUtil.getNextSequenceValue(seqClass);
						//logger.info("id>>>>>"+id);
						FieldUtils.writeField (idField.getField(), entity, id, true );
						

						//logger.info("Generated id for entity (" + className + "):" + id);
						
					}
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new HibernateException("Unable to generate id", e);
		}
	}

	private IdField getIdField(Object entity) throws IllegalArgumentException, IllegalAccessException {
		//logger.info("Hi im running");
		Class<?> currentClass = entity.getClass();
		//logger.info("currentClass>>>>"+currentClass.toString());
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			//logger.info("declaredFields size>>>>>" + declaredFields.length);
			for (final Field field : declaredFields) {
				//logger.info("field>>>" + field.toString());
				GeneratedId generatedId = field.getAnnotation(GeneratedId.class);
				for (Annotation annotation : field.getAnnotations() ) {
					if (annotation != null) {
						//logger.info(annotation.toString());
						//logger.info(annotation.annotationType().getName());
					}
				}
				
				
				//logger.info("generatedId.class>>>" + GeneratedId.class.toString());
				if (generatedId != null) {
					//logger.info("generatedId getclass getname"+generatedId.getClass().getName());
					//logger.info(FieldUtils.getAllFields(field));
					String value = (String) FieldUtils.readField (field, entity, true);
					//logger.info(field.getName());
					//logger.info("value>>>"+value);

					//logger.info("Id value for entity (" + currentClass.getName() + "):" + value);
					
					
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
