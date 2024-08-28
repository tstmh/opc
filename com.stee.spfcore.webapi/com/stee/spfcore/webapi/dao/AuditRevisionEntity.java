package com.stee.spfcore.webapi.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "\"AUDIT_REVISION\"", schema = "\"SPFCORE\"")
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevisionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@RevisionNumber
	@Column(name="\"ID\"")
	private int id;
	
	@RevisionTimestamp
	@Column(name="\"TIMESTAMP\"")
	private Date timestamp;
	
	@Column(name="\"USERNAME\"")
	private String username;

	public AuditRevisionEntity () {}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

}
