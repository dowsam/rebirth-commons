/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons HttpRequestUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;

/**
 * The Class HttpRequestUtils.
 *
 * @author l.xue.nong
 */
public abstract class HttpRequestUtils {
	
	/** The nullstring. */
	private static String NULLSTRING = "";

	/** The Constant DEFAULT_ENCODING. */
	public static final String DEFAULT_ENCODING = "ISO-8859-1";

	/** The Constant TARGET_ENCODING. */
	public static final String TARGET_ENCODING = "UTF-8";

	/**
	 * Gets the encoded parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param encoding the encoding
	 * @param defautlValue the defautl value
	 * @return the encoded parameter
	 */
	public static String getEncodedParameter(HttpServletRequest request, String name, String encoding,
			String defautlValue) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defautlValue;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			temp = new String(temp.getBytes(DEFAULT_ENCODING), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return defautlValue;
		}
		return temp;
	}

	/**
	 * Gets the encoded parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param encoding the encoding
	 * @return the encoded parameter
	 */
	public static String getEncodedParameter(HttpServletRequest request, String name, String encoding) {
		return getEncodedParameter(request, name, encoding, null);
	}

	/**
	 * Gets the parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @return the parameter
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		return getEncodedParameter(request, name, TARGET_ENCODING, null);
	}

	/**
	 * Gets the parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param defaultValue the default value
	 * @return the parameter
	 */
	public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
		return getEncodedParameter(request, name, TARGET_ENCODING, defaultValue);
	}

	/**
	 * Gets the encoded parameters.
	 *
	 * @param request the request
	 * @param name the name
	 * @param encoding the encoding
	 * @return the encoded parameters
	 */
	public static String[] getEncodedParameters(HttpServletRequest request, String name, String encoding) {

		String[] temp = request.getParameterValues(name);
		if (temp == null) {
			return null;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] != null) {
					temp[i] = new String(temp[i].getBytes(DEFAULT_ENCODING), encoding);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Gets the parameters.
	 *
	 * @param request the request
	 * @param name the name
	 * @return the parameters
	 */
	public static String[] getParameters(HttpServletRequest request, String name) {
		return getEncodedParameters(request, name, TARGET_ENCODING);
	}

	/**
	 * Gets the boolean parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param defaultVal the default val
	 * @return the boolean parameter
	 */
	public static boolean getBooleanParameter(HttpServletRequest request, String name, boolean defaultVal) {
		String temp = request.getParameter(name);
		if ("true".equalsIgnoreCase(temp) || "y".equalsIgnoreCase(temp)) {
			return true;
		} else if ("false".equalsIgnoreCase(temp) || "n".equalsIgnoreCase(temp)) {
			return false;
		} else {
			return defaultVal;
		}
	}

	/**
	 * Gets the int parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param defaultNum the default num
	 * @return the int parameter
	 */
	public static int getIntParameter(HttpServletRequest request, String name, int defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Integer.parseInt(temp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultNum;
	}

	/**
	 * Gets the float parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param defaultNum the default num
	 * @return the float parameter
	 */
	public static float getFloatParameter(HttpServletRequest request, String name, float defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Float.parseFloat(temp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultNum;
	}

	/**
	 * Gets the double parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param defaultNum the default num
	 * @return the double parameter
	 */
	public static double getDoubleParameter(HttpServletRequest request, String name, double defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Double.parseDouble(temp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultNum;
	}

	/**
	 * Gets the long parameter.
	 *
	 * @param request the request
	 * @param name the name
	 * @param defaultNum the default num
	 * @return the long parameter
	 */
	public static long getLongParameter(HttpServletRequest request, String name, long defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Long.parseLong(temp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultNum;
	}
}
