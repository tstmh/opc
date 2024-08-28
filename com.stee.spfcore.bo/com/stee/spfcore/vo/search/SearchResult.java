package com.stee.spfcore.vo.search;

public class SearchResult 
{
	private String title;
	private String link;
	private String summary;
	
	public SearchResult() 
	{
		super();
	}

	public SearchResult(String title, String link, String summary) 
	{
		super();
		this.title = title;
		this.link = link;
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
