/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons RequestCallBackLogUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The Class RequestCallBackLogUtils.
 *
 * @author l.xue.nong
 */
public abstract class RequestCallBackLogUtils {

	/** The linked list. */
	private static LinkedBlockingQueue<String> linkedList = new LinkedBlockingQueue<String>();

	/** The max elements. */
	public static int maxElements = 1000;

	/**
	 * Adds the.
	 *
	 * @param log the log
	 */
	public static void add(String log) {
		linkedList.offer(log);
	}

	/**
	 * Poll first.
	 *
	 * @return the string
	 */
	public static String pollFirst() {
		return linkedList.poll();
	}

	/**
	 * Drain to.
	 *
	 * @param list the list
	 */
	public static void drainTo(List<String> list) {
		linkedList.drainTo(list, maxElements);
	}

	/**
	 * Drain to.
	 *
	 * @param list the list
	 * @param maxElements the max elements
	 */
	public static void drainTo(List<String> list, int maxElements) {
		linkedList.drainTo(list, maxElements);
	}

	/**
	 * Clean.
	 */
	public static void clean() {
		if (linkedList != null)
			linkedList.clear();
	}
}
