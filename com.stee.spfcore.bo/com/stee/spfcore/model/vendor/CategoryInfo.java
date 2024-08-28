package com.stee.spfcore.model.vendor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"VENDOR_CATEGORIES\"", schema = "\"SPFCORE\"")
@XStreamAlias("VendorCategoryInfo")
@Audited
@SequenceDef (name="CategoryInfo_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class CategoryInfo {

	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;

	@Column(name = "\"CATEGORY_ID\"", length = 10)
	private String categoryId;

	@Formula("(select c.NAME from SPFCORE.CATEGORY_CATEGORIES c where c.ID = CATEGORY_ID)")
	@NotAudited
	@XStreamOmitField
	private String name;

	public CategoryInfo() {
		super();
	}

	public CategoryInfo(String categoryId) {
		super();
		this.categoryId = categoryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
