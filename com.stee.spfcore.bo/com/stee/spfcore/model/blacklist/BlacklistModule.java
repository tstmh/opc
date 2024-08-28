package com.stee.spfcore.model.blacklist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"BLACKLIST_MODULES\"", schema = "\"SPFCORE\"")
public class BlacklistModule {

	@Id
	@Column(name = "\"NAME\"", length = 50)
	private String name;

	@Column(name = "\"DISPLAY_NAME\"", length = 100)
	private String displayName;

	public BlacklistModule(String name, String displayName) {
		super();
		this.name = name;
		this.displayName = displayName;
	}

	public BlacklistModule() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
