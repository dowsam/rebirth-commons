/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ThreadRenamingRunnable.java 2012-3-29 15:15:15 l.xue.nong$$
 */

package cn.com.restart.commons.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ThreadRenamingRunnable.
 *
 * @author l.xue.nong
 */
public class ThreadRenamingRunnable implements Runnable {

	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ThreadRenamingRunnable.class);

	
	/** The thread name determiner. */
	private static volatile ThreadNameDeterminer threadNameDeterminer = ThreadNameDeterminer.PROPOSED;

	
	/**
	 * Gets the thread name determiner.
	 *
	 * @return the thread name determiner
	 */
	public static ThreadNameDeterminer getThreadNameDeterminer() {
		return threadNameDeterminer;
	}

	
	/**
	 * Sets the thread name determiner.
	 *
	 * @param threadNameDeterminer the new thread name determiner
	 */
	public static void setThreadNameDeterminer(ThreadNameDeterminer threadNameDeterminer) {
		if (threadNameDeterminer == null) {
			throw new NullPointerException("threadNameDeterminer");
		}
		ThreadRenamingRunnable.threadNameDeterminer = threadNameDeterminer;
	}

	
	/** The runnable. */
	private final Runnable runnable;
	
	
	/** The proposed thread name. */
	private final String proposedThreadName;

	
	/**
	 * Instantiates a new thread renaming runnable.
	 *
	 * @param runnable the runnable
	 * @param proposedThreadName the proposed thread name
	 */
	public ThreadRenamingRunnable(Runnable runnable, String proposedThreadName) {
		if (runnable == null) {
			throw new NullPointerException("runnable");
		}
		if (proposedThreadName == null) {
			throw new NullPointerException("proposedThreadName");
		}
		this.runnable = runnable;
		this.proposedThreadName = proposedThreadName;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		final Thread currentThread = Thread.currentThread();
		final String oldThreadName = currentThread.getName();
		final String newThreadName = getNewThreadName(oldThreadName);

		
		boolean renamed = false;
		if (!oldThreadName.equals(newThreadName)) {
			try {
				currentThread.setName(newThreadName);
				renamed = true;
			} catch (SecurityException e) {
				logger.debug("Failed to rename a thread due to security restriction.", e);
			}
		}

		
		try {
			runnable.run();
		} finally {
			if (renamed) {
				
				
				currentThread.setName(oldThreadName);
			}
		}
	}

	
	/**
	 * Gets the new thread name.
	 *
	 * @param currentThreadName the current thread name
	 * @return the new thread name
	 */
	private String getNewThreadName(String currentThreadName) {
		String newThreadName = null;

		try {
			newThreadName = getThreadNameDeterminer().determineThreadName(currentThreadName, proposedThreadName);
		} catch (Throwable t) {
			logger.warn("Failed to determine the thread name", t);
		}

		return newThreadName == null ? currentThreadName : newThreadName;
	}
}
