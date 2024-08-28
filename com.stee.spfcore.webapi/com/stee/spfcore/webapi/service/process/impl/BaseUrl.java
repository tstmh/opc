package com.stee.spfcore.webapi.service.process.impl;

import com.stee.spfcore.webapi.service.config.IProcessConfig;
import com.stee.spfcore.webapi.service.config.ServiceConfig;

public class BaseUrl {
	
	private static String contextroot = "/rest/bpm/wle";
	private static String version = "/v1";
	private static String contentType = "application/json";

	private String baseUrl;
	private String path;
	private String query;
	
	protected BaseUrl(String path, String query) {
		IProcessConfig config = ServiceConfig.getInstance().getProcessConfig();

		this.baseUrl = "http://" + config.hostname() + ":" + config.port();
		this.path = contextroot + version + path;
		this.query = "?" + query;
	}
	
	public String getUrl() {
		return baseUrl + this.path + this.query;
	}
	
	public String getBaseUrl() {
		return this.baseUrl;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public static String getContentType() {
		return contentType;
	}
}

