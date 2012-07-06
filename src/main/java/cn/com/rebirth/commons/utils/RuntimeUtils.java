/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons RuntimeUtils.java 2012-7-6 10:22:14 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.lang.ref.WeakReference;

/**
 * The Class RuntimeUtils.
 *
 * @author l.xue.nong
 */
public abstract class RuntimeUtils {

	/**
	 * Gc.
	 */
	public static void gc() {
		Object obj = new Object();
		WeakReference<Object> ref = new WeakReference<Object>(obj);
		obj = null;
		while (ref.get() != null)
			System.gc();
	}

	/**
	 * Gc.
	 *
	 * @param count the count
	 */
	public static void gc(int count) {
		for (; count != 0; count--)
			gc();
	}

	/**
	 * Gc on exit.
	 */
	public static void gcOnExit() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				gc();
			}
		});
	}
}
