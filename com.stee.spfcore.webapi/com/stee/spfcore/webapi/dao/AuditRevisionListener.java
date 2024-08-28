package com.stee.spfcore.webapi.dao;

import java.util.Date;

import org.hibernate.envers.RevisionListener;

import com.stee.spfcore.webapi.model.UserSessionUtil;

public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object object) {
		
		final AuditRevisionEntity auditEntity = (AuditRevisionEntity) object;
		
		String user = UserSessionUtil.getUser();
		auditEntity.setUsername(user);
		auditEntity.setTimestamp(new Date());
		
	}

}
