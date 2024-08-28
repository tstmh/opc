package com.stee.spfcore.webapi.dao.utils;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.stee.spfcore.webapi.config.EnvironmentConfig;

@Component
public class SequenceUtil {

private static final String DEFAULT_SCHEMA = "SPFCORE";
	
	private EnvironmentConfig config;
	
	private Map<Class<?>, SequenceGenerator> sequenceStatementMap;
	private Dialect dialect;
	
	@Autowired
	public SequenceUtil (EnvironmentConfig config) {
		
		this.config = config;
		sequenceStatementMap = new HashMap<Class<?>, SequenceGenerator> (); 
		System.out.println("config," + config.getEnvironmentType().toString());
//		if (config.isInternet()) {
//			dialect = new DB2Dialect();
//		}
//		else {
			dialect = new SQLServer2012Dialect();
//		}
		
		
	}
	
	public String getNextSequenceValue (Class<?> clazz) {
		
		SequenceGenerator generator = getSequenceStatement (clazz);
		
		return generator.getNextSequenceValue();
	}
	
	public String getNextSequenceValue (Class<?> clazz, String format) {
		
		SequenceGenerator generator = getSequenceStatement (clazz);
		
		return generator.getNextSequenceValue(format);
	}
	
	private synchronized SequenceGenerator getSequenceStatement (Class<?> clazz) {
		
		SequenceGenerator generator = sequenceStatementMap.get(clazz);
		
		if (generator != null) {
			return generator;
		}
		
		SequenceDef def = clazz.getAnnotation(SequenceDef.class);
		if (def == null) {
			throw new IllegalArgumentException("Sequence definition not found for the specified class.");
		}
		
		String schema = def.schema();
		if (schema == null || schema.trim().isEmpty()) {
			schema = DEFAULT_SCHEMA;
		}
		
		String name = def.name();
		if (name == null || name.trim().isEmpty()) {
			name = clazz.getSimpleName();
		}
		
		String sequenceName = schema + "." + name;
		
		String sqlStatement = dialect.getSequenceNextValString(sequenceName);
		
		String format = null;
		
		if (config.isInternet()) {
			format = def.internetFormat();
		}
		else {
			format = def.intranetFormat();
		}
		
		generator = new SequenceGenerator(sqlStatement, format);
		
		sequenceStatementMap.put(clazz, generator);
		
		return generator;
	}
	
}
