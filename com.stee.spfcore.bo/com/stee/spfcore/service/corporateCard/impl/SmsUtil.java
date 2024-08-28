package com.stee.spfcore.service.corporateCard.impl;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.stee.spfcore.service.configuration.ISmsServerConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.notification.impl.SmsServer;
import com.stee.spfcore.utils.CleanPath;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.rest.RESTInvoker;

import static com.ibm.java.diagnostics.utils.Context.logger;

public class SmsUtil {
	
	private static final Logger logger = Logger.getLogger(SmsUtil.class.getName());
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String REST_BASE_URL = "/sms/SendSMS";
	private static final String CONTENT_TYPE = "text/xml";
	
	private RESTInvoker invoker;
	private boolean isSSL = false;
	private SSLContext sslContext;
	
	public SmsUtil () {
		ISmsServerConfig config = ServiceConfig.getInstance().getSmsServerConfig();
		
		String hostname = config.hostname();
		int port = config.port();
		String protocol = config.protocol();
		String trustStoreFile = config.trustStore();
		String trustStorePassword = "";

        String encryptionKey = EnvironmentUtils.getEncryptionKey();

        if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
            try {
                Encipher encipher = new Encipher( encryptionKey );
                trustStorePassword = encipher.decrypt(config.trustStorePassword() );
            }
            catch ( Exception e ) {
                logger.log( Level.SEVERE, "Error while decrypting the configured password.", e );
            }
        }
		
		String prefix = null;
		if ("ssl".equals(protocol)) {
			prefix = "https://";
			isSSL = true;
			buildSSLContext (trustStoreFile, trustStorePassword);
		}
		else {
			prefix = "http://";
			isSSL = false;
		}
		
		String url =  prefix + hostname + ":" + port + REST_BASE_URL;
		
		invoker = new RESTInvoker(url);
	}
	
	private void buildSSLContext (String trustStoreFile, String trustStorePassword) {
		FileInputStream fis = null;
		try {
			KeyStore trustStore = KeyStore.getInstance("JKS");
			fis = new FileInputStream (trustStoreFile);
			// Open our file and read the truststore
			trustStore.load(fis, trustStorePassword.toCharArray());
			
			// Create a default trust and key manager
			TrustManagerFactory trustManagerFactory
	              = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	      
			// Initialise the managers
			trustManagerFactory.init(trustStore);

			//to revert, change it back to TLSv1
			sslContext = SSLContext.getInstance("TLSv1.2");
	      
			sslContext.init (null, trustManagerFactory.getTrustManagers(), null);

			SSLContext.setDefault(sslContext);
		} 
		
		catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | KeyManagementException e) {
			logger.log(Level.SEVERE, "Fail to initialize SSL Context", e);
		}
		
		finally {
			try {
        		if( fis != null) {
        			fis.close();
        		}
			} catch (IOException e) {
				logger.severe(String.valueOf(e));
			}
		}
	}
	
	
	public void send (String smsContent, List<String> recipients, Map<String,String> smsStatusMap) {
		
		if (recipients.isEmpty()) {
			return;
		}
		
		List<List<String>> partitionedList = partition (recipients);
		
		for (List<String> subList: partitionedList) {
			sendBatch (smsContent, subList, smsStatusMap);
		}
	}
	
	
	private List<List<String>> partition (List<String> list) {
		
		List<List<String>> result = new ArrayList<>();
		
		List<String> subList = new ArrayList<>();
		
		for (String value : list) {
			subList.add(value);
			
			// Partition into 100 per sublist
			if (subList.size() >= 100) {
				result.add(subList);
				subList = new ArrayList<>();
			}
		}
		
		if (!subList.isEmpty()) {
			result.add(subList);
		}
		
		return result;
	}
	
	
	private void sendBatch (String smsContent, List<String> recipients, Map<String,String> smsStatusMap) {
		
		String message = buildMessage (smsContent, recipients);
		
		try {
			String response = null;
			
			if (isSSL) {
				response = invoker.postWithSSL(CONTENT_TYPE, message, sslContext);
			}
			else {
				response = invoker.post(CONTENT_TYPE, message);
			}
			
			// If somehow fail to send or retrieve the status code, will assume 
			// all not send (not added to smsStatusMap).
			extractCodes (response, smsStatusMap);
			
		} 
		catch (Exception e) {
			logger.severe(String.format("Fail to send SMS: %s %s",Util.replaceNewLine( smsContent ), e));
		}
	}
	
	private String buildMessage (String smsContent, List<String> recipients) {
		
		// Root element
    Element rootElement = new Element ("MTBATCHREQUEST");
    Document doc = new Document (rootElement);	
    
    Element requestIDElement = new Element ("REQUESTID");
    requestIDElement.setText("MC" + System.currentTimeMillis());
    rootElement.addContent(requestIDElement);
		
    Element appCodeElement = new Element("APPCODE");
    appCodeElement.setText("SPFAPP01");
    rootElement.addContent(appCodeElement);
    
    Element cpCodeElement = new Element("CPCODE");
    cpCodeElement.setText("NCS78794");
    rootElement.addContent(cpCodeElement);
    
    Element usetPoaElement = new Element("USETPOA");
    usetPoaElement.setText("1");
    rootElement.addContent(usetPoaElement);
    
    Date currentDate = new Date ();
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Element reqDateElement = new Element("REQDATE");
    reqDateElement.setText(dateFormat.format(currentDate));
    rootElement.addContent(reqDateElement);
    
    SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
    Element reqTimeElement = new Element("REQTIME");
    reqTimeElement.setText(timeFormat.format(currentDate));
    rootElement.addContent(reqTimeElement);
    
    Element dcsElement = new Element("DCS");
    dcsElement.setText("001");
    rootElement.addContent(dcsElement);
    
    Element msgElement = new Element ("MSG");
		CDATA cdata = new CDATA(smsContent);
		msgElement.addContent(cdata);
		rootElement.addContent(msgElement);
		
		for (int i = 0; i < recipients.size(); i++) {
			
			String recipient = recipients.get(i);
			String id = String.valueOf(i + 1);
			
			Element smsElement = new Element ("SMS");
			
			Element smsRefElement = new Element ("SMSREF");
			smsRefElement.setText(id);
			smsElement.addContent(smsRefElement);
			
			Element destElement = new Element ("DEST");
			destElement.setText(recipient);
			smsElement.addContent(destElement);
			
			rootElement.addContent(smsElement);
		}
		
		XMLOutputter xmlOutput = new XMLOutputter();
		
		xmlOutput.setFormat(Format.getPrettyFormat());
    
		StringWriter writer = new StringWriter();
		
    try {
			xmlOutput.output(doc, writer);
		} 
    catch (IOException e) {
    	logger.log(Level.SEVERE, "Fail to generate SMS Request XML.");
    	return null;
		} 
    
		return writer.toString();
	}
		
	
	private void extractCodes (String response, Map<String,String> smsStatusMap) {
		
		SAXBuilder builder = new SAXBuilder();
		// Disable external entity resolution
		builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
		builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		
		Document doc;
		try {
			doc = builder.build(new ByteArrayInputStream(response.getBytes()));
		}
		catch (JDOMException | IOException e) {
			logger.log(Level.SEVERE, "Fail to process SMS status");
			return;
		}
		
		Element rootElement = doc.getRootElement();
		List<Element> children = rootElement.getChildren("SMS");
		
		for (Element element : children) {
			String number = element.getChild("DEST").getTextTrim();
			String status = element.getChild("STATUSCODE").getTextTrim ();
			smsStatusMap.put(number, status);
		}
		
	}

	//20240627 SM made changes to this method FOR opc! mainly changed C to E and file path.
	public void sendExtSMS(String smsContent, List<String> recipients) {
		logger.info("sendExtSMS method started");

		List<String> command = new ArrayList<>();
		String dateString = getDateString();
		String encodedText = null;

		try {
			encodedText = URLEncoder.encode(smsContent, "UTF-8");
			logger.info("Encoded SMS content: " + encodedText);
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "Failed to encode SMS content", e);
			return;
		}

		String recipientPath = "E:\\SPF\\SMS_LOGS\\SMS_Recipient\\recipient_" + dateString + ".txt";
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(recipientPath, "UTF-8");
			for (String value : recipients) {
				pw.println(value);
			}
			logger.info("Recipient file created at: " + recipientPath);
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			logger.log(Level.SEVERE, "Failed to create recipient file", ex);
			return;
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

		String bpmPath = "E:\\IBM\\Workflow";
		logger.info("BPM Path: " + bpmPath);

		command.add(bpmPath + "\\java\\bin\\java.exe");
		command.add("-jar");
		command.add(bpmPath + "\\java\\bin\\SMSTester.jar");
		command.add(recipientPath);
		command.add(encodedText);
		command.add("true");

		logger.info("Command to be executed: " + command);

		ProcessBuilder pb = new ProcessBuilder(command);
		File log = new File("E:\\SPF\\SMS_LOGS\\smslog_batch_" + dateString + ".txt");

		pb.redirectErrorStream(true);
		pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));

		logger.info("Process log file: " + log.getAbsolutePath());
		logger.info("ProcessBuilder environment: " + pb.environment());
		logger.info("ProcessBuilder directory: " + pb.directory());

		try {
			Process process = pb.start();
			logger.info("Process started");

			// Capture the process output
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					logger.info("Process output: " + line);
				}
			}

			int exitCode = process.waitFor();
			logger.info("Process exited with code: " + exitCode);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to send SMS with standalone app", e);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "Process was interrupted", e);
			Thread.currentThread().interrupt(); // Preserve the interruption status
		}

		logger.info("sendExtSMS method finished");
	}


	private static String getDateString() {
		
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.YEAR) +
				String.valueOf(calendar.get(Calendar.MONTH) + 1) +
				calendar.get(Calendar.DATE) +
				calendar.get(Calendar.HOUR_OF_DAY) +
				calendar.get(Calendar.MINUTE) +
				calendar.get(Calendar.MILLISECOND);
		
	}
	
}
