/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ExceptionsHelper.java 2012-7-6 10:22:15 l.xue.nong$$
 */

package cn.com.rebirth.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ExceptionsHelper.
 *
 * @author l.xue.nong
 */
public final class ExceptionsHelper {

	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExceptionsHelper.class);

	
	/**
	 * Unwrap cause.
	 *
	 * @param t the t
	 * @return the throwable
	 */
	public static Throwable unwrapCause(Throwable t) {
		int counter = 0;
		Throwable result = t;
		while (result instanceof RebirthWrapperException) {
			if (result.getCause() == null) {
				return result;
			}
			if (result.getCause() == result) {
				return result;
			}
			if (counter++ > 10) {
				
				logger.warn("Exception cause unwrapping ran for 10 levels...", t);
				return result;
			}
			result = result.getCause();
		}
		return result;
	}

	
	/**
	 * Detailed message.
	 *
	 * @param t the t
	 * @return the string
	 */
	public static String detailedMessage(Throwable t) {
		return detailedMessage(t, false, 0);
	}

	
	/**
	 * Detailed message.
	 *
	 * @param t the t
	 * @param newLines the new lines
	 * @param initialCounter the initial counter
	 * @return the string
	 */
	public static String detailedMessage(Throwable t, boolean newLines, int initialCounter) {
		if (t == null) {
			return "Unknown";
		}
		int counter = initialCounter + 1;
		if (t.getCause() != null) {
			StringBuilder sb = new StringBuilder();
			while (t != null) {
				if (t.getMessage() != null) {
					sb.append(t.getClass().getSimpleName()).append("[");
					sb.append(t.getMessage());
					sb.append("]");
					if (!newLines) {
						sb.append("; ");
					}
				}
				t = t.getCause();
				if (t != null) {
					if (newLines) {
						sb.append("\n");
						for (int i = 0; i < counter; i++) {
							sb.append("\t");
						}
					} else {
						sb.append("nested: ");
					}
				}
				counter++;
			}
			return sb.toString();
		} else {
			return t.getClass().getSimpleName() + "[" + t.getMessage() + "]";
		}
	}
}
