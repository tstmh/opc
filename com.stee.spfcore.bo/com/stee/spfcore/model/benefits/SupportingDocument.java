package com.stee.spfcore.model.benefits;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name="\"APPLICATION_SUPPORTING_DOCUMENT\"", schema="\"SPFCORE\"")
@XStreamAlias("SupportingDocument")
@Audited
public class SupportingDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="\"ID\"")
	@XStreamOmitField
	private long id;
	
	@Column(name="\"DOCUMENT_NAME\"", length=200)
	private String documentName;

	@Column(name="\"DOCUMENT_TYPE\"", length=50)
	private String documentType;

	@Column(name="\"OTHER_DOCUMENT_TYPE\"", length=100)
	private String otherDocumentType;

	@Column(name="\"FEB_ID\"", length=50)
	private String febId;

	@Column(name="\"BPM_ID\"", length=50)
	private String bpmId;

	public SupportingDocument() {	
	}

	public SupportingDocument(String documentName,
			String documentType, String otherDocumentType, String febId,
			String bpmId) {
		super();
		this.documentName = documentName;
		this.documentType = documentType;
		this.otherDocumentType = otherDocumentType;
		this.febId = febId;
		this.bpmId = bpmId;
	}

	public long getId() {
		return id;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getOtherDocumentType() {
		return otherDocumentType;
	}

	public void setOtherDocumentType(String otherDocumentType) {
		this.otherDocumentType = otherDocumentType;
	}

	public String getFebId() {
		return febId;
	}

	public void setFebId(String febId) {
		this.febId = febId;
	}

	public String getBpmId() {
		return bpmId;
	}

	public void setBpmId(String bpmId) {
		this.bpmId = bpmId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupportingDocument other = (SupportingDocument) obj;
		return (id == other.id);
	}
}
