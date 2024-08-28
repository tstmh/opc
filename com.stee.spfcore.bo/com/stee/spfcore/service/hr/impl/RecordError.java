package com.stee.spfcore.service.hr.impl;

public class RecordError {

	private String id;
	private String code;
	private String tag;
	private String desc;

	public RecordError(String id, String code,String desc, String tag) {
		this.id = id;
		this.code = code;
		this.desc = desc;
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
