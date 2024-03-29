/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons LoggingRunnable.java 2012-7-6 10:22:16 l.xue.nong$$
 */

package cn.com.rebirth.commons.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class LoggingRunnable.
 *
 * @author l.xue.nong
 */
public class LoggingRunnable implements Runnable {

	
	/** The runnable. */
	private final Runnable runnable;
	
	
	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(getClass());

	
	/**
	 * Instantiates a new logging runnable.
	 *
	 * @param runnable the runnable
	 */
	public LoggingRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			runnable.run();
		} catch (Exception e) {
			logger.warn("failed to execute [{}]", e, runnable.toString());
		}
	}
}
