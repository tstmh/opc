package com.stee.spfcore.utils.rest;

import com.stee.spfcore.utils.Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RESTInvoker {

	private static final Logger logger = Logger.getLogger(RESTInvoker.class.getName());

	private static final String USER_AGENT = "Mozilla/5.0";
	
	private String baseUrl;
	private char [] authHeader;

	public RESTInvoker(String baseUrl, String username, String password) {
		this.baseUrl = baseUrl;
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
		authHeader = ("Basic " + new String(encodedAuth)).toCharArray();
	}
	
	public RESTInvoker(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String get(String path, String acceptType) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			httpClient = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();

			HttpGet httpGet = new HttpGet(baseUrl + path);
			httpGet.setHeader(HttpHeaders.ACCEPT, acceptType);
			
			if (authHeader != null) {
				httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				throw new RESTInvocationException ("Failed : HTTP error code : " + statusCode);
			}

			return EntityUtils.toString(response.getEntity());

		} catch (IOException e) {
			throw new RESTInvocationException ("Failed to invoke GET REST Service: " + baseUrl + path, e);

		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}
	}

	public String post(String path, String acceptType, String contentType, String content) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			httpClient = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();

			HttpPost httpPost = new HttpPost(baseUrl + path);
			httpPost.setHeader(HttpHeaders.ACCEPT, acceptType);
			
			if (authHeader != null) {
				httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

			httpPost.setEntity(new StringEntity(content));

			response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
				}
				throw new RESTInvocationException ("Failed : HTTP error code : " + statusCode);
			}

			return EntityUtils.toString(response.getEntity());

		} catch (IOException e) {
			throw new RESTInvocationException ("Failed to invoke GET REST Service: " + baseUrl + path, e);

		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}

	}

	public void getAsFile(String path, String folder, String filename) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		FileOutputStream out = null;

		try {
			// Somehow the Server return a content coding that HttpClient doesn't support when
			// this method is invoked in EJB. Hence, need to disable content compression.
			httpClient = HttpClientBuilder.create().setUserAgent(USER_AGENT).disableContentCompression().build();

			if ( logger.isLoggable( Level.INFO ) ) {
				logger.info(String.format("getAsFile(), baseUrl=%s, path=%s, folder=%s, filename=%s", baseUrl, path, folder, filename));
			}
			HttpGet httpGet = new HttpGet(baseUrl + path);
			
			if (authHeader != null) {
				httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
				}
				throw new RESTInvocationException ("Failed : HTTP error code : " + statusCode);
			}

			File file = new File(folder, filename);
			out = new FileOutputStream(file, false);

			response.getEntity().writeTo(out);
		} catch (MalformedURLException e) {
			logger.warning(String.format("Malformed URL: %s %s %s", baseUrl, Util.replaceNewLine( path ), e));
		} catch (IOException e) {
			logger.warning(String.format("Fail to get response from %s %s %s", baseUrl, Util.replaceNewLine( path ), e));
		} finally {
			
			try {
				if (out != null) {
					out.close();
				}
			}
			catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close File Outputstream", ex);
				}
			}
			
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} 
			catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}

	}
	
	
	public String uploadFile(String path, String filepath) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		
		try {
			httpClient = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();

			HttpPost httpPost = new HttpPost(baseUrl + path);
			httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
			
			if (authHeader != null) {
				httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			File file = new File(filepath);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody("data", file);
			
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			
			response = httpClient.execute(httpPost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
				}
				throw new RESTInvocationException ("Failed : HTTP error code : " + statusCode);
			}

			return EntityUtils.toString(response.getEntity());
		} 
		catch (MalformedURLException e) {
			throw new RESTInvocationException ("Failed to post file to REST service: " + baseUrl + path, e);
		} 
		catch (IOException e) {
			throw new RESTInvocationException ("Failed to post file to REST Service: " + baseUrl + path, e);
		} 
		finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} 
			catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}

	}
	
	
	public void delete (String path) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			httpClient = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();

			HttpDelete httpDelete = new HttpDelete (baseUrl + path);
			
			if (authHeader != null) {
				httpDelete.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			response = httpClient.execute(httpDelete);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
				}
				throw new RESTInvocationException("Failed : HTTP error code : " + statusCode);
			}

		} catch (MalformedURLException e) {

			logger.warning(String.format("Malformed URL: %s %s %s", baseUrl, path, e));
		} catch (IOException e) {

			logger.warning(String.format("Fail to get response from %s %s %s", baseUrl, path, e));
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}
	}
	
	
	public String post (String contentType, String content) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			
			httpClient = HttpClientBuilder.create().setUserAgent(USER_AGENT).build();
			
			HttpPost httpPost = new HttpPost(baseUrl);
			
			if (authHeader != null) {
				httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

			httpPost.setEntity(new StringEntity(content));

			response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
				}
				throw new RESTInvocationException ("Failed : HTTP error code : " + statusCode);
			}

			return EntityUtils.toString(response.getEntity());

		} catch (IOException e) {
			throw new RESTInvocationException ("Failed to invoke GET REST Service: " + baseUrl, e);

		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}

	}
	
	
	public String postWithSSL (String contentType, String content, SSLContext sslContext) throws RESTInvocationException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		try {
			
			httpClient = HttpClients.custom().setSSLContext(sslContext).setUserAgent(USER_AGENT).build();
			
			HttpPost httpPost = new HttpPost(baseUrl);
			
			if (authHeader != null) {
				httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authHeader));
			}
			
			httpPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

			httpPost.setEntity(new StringEntity(content));

			response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			String status = String.valueOf(statusCode);
			if (statusCode != 200 && statusCode != 201) {
				if ( logger.isLoggable( Level.WARNING ) ) {
					logger.warning(String.format("Failed : HTTP error code : %s", statusCode));
				}
				throw new RESTInvocationException ("Failed : HTTP error code : " + statusCode);
			}

			return status;

		} catch (IOException e) {
			throw new RESTInvocationException("Failed to invoke GET REST Service: " + baseUrl, e);

		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException ex) {
				if (logger.isLoggable(Level.INFO)) {
					logger.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}

	}
}
