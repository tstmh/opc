package com.stee.spfcore.vo.code;

import java.util.List;

import com.stee.spfcore.model.code.CodeMapping;


public class SearchResult {
	
	private List<CodeMapping> codeMapping;
	
	private int totalCount;
	
	private int pageNum;
	
	private int pageSize;

	public SearchResult (List<CodeMapping> codeMapping, int totalCount, int pageNum, int pageSize) {
		super();
		this.codeMapping = codeMapping;
		this.totalCount = totalCount;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public SearchResult () {
		super();
	}

	
	public List<CodeMapping> getCodeMapping () {
		return codeMapping;
	}

	
	public void setCodeMapping (List<CodeMapping> codeMapping) {
		this.codeMapping = codeMapping;
	}

	
	public int getTotalCount () {
		return totalCount;
	}

	
	public void setTotalCount (int totalCount) {
		this.totalCount = totalCount;
	}

	
	public int getPageNum () {
		return pageNum;
	}

	
	public void setPageNum (int pageNum) {
		this.pageNum = pageNum;
	}

	
	public int getPageSize () {
		return pageSize;
	}

	
	public void setPageSize (int pageSize) {
		this.pageSize = pageSize;
	}
}
