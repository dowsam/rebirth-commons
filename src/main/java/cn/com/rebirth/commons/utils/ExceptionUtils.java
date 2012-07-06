/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ExceptionUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Class ExceptionUtils.
 *
 * @author l.xue.nong
 */
public abstract class ExceptionUtils {
	
	/**
	 * Unchecked.
	 *
	 * @param e the e
	 * @return the runtime exception
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException(e.getMessage(), e);
	}

	/**
	 * Gets the stack trace as string.
	 *
	 * @param e the e
	 * @return the stack trace as string
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
