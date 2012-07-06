/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ThreadNameDeterminer.java 2012-7-6 10:22:12 l.xue.nong$$
 */

package cn.com.rebirth.commons.thread;


/**
 * The Interface ThreadNameDeterminer.
 *
 * @author l.xue.nong
 */
public interface ThreadNameDeterminer {

	
	/** The proposed. */
	ThreadNameDeterminer PROPOSED = new ThreadNameDeterminer() {
		public String determineThreadName(String currentThreadName, String proposedThreadName) throws Exception {
			return proposedThreadName;
		}
	};

	
	/** The current. */
	ThreadNameDeterminer CURRENT = new ThreadNameDeterminer() {
		public String determineThreadName(String currentThreadName, String proposedThreadName) throws Exception {
			return null;
		}
	};

	
	/**
	 * Determine thread name.
	 *
	 * @param currentThreadName the current thread name
	 * @param proposedThreadName the proposed thread name
	 * @return the string
	 * @throws Exception the exception
	 */
	String determineThreadName(String currentThreadName, String proposedThreadName) throws Exception;
}
