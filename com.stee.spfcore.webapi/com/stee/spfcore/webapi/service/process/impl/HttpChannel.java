package com.stee.spfcore.webapi.service.process.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import com.stee.spfcore.webapi.service.process.ProcessServiceException;

public class HttpChannel {

	private static final Logger logger = LoggerFactory.getLogger(HttpChannel.class);

	private char [] encodedCredential;

	public HttpChannel(String username, String password) {
		String credential = username + ":" + password;
		this.encodedCredential = new String(Base64.encodeBase64(credential.getBytes(Charset.forName("ISO-8859-1")))).toCharArray();
	}

	public String post(String url, String acceptType, String contentType) throws ProcessServiceException {
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HttpHeaders.ACCEPT, acceptType);
		httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + String.valueOf(encodedCredential));
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		CloseableHttpResponse response = null;
		
		try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode != 200 && statusCode != 201) {
				logger.warn("Failed : HTTP error code : " + statusCode);
				logger.warn("Failed : Request line : " + httpPost.getRequestLine());
				throw new ProcessServiceException("Failed : HTTP error code : " + statusCode);
			}

			return EntityUtils.toString(response.getEntity());
		}
		catch (MalformedURLException exception) {
			logger.warn("Malformed URL:" + httpPost.getRequestLine(), exception);
			throw new ProcessServiceException("Failed to invoke POST Service: " + httpPost.getRequestLine(), exception);
		}
		catch (IOException exception) {
			logger.warn("Fail to get response from " + httpPost.getRequestLine(), exception);
			throw new ProcessServiceException("Failed to invoke POST Service: " + httpPost.getRequestLine(), exception);
		}
		finally {
			try {
				if (response != null) {
					response.close();
				}
			}
			catch (IOException exception) {
				logger.info("Fail to close HttpResponse", exception);
			}
			
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			}
			catch (IOException exception) {
				logger.info("Fail to close HttpClient", exception);
			}
		}
	}
	
}
