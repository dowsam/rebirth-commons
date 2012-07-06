/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SumMallSearchNullPointerException.java 2012-3-29 15:14:34 l.xue.nong$$
 */

package cn.com.rebirth.commons.exception;


/**
 * The Class SumMallSearchNullPointerException.
 *
 * @author l.xue.nong
 */
public class RestartNullPointerException extends RestartException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1904811712517139723L;

	
	/**
	 * Instantiates a new sum mall search null pointer exception.
	 */
	public RestartNullPointerException() {
		super(null);
	}

	
	/**
	 * Instantiates a new sum mall search null pointer exception.
	 *
	 * @param msg the msg
	 */
	public RestartNullPointerException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new sum mall search null pointer exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RestartNullPointerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}