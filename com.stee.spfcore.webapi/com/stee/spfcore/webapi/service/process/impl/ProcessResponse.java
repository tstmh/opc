package com.stee.spfcore.webapi.service.process.impl;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.stee.spfcore.webapi.service.process.ProcessServiceException;

public class ProcessResponse {

	private static final Logger logger = Logger.getLogger(ProcessResponse.class.getName());

	private JSONObject data;
	
	public ProcessResponse(String response) throws ProcessServiceException {
		
		try {
			JSONObject json = new JSONObject(new JSONTokener(response));
			this.data = json.getJSONObject("data");
		}
		catch (JSONException exception) {
			logger.log (Level.SEVERE, "Fail to process response:", exception);
			throw new ProcessServiceException("Fail to process response:" + exception);
		}
	}
	
	public String getInstanceId() throws ProcessServiceException {
		try {
			return this.data.getString("piid");
		}
		catch (JSONException exception) {
			logger.log (Level.SEVERE, "Fail to process response:", exception);
			throw new ProcessServiceException("Fail to process response:" + exception);
		}
	}
}
