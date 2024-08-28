package com.stee.spfcore.portal.usermgr.model;

/**
 * Actual deployment use singpass instead of portal account.
 *
 */
public class User {

	private String username;
	private String name;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
