/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons RuntimeUtils.java 2012-2-2 13:35:34 l.xue.nong$$
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
	 * This method guarantees that garbage collection is
	 * done unlike <code>{@link System#gc()}</code>.
	 */
	public static void gc() {
		Object obj = new Object();
		WeakReference<Object> ref = new WeakReference<Object>(obj);
		obj = null;
		while (ref.get() != null)
			System.gc();
	}

	/**
	 * calls <code>{@link #gc()}</code> <code>count</code> times.
	 *
	 * @param count the count
	 */
	public static void gc(int count) {
		for (; count != 0; count--)
			gc();
	}

	/**
	 * This method guarantees that garbage colleciton is
	 * done after JVM shutdown is initialized.
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
