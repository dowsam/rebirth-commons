/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons FreeMarkers.java 2012-8-2 11:53:53 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * The Class FreeMarkers.
 *
 * @author l.xue.nong
 */
public class FreeMarkerUtils {

	/**
	 * Rendere string.
	 *
	 * @param templateString the template string
	 * @param model the model
	 * @return the string
	 */
	public static String rendereString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Render template.
	 *
	 * @param template the template
	 * @param model the model
	 * @return the string
	 */
	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Builds the configuration.
	 *
	 * @param directory the directory
	 * @return the configuration
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Configuration buildConfiguration(String directory) throws IOException {
		Configuration cfg = new Configuration();
		Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(path.getFile());
		return cfg;
	}
}
