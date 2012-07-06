/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ServletUtils.java 2012-7-6 10:22:14 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class ServletUtils.
 *
 * @author l.xue.nong
 */
public abstract class ServletUtils {
	// -- Content Type 定义 --//
	/** The Constant TEXT_TYPE. */
	public static final String TEXT_TYPE = "text/plain";

	/** The Constant JSON_TYPE. */
	public static final String JSON_TYPE = "application/json";

	/** The Constant XML_TYPE. */
	public static final String XML_TYPE = "text/xml";

	/** The Constant HTML_TYPE. */
	public static final String HTML_TYPE = "text/html";

	/** The Constant JS_TYPE. */
	public static final String JS_TYPE = "text/javascript";

	/** The Constant EXCEL_TYPE. */
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";

	// -- Header 定义 --//
	/** The Constant AUTHENTICATION_HEADER. */
	public static final String AUTHENTICATION_HEADER = "Authorization";

	// -- 常用数值定义 --//
	/** The Constant ONE_YEAR_SECONDS. */
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	/**
	 * Sets the expires header.
	 *
	 * @param response the response
	 * @param expiresSeconds the expires seconds
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header
		response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
	}

	/**
	 * Sets the no cache header.
	 *
	 * @param response the new no cache header
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader("Expires", 0);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache");
	}

	/**
	 * Sets the last modified header.
	 *
	 * @param response the response
	 * @param lastModifiedDate the last modified date
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	/**
	 * Sets the etag.
	 *
	 * @param response the response
	 * @param etag the etag
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	/**
	 * Check if modified since.
	 *
	 * @param request the request
	 * @param response the response
	 * @param lastModified the last modified
	 * @return true, if successful
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * Check if none match etag.
	 *
	 * @param request the request
	 * @param response the response
	 * @param etag the etag
	 * @return true, if successful
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the file download header.
	 *
	 * @param response the response
	 * @param fileName the file name
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * Gets the parameters starting with.
	 *
	 * @param request the request
	 * @param _prefix the _prefix
	 * @return the parameters starting with
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getParametersStartingWith(HttpServletRequest request, String _prefix) {
		String prefix = _prefix;
		Enumeration paramNames = request.getParameterNames();
		Map params = new TreeMap();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {// NOSONAR
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * Encode http basic.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the string
	 */
	public static String encodeHttpBasic(String userName, String password) {
		String encode = userName + ":" + password;
		return "Basic " + EncodeUtils.encodeBase64(encode.getBytes());
	}
}
