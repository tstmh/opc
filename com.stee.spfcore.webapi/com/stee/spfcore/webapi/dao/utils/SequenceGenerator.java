package com.stee.spfcore.webapi.dao.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.stee.spfcore.webapi.utils.BeanUtil;

public class SequenceGenerator {
	
	private String sqlStatement;
	private String format;
	
	protected SequenceGenerator (String sqlStatement, String format) {
		this.sqlStatement = sqlStatement;
		this.format = format;
	}
	
	public String getNextSequenceValue () {
		return getNextSequenceValue(format);
	}
	
	public String getNextSequenceValue (String newFormat) {
		EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
		Session session = entityManager.unwrap(Session.class);
		
		JDBCWork work = new JDBCWork(newFormat);
		session.doWork(work);
		
		return work.getId();
	}

	private class JDBCWork implements Work {

		private String newFormat;
		private String id;
		
		public JDBCWork(String newFormat) {
			super();
			this.newFormat = newFormat;
		}

		@Override           
		public void execute(Connection connection) throws SQLException {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqlStatement);
			long value = 0;
			if (rs.next ()) {
				value = rs.getLong(1);
			}
			
			if (newFormat != null && !newFormat.trim().isEmpty()) {
				id = String.format (newFormat, value, new Date());
			}
			else {
				id = String.valueOf(value);
			}
		}

		public String getId() {
			return id;
		}
	}
	
}
