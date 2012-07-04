/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ThreadNameDeterminer.java 2012-3-29 15:15:07 l.xue.nong$$
 */

package cn.com.restart.commons.thread;


/**
 * The Interface ThreadNameDeterminer.
 *
 * @author l.xue.nong
 */
public interface ThreadNameDeterminer {

	
	/** The PROPOSED. */
	ThreadNameDeterminer PROPOSED = new ThreadNameDeterminer() {
		public String determineThreadName(String currentThreadName, String proposedThreadName) throws Exception {
			return proposedThreadName;
		}
	};

	
	/** The CURRENT. */
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
