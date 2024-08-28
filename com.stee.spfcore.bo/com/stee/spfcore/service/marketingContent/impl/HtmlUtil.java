package com.stee.spfcore.service.marketingContent.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class HtmlUtil {

	private static final String ECM_DOCUMENT_ID_PARAM_NAME = "documentId";
	
	private String contentUrl;
	private String baseUrl;
	
	public HtmlUtil (String baseUrl, String contentUrl) {
		this.baseUrl = baseUrl;
		this.contentUrl = contentUrl;
	}
	
	public String process (String orginalHtml, List<String> images, boolean clean) {
		
		Document doc = Jsoup.parse(orginalHtml);
		
		Elements links = doc.select("img[src]");
		
		for (Element link : links) {
			
			String docId = extractImageContentId(link.attr("src"));
			
			if (docId != null) {
				if (!images.contains(docId)) {
					images.add(docId);
				}
				
				String newUrl = contentUrl + docId;
				link.attr("src", newUrl);
			}
		}
		
		doc.setBaseUri(baseUrl);
		
		if (clean) {
			doc = new Cleaner(Whitelist.relaxed().preserveRelativeLinks(true).addTags("span").addAttributes(":all", "style")).clean(doc);
		}
		
		return doc.toString();
	}
	
	private String extractImageContentId (String link) {
		
		List<NameValuePair> list = URLEncodedUtils.parse(link, StandardCharsets.UTF_8);
		
		for (NameValuePair pair : list) {
			if (ECM_DOCUMENT_ID_PARAM_NAME.equals(pair.getName())) {
				return pair.getValue();
			}
		}
		
		return null;
	}
	
	
	public String removeMceAttribute (String orginalHtml) {
		
		Document doc = Jsoup.parse(orginalHtml);
		
		Elements elements = doc.select("[data-mce-style]");
		for (Element element : elements) {
			element.removeAttr("data-mce-style");
		}
		
		elements = doc.select("[data-mce-src]");
		for (Element element : elements) {
			element.removeAttr("data-mce-src");
		}
		
		return doc.toString();
	}
	
	
}
