package com.stee.spfcore.service.report.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.service.configuration.ISSRSProxyConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.report.ReportServiceException;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;


public class ReportUtil {
	
	private static final Logger LOGGER = Logger.getLogger(ReportUtil.class.getName());
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	
	private RequestConfig.Builder builder;
	private NTCredentials ntCreds;
	
	public ReportUtil () {
		
		ISSRSProxyConfig config = ServiceConfig.getInstance().getSSRSProxyConfig();
		
		String password = config.password();
		
		String encryptionKey = EnvironmentUtils.getEncryptionKey();
		
		if (encryptionKey != null && !encryptionKey.isEmpty()) {
			try {
				Encipher encipher = new Encipher(encryptionKey);
				password = encipher.decrypt(password);
			}
			catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error while decrypting the configured password.", e);
			}
		}
		
		String host = "localhost";
		
		ntCreds = new NTCredentials(config.username(), password, host, config.domain());
		/*SSAT Fix- Remove away Logger*/
		
		if (LOGGER.isLoggable(Level.INFO)) {
			/*SSAT Fix- Remove away Logger*/
		}
		
		builder = RequestConfig.custom().setRedirectsEnabled(false).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setConnectTimeout(-1);
		
	}
	
	
	public void downloadReport (String reportPath, Map<String, String> parameters, String reportFile) throws ReportServiceException {
		
		ISSRSProxyConfig config = ServiceConfig.getInstance().getSSRSProxyConfig();
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		FileOutputStream out = null;
		String reportURL = null;
		
		try {
			URIBuilder uribuilder = new URIBuilder();
			uribuilder.setScheme(config.protocol()).setHost(config.hostname()).setPort(config.port()).setPath("/" + config.virtualDirectory());
			
			uribuilder.setParameter(reportPath, null);

			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				uribuilder.setParameter(key, value);
			}
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, ntCreds);
			
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			clientBuilder.setDefaultRequestConfig(builder.build());
			clientBuilder.setDefaultCredentialsProvider(credsProvider);
			clientBuilder.setUserAgent(USER_AGENT);
			clientBuilder.disableContentCompression();
			
			httpClient = clientBuilder.build();

			reportURL = uribuilder.toString();

			if (LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("downloadReport(), reportUrl=%s, filename=%s", reportURL, reportFile));
			}
			HttpGet httpGet = new HttpGet(uribuilder.toString());
			
			response = httpClient.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201 && LOGGER.isLoggable(Level.WARNING)) {
				LOGGER.warning(String.format("Failed : HTTP error code : %s", statusCode));
			}

			File file = new File(reportFile);
			out = new FileOutputStream(file, false);

			response.getEntity().writeTo(out);
		} 
		catch (MalformedURLException e) {
			LOGGER.warning(String.format("Malformed URL: %s %s", reportURL, e));
		} 
		catch (IOException e) {
			LOGGER.warning(String.format("Fail to get response from %s %s", reportURL, e));

		}
		finally {
			
			try {
				if (out != null) {
					out.close();
				}
			}
			catch (IOException ex) {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "Fail to close FileOutputstream", ex);
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
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}
	}
	
public String getReportUrl (String reportPath, Map<String, String> parameters) throws ReportServiceException {
		
		ISSRSProxyConfig config = ServiceConfig.getInstance().getSSRSProxyConfig();
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		FileOutputStream out = null;
		String reportURL = null;
		
		try {
			URIBuilder uribuilder = new URIBuilder();
			uribuilder.setScheme(config.protocol()).setHost(config.hostname()).setPort(config.port()).setPath("/" + config.virtualDirectory());
			
			uribuilder.setParameter(reportPath, null);

			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				uribuilder.setParameter(key, value);
			}
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, ntCreds);
			
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			clientBuilder.setDefaultRequestConfig(builder.build());
			clientBuilder.setDefaultCredentialsProvider(credsProvider);
			clientBuilder.setUserAgent(USER_AGENT);
			clientBuilder.disableContentCompression();
			
			httpClient = clientBuilder.build();

			reportURL = uribuilder.toString();

			if ( LOGGER.isLoggable( Level.INFO ) ) {
				LOGGER.info(String.format("downloadReport(), reportUrl=%s", reportURL));
			}
			return reportURL;
		} 
		
		finally {
			
			try {
				if (false) {
					out.close();
				}
			}
			catch (IOException ex) {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "Fail to close FileOutputstream", ex);
				}
			}
			
			try {
                if (httpClient != null) {
					httpClient.close();
				}
			} 
			catch (IOException ex) {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}
	}
	
	public BinaryFile downloadReport (String reportPath, Map<String, String> parameters) throws ReportServiceException {
		
		ISSRSProxyConfig config = ServiceConfig.getInstance().getSSRSProxyConfig();
		
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		FileOutputStream out = null;
		String reportURL = null;
		
		try {
			URIBuilder uribuilder = new URIBuilder();
			uribuilder.setScheme(config.protocol()).setHost(config.hostname()).setPort(config.port()).setPath("/" + config.virtualDirectory());
			
			uribuilder.setParameter(reportPath, null);

			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				uribuilder.setParameter(key, value);
			}
			
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(AuthScope.ANY, ntCreds);
			
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
			clientBuilder.setDefaultRequestConfig(builder.build());
			clientBuilder.setDefaultCredentialsProvider(credsProvider);
			clientBuilder.setUserAgent(USER_AGENT);
			clientBuilder.disableContentCompression();
			
			httpClient = clientBuilder.build();

			reportURL = uribuilder.toString();
					
			if (LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info(String.format("downloadReport(), reportUrl=%s", reportURL));
			}
			HttpGet httpGet = new HttpGet(uribuilder.toString());
			
			response = httpClient.execute(httpGet);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201 && LOGGER.isLoggable(Level.WARNING)) {
				LOGGER.warning(String.format("Failed : HTTP error code : %s", statusCode));
			}

			BinaryFile binaryFile = new BinaryFile();
			binaryFile.setName(getFileName(response));
			binaryFile.setContentType(getContentType(response));
			binaryFile.setContent(EntityUtils.toByteArray(response.getEntity()));
		
			return binaryFile;
		} 
		catch (MalformedURLException e) {
			throw new ReportServiceException ("Failed to invoke SSRS Report Service: " + reportURL, e);
		} 
		catch (IOException e) {
			throw new ReportServiceException ("Failed to invoke SSRS Report Service: " + reportURL, e);
		}
		finally {

			try {
				if (out != null) {
					out.close();
				}
			}
			catch (IOException ex) {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "Fail to close FileOutputstream", ex);
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
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "Fail to close HttpClient/HttpResponse", ex);
				}
			}
		}
	}
	
	private String getContentType (CloseableHttpResponse response) {
		Header header = response.getFirstHeader("Content-Type");
		
		String headerString = header.getValue();
		String [] param = headerString.split(";");
		
		return param [0];
	}
	
	private String getFileName (CloseableHttpResponse response) throws UnsupportedEncodingException {
		Header header = response.getFirstHeader("Content-Disposition");
		String headerString = header.getValue();
		
		String[] params = headerString.split(";");
		for ( String param : params) {
			param = param.trim();
			if (param.startsWith("filename")) {
				int index = param.indexOf("=");
				param = param.substring(index + 1);
				
				if (param.startsWith("UTF-8''")) {
					param = param.substring(7);
					
					param = URLDecoder.decode(param, "UTF-8");
				}
				
				return param;
			}
		}
		
		return "Unknown";
	}
	
}
