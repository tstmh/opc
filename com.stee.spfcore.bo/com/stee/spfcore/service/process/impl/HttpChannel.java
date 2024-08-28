package com.stee.spfcore.service.process.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

import com.stee.spfcore.service.process.ProcessServiceException;

public class HttpChannel {

	private static final Logger logger = Logger.getLogger(HttpChannel.class.getName());

	private char [] encodedCredential;

	public HttpChannel(String username, String password) {
		String credential = username + ":" + password;
		this.encodedCredential = new String(Base64.encodeBase64(credential.getBytes(StandardCharsets.ISO_8859_1))).toCharArray();
	}

	public String post(String url, String acceptType) throws ProcessServiceException {
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HttpHeaders.ACCEPT, acceptType);
		httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + String.valueOf(encodedCredential));
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
		CloseableHttpResponse response = null;
		
		try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
					logger.warning(String.format("Failed : Request line : %s", httpPost.getRequestLine()));
				}
				throw new ProcessServiceException("Failed : HTTP error code : " + statusCode);
			}

			return EntityUtils.toString(response.getEntity());
		}
		catch (MalformedURLException exception) {
			throw new ProcessServiceException("Failed to invoke POST Service: " + httpPost.getRequestLine(), exception);
		}
		catch (IOException exception) {
			throw new ProcessServiceException("Failed to invoke POST Service: " + httpPost.getRequestLine(), exception);
		}
		finally {
			try {
				if (response != null) {
					response.close();
				}
			}
			catch (IOException exception) {
				logger.log(Level.INFO, "Fail to close HttpResponse", exception);
			}
			
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			}
			catch (IOException exception) {
				logger.log(Level.INFO, "Fail to close HttpClient", exception);
			}
		}
	}
	
}
