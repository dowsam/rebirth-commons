/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons TimeUtils.java 2012-2-2 11:00:17 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

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
	 * 开始统计时间.
	 */
	public static void begin() {
		times.add(System.currentTimeMillis());
	}

	/**
	 * 结束统计时间 并输出结果.
	 *
	 * @param title the title
	 */
	public static void end(String title) {
		String message = title + " : " + (System.currentTimeMillis() - times.pop());
		logger.info(message);
	}

	/**
	 * sleep等待,单位毫秒.
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
