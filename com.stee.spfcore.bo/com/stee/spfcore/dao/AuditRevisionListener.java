package com.stee.spfcore.dao;

import java.util.Date;

import org.hibernate.envers.RevisionListener;

public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object object) {
		
		final AuditRevisionEntity auditEntity = (AuditRevisionEntity) object;
		
		String user = SessionFactoryUtil.getUser();
		if (user == null) {
			user = "Unknown";
		}
		auditEntity.setUsername(user);
		auditEntity.setTimestamp(new Date());
	}

}
