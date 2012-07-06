/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons TimeUtils.java 2012-7-6 10:22:12 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TimeUtils.
 *
 * @author l.xue.nong
 */
public abstract class TimeUtils {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(TimeUtils.class);

	/** The times. */
	private static Stack<Long> times = new Stack<Long>();

	/**
	 * Begin.
	 */
	public static void begin() {
		times.add(System.currentTimeMillis());
	}

	/**
	 * End.
	 *
	 * @param title the title
	 */
	public static void end(String title) {
		String message = title + " : " + (System.currentTimeMillis() - times.pop());
		logger.info(message);
	}

	/**
	 * Sleep.
	 *
	 * @param millis the millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {//NOSONAR
		}
	}
}
