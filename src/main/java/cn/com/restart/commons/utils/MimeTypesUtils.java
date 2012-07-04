/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons MimeTypesUtils.java 2012-2-2 13:35:47 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class MimeTypesUtils.
 *
 * @author l.xue.nong
 */
public abstract class MimeTypesUtils {

	/** The mimetypes. */
	private static Properties mimetypes = null;

	/** The ext pattern. */
	private static Pattern extPattern;

	/** The default type. */
	private static String defaultType = "application/octet-stream";

	/** The logger. */
	private static Log logger = LogFactory.getLog(MimeTypesUtils.class);

	static {
		extPattern = Pattern.compile("^.*\\.([^.]+)$");
	}

	/**
	 * Gets the mime type.
	 *
	 * @param filename the filename
	 * @return the mime type
	 */
	public static String getMimeType(String filename) {
		return getMimeType(filename, defaultType);
	}

	/**
	 * Gets the mime type.
	 *
	 * @param filename the filename
	 * @param defaultMimeType the default mime type
	 * @return the mime type
	 */
	public static String getMimeType(String filename, String defaultMimeType) {
		Matcher matcher = extPattern.matcher(filename.toLowerCase());
		String ext = "";
		if (matcher.matches()) {
			ext = matcher.group(1);
		}
		if (ext.length() > 0) {
			String mimeType = mimetypes().getProperty(ext);
			if (mimeType == null) {
				return defaultMimeType;
			}
			return mimeType;
		}
		return defaultMimeType;
	}

	/**
	 * Gets the content type.
	 *
	 * @param filename the filename
	 * @return the content type
	 */
	public static String getContentType(String filename) {
		return getContentType(filename, "application/octet-stream");
	}

	/**
	 * Gets the content type.
	 *
	 * @param filename the filename
	 * @param defaultContentType the default content type
	 * @return the content type
	 */
	public static String getContentType(String filename, String defaultContentType) {
		String contentType = getMimeType(filename, null);
		if (contentType == null) {
			contentType = defaultContentType;
		}
		if (contentType != null && contentType.startsWith("text/")) {
			return contentType + "; charset=" + ResponseTypeOutputUtils.DEFAULT_ENCODING;
		}
		return contentType;
	}

	/**
	 * Checks if is valid mime type.
	 *
	 * @param mimeType the mime type
	 * @return true, if is valid mime type
	 */
	public static boolean isValidMimeType(String mimeType) {
		if (mimeType == null) {
			return false;
		} else if (mimeType.indexOf(";") != -1) {
			return mimetypes().contains(mimeType.split(";")[0]);
		} else {
			return mimetypes().contains(mimeType);
		}
	}

	/**
	 * Inits the mimetypes.
	 */
	private static synchronized void initMimetypes() {
		if (mimetypes != null)
			return;
		// Load default mimetypes from the framework
		try {
			InputStream is = MimeTypesUtils.class.getClassLoader().getResourceAsStream("mime-types.properties");
			mimetypes = new Properties();
			mimetypes.load(is);
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
		}
	}

	/**
	 * Mimetypes.
	 *
	 * @return the properties
	 */
	private static Properties mimetypes() {
		if (mimetypes == null) {
			initMimetypes();
		}
		return mimetypes;
	}
}
