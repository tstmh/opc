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

import com.stee.spfcore.service.process.ProcessServiceException;

public class AddDocumentUrl extends ProcessInstanceUrl {

    static {
        Logger.getLogger(AddDocumentUrl.class.getName());
    }

    public AddDocumentUrl(String instanceId, String documentName) throws ProcessServiceException {
		super(instanceId, getQuery(documentName));
	}
	
	private static String getQuery(String documentName) throws ProcessServiceException {
		
		try {		
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("action", "addDocument"));
			params.add(new BasicNameValuePair("name", documentName));
			params.add(new BasicNameValuePair("docType", "file"));
			params.add(new BasicNameValuePair("parts", "all"));
			params.add(new BasicNameValuePair("accept", "application/json"));
			params.add(new BasicNameValuePair("override-content-type", "text/plain"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
		
			return EntityUtils.toString(entity);
		}
		catch (ParseException | IOException exception) {
			throw new ProcessServiceException("Fail to convert query to string:" + exception);
		}
	}
}
