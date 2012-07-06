/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthNullPointerException.java 2012-7-6 10:22:17 l.xue.nong$$
 */

package cn.com.rebirth.commons.exception;


/**
 * The Class rebirthNullPointerException.
 *
 * @author l.xue.nong
 */
public class RebirthNullPointerException extends RebirthException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1904811712517139723L;

	
	/**
	 * Instantiates a new rebirth null pointer exception.
	 */
	public RebirthNullPointerException() {
		super(null);
	}

	
	/**
	 * Instantiates a new rebirth null pointer exception.
	 *
	 * @param msg the msg
	 */
	public RebirthNullPointerException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new rebirth null pointer exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RebirthNullPointerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}