package com.stee.spfcore.model.benefits;

import java.util.ArrayList;
import java.util.List;

import com.stee.spfcore.model.internal.ApplicationType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("RequestDocuments")
public class RequestDocuments {

	private String referenceNumber;
	private ApplicationType type;

	@XStreamImplicit(itemFieldName="filename")
	private List<String> filenames;
	
	public RequestDocuments() {
		filenames = new ArrayList<>();
	}

	public RequestDocuments(String referenceNumber, ApplicationType type,
			List<String> filenames) {
		this.referenceNumber = referenceNumber;
		this.type = type;
		this.filenames = filenames;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public ApplicationType getType() {
		return type;
	}

	public List<String> getFilenames() {
		return filenames;
	}
}
