package com.stee.spfcore.model.marketingContent;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "MARKETING_CONTENT_SETS", schema = "SPFCORE")
@XStreamAlias("MarketingContentSet")
@Audited
@SequenceDef (name="MarketingContent_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class MarketingContentSet {

	@Id
	@Column(name = "\"ID\"")
	private String id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "\"SET_ID\"")
	@Fetch(value = FetchMode.SUBSELECT)
	@OrderColumn(name = "\"LIST_ORDER\"")
	private List<MarketingContent> contents;

	public MarketingContentSet() {
		super();
	}

	public MarketingContentSet(String id, List<MarketingContent> contents) {
		super();
		this.id = id;
		this.contents = contents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<MarketingContent> getContents() {
		return contents;
	}

	public void setContents(List<MarketingContent> contents) {
		this.contents = contents;
	}

}
