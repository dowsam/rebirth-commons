/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons ThreadUtils.java 2012-2-2 10:49:31 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

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
	 * sleep等待,单位毫秒,忽略InterruptedException.
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
	 * 按照ExecutorService JavaDoc示例代码编写的Graceful Shutdown方法.
	 * 先使用shutdown尝试执行所有任务.
	 * 超时后调用shutdownNow取消在workQueue中Pending的任务,并中断所有阻塞函数.
	 * 另对在shutdown时线程本身被调用中断做了处理.
	 *
	 * @param pool the pool
	 * @param shutdownTimeout the shutdown timeout
	 * @param shutdownNowTimeout TODO
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
	 * 直接调用shutdownNow的方法.
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
	 * 自定义ThreadFactory,可定制线程池的名称.
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
	 * 获取Java VM中当前运行的所有线程.
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
