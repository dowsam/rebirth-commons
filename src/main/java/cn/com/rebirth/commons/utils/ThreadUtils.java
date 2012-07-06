/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ThreadUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class ThreadUtils.
 *
 * @author l.xue.nong
 */
public abstract class ThreadUtils {
	
	/**
	 * Sleep.
	 *
	 * @param millis the millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// Ignore.
		}
	}

	/**
	 * Graceful shutdown.
	 *
	 * @param pool the pool
	 * @param shutdownTimeout the shutdown timeout
	 * @param shutdownNowTimeout the shutdown now timeout
	 * @param timeUnit the time unit
	 */
	public static void gracefulShutdown(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout,
			TimeUnit timeUnit) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(shutdownTimeout, timeUnit)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(shutdownNowTimeout, timeUnit)) {
					System.err.println("Pool did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Normal shutdown.
	 *
	 * @param pool the pool
	 * @param timeout the timeout
	 * @param timeUnit the time unit
	 */
	public static void normalShutdown(ExecutorService pool, int timeout, TimeUnit timeUnit) {
		try {
			pool.shutdownNow();
			if (!pool.awaitTermination(timeout, timeUnit)) {
				System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * A factory for creating CustomizableThread objects.
	 */
	public static class CustomizableThreadFactory implements ThreadFactory {

		/** The name prefix. */
		private final String namePrefix;
		
		/** The thread number. */
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		/**
		 * Instantiates a new customizable thread factory.
		 *
		 * @param poolName the pool name
		 */
		public CustomizableThreadFactory(String poolName) {
			namePrefix = poolName + "-";
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		public Thread newThread(Runnable runable) {
			return new Thread(runable, namePrefix + threadNumber.getAndIncrement());
		}
	}

	/**
	 * Find all threads.
	 *
	 * @return the list
	 */
	public static List<Thread> findAllThreads() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		// 遍历线程组树，获取根线程组
		while (group != null) {
			topGroup = group;
			group = group.getParent();
		}
		// 激活的线程数加倍
		int estimatedSize = topGroup.activeCount() * 2;
		Thread[] slackList = new Thread[estimatedSize];
		//获取根线程组的所有线程
		int actualSize = topGroup.enumerate(slackList);
		// copy into a list that is the exact size
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);
		return Arrays.asList(list);
	}
}
