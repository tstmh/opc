package com.stee.spfcore.service.marketingContent.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.service.configuration.ITemplateConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.marketingContent.MarketingContentException;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.utils.template.TemplateUtil;


public class TemplateHelper {

	private static Logger logger = Logger.getLogger(TemplateHelper.class.getName());
	private String templateFolder;

	public TemplateHelper () {
		ITemplateConfig config = ServiceConfig.getInstance().getTemplateConfig();
		templateFolder = config.templateFolder();
	}

	public String processHTML (String templateName, boolean isEmail, String attachmentURL, String header, String title, String body) {

		Map<String, Object> context = new HashMap<String, Object> ();

		String formatedBody = formatBody(body);

		context.put("isEmail", isEmail);
		context.put("header", header);
		context.put("title", title);
		context.put("body", formatedBody);
		context.put("attachmentURL", attachmentURL);

		TemplateUtil templateUtil = TemplateUtil.getInstance();

		return templateUtil.format(templateName, context);
	}

	/**
	 * Convert the new line to <BR>
	 * @param body
	 * @return
	 */
	private String formatBody (String body) {

		String[] lines = body.split("\\r?\\n");

		StringBuilder builder = new StringBuilder(lines [0]);
		for (int i = 1; i < lines.length; i++) {
			builder.append("<BR>");
			builder.append(lines [i]);
		}

		return builder.toString();
	}


	public byte [] getResourceContent (String fileName) throws MarketingContentException {

		File file = new File(templateFolder, fileName);
		try {
			return Files.readAllBytes(file.toPath());
		}
		catch ( IOException e ) {
			logger.log( Level.WARNING, "Fail to load template resource from " + Util.replaceNewLine( file.getAbsolutePath() ), e );
			throw new MarketingContentException( "Fail to load template resource from " + Util.replaceNewLine( file.getAbsolutePath() ), e );
		}
	}

}
