package com.stee.spfcore.model.course;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Supervisor {

	@Column( name = "\"SUPERVISOR_NAME\"", length = 255 )
	private String name;
	
	@Column( name = "\"SUPERVISOR_EMAIL\"", length = 256 )
	private String email;

	public Supervisor() {
		super();
	}

	public Supervisor(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
