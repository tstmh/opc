package com.stee.spfcore.vo.code;

import com.stee.spfcore.model.code.CodeType;

public class CodeTypeIdPair {

	private CodeType type;
	
	private String id;

	public CodeTypeIdPair() {
		super();
	}

	public CodeTypeIdPair( CodeType type, String id ) {
		super();
		this.type = type;
		this.id = id;
	}

	public CodeType getType() {
		return type;
	}

	public void setType( CodeType type ) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CodeTypeIdPair [type=" + type + ", id=" + id + "]";
	}
}
