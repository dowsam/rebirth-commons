/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons PropertiesUtils.java 2012-7-6 10:22:14 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * The Class PropertiesUtils.
 *
 * @author l.xue.nong
 */
public abstract class PropertiesUtils {
	
	/** The Constant DEFAULT_ENCODING. */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

	/** The properties persister. */
	private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	/** The resource loader. */
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * Load properties.
	 *
	 * @param locations the locations
	 * @return the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Properties loadProperties(String... locations) throws IOException {
		Properties props = new Properties();

		for (String location : locations) {

			logger.debug("Loading properties file from classpath:" + location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
			} catch (IOException ex) {
				logger.info("Could not load properties from classpath:" + location + ": " + ex.getMessage());
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return props;
	}
}
