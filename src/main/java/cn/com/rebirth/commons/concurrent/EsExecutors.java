/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons EsExecutors.java 2012-7-6 10:22:17 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jsr166y.LinkedTransferQueue;
import cn.com.rebirth.commons.settings.Settings;


/**
 * The Class EsExecutors.
 *
 * @author l.xue.nong
 */
public class EsExecutors {

	
	/**
	 * New scaling executor service.
	 *
	 * @param min the min
	 * @param max the max
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param threadFactory the thread factory
	 * @return the es thread pool executor
	 */
	public static EsThreadPoolExecutor newScalingExecutorService(int min, int max, long keepAliveTime, TimeUnit unit,
			ThreadFactory threadFactory) {
		ExecutorScalingQueue<Runnable> queue = new ExecutorScalingQueue<Runnable>();
		
		EsThreadPoolExecutor executor = new EsThreadPoolExecutor(min, max, keepAliveTime, unit, queue, threadFactory,
				new ForceQueuePolicy());
		queue.executor = executor;
		return executor;
	}

	
	/**
	 * New blocking executor service.
	 *
	 * @param min the min
	 * @param max the max
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param threadFactory the thread factory
	 * @param capacity the capacity
	 * @param waitTime the wait time
	 * @param waitTimeUnit the wait time unit
	 * @return the es thread pool executor
	 */
	public static EsThreadPoolExecutor newBlockingExecutorService(int min, int max, long keepAliveTime, TimeUnit unit,
			ThreadFactory threadFactory, int capacity, long waitTime, TimeUnit waitTimeUnit) {
		ExecutorBlockingQueue<Runnable> queue = new ExecutorBlockingQueue<Runnable>(capacity);
		EsThreadPoolExecutor executor = new EsThreadPoolExecutor(min, max, keepAliveTime, unit, queue, threadFactory,
				new TimedBlockingPolicy(waitTimeUnit.toMillis(waitTime)));
		queue.executor = executor;
		return executor;
	}

	
	/**
	 * Thread name.
	 *
	 * @param settings the settings
	 * @param namePrefix the name prefix
	 * @return the string
	 */
	public static String threadName(Settings settings, String namePrefix) {
		String name = settings.get("name");
		if (name == null) {
			name = "rebirth";
		} else {
			name = "rebirth[" + name + "]";
		}
		return name + namePrefix;
	}

	
	/**
	 * Daemon thread factory.
	 *
	 * @param settings the settings
	 * @param namePrefix the name prefix
	 * @return the thread factory
	 */
	public static ThreadFactory daemonThreadFactory(Settings settings, String namePrefix) {
		return daemonThreadFactory(threadName(settings, namePrefix));
	}

	
	/**
	 * Daemon thread factory.
	 *
	 * @param namePrefix the name prefix
	 * @return the thread factory
	 */
	public static ThreadFactory daemonThreadFactory(String namePrefix) {
		final ThreadFactory f = java.util.concurrent.Executors.defaultThreadFactory();
		final String o = namePrefix + "-";

		return new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = f.newThread(r);
				t.setName(o + t.getName());
				t.setDaemon(true);
				return t;
			}
		};
	}

	
	/**
	 * Instantiates a new es executors.
	 */
	private EsExecutors() {
	}

	
	/**
	 * The Class ExecutorScalingQueue.
	 *
	 * @param <E> the element type
	 * @author l.xue.nong
	 */
	static class ExecutorScalingQueue<E> extends LinkedTransferQueue<E> {

		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -5037741031085628L;
		
		/** The executor. */
		ThreadPoolExecutor executor;

		
		/**
		 * Instantiates a new executor scaling queue.
		 */
		public ExecutorScalingQueue() {
		}

		
		/* (non-Javadoc)
		 * @see jsr166y.LinkedTransferQueue#offer(java.lang.Object)
		 */
		@Override
		public boolean offer(E e) {
			int left = executor.getMaximumPoolSize() - executor.getCorePoolSize();
			if (!tryTransfer(e)) {
				if (left > 0) {
					return false;
				} else {
					return super.offer(e);
				}
			} else {
				return true;
			}
		}
	}

	
	/**
	 * The Class ExecutorBlockingQueue.
	 *
	 * @param <E> the element type
	 * @author l.xue.nong
	 */
	static class ExecutorBlockingQueue<E> extends ArrayBlockingQueue<E> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 7422339322775570709L;
		
		/** The executor. */
		ThreadPoolExecutor executor;

		
		/**
		 * Instantiates a new executor blocking queue.
		 *
		 * @param capacity the capacity
		 */
		ExecutorBlockingQueue(int capacity) {
			super(capacity);
		}

		
		/* (non-Javadoc)
		 * @see java.util.concurrent.ArrayBlockingQueue#offer(java.lang.Object)
		 */
		@Override
		public boolean offer(E o) {
			int allWorkingThreads = executor.getActiveCount() + super.size();
			return allWorkingThreads < executor.getPoolSize() && super.offer(o);
		}
	}

	
	/**
	 * The Class ForceQueuePolicy.
	 *
	 * @author l.xue.nong
	 */
	static class ForceQueuePolicy implements RejectedExecutionHandler {

		
		/* (non-Javadoc)
		 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
		 */
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				executor.getQueue().put(r);
			} catch (InterruptedException e) {
				
				throw new RejectedExecutionException(e);
			}
		}
	}

	
	/**
	 * The Class TimedBlockingPolicy.
	 *
	 * @author l.xue.nong
	 */
	static class TimedBlockingPolicy implements RejectedExecutionHandler {

		
		/** The wait time. */
		private final long waitTime;

		
		/**
		 * Instantiates a new timed blocking policy.
		 *
		 * @param waitTime the wait time
		 */
		public TimedBlockingPolicy(long waitTime) {
			this.waitTime = waitTime;
		}

		
		/* (non-Javadoc)
		 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
		 */
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				boolean successful = executor.getQueue().offer(r, waitTime, TimeUnit.MILLISECONDS);
				if (!successful)
					throw new RejectedExecutionException("Rejected execution after waiting " + waitTime
							+ " ms for task [" + r.getClass() + "] to be executed.");
			} catch (InterruptedException e) {
				throw new RejectedExecutionException(e);
			}
		}
	}
}
