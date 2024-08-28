package com.stee.spfcore.utils.template;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.stee.spfcore.service.configuration.ITemplateConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;

public class TemplateUtil {

	private static final String SMS_TEMPLATE_TIMESTAMP = "TIMESTAMP";
	private static final String SMS_TEMPLATE_TEXT_COLUMN = "SMS_TEXT";
	private static final String SMS_TEMPLATE_NAME_COLUMN = "NAME";
	private static final String SMS_TEMPLATE_DB_TABLE = "SPFCORE.TEMPLATES";
	private static final String EMAIL_TEMPLATE_BODY_COLUMN = "EMAIL_BODY";
	private static final String EMAIL_TEMPLATE_TIMESTAMP = "TIMESTAMP";
	private static final String EMAIL_TEMPLATE_SUBJECT_COLUMN = "EMAIL_SUBJECT";
	private static final String EMAIL_TEMPLATE_NAME_COLUMN = "NAME";
	private static final String EMAIL_TEMPLATE_DB_TABLE = "SPFCORE.TEMPLATES";
	
	private static TemplateUtil instance = null;
	
	public static synchronized TemplateUtil getInstance () {
		
		if (instance == null) {
			instance = new TemplateUtil();
		}
		return instance;
	}
	
	private VelocityEngine subjectTemplateEngine;
	private VelocityEngine bodyTemplateEngine;
	private VelocityEngine smsTemplateEngine;
	private VelocityEngine textTemplateEngine;
	
	
	private TemplateUtil () {
		
		ITemplateConfig config = ServiceConfig.getInstance().getTemplateConfig();
		
		initSubjectTemplateEngine (config);
		initBodyTemplateEngine (config);
		initSmsTemplateEngine (config);
		initTextTemplateEngine (config);
	}
	
	
	private void initSubjectTemplateEngine (ITemplateConfig config) {
		
		Properties props = new Properties();
		props.setProperty ("resource.loader", "ds");
		props.setProperty ("ds.resource.loader.class", "org.apache.velocity.runtime.resource.loader.DataSourceResourceLoader");
		props.setProperty	("ds.resource.loader.resource.datasource", config.dataSource());
		props.setProperty	("ds.resource.loader.resource.table", EMAIL_TEMPLATE_DB_TABLE);
		props.setProperty	("ds.resource.loader.resource.keycolumn", EMAIL_TEMPLATE_NAME_COLUMN);
		props.setProperty	("ds.resource.loader.resource.templatecolumn", EMAIL_TEMPLATE_SUBJECT_COLUMN);
		props.setProperty	("ds.resource.loader.resource.timestampcolumn", EMAIL_TEMPLATE_TIMESTAMP);
		props.setProperty	("ds.resource.loader.cache", String.valueOf(config.cacheTemplate()));
		props.setProperty	("ds.resource.loader.modificationCheckInterval", String.valueOf(config.modificationCheckInterval()));
		props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.JdkLogChute");
		
		subjectTemplateEngine = new VelocityEngine(props);
		subjectTemplateEngine.init();
	}
	
	private void initBodyTemplateEngine (ITemplateConfig config) {
		
		Properties props = new Properties();
		props.setProperty ("resource.loader", "ds");
		props.setProperty ("ds.resource.loader.class", "org.apache.velocity.runtime.resource.loader.DataSourceResourceLoader");
		props.setProperty	("ds.resource.loader.resource.datasource", config.dataSource());
		props.setProperty	("ds.resource.loader.resource.table", EMAIL_TEMPLATE_DB_TABLE);
		props.setProperty	("ds.resource.loader.resource.keycolumn", EMAIL_TEMPLATE_NAME_COLUMN);
		props.setProperty	("ds.resource.loader.resource.templatecolumn", EMAIL_TEMPLATE_BODY_COLUMN);
		props.setProperty	("ds.resource.loader.resource.timestampcolumn", EMAIL_TEMPLATE_TIMESTAMP);
		props.setProperty	("ds.resource.loader.cache", String.valueOf(config.cacheTemplate()));
		props.setProperty	("ds.resource.loader.modificationCheckInterval", String.valueOf(config.modificationCheckInterval()));
		props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.JdkLogChute");
		
		bodyTemplateEngine = new VelocityEngine(props);
		bodyTemplateEngine.init();
	}
	
	private void initSmsTemplateEngine (ITemplateConfig config) {
		
		Properties props = new Properties();
		props.setProperty ("resource.loader", "ds");
		props.setProperty ("ds.resource.loader.class", "org.apache.velocity.runtime.resource.loader.DataSourceResourceLoader");
		props.setProperty	("ds.resource.loader.resource.datasource", config.dataSource());
		props.setProperty	("ds.resource.loader.resource.table", SMS_TEMPLATE_DB_TABLE);
		props.setProperty	("ds.resource.loader.resource.keycolumn", SMS_TEMPLATE_NAME_COLUMN);
		props.setProperty	("ds.resource.loader.resource.templatecolumn", SMS_TEMPLATE_TEXT_COLUMN);
		props.setProperty	("ds.resource.loader.resource.timestampcolumn", SMS_TEMPLATE_TIMESTAMP);
		props.setProperty	("ds.resource.loader.cache", String.valueOf(config.cacheTemplate()));
		props.setProperty	("ds.resource.loader.modificationCheckInterval", String.valueOf(config.modificationCheckInterval()));
		props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.JdkLogChute");
		
		smsTemplateEngine = new VelocityEngine(props);
		smsTemplateEngine.init();
	}
	
	private void initTextTemplateEngine (ITemplateConfig config) {
		
		String path = config.templateFolder();
		Properties props = new Properties();
		props.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path);
		props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.JdkLogChute");
		
		textTemplateEngine = new VelocityEngine(props);
		textTemplateEngine.init();
	}
	
	
	/**
	 * Return the template content without any data to be merge into the template.
	 * @param templateName
	 * @return
	 */
	public String format (String templateName) {
		
		StringWriter writer = new StringWriter();
		Template template = null;
		
		if (subjectTemplateEngine.resourceExists(templateName)) {
			template = subjectTemplateEngine.getTemplate(templateName);
		}
		else if (bodyTemplateEngine.resourceExists(templateName)) {
			template = bodyTemplateEngine.getTemplate(templateName);
		}
		else if (smsTemplateEngine.resourceExists(templateName)){
			template = smsTemplateEngine.getTemplate(templateName);
		}
		else {
			// If template doesn't exist, ResourceNotFoundException will be thrown.
			template = textTemplateEngine.getTemplate(templateName);
		}
		
		VelocityContext context = new VelocityContext();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	
	/**
	 * Return the template context with the data in the context merged.
	 * @param templateName
	 * @param context
	 * @return
	 */
	public String format (String templateName, Map<String, ?> context) {
		
		StringWriter writer = new StringWriter();
		
		Template template = null;
		
		if (subjectTemplateEngine.resourceExists(templateName)) {
			template = subjectTemplateEngine.getTemplate(templateName);
		}
		else if (bodyTemplateEngine.resourceExists(templateName)) {
			template = bodyTemplateEngine.getTemplate(templateName);
		}
		else if (smsTemplateEngine.resourceExists(templateName)){
			template = smsTemplateEngine.getTemplate(templateName);
		}
		else {
			// If template doesn't exist, ResourceNotFoundException will be thrown.
			template = textTemplateEngine.getTemplate(templateName);
		}
		
		VelocityContext velocityContext = new VelocityContext (context);
		template.merge(velocityContext, writer);
		
		return writer.toString();
		
	}
	
	
	public String formatEmailSubject (String templateName) {
		StringWriter writer = new StringWriter();
		Template template = null;
		
		template = subjectTemplateEngine.getTemplate(templateName);
		
		VelocityContext context = new VelocityContext();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String formatEmailSubject (String templateName, Map<String, ?> context) {
		StringWriter writer = new StringWriter();
		
		Template template = null;
		
		template = subjectTemplateEngine.getTemplate(templateName);
		
		VelocityContext velocityContext = new VelocityContext (context);
		template.merge(velocityContext, writer);
		
		return writer.toString();
	}
	
	public String formatEmailBody (String templateName) {
		StringWriter writer = new StringWriter();
		Template template = null;
		
		template = bodyTemplateEngine.getTemplate(templateName);
		
		VelocityContext context = new VelocityContext();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String formatEmailBody (String templateName, Map<String, ?> context) {
		StringWriter writer = new StringWriter();
		
		Template template = null;
		
		template = bodyTemplateEngine.getTemplate(templateName);
		
		VelocityContext velocityContext = new VelocityContext (context);
		template.merge(velocityContext, writer);
		
		return writer.toString();
	}
	
	public String formatSmsText (String templateName) {
		StringWriter writer = new StringWriter();
		Template template = null;
		
		template = smsTemplateEngine.getTemplate(templateName);
		
		VelocityContext context = new VelocityContext();
		template.merge(context, writer);
		
		return writer.toString();
	}
	
	public String formatSmsText (String templateName, Map<String, ?> context) {
		StringWriter writer = new StringWriter();
		
		Template template = null;
		
		template = smsTemplateEngine.getTemplate(templateName);
		
		VelocityContext velocityContext = new VelocityContext (context);
		template.merge(velocityContext, writer);
		
		return writer.toString();
	}
	
}
