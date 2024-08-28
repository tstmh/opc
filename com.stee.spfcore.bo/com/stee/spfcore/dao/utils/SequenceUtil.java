package com.stee.spfcore.dao.utils;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2012Dialect;

import com.stee.spfcore.model.annotation.SequenceDef;
import com.stee.spfcore.utils.EnvironmentUtils;

public class SequenceUtil {

	private static final String DEFAULT_SCHEMA = "SPFCORE";

	private static SequenceUtil instance;

	public synchronized static SequenceUtil getInstance () {

		if (instance == null) {
			instance = createSequenceUtil ();
		}

		return instance;
	}

	private static SequenceUtil createSequenceUtil () {
		return new SequenceUtil();
	}


	private Map<Class<?>, SequenceGenerator> sequenceStatementMap;
	private Dialect dialect;

	private SequenceUtil () {
		sequenceStatementMap = new HashMap<Class<?>, SequenceGenerator> ();
		if (EnvironmentUtils.isInternet()) {
			dialect = new DB2Dialect();
		}
		else {
			dialect = new SQLServer2012Dialect();
		}
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

		if (EnvironmentUtils.isInternet()) {
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
