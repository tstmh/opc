package com.stee.spfcore.service.process.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import com.stee.spfcore.service.process.ProcessServiceException;

public class StartProcessUrl extends ProcessUrl {

	private static final Logger logger = Logger.getLogger(StartProcessUrl.class.getName());

	public StartProcessUrl(String bpdId, String branchId) throws ProcessServiceException {
		super(getQuery(bpdId, branchId));
	}

	public StartProcessUrl(String bpdId, String branchId, String snapshotId, JSONObject json) throws ProcessServiceException {
		super(getQuery(bpdId, branchId, snapshotId, json));
	}
	
	private static String getQuery(String bpdId, String branchId) throws ProcessServiceException {
		
		try {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("action", "start"));
			params.add(new BasicNameValuePair("bpdId", bpdId));
			params.add(new BasicNameValuePair("branchId", branchId));
			params.add(new BasicNameValuePair("parts", "all"));
			params.add(new BasicNameValuePair("x-method-override", "POST"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
			logger.log(Level.ALL,"Added x-method override");
			return EntityUtils.toString(entity);
		}
		catch (ParseException | IOException exception) {
			throw new ProcessServiceException("Fail to convert query to string:" + exception);
		}
	}
	
	private static String getQuery(String bpdId, String branchId, String snapshotId, JSONObject json) throws ProcessServiceException {
			
		try {
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("action", "start"));
			params.add(new BasicNameValuePair("bpdId", bpdId));
			
			if (snapshotId != null && !snapshotId.trim().isEmpty()) {
				params.add(new BasicNameValuePair("snapshotId", snapshotId));
			}
			
			if (branchId != null && !branchId.trim().isEmpty()) {
				params.add(new BasicNameValuePair("branchId", branchId));
			}
			
			params.add(new BasicNameValuePair("params", json.toString()));
			params.add(new BasicNameValuePair("parts", "all"));
			params.add(new BasicNameValuePair("x-method-override", "POST"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
		
			return EntityUtils.toString(entity);
		}
		catch (ParseException | IOException exception) {
			throw new ProcessServiceException("Fail to convert query to string:" + exception);
		}
	}
}
