package com.stee.spfcore.webapi.service.process.impl;

public class ProcessUrl extends BaseUrl {

	private static String resource = "/process";
	
	protected ProcessUrl(String query) {
		super(resource, query);
	}
	
	protected ProcessUrl(String instanceId, String query) {
		super(resource + "/" + instanceId, query);
	}
}
